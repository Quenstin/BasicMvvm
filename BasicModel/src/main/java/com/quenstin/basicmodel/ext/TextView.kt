package com.hdyj.basicmodel.ext

import android.graphics.Paint
import android.widget.TextView
import kotlinx.coroutines.flow.callbackFlow


/**
 * 字体加粗相当于bold
 */
fun TextView.setFakeBold() {
    paint.isFakeBoldText = true
}
fun TextView.setNoFakeBold() {
    paint.isFakeBoldText = false
}

/**
 * 字体加粗
 *  width：值越大字体越粗
 *  1f 相当于设计稿中的 Medium
 */
fun TextView.setTextWidth(width: Float = 1f) {
    paint.strokeWidth = width
    paint.style = Paint.Style.FILL_AND_STROKE
    invalidate()
}




