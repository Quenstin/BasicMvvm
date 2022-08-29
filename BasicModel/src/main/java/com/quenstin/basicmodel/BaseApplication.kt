package com.quenstin.basicmodel

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.example.basicmodel.BuildConfig
import com.quenstin.basicmodel.utils.DownloadUtils
import com.quenstin.basicmodel.utils.hdyjLogd
import com.kingja.loadsir.core.LoadSir
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.quenstin.basicmodel.state.callback.*


/**
 * Created by zhgq on 2021/4/10
 * Describe：BaseApplication提取初始化第三方库的逻辑，主要是为了初始化第三方库
 * tip:适合初始化第三方框架，其他Application不要继承该类，不然会出现重复初始化的问题
 */
open class BaseApplication : Application() {
    companion object {
        lateinit var mInstance: Application

    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        initLogger()
        initUpdate()
        initARouter()
        initLoadSir()
        initDownload(this)
        hdyjLogd("----init--BaseApplication---- ${Thread.currentThread().name}")
    }

    /**
     * 初始化多视图
     */
    private fun initLoadSir() {
        LoadSir.beginBuilder()
            .addCallback(ErrorCallBack())
            .addCallback(LoadingCallBack())
            .addCallback(EmptyCallBack())
            .addCallback(HttpErrorCallBack())
            .addCallback(JsonErrorCallBack())
            .addCallback(Http404ErrorCallBack())
            .addCallback(NotWorkCallBack())
            .addCallback(TimeOutErrorCallBack())
            .commit()
    }

    /**
     * 初始化logger
     */
    private fun initLogger() {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true) // （可选）是否显示线程信息。 默认值为true
            .methodCount(5) //（可选）要显示的方法行数。 默认2
            .methodOffset(1) //（可选）设置调用堆栈的函数偏移值，0的话则从打印该Log的函数开始输出堆栈信息，默认是0
            //.logStrategy(customLog)  //（可选）更改要打印的日志策略。 默认LogCat
            .tag("HDYJ_LOG") //（可选）TAG内容. 默认是 PRETTY_LOGGER
            .build()

        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return true
            }
        })
    }

    /**
     * 初始化ARouter
     */
    private fun initARouter() {
        // 打印日志，这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }


    /**
     * 初始化内部更新
     */
    private fun initUpdate() {
//        XUpdate.get()
//            .debug(true) //默认设置只在wifi下检查版本更新
//            .isWifiOnly(false) //默认设置使用get请求检查版本
//            .isGet(true) //默认设置非自动模式，可根据具体使用配置
//            .isAutoMode(false) //设置默认公共请求参数
//            .param("versionCode", AppUtils.getAppVersionCode())
//            .param("appKey", packageName) //设置版本更新出错的监听
//            .setOnUpdateFailureListener { error: UpdateError ->
//                error.printStackTrace()
//                //对不同错误进行处理
//                if (error.code == UpdateError.ERROR.CHECK_NO_NEW_VERSION) {
//                    toastShow(this, content = "当前已是最新版本!")
//                } else {
//                    toastShow(this, content = error.toString())
//                }
//            } //设置是否支持静默安装，默认是true
//            .supportSilentInstall(false) //这个必须设置！实现网络请求功能。
//            .setIUpdateHttpService(OKHttpUpdateHttpService()) //这个必须初始化
//            .init(this)
    }


    /**
     * 初始化下载器
     */
    private fun initDownload(application: Application) {
        DownloadUtils().initDownload(application)
    }


}