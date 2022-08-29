package com.quenstin.basicmodel.state.callback

import com.example.basicmodel.R
import com.kingja.loadsir.callback.Callback

/**
 * Created by zhgq on 2020/6/16
 * Describe：解析异常回调
 */
class JsonErrorCallBack : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_json_error
    }
}