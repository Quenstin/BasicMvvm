package com.hdyj.basicmodel.ext

import android.content.Context
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hdyj.basicmodel.view.activity.BaseActivity
import java.io.InputStream
import java.io.OutputStream

/**
 * Created by hdyjzgq
 * data on 2021/9/6
 * function is ：
 */
fun BaseQuickAdapter<*,*>.getResColor(context: Context,color:Int) = ContextCompat.getColor(context,color)
/**
 * 设置背景图
 */
fun BaseQuickAdapter<*,*>.getResDrawable(context: Context,drawable: Int) = ContextCompat.getDrawable(context, drawable)