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
import com.quenstin.basicmodel.utils.AppLoge
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
 * isShowDialog:是否显示加载框默认显示
 * loadingMessage：加载中提示信息
 */
fun <T> BaseViewModel<*>.simpleLaunch(
        block: Block<T>,
        error: Error?=null,
        isShowDialog: Boolean = true,
        loadingMessage: String = "请求网络中..."
): Job {

    if (isShowDialog)loadState.value = State(StateType.LOADING, loadingMessage)
    return viewModelScope.launch {
        try {
            delay(300)
            loadState.value = State(StateType.SUCCESS)
            block.invoke(this)
        } catch (e: Exception) {
            onError(e, loadState)
            error?.invoke(e)
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
        is SocketTimeoutException,
        is UnknownHostException,
        is HttpException,
        is SSLHandshakeException -> {
            AppLoge("--------网络错误")
            loadState.value = State(StateType.NETWORK_ERROR)
        }
        // 数据解析错误
        is JSONException, is JsonIOException, is JsonParseException, is JsonSyntaxException ,is ParseException, is MalformedJsonException -> {
            AppLoge("--------数据解析错误")
            loadState.value = State(StateType.JSON)

        }
        is TimeoutCancellationException ->{
            AppLoge("--------网络连接超时")
            loadState.value = State(StateType.TIMEOUT)
        }
        // 其他错误
        else -> {
            AppLoge("--------${e.message}")
            loadState.value = State(StateType.ERROR, e.message.toString())
        }
    }
}

