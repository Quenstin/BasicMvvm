package com.quenstin.basicmodel.http

import com.quenstin.basicmodel.http.net.CoroutineCallAdapterFactory
import com.quenstin.basicmodel.http.net.interceptor.CacheInterceptor
import com.quenstin.basicmodel.http.net.interceptor.LogInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by hdyjzgq
 * data on 3/29/21
 * function is ：
 */
class RetrofitFactory private constructor() {
    private val mBaseUrl = "https://2020.fc62.com/bbs/"
    private val BASE_URL = "https://www.baidu.com"


    private val mRetrofit: Retrofit

    fun <T> create(clazz: Class<T>): T {
        return mRetrofit.create(clazz)
    }

    init {
        mRetrofit =
            Retrofit.Builder().baseUrl(mBaseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
                .client(initOkHttpClient())
                .build()
    }

    companion object {
        val instance by lazy {
            RetrofitFactory()
        }
    }

    /**
     * 配置okHttp
     */
    private fun initOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(initCookieIntercept())
            .addInterceptor(LogInterceptor())
            .addInterceptor(initCommonInterceptor())
            .addInterceptor(CacheInterceptor())
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    /**
     * cookie拦截器
     */
    private fun initCookieIntercept(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            val requestUrl = request.url().toString()
            val domain = request.url().host()
            //只保存登录或者注册
//            if(requestUrl.contains(Constant.SAVE_USER_LOGIN_KEY) || requestUrl.contains(Constant.SAVE_USER_REGISTER_KEY)){
//                val mCookie = response.headers(Constant.SET_COOKIE_KEY)
//                mCookie?.let {
//                    saveCookie(domain,parseCookie(it))
//                }
//            }
            response
        }

    }



    /**
     *请求头拦截
     */
    private fun initCommonInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("charset", "UTF-8")
                .build()

            chain.proceed(request)
        }
    }

    private fun parseCookie(it: List<String>): String {
        if (it.isEmpty()) {
            return ""
        }

        val stringBuilder = StringBuilder()

        it.forEach { cookie ->
            stringBuilder.append(cookie).append(";")
        }

        if (stringBuilder.isEmpty()) {
            return ""
        }
        //末尾的";"去掉
        return stringBuilder.deleteCharAt(stringBuilder.length - 1).toString()
    }

    private fun saveCookie(domain: String?, parseCookie: String) {
//        domain?.let {
//            var resutl :String by SPreference("cookie",parseCookie)
//            resutl = parseCookie
//        }
    }
}