package com.quenstin.basicmodel.livedata

import androidx.lifecycle.LiveData
import androidx.arch.core.internal.SafeIterableMap
import androidx.lifecycle.Observer

/**
 * Created by hdyjzgq
 * data on 6/9/21
 * function is ：
 */
object LiveDataUtil {
    /**
     * 通过反射，获取指定LiveData中的Observer对象
     *
     * @param liveData 指定的LiveData
     * @param identityHashCode 想要获取的Observer对象的identityHashCode `System.identityHashCode`
     * @return
     */
    @JvmStatic
    fun getObserver(liveData: LiveData<*>, identityHashCode: Int): Observer<*>? {
        if (liveData == null || identityHashCode == null) {
            return null
        }
        try {
            val field = LiveData::class.java.getDeclaredField("mObservers")
            field.isAccessible = true
            val observers = field[liveData] as SafeIterableMap<Observer<*>, Any>
            if (observers != null) {
                for ((observer) in observers) {
                    if (System.identityHashCode(observer) == identityHashCode) {
                        return observer
                    }
                }
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        return null
    }
}