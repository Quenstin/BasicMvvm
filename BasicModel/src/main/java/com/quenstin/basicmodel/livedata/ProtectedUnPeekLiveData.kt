package com.quenstin.basicmodel.livedata

import androidx.fragment.app.Fragment
import com.quenstin.basicmodel.livedata.LiveDataUtil.getObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.quenstin.basicmodel.livedata.LiveDataUtil
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by hdyjzgq
 * data on 6/9/21
 * function is ：
 */
open class ProtectedUnPeekLiveData<T> : LiveData<T?>() {
    @JvmField
    protected var isAllowNullValue = false
    private val observers = ConcurrentHashMap<Int, Boolean?>()

    /**
     * 保存外部传入的 Observer 与代理 Observer 之间的映射关系
     */
    private val observerMap = ConcurrentHashMap<Int, Int?>()

    /**
     * 这里会持有永久性注册的 Observer 对象，因为是永久性注册的，必须调用 remove 才会注销，所有这里持有 Observer 对象不存在内存泄漏问题，
     * 因为一旦泄漏了，只能说明是业务使用方没有 remove
     */
    private val foreverObservers = ConcurrentHashMap<Int, Observer<*>?>()
    private fun createProxyObserver(originalObserver: Observer<*>, observeKey: Int): Observer<T> {
        return Observer { t: T ->
            if (!observers[observeKey]!!) {
                observers[observeKey] = true
                if (t != null || isAllowNullValue) {
                    originalObserver.onChanged(t as Nothing?)
                }
            }
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        var owner = owner
        if (owner is Fragment && owner.viewLifecycleOwner != null) {
            /**
             * Fragment 的场景下使用 getViewLifeCycleOwner 来作为 liveData 的订阅者，
             * 如此可确保 "视图实例" 的生命周期安全（getView 不为 null），
             * 因而需要注意的是，对 getViewLifeCycleOwner 的使用应在 onCreateView 之后和 onDestroyView 之前。
             *
             * 如果这样说还不理解，详见《LiveData 鲜为人知的 身世背景 和 独特使命》篇的解析
             * https://xiaozhuanlan.com/topic/0168753249
             */
            owner = owner.viewLifecycleOwner
        }
        val observeKey = System.identityHashCode(observer)
        observe(observeKey, owner, observer)
    }

    override fun observeForever(observer: Observer<in T?>) {
        val observeKey = System.identityHashCode(observer)
        observeForever(observeKey, observer)
    }

    private fun observe(
        observeKey: Int,
        owner: LifecycleOwner,
        observer: Observer<in T?>
    ) {
        if (observers[observeKey] == null) {
            observers[observeKey] = true
        }
        var registerObserver: Observer<*>?
        if (observerMap[observeKey] == null) {
            registerObserver = createProxyObserver(observer, observeKey)
            // 保存外部 Observer 以及内部代理 Observer 的映射关系
            observerMap[observeKey] = System.identityHashCode(registerObserver)
        } else {
            // 通过反射拿到真正注册到 LiveData 中的 Observer
            val registerObserverStoreId = observerMap[observeKey]
            registerObserver = getObserver(this, registerObserverStoreId!!)
            if (registerObserver == null) {
                registerObserver = createProxyObserver(observer, observeKey)
                // 保存外部 Observer 以及内部代理 Observer 的映射关系
                observerMap[observeKey] = System.identityHashCode(registerObserver)
            }
        }
        super.observe(owner, registerObserver as Observer<in T?>)
    }

    private fun observeForever(observeKey: Int, observer: Observer<in T?>) {
        if (observers[observeKey] == null) {
            observers[observeKey] = true
        }
        var registerObserver = foreverObservers[observeKey]
        if (registerObserver == null) {
            registerObserver = createProxyObserver(observer, observeKey)
            foreverObservers[observeKey] = registerObserver
        }
        super.observeForever(registerObserver as Observer<in T?>)
    }

    override fun removeObserver(observer: Observer<in T?>) {
        val observeKey = System.identityHashCode(observer)
        var registerObserver = foreverObservers.remove(observeKey)
        if (registerObserver == null && observerMap.containsKey(observeKey)) {
            // 反射拿到真正注册到 LiveData 中的 observer
            val registerObserverStoreId = observerMap.remove(observeKey)
            registerObserver = getObserver(this, registerObserverStoreId!!)
        }
        if (registerObserver != null) {
            observers.remove(observeKey)
        }
        super.removeObserver((registerObserver ?: observer) as Observer<in T?>)
    }

    /**
     * 重写的 setValue 方法，默认不接收 null
     * 可通过 Builder 配置允许接收
     * 可通过 Builder 配置消息延时清理的时间
     *
     *
     * override setValue, do not receive null by default
     * You can configure to allow receiving through Builder
     * And also, You can configure the delay time of message clearing through Builder
     *
     * @param value
     */
    override fun setValue(value: T?) {
        if (value != null || isAllowNullValue) {
            for (entry in observers.entries) {
                entry.setValue(false)
            }
            super.setValue(value)
        }
    }

    fun clear() {
        super.setValue(null)
    }
}