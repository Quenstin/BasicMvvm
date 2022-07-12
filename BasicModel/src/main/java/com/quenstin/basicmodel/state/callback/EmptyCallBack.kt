package com.hdyj.basicmodel.state.callback

import com.example.basicmodel.R
import com.kingja.loadsir.callback.Callback

/**
 * Created by zhgq on 2020/6/16
 * Describe：null回调
 */
class EmptyCallBack : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }
}