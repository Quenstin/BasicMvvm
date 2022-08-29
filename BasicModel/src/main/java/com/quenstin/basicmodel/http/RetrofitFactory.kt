package com.quenstin.basicmodel.http

import com.blankj.utilcode.util.SPUtils
import com.quenstin.basicmodel.common.BasicCommon
import com.quenstin.basicmodel.http.net.CoroutineCallAdapterFactory
import com.quenstin.basicmodel.http.net.interceptor.CacheInterceptor
import com.quenstin.basicmodel.http.net.interceptor.LogInterceptor
import com.quenstin.basicmodel.http.net.manager.NullHostNameVerifier
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * Created by hdyjzgq
 * data on 3/29/21
 * function is ：
 */
class RetrofitFactory private constructor() {


    private val mRetrofit: Retrofit

    fun <T> create(clazz: Class<T>): T {
        return mRetrofit.create(clazz)
    }

    init {
        mRetrofit =
            Retrofit.Builder().baseUrl(BasicCommon.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
                .client(initOkHttpClient())
                .build()
    }

    companion object {
        var mBaseUrl = "https://www.baidu.com"
        val instance by lazy {
            RetrofitFactory()
        }
    }

    /**
     * 配置okHttp
     */
    private fun initOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .sslSocketFactory(NullHostNameVerifier.getSocketFactory(),getX509TrustManager()) //由于没有证书设置不校验SSL
            .hostnameVerifier(NullHostNameVerifier()) //由于IP不确定设置不校验HOST
            .addInterceptor(initCookieIntercept())
            .addInterceptor(LogInterceptor())
            .addInterceptor(initCommonInterceptor())
            .addInterceptor(CacheInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private fun getX509TrustManager():X509TrustManager{
        val trustManagerFactory: TrustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers: Array<TrustManager> = trustManagerFactory.getTrustManagers()
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            ("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers))
        }
        val trustManager: X509TrustManager = trustManagers[0] as X509TrustManager

        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
        return trustManager
    }

    /**
     * cookie拦截器
     */
    private fun initCookieIntercept(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
//            val requestUrl = request.url().toString()
//            val domain = request.url().host()
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
                .addHeader("token", SPUtils.getInstance().getString(BasicCommon.COMMON_TOKEN))
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

//    private fun saveCookie(domain: String?, parseCookie: String) {
//        domain?.let {
//            var resutl :String by SPreference("cookie",parseCookie)
//            resutl = parseCookie
//        }
//    }
}