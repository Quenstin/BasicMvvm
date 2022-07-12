/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hdyj.basicmodel.http.net.manager

import androidx.annotation.NonNull
import com.hdyj.basicmodel.http.RetrofitFactory
import com.xuexiang.xupdate.proxy.IUpdateHttpService
import com.xuexiang.xupdate.utils.UpdateUtils
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.FileCallBack
import com.zhy.http.okhttp.callback.StringCallback
import com.zhy.http.okhttp.request.RequestCall
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.*


/**
 * 使用okhttp
 *
 * @author xuexiang
 * @since 2018/7/10 下午4:04
 */
class OKHttpUpdateHttpService @JvmOverloads constructor(private val mIsPostJson: Boolean = false) :
    IUpdateHttpService {



    override fun asyncGet(
        @NonNull url: String,
        @NonNull params: Map<String, Any>,
        @NonNull callBack: IUpdateHttpService.Callback
    ) {
        OkHttpUtils.get()
            .url(url)
            .params(transform(params))
            .build()
            .execute(object : StringCallback() {
                override fun onError(call: Call, e: Exception, id: Int) {
                    callBack.onError(e)
                }

                override fun onResponse(response: String, id: Int) {
                    callBack.onSuccess(response)
                }
            })
    }

    override fun asyncPost(
        @NonNull url: String,
        @NonNull params: Map<String, Any>,
        @NonNull callBack: IUpdateHttpService.Callback
    ) {
        //这里默认post的是Form格式，使用json格式的请修改 post -> postString
        val requestCall: RequestCall = if (mIsPostJson) {
            OkHttpUtils.postString()
                .url(url)
                .content(UpdateUtils.toJson(params))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
        } else {
            OkHttpUtils.post()
                .url(url)
                .params(transform(params))
                .build()
        }
        requestCall
            .execute(object : StringCallback() {
                override fun onError(call: Call, e: Exception, id: Int) {
                    callBack.onError(e)
                }

                override fun onResponse(response: String, id: Int) {
                    callBack.onSuccess(response)
                }
            })
    }

    override fun download(
        @NonNull url: String,
        @NonNull path: String,
        @NonNull fileName: String,
        @NonNull callback: IUpdateHttpService.DownloadCallback
    ) {
        OkHttpUtils.get()
            .url(url)
            .tag(url)
            .build()
            .execute(object : FileCallBack(path, fileName) {
                override fun inProgress(progress: Float, total: Long, id: Int) {
                    callback.onProgress(progress, total)
                }

                override fun onError(call: Call, e: Exception, id: Int) {
                    callback.onError(e)
                }

                override fun onResponse(response: File?, id: Int) {
                    callback.onSuccess(response)
                }

                override fun onBefore(request: Request?, id: Int) {
                    super.onBefore(request, id)
                    callback.onStart()
                }
            })

    }

    override fun cancelDownload(@NonNull url: String) {
        OkHttpUtils.getInstance().cancelTag(url)
    }

    private fun transform(params: Map<String, Any>): Map<String, String> {
        val map: MutableMap<String, String> = TreeMap()
        for ((key, value) in params) {
            map[key] = value.toString()
        }
        return map
    }
}