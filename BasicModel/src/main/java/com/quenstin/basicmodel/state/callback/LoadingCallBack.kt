package com.hdyj.basicmodel.state.callback

import android.content.Context
import android.view.View
import android.view.Window
import com.example.basicmodel.R
import com.kingja.loadsir.callback.Callback

/**
 * Created by zhgq on 2020/6/16
 * Describe：加载中回调
 */
class LoadingCallBack : Callback() {

    override fun onCreateView(): Int {

        return R.layout.layout_loading
    }

    override fun getSuccessVisible() = true


}