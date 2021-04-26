package com.quenstin.basicmodel.state

import androidx.annotation.StringRes

/**
 * Created by hdyjzgq
 * data on 3/29/21
 * function is ：页面状态信息类
 */
data class State(var code : StateType, var message : String = "", @StringRes var tip : Int = 0) {
}