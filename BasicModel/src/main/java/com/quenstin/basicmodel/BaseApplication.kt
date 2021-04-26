package com.quenstin.basicmodel

import android.app.Application
import com.quenstin.basicmodel.state.callback.*
import com.kingja.loadsir.core.LoadSir
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger


/**
 * Created by zhgq on 2021/4/10
 * Describe：app
 */
open class BaseApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        initLoadSir()
        initLogger()

    }


    /**
     * 初始化多视图
     */
    private fun initLoadSir(){
        LoadSir.beginBuilder()
            .addCallback(ErrorCallBack())
            .addCallback(LoadingCallBack())
            .addCallback(EmptyCallBack())
            .addCallback(HttpErrorCallBack())
            .addCallback(JsonErrorCallBack())
            .commit()
    }

    /**
     * 初始化logger
     */
    private fun initLogger(){
        Logger.addLogAdapter(AndroidLogAdapter())
    }


}