package com.quenstin.basicmodel.livedata;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Created by hdyjzgq
 * data on 6/9/21
 * function is ：
 */
public class ProtectedUnPeekLiveData<T> extends LiveData<T> {
    protected boolean isAllowNullValue;

    private final ConcurrentHashMap<Integer, Boolean> observers = new ConcurrentHashMap<>();

    /**
     * 保存外部传入的 Observer 与代理 Observer 之间的映射关系
     */
    private final ConcurrentHashMap<Integer, Integer> observerMap = new ConcurrentHashMap<>();

    /**
     * 这里会持有永久性注册的 Observer 对象，因为是永久性注册的，必须调用 remove 才会注销，所有这里持有 Observer 对象不存在内存泄漏问题，
     * 因为一旦泄漏了，只能说明是业务使用方没有 remove
     */
    private final ConcurrentHashMap<Integer, Observer> foreverObservers = new ConcurrentHashMap<>();

    private Observer<T> createProxyObserver(@NonNull Observer originalObserver, @NonNull Integer observeKey) {
        return t -> {
            if (!observers.get(observeKey)) {
                observers.put(observeKey, true);
                if (t != null || isAllowNullValue) {
                    originalObserver.onChanged(t);
                }
            }
        };
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        if (owner instanceof Fragment && ((Fragment) owner).getViewLifecycleOwner() != null) {
            /**
             * Fragment 的场景下使用 getViewLifeCycleOwner 来作为 liveData 的订阅者，
             * 如此可确保 "视图实例" 的生命周期安全（getView 不为 null），
             * 因而需要注意的是，对 getViewLifeCycleOwner 的使用应在 onCreateView 之后和 onDestroyView 之前。
             *
             * 如果这样说还不理解，详见《LiveData 鲜为人知的 身世背景 和 独特使命》篇的解析
             * https://xiaozhuanlan.com/topic/0168753249
             */
            owner = ((Fragment) owner).getViewLifecycleOwner();
        }

        Integer observeKey = System.identityHashCode(observer);
        observe(observeKey, owner, observer);
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        Integer observeKey = System.identityHashCode(observer);
        observeForever(observeKey, observer);
    }

    private void observe(@NonNull Integer observeKey,
                         @NonNull LifecycleOwner owner,
                         @NonNull Observer<? super T> observer) {

        if (observers.get(observeKey) == null) {
            observers.put(observeKey, true);
        }

        Observer registerObserver;
        if (observerMap.get(observeKey) == null) {
            registerObserver = createProxyObserver(observer, observeKey);
            // 保存外部 Observer 以及内部代理 Observer 的映射关系
            observerMap.put(observeKey, System.identityHashCode(registerObserver));
        } else {
            // 通过反射拿到真正注册到 LiveData 中的 Observer
            Integer registerObserverStoreId = observerMap.get(observeKey);
            registerObserver = LiveDataUtil.getObserver(this, registerObserverStoreId);
            if (registerObserver == null) {
                registerObserver = createProxyObserver(observer, observeKey);
                // 保存外部 Observer 以及内部代理 Observer 的映射关系
                observerMap.put(observeKey, System.identityHashCode(registerObserver));
            }
        }

        super.observe(owner, registerObserver);
    }

    private void observeForever(@NonNull Integer observeKey, @NonNull Observer<? super T> observer) {

        if (observers.get(observeKey) == null) {
            observers.put(observeKey, true);
        }

        Observer registerObserver = foreverObservers.get(observeKey);
        if (registerObserver == null) {
            registerObserver = createProxyObserver(observer, observeKey);
            foreverObservers.put(observeKey, registerObserver);
        }

        super.observeForever(registerObserver);
    }

    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        Integer observeKey = System.identityHashCode(observer);
        Observer registerObserver = foreverObservers.remove(observeKey);
        if (registerObserver == null && observerMap.containsKey(observeKey)) {
            // 反射拿到真正注册到 LiveData 中的 observer
            Integer registerObserverStoreId = observerMap.remove(observeKey);
            registerObserver = LiveDataUtil.getObserver(this, registerObserverStoreId);
        }

        if (registerObserver != null) {
            observers.remove(observeKey);
        }

        super.removeObserver(registerObserver != null ? registerObserver : observer);
    }

    /**
     * 重写的 setValue 方法，默认不接收 null
     * 可通过 Builder 配置允许接收
     * 可通过 Builder 配置消息延时清理的时间
     * <p>
     * override setValue, do not receive null by default
     * You can configure to allow receiving through Builder
     * And also, You can configure the delay time of message clearing through Builder
     *
     * @param value
     */
    @Override
    protected void setValue(T value) {
        if (value != null || isAllowNullValue) {
            for (Map.Entry<Integer, Boolean> entry : observers.entrySet()) {
                entry.setValue(false);
            }
            super.setValue(value);
        }
    }

    public void clear() {
        super.setValue(null);
    }
}
