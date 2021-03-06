package com.hdyj.basicmodel.http.net.interceptor

import com.hdyj.basicmodel.http.net.interceptor.ZipHelper.Companion.decompressForGzip
import com.hdyj.basicmodel.http.net.interceptor.ZipHelper.Companion.decompressToStringForZlib
import com.hdyj.basicmodel.utils.CharacterHandler.Companion.jsonFormat
import com.hdyj.basicmodel.utils.UrlEncoderUtils.Companion.hasUrlEncoded
import com.hdyj.basicmodel.utils.hdyjLoge
import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by hdyjzgq
 * data on 4/12/21
 * function is ：log拦截器
 */
class LogInterceptor : Interceptor {
    private val printLevel = Level.NONE

    constructor() {}

    constructor(printLevel: Level?) {

    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val logRequest =
            printLevel == Level.ALL || printLevel != Level.NONE && printLevel == Level.REQUEST
        if (logRequest) {
            hdyjLoge(
                "请求地址--->  ${request.url()} \n" +
                        " method--->  ${request.method()} \n 请求body-->  ${parseParams(request)}"
            )

        }
        val logResponse =
            printLevel == Level.ALL || printLevel != Level.NONE && printLevel == Level.RESPONSE
        val t1 = if (logResponse) System.nanoTime() else 0
        val originalResponse: Response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            e.message?.let {
                hdyjLoge("Http Error: $it---${request.url()}")

            }
            throw e
        }
        val t2 = if (logResponse) System.nanoTime() else 0
        val responseBody = originalResponse.body()

