package com.quenstin.basicmodel.ext

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.launcher.ARouter
import com.example.basicmodel.R
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ktx.immersionBar
import com.quenstin.basicmodel.view.activity.BaseActivity
import com.quenstin.basicmodel.widget.ToastCustom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * 沉浸式状态栏
 */
fun BaseActivity<*>.initBar(color: Int = R.color.white) {
    immersionBar {
        fitsSystemWindows(false)
        statusBarColor(color)
//        hideBar(BarHide.FLAG_HIDE_BAR)
        hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
        statusBarDarkFont(true)
    }
}


/**
 * 注册ARouter 在传值时需要
 */
fun BaseActivity<*>.initARouter() {
    ARouter.getInstance().inject(this)

}

/**
 * 获取颜色id
 */
fun BaseActivity<*>.getResColor(color: Int) = ContextCompat.getColor(this, color)

/**
 * 设置背景图
 */
fun BaseActivity<*>.getResDrawable(drawable: Int) = ContextCompat.getDrawable(this, drawable)



/**
 *
 * 可设置标题栏背景色和标题颜色
 * title：页面标题
 * bgColor：标题颜色
 * titleColor：title字体颜色
 * backColor：返回按钮颜色
 */
@RequiresApi(Build.VERSION_CODES.FROYO)
fun BaseActivity<*>.setActivityTitle(
    title: String,
    bgColor: Int = R.color.white,
    titleColor: Int = R.color.black,
    backColor: Int = R.color.ff444444,
    closeColor: Int = R.color.black,
    showCloseBg: Boolean = true
) {
    val imageBack = findViewById<View>(R.id.basicTitleBack)
    val basicBackIcon = findViewById<ImageView>(R.id.basicBackIcon)
    val titleTv = findViewById<TextView>(R.id.basicTextTitle)
    val rootBg = findViewById<ConstraintLayout>(R.id.basicTitleRoot)
    val close = findViewById<ImageView>(R.id.basicClose)
    val closeBg = findViewById<View>(R.id.rightBg)

    basicBackIcon.setColorFilter(getResColor(backColor))
    close.setColorFilter(getResColor(closeColor))
    rootBg.setBackgroundColor(getResColor(bgColor))
    titleTv.setTextColor(getResColor(titleColor))
    if (!showCloseBg) {
        close.visibility = View.GONE
        closeBg.visibility = View.GONE
    }

    imageBack.setOnClickListener { finish() }
    closeBg.setOnClickListener {
        ARouter.getInstance().build("/MainLibrary/HomeActivity").navigation()
    }
    titleTv.text = title
}

/**
 * 统一的自定义toast
 */
fun AppCompatActivity.toastShow(
    title: String = "提示",
    content: CharSequence,
    icon: Int? = null,
    layoutParams: ConstraintLayout.LayoutParams
) {

    lifecycleScope.launch(Dispatchers.Main) {
        ToastCustom.createToast(this@toastShow, title, content, icon, layoutParams)
    }
}

fun AppCompatActivity.toastShow(title: String = "提示", content: CharSequence, icon: Int? = null) {

    lifecycleScope.launch(Dispatchers.Main) {
        ToastCustom.createToast(this@toastShow, title, content, icon)

    }
}


