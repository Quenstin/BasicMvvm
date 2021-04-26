package com.quenstin.basicmodel.utils

import com.orhanobut.logger.Logger

/**
 * Created by Appzgq
 * data on 4/13/21
 * function is ：logger二次封装
 */
fun AppLoge(str:String){
    Logger.e(str)
}

fun AppLogd(str:String){
    Logger.d(str)
}

fun AppLogi(str:String){
    Logger.i(str)
}

fun AppLogt(str:String){
    Logger.t(str)
}

fun AppLogv(str:String){
    Logger.v(str)
}

fun AppLogw(str:String){
    Logger.w(str)
}

fun AppLogwtf(str:String){
    Logger.wtf(str)
}