        //打印响应结果
        var bodyString: String? = null
        if (responseBody != null && isParseable(responseBody.contentType())) {
            //此处会导致OOM，请谨慎打开！！！！！
//            bodyString = printResult(request, originalResponse, logResponse)
        }
        if (logResponse) {
            val segmentList =
                request.url().encodedPathSegments()
            val header: String = if (originalResponse.networkResponse() == null) {
                originalResponse.headers().toString()
            } else {
                originalResponse.networkResponse()!!.request().headers().toString()
            }
            val code = originalResponse.code()
            val isSuccessful = originalResponse.isSuccessful
            val message = originalResponse.message()
            val url = originalResponse.request().url().toString()
            if (responseBody != null && isParseable(responseBody.contentType())) {
                hdyjLoge("请求地址--->  $url \n状态码--->  $code")
                hdyjLoge("请求时长--->  ${TimeUnit.NANOSECONDS.toMillis(t2 - t1)}ms")
                hdyjLoge("请求结果--->  $bodyString")

            } else {
                hdyjLoge("请求地址--->  $url-->状态码--->  $code")
                hdyjLoge("请求结果--->  $message--->  $bodyString")

            }
        }
        return originalResponse
    }

    /**
     * 打印响应结果
     *
     * @param request     [Request]
     * @param response    [Response]
     * @param logResponse 是否打印响应结果
     * @return 解析后的响应结果
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun printResult(
        request: Request,
        response: Response,
        logResponse: Boolean
    ): String? {
        return try {
            //读取服务器返回的结果
            val responseBody = response.newBuilder().build().body()
            val source = responseBody!!.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()

            //获取content的压缩类型
            val encoding = response
                .headers()["Content-Encoding"]
            val clone = buffer.clone()

            //解析response content
            parseContent(responseBody, encoding, clone)
        } catch (e: IOException) {
            e.printStackTrace()
            "{\"error\": \"" + e.message + "\"}"
        }
    }

    /**
     * 解析服务器响应的内容
     *
     * @param responseBody [ResponseBody]
     * @param encoding     编码类型
     * @param clone        克隆后的服务器响应内容
     * @return 解析后的响应结果
     */
    private fun parseContent(
        responseBody: ResponseBody?,
        encoding: String?,
        clone: Buffer
    ): String? {
        var charset = Charset.forName("UTF-8")
        val contentType = responseBody!!.contentType()
        if (contentType != null) {
            charset = contentType.charset(charset)
        }
        //content 使用 gzip 压缩
        return if ("gzip".equals(encoding, ignoreCase = true)) {
            //解压
            decompressForGzip(
                clone.readByteArray(),
                convertCharset(charset)
            )
        } else if ("zlib".equals(encoding, ignoreCase = true)) {
            //content 使用 zlib 压缩
            decompressToStringForZlib(
                clone.readByteArray(),
                convertCharset(charset)
            )
        } else {
            //content 没有被压缩, 或者使用其他未知压缩方式
            clone.readString(charset)
        }
    }

    /**
     * 为了安全这个地方可以根据项目是否处于debug模式来切换打印信息
     */
    enum class Level {
        /**
         * 不打印log
         */
        NONE,

        /**
         * 只打印请求信息
         */
        REQUEST,

        /**
         * 只打印响应信息
         */
        RESPONSE,

        /**
         * 所有数据全部打印
         */
        ALL
    }

    companion object {
        /**
         * 解析请求服务器的请求参数
         *
         * @param request [Request]
         * @return 解析后的请求信息
         * @throws UnsupportedEncodingException
         */
        @Throws(UnsupportedEncodingException::class)
        fun parseParams(request: Request): String {
            return try {
                val body = request.newBuilder().build().body() ?: return ""
                val requestbuffer = Buffer()
                body.writeTo(requestbuffer)
                var charset = Charset.forName("UTF-8")
                val contentType = body.contentType()
                if (contentType != null) {
                    charset = contentType.charset(charset)
                }
                var json = requestbuffer.readString(charset)
                if (hasUrlEncoded(json)) {
                    json = URLDecoder.decode(
                        json,
                        convertCharset(charset)
                    )
                }
                jsonFormat(json)
            } catch (e: IOException) {
                e.printStackTrace()
                "{\"error\": \"" + e.message + "\"}"
            }
        }

        /**
         * 是否可以解析
         *
         * @param mediaType [MediaType]
         * @return `true` 为可以解析
         */
        fun isParseable(mediaType: MediaType?): Boolean {
            return if (mediaType?.type() == null) {
                false
            } else isText(mediaType) || isPlain(
                mediaType
            )
                    || isJson(mediaType) || isForm(
                mediaType
            )
                    || isHtml(mediaType) || isXml(
                mediaType
            )
        }

        fun isText(mediaType: MediaType?): Boolean {
            return if (mediaType?.type() == null) {
                false
            } else "text" == mediaType.type()
        }

        fun isPlain(mediaType: MediaType?): Boolean {
            return if (mediaType?.subtype() == null) {
                false
            } else mediaType.subtype()
                .toLowerCase().contains("plain")
        }

        @JvmStatic
        fun isJson(mediaType: MediaType?): Boolean {
            return if (mediaType?.subtype() == null) {
                false
            } else mediaType.subtype().toLowerCase(Locale.getDefault()).contains("json")
        }

        @JvmStatic
        fun isXml(mediaType: MediaType?): Boolean {
            return if (mediaType?.subtype() == null) {
                false
            } else mediaType.subtype().toLowerCase(Locale.getDefault()).contains("xml")
        }

        fun isHtml(mediaType: MediaType?): Boolean {
            return if (mediaType?.subtype() == null) {
                false
            } else mediaType.subtype().toLowerCase(Locale.getDefault()).contains("html")
        }

        fun isForm(mediaType: MediaType?): Boolean {
            return if (mediaType?.subtype() == null) {
                false
            } else mediaType.subtype().toLowerCase(Locale.getDefault())
                .contains("x-www-form-urlencoded")
        }

        fun convertCharset(charset: Charset?): String {
            val s = charset.toString()
            val i = s.indexOf("[")
            return if (i == -1) {
                s
            } else s.substring(i + 1, s.length - 1)
        }
    }
}