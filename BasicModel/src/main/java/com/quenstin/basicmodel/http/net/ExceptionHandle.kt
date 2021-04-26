package com.quenstin.basicmodel.http.net

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 * Created by hdyjzgq
 * data on 4/12/21
 * function is ：根据不同异常返回信息
 */
object ExceptionHandle {

    fun handleException(e: Throwable?): AppException {
        val ex: AppException
        e?.let {
            when (it) {
                is HttpException -> {
                    ex = AppException(Error.NETWORK_ERROR,e)
                    return ex
                }
                is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                    ex = AppException(Error.PARSE_ERROR,e)
                    return ex
                }
                is ConnectException -> {
                    ex = AppException(Error.NETWORK_ERROR,e)
                    return ex
                }
                is javax.net.ssl.SSLException -> {
                    ex = AppException(Error.SSL_ERROR,e)
                    return ex
                }
                is ConnectTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR,e)
                    return ex
                }
                is java.net.SocketTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR,e)
                    return ex
                }
                is java.net.UnknownHostException -> {
                    ex = AppException(Error.TIMEOUT_ERROR,e)
                    return ex
                }
                is AppException -> return it

                else -> {
                    ex = AppException(Error.UNKNOWN,e)
                    return ex
                }
            }
        }
        ex = AppException(Error.UNKNOWN,e)
        return ex
    }
}