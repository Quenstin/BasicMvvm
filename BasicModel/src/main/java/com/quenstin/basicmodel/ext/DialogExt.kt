package com.hdyj.basicmodel.ext

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ToastUtils
import com.example.basicmodel.R
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ktx.immersionBar
import com.hdyj.basicmodel.utils.NavigationBarUtils
import java.lang.ref.WeakReference

/**
 * Created by hdyjzgq
 * data on 2021/8/19
 * function is ：dialog拓展
 */

/**
 * dialog隐藏导航栏
 */
fun Dialog.initActivityBar(activity: Activity) {
    immersionBar(activity) {
        fitsSystemWindows(true)
        hideBar(BarHide.FLAG_HIDE_BAR)
        statusBarDarkFont(true)
    }
}


/**
 * dialog隐藏导航栏2.0
 */
fun Dialog.showDialog() {
    try {
        val windowWr = WeakReference<Window>(window).get()
        NavigationBarUtils.focusNotAle(windowWr!!)
        show()
        NavigationBarUtils.hideNavigationBar(windowWr)
        NavigationBarUtils.clearFocusNotAle(windowWr)
    } catch (e: Exception) {
        ToastUtils.showLong(e.toString())
        e.printStackTrace()
    }
}

/**
 * 设置dialog底部弹出
 */
fun Dialog.setGravityBottom() {
    window!!.setGravity(Gravity.BOTTOM)
    val params = window!!.attributes
    params.width = WindowManager.LayoutParams.MATCH_PARENT
    params.height = WindowManager.LayoutParams.MATCH_PARENT
    window!!.setWindowAnimations(R.style.AnimBottom2Top)
    window!!.attributes = params
}

fun Dialog.setFullScreenGravityBottom() {
    window!!.setGravity(Gravity.BOTTOM)
    val params = window!!.attributes
    params.width = WindowManager.LayoutParams.MATCH_PARENT
    params.height = WindowManager.LayoutParams.MATCH_PARENT
    window!!.setWindowAnimations(R.style.AnimBottom2Top)
    window!!.attributes = params
}

/**
 * 设置dialog底部弹出
 */
fun Dialog.setLayoutStyle(width: Int, height: Int) {
    window!!.setGravity(Gravity.BOTTOM)
    val params = window!!.attributes
    params.width = width
    params.height = height
    window!!.setWindowAnimations(R.style.AnimBottom2Top)
    window!!.attributes = params
}

/**
 * 设置dialog位置
 */
fun Dialog.setGravityStyle(gravity: Int) {
    window!!.setGravity(gravity)
    val params = window!!.attributes
    params.width = WindowManager.LayoutParams.MATCH_PARENT
    params.height = WindowManager.LayoutParams.WRAP_CONTENT
    window!!.setWindowAnimations(R.style.AnimCenter)
    window!!.attributes = params
}

fun Dialog.getResColor(color: Int) = ContextCompat.getColor(context, color)


