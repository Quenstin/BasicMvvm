package com.quention.basicmvvm

import com.quenstin.basicmodel.http.RetrofitFactory
import com.quenstin.basicmodel.BaseApplication

/**
 * Created by hdyjzgq
 * data on 4/26/21
 * function is ：
 */
class Application:BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        RetrofitFactory.mBaseUrl="https://wanandroid.com/"
    }
}