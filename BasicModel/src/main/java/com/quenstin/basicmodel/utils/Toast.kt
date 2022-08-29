package com.quenstin.basicmodel.utils

import android.content.Context
import com.quenstin.basicmodel.widget.ToastCustom

/**
 * Created by hdyjzgq
 * data on 2021/9/8
 * function is ：吐司工具二次封装
 */

fun toastShow(context: Context, title: String = "提示", content: CharSequence, icon: Int? = null) {
    ToastCustom.createToast(context, title, content,icon)

}