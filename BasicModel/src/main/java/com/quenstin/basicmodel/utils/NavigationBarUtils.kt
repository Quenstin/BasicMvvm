package com.hdyj.basicmodel.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.NonNull


/**
 * author : whl
 *
 * date:2021/12/31
 *
 * function:
 */
object NavigationBarUtils {

    fun hideNavigationBar(window: Window) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.decorView
            .setOnSystemUiVisibilityChangeListener {
                var uiOptions: Int = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or  //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or  //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN or  //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                uiOptions =
                    uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                window.decorView.systemUiVisibility = uiOptions
            }
    }


    /**
     * dialog 需要全屏的时候用，和clearFocusNotAle() 成对出现
     * 在show 前调用  focusNotAle   show后调用clearFocusNotAle
     * @param window
     */
    fun focusNotAle(window: Window) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        );
    }

    /**
     * dialog 需要全屏的时候用，focusNotAle() 成对出现
     * 在show 前调用  focusNotAle   show后调用clearFocusNotAle
     * @param window
     */
    fun clearFocusNotAle(window: Window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    /**
     * 判断虚拟导航栏是否显示
     * @param window
     */
    fun checkNavigationBarShow(@NonNull context: Context, @NonNull window: Window): Boolean {
        val systemUiVisibility = (context as Activity).window.decorView.systemUiVisibility
        if (systemUiVisibility == View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION || systemUiVisibility == View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) {

            return true;
        }
        return false;
    }


}