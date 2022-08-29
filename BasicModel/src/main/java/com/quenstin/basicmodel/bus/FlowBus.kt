package com.quenstin.basicmodel.bus

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by hdyjzgq
 * data on 2022/4/24
 * function is ：flow事件总线
 *
 * 使用说明：发送事件FlowBus.post<String>post().tryEmit("234")
 *          接收事件：FlowBus.observe<String>().observe(this){
                            toastShow(content = it)
                    }
 */
object FlowBus {

    val events = ConcurrentHashMap<Any, MutableSharedFlow<Any>>()
    val stickyEvents = ConcurrentHashMap<Any, MutableSharedFlow<Any>>()


    /**
     * 发送粘性事件
     */
    inline fun <reified T> postSticky(): MutableSharedFlow<T> {
        if (!stickyEvents.containsKey(T::class.java)) {
            stickyEvents[T::class.java] = MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
        }
        return stickyEvents[T::class.java] as MutableSharedFlow<T>
    }


    /**
     * 接收粘性事件
     */
    inline fun <reified T> observeSticky(): LiveData<T> {
        return postSticky<T>().asLiveData()
    }


    /**
     * 发送普通事件
     */
    inline fun <reified T> post(): MutableSharedFlow<T> {
        if (!events.containsKey(T::class.java)) {
            events[T::class.java] = MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
        }
        return events[T::class.java] as MutableSharedFlow<T>
    }

    /**
     * 接收事件
     */
    inline fun <reified T> observe(): LiveData<T> {
        return post<T>().asLiveData()
    }
}