package com.quenstin.basicmodel.ext

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.quenstin.basicmodel.state.State
import com.quenstin.basicmodel.state.StateType
import com.quenstin.basicmodel.utils.AppLoge
import com.quenstin.basicmodel.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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

/**
 * 接口没有统一返回固定的响应信息暂且使用这个协程
 */
fun <T> BaseViewModel<*>.simpleLaunch(
    block: Block<T>,
    isShowDialog: Boolean = true,
    loadingMessage: String = "请求网络中..."
): Job {

    if (isShowDialog)loadState.value = State(StateType.LOADING, loadingMessage)
    return viewModelScope.launch {
        try {
            loadState.value = State(StateType.SUCCESS)
            block.invoke(this)
        } catch (e: Exception) {
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
        is SocketTimeoutException,
        is UnknownHostException,
        is HttpException,
        is SSLHandshakeException -> {
            AppLoge("--------网络错误")
            loadState.value = State(StateType.NETWORK_ERROR)
        }
        // 数据解析错误
        is JSONException, is JsonIOException, is JsonParseException, is JsonSyntaxException -> {
            AppLoge("--------数据解析错误")
            loadState.value = State(StateType.JSON)

        }
        // 其他错误
        else -> {
            AppLoge("--------${e.message}")
            loadState.value = State(StateType.ERROR, e.message.toString())
        }
    }
}

