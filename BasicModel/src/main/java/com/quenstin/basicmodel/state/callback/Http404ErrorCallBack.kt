package com.quenstin.basicmodel.state.callback

import com.example.basicmodel.R
import com.kingja.loadsir.callback.Callback

/**
 * Created by zhgq on 2020/6/16
 * Describe：404回调
 */
class Http404ErrorCallBack : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_http_error_404
    }
}