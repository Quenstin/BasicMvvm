package com.hdyj.basicmodel.utils

import com.blankj.utilcode.util.AppUtils
import com.orhanobut.logger.Logger
import java.lang.reflect.Array
import java.util.*

/**
 * Created by hdyjzgq
 * data on 4/13/21
 * function is ：logger二次封装
 * debug模式下会打印log
 */

var isOpen = AppUtils.isAppDebug()


fun hdyjLoge(tag: String, str: String = "") {
    if (isOpen)
        Logger.e("$tag--$str")
}

fun hdyjLogd(tag: String, str: String = "") {
    if (isOpen)
        Logger.d("$tag----$str")
}

fun hdyjLogi(tag: String, str: String = "") {
    if (isOpen)
        Logger.i("$tag----$str")
}

fun hdyjLogt(tag: String, str: String = "") {
    if (isOpen)
        Logger.t("$tag----$str")
}

fun hdyjLogv(tag: String, str: String = "") {
    if (isOpen)
        Logger.v("$tag----$str")
}

fun hdyjLogw(tag: String, str: String = "") {
    if (isOpen)
        Logger.w("$tag----$str")
}
fun hdyjLogw(tag: String, str: String = "", str2: String = "") {
    if (isOpen)
        Logger.w("$tag----$str ---$str2")
}

fun hdyjLogwtf(tag: String, str: String = "") {
    if (isOpen)
        Logger.wtf("$tag----$str")
}