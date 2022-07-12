package com.hdyj.basicmodel.widget

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.CountDownTimer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import coil.load
import com.example.basicmodel.R

/**
 * Created by hdyjzgq
 * data on 2021/9/24
 * function is ：自定义toast
 *
 *  只做了最简单的提示，还可以根据后续的需求进行拓展
 *
 *
 *  2021/12/26 上方加了icon
 */
class ToastCustom {

    companion object {
        private lateinit var layoutInflater: LayoutInflater
        private var ToastBgColor: Int = R.color.ff222222


        /**
         * 创建toast
         */
        fun createToast(
            context: Context,
            title: String? = null,
            content: CharSequence,
            icon: Int? = null,
        ) {
            layoutInflater = LayoutInflater.from(context)
            val layout = layoutInflater.inflate(
                R.layout.layout_custom_toast,
                null
            )
            initToast(context, layout, title, content, icon)
        }


        /**
         * 创建自定义布局参数的toast
         */
        fun createToast(
            context: Context,
            title: String? = null,
            content: CharSequence,
            icon: Int? = null,
            layoutParams: ConstraintLayout.LayoutParams,
        ) {
            layoutInflater = LayoutInflater.from(context)
            val layout = layoutInflater.inflate(
                R.layout.layout_custom_toast,
                null
            )
            layout.layoutParams = layoutParams
            initToast(context, layout, title, content, icon)
        }

        private fun initToast(
            context: Context,
            layout: View,
            title: String?,
            content: CharSequence,
            icon: Int?,
        ) {
            val titleText = layout.findViewById<TextView>(R.id.customToastTitle)

            val toastIcon = layout.findViewById<ImageView>(R.id.customToastIcon)
            if (icon != null) {
                toastIcon.load(icon)
                titleText.visibility = View.GONE
            } else {
                titleText.visibility = View.VISIBLE
                toastIcon.visibility = View.GONE
            }
            if (title != null && title != "") {
                //拓展函数里有默认参数，只能在这里进行区分
                titleText.text = title
            } else {
                titleText.visibility = View.GONE
            }

            val contentText = layout.findViewById<TextView>(R.id.customToastDescription)
            contentText.text = content


            // round background color
            setBackgroundAndFilter(
                R.drawable.basic_toast_bg,
                ToastBgColor, layout, context
            )

            Toast(context).apply {
                duration = Toast.LENGTH_SHORT
                setGravity(Gravity.CENTER, 0, 0)
                view = layout
                show()
            }


        }

        /**
         * 弹出时间倒计时
         */
        private fun startTimer(duration: Long, toast: Toast) {
            val timer = object : CountDownTimer(duration, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    // do nothing
                }

                override fun onFinish() {
                    toast.cancel()
                }
            }
            timer.start()
        }


        /**
         * 设置taost背景
         */
        private fun setBackgroundAndFilter(
            @DrawableRes background: Int,
            @ColorRes colorFilter: Int,
            layout: View,
            context: Context
        ) {
            val drawable = ContextCompat.getDrawable(context, background)
            drawable?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(context, colorFilter),
                PorterDuff.Mode.MULTIPLY
            )
            layout.background = drawable
        }
    }
}