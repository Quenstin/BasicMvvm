package com.quenstin.basicmodel.ext

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.basicmodel.R
import com.gyf.immersionbar.ktx.immersionBar
import com.quenstin.basicmodel.view.activity.BaseActivity


/**
 * 沉浸式状态栏
 */
fun BaseActivity<*>.initBar() {
    immersionBar {
        fitsSystemWindows(true)
        statusBarColor(R.color.white)
        navigationBarColor(R.color.white)
        statusBarDarkFont(true)
    }
}

/**
 * 页面标题
 */
fun BaseActivity<*>.setActivityTitle(title: String) {
    val imageBack = findViewById<ImageView>(R.id.basicTitleBack)
    val titleTv = findViewById<TextView>(R.id.basicTextTitle)
    imageBack.setOnClickListener { finish() }
    titleTv.text = title
}

/**
 * 页面标题
 * 可设置标题栏背景色和标题颜色
 */
fun BaseActivity<*>.setActivityTitle(title: String, color:Int, titleColor:Int) {
    val imageBack = findViewById<ImageView>(R.id.basicTitleBack)
    val titleTv = findViewById<TextView>(R.id.basicTextTitle)
    val rootBg=findViewById<ConstraintLayout>(R.id.basicTitleRoot)
    rootBg.setBackgroundColor(resources.getColor(color))
    titleTv.setTextColor(resources.getColor(titleColor))
    imageBack.setOnClickListener { finish() }
    titleTv.text = title
}