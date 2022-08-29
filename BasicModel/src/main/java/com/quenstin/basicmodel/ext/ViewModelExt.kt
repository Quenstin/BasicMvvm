package com.quenstin.basicmodel.ext

import android.net.ParseException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import com.quenstin.basicmodel.state.State
import com.quenstin.basicmodel.state.StateType
import com.quenstin.basicmodel.utils.hdyjLoge
import com.quenstin.basicmodel.viewmodel.BaseViewModel
import kotlinx.coroutines.*
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * Created by hdyjzgq
 * data on 4/12/21
 * function is ：数据解析的拓展在viewModel中使用
 */

typealias Block<T> = suspend (CoroutineScope) -> T
typealias Error = suspend (Exception) -> Unit

/**
 * 接口没有统一返回固定的响应信息暂且使用这个协程
 * block:请求协程
 * error：不需要自己处理请求错误的时候就不需要传入
 * dispatchers:指定线程
 * isShowDialog:是否显示加载框默认显示
 * showTime:加载进度显示的时常(tip:默认是有的不需要设置时长，但是个别情况下会出现空白页面)
 *
 *
 *
 * ps:
 *  empty状态需要自己在view中根据返回的数据是否为null来手动调用showEmpty方法，还没想到好的
 */
fun <T> BaseViewModel<*>.simpleLaunch(
    block: Block<T>,
    error: Error? = null,
    isShowDialog: Boolean = true,
    showTime: Long = 0
): Job {

    //此处修改为post,如果在IO线程调用了 simpleLaunch 会报错
    if (isShowDialog) loadState.postValue(State(StateType.LOADING))
    return viewModelScope.launch {
        try {
            block.invoke(this)
            delay(showTime)
            loadState.postValue(State(StateType.SUCCESS))
        } catch (e: Exception) {
            e.printStackTrace()
            hdyjLoge("----报错的ViewModel---${this@simpleLaunch::class.simpleName}--")
            error?.invoke(e)
            onError(e, loadState)
        }
    }
}

/**
 * 统一处理错误
 * @param e 异常
 * @param loadState 页面状态
 */
private fun onError(e: Exception, loadState: MutableLiveData<State>) {

    when (e) {
        // 网络请求失败
        is ConnectException,
        is UnknownHostException,
        is HttpException,
        is SSLHandshakeException -> {
            hdyjLoge("--------网络错误${e.message}")
            loadState.value = State(StateType.ERROR_404)
        }
        // 数据解析错误
        is JSONException,
        is JsonIOException,
        is JsonParseException,
        is JsonSyntaxException,
        is ParseException,
        is MalformedJsonException -> {
            hdyjLoge("--------数据解析错误${e.message}")
            loadState.value = State(StateType.JSON)

        }
        is TimeoutCancellationException,
        is SocketTimeoutException -> {
            hdyjLoge("--------网络连接超时${e.message}")
            loadState.value = State(StateType.TIMEOUT)
        }
        // 其他错误
        else -> {
            hdyjLoge("--------${e.message}")
            loadState.value = State(StateType.ERROR, e.message.toString())
        }
    }
}

