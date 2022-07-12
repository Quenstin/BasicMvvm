package com.hdyj.basicmodel.state.callback

import com.example.basicmodel.R
import com.kingja.loadsir.callback.Callback

/**
 * Created by hdyjzgq
 * data on 2021/9/22
 * function is ：无网络回调
 */
class NotWorkCallBack: Callback() {
    override fun onCreateView()= R.layout.layout_not_work_error
}