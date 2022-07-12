package com.hdyj.basicmodel.ext

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.view.*
import androidx.coordinatorlayout.widget.ViewGroupUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.basicmodel.R

/**
 * Created by hdyjzgq
 * data on 4/9/21
 * function is ：View拓展函数
 */

/**
 * 设置颜色
 */
fun View.getResColor(color: Int) = ContextCompat.getColor(context, color)

/**
 * 设置view显示
 */
fun View.visible() {
    visibility = View.VISIBLE
}


/**
 * 设置view占位隐藏
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
 */
fun View.visibleOrGone(flag: Boolean) {
    visibility = if (flag) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

/**
 * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
 */
fun View.visibleOrInvisible(flag: Boolean) {
    visibility = if (flag) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

/**
 * 设置view隐藏
 */
fun View.gone() {
    visibility = View.GONE
}


fun createBitmapSafely(width: Int, height: Int, config: Bitmap.Config, retryCount: Int): Bitmap? {
    try {
        return Bitmap.createBitmap(width, height, config)
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
        if (retryCount > 0) {
            System.gc()
            return createBitmapSafely(width, height, config, retryCount - 1)
        }
        return null
    }
}


/**
 * 防止重复点击事件 默认0.5秒内不可重复点击
 * @param interval 时间间隔 默认0.5秒
 * @param action 执行方法
 */
var lastClickTime = 0L
fun View.clickNoRepeat(interval: Long = 500, action: (view: View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
            return@setOnClickListener
        }
        lastClickTime = currentTime
        action(it)
    }
}


fun Any?.notNull(notNullAction: (value: Any) -> Unit, nullAction1: () -> Unit) {
    if (this != null) {
        notNullAction.invoke(this)
    } else {
        nullAction1.invoke()
    }
}


/**
 * 设置view的背景色和圆角角度
 * 减少shape的创建
 */
fun View.setShapeBg(color: Int = R.color.white, radius: Int = 2) {
    background = GradientDrawable().apply {
        setColor(getResColor(color))
        cornerRadius = ConvertUtils.dp2px(radius.toFloat()).toFloat()
    }
}

/**
 * 设置view的背景色和圆角角度/边框
 * 减少shape的创建
 */
fun View.setShapeStrokeBg(
    color: Int = R.color.white,
    radius: Int = 2,
    sColor: Int = R.color.ff0094ff
) {
    background = GradientDrawable().apply {
        setColor(getResColor(color))
        setStroke(1, getResColor(sColor))
        cornerRadius = ConvertUtils.dp2px(radius.toFloat()).toFloat()
    }
}

/**
 * 设置view的背景色/形状
 * 减少shape的创建
 */
fun View.setShapeOvalBg(color: Int = R.color.white) {
    background = GradientDrawable().apply {
        setColor(getResColor(color))
        shape = GradientDrawable.OVAL
    }
}

/**
 * 设置背景/是否是矩形或者圆/圆角角度/边框线
 */
fun View.setShapeAllBg(
    bgColor: Int = R.color.white,
    isOval: Boolean = false,
    radius: Int = 2,
    strokeColor: Int = R.color.transparent,
    strokeWidth: Int = 0
) {
    background = GradientDrawable().apply {
        setColor(getResColor(bgColor))
        if (isOval) {
            shape = GradientDrawable.OVAL
        } else {
            cornerRadius = ConvertUtils.dp2px(radius.toFloat()).toFloat()
        }
        setStroke(strokeWidth, getResColor(strokeColor))

    }
}


/**
 * 设置view的背景色和四个圆角角度
 * 减少shape的创建
 * top-left, top-right, bottom-right, bottom-left.
 */
fun View.setShapeRadiusBg(color: Int = R.color.white, tRadius: Float = 2f, bRadius: Float = 2f) {
    background = GradientDrawable().apply {
        setColor(getResColor(color))
        val ii =
            floatArrayOf(tRadius, tRadius, tRadius, tRadius, bRadius, bRadius, bRadius, bRadius)
        cornerRadii = ii
    }
}

/**
 * 设置无边框
 */
fun View.noBorder() {
    background = GradientDrawable().apply {
        setStroke(0, getResColor(R.color.transparent))
    }
}


fun RecyclerView.addNoContentFooter() {
    val footer = LayoutInflater.from(context)
        .inflate(R.layout.lauout_footer_nomore_content, this, false)
    (adapter as BaseQuickAdapter<*, *>).setFooterView(footer)
}


/**
 * 扩大view的点击区域
 */
@SuppressLint("RestrictedApi")
fun View.expand(dx: Int, dy: Int){
    // 将刚才定义代理类放到方法内部，调用方不需要了解这些细节
    class MultiTouchDelegate(bound: Rect? = null, delegateView: View) : TouchDelegate(bound, delegateView) {
        val delegateViewMap = mutableMapOf<View, Rect>()
        private var delegateView: View? = null

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val x = event.x.toInt()
            val y = event.y.toInt()
            var handled = false
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    delegateView = findDelegateViewUnder(x, y)
                }
                MotionEvent.ACTION_CANCEL -> {
                    delegateView = null
                }
            }
            delegateView?.let {
                event.setLocation(it.width / 2f, it.height / 2f)
                handled = it.dispatchTouchEvent(event)
            }
            return handled
        }

        private fun findDelegateViewUnder(x: Int, y: Int): View? {
            delegateViewMap.forEach { entry -> if (entry.value.contains(x, y)) return entry.key }
            return null
        }
    }

    // 获取当前控件的父控件
    val parentView = parent as? ViewGroup
    // 若父控件不是 ViewGroup, 则直接返回
    parentView ?: return

    // 若父控件未设置触摸代理，则构建 MultiTouchDelegate 并设置给它
    if (parentView.touchDelegate == null) parentView.touchDelegate = MultiTouchDelegate(delegateView = this)
    post {
        val rect = Rect()
        // 获取子控件在父控件中的区域
        ViewGroupUtils.getDescendantRect(parentView, this, rect)
        // 将响应区域扩大
        rect.inset(- dx, - dy)
        // 将子控件作为代理控件添加到 MultiTouchDelegate 中
        (parentView.touchDelegate as? MultiTouchDelegate)?.delegateViewMap?.put(this, rect)
    }
}


