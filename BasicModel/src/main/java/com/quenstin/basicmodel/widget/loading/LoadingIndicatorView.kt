package com.quenstin.basicmodel.widget.loading

import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import com.example.basicmodel.R
import com.quenstin.basicmodel.utils.AppLoge

/**
 * Created by hdyjzgq
 * data on 4/21/21
 * function is ：加载view
 */
class LoadingIndicatorView(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val MIN_SHOW_TIME = 500 // ms

    private val MIN_DELAY = 500 // ms


    private var mStartTime: Long = -1

    private var mPostedHide = false

    private var mPostedShow = false

    private var mDismissed = false

    private val mDelayedHide = Runnable {
        mPostedHide = false
        mStartTime = -1
        visibility = GONE
    }

    private val mDelayedShow = Runnable {
        mPostedShow = false
        if (!mDismissed) {
            mStartTime = System.currentTimeMillis()
            visibility = VISIBLE
        }
    }

    var mMinWidth = 0
    var mMaxWidth = 0
    var mMinHeight = 0
    var mMaxHeight = 0

    private var mIndicator: Indicator? = null
    private var mIndicatorColor = 0

    private var mShouldStartAnimationDrawable = false

    init {

        init(context, attrs, defStyleAttr, R.style.LoadingIndicatorView)
    }


    private fun init(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        mMinWidth = 24
        mMaxWidth = 48
        mMinHeight = 24
        mMaxHeight = 48
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.LoadingIndicatorView, defStyleAttr, defStyleRes
        )
        mMinWidth = a.getDimensionPixelSize(R.styleable.LoadingIndicatorView_minWidth, mMinWidth)
        mMaxWidth = a.getDimensionPixelSize(R.styleable.LoadingIndicatorView_maxWidth, mMaxWidth)
        mMinHeight =
            a.getDimensionPixelSize(R.styleable.LoadingIndicatorView_minHeight, mMinHeight)
        mMaxHeight =
            a.getDimensionPixelSize(R.styleable.LoadingIndicatorView_maxHeight, mMaxHeight)
        val indicatorName: String? = a.getString(R.styleable.LoadingIndicatorView_indicatorName)
        mIndicatorColor = a.getColor(R.styleable.LoadingIndicatorView_indicatorColor, Color.WHITE)
        if (indicatorName != null) {
            setIndicator(indicatorName)
        }
        if (mIndicator == null) {
            setIndicator(BallSpinFadeLoaderIndicator())
        }
        a.recycle()
    }

    fun getIndicator(): Indicator? {
        return mIndicator
    }

    private fun setIndicator(d: Indicator) {
        if (mIndicator !== d) {
            if (mIndicator != null) {
                mIndicator!!.callback = null
                unscheduleDrawable(mIndicator)
            }
            mIndicator = d
            //need to set indicator color again if you didn't specified when you update the indicator .
            setIndicatorColor(mIndicatorColor)
            if (d != null) {
                d.callback = this
            }
            postInvalidate()
        }
    }


    /**
     * setIndicatorColor(0xFF00FF00)
     * or
     * setIndicatorColor(Color.BLUE)
     * or
     * setIndicatorColor(Color.parseColor("#FF4081"))
     * or
     * setIndicatorColor(0xFF00FF00)
     * or
     * setIndicatorColor(getResources().getColor(android.R.color.black))
     * @param color
     */
    fun setIndicatorColor(color: Int) {
        mIndicatorColor = color
        mIndicator!!.setColor(color)
    }


    /**
     * You should pay attention to pass this parameter with two way:
     * for example:
     * 1. Only class Name,like "SimpleIndicator".(This way would use default package name with
     * "com.wang.avi.indicators")
     * 2. Class name with full package,like "com.my.android.indicators.SimpleIndicator".
     * @param indicatorName the class must be extend Indicator .
     */
    fun setIndicator(indicatorName: String) {
        if (TextUtils.isEmpty(indicatorName)) {
            return
        }
        val drawableClassName = StringBuilder()
        if (!indicatorName.contains(".")) {
            val defaultPackageName = javaClass.getPackage()!!.name
            drawableClassName.append(defaultPackageName)
                .append(".indicators")
                .append(".")
        }
        drawableClassName.append(indicatorName)
        try {
            val drawableClass = Class.forName(drawableClassName.toString())
            val indicator = drawableClass.newInstance() as Indicator
            setIndicator(indicator)
        } catch (e: ClassNotFoundException) {
            AppLoge( "Didn't find your class , check the name again !")
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    fun smoothToShow() {
        startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
        visibility = VISIBLE
    }

    fun smoothToHide() {
        startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_out))
        visibility = GONE
    }

    fun hide() {
        mDismissed = true
        removeCallbacks(mDelayedShow)
        val diff = System.currentTimeMillis() - mStartTime
        if (diff >= MIN_SHOW_TIME || mStartTime == -1L) {
            // The progress spinner has been shown long enough
            // OR was not shown yet. If it wasn't shown yet,
            // it will just never be shown.
            visibility = GONE
        } else {
            // The progress spinner is shown, but not long enough,
            // so put a delayed message in to hide it when its been
            // shown long enough.
            if (!mPostedHide) {
                postDelayed(mDelayedHide, MIN_SHOW_TIME - diff)
                mPostedHide = true
            }
        }
    }

    fun show() {
        // Reset the start time.
        mStartTime = -1
        mDismissed = false
        removeCallbacks(mDelayedHide)
        if (!mPostedShow) {
            postDelayed(mDelayedShow, MIN_DELAY.toLong())
            mPostedShow = true
        }
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return (who === mIndicator
                || super.verifyDrawable(who))
    }

    fun startAnimation() {
        if (visibility != VISIBLE) {
            return
        }
        if (mIndicator is Animatable) {
            mShouldStartAnimationDrawable = true
        }
        postInvalidate()
    }

    fun stopAnimation() {
        if (mIndicator is Animatable) {
            mIndicator!!.stop()
            mShouldStartAnimationDrawable = false
        }
        postInvalidate()
    }

    override fun setVisibility(v: Int) {
        if (visibility != v) {
            super.setVisibility(v)
            if (v == GONE || v == INVISIBLE) {
                stopAnimation()
            } else {
                startAnimation()
            }
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation()
        } else {
            startAnimation()
        }
    }

    override fun invalidateDrawable(dr: Drawable) {
        if (verifyDrawable(dr)) {
            val dirty: Rect = dr.getBounds()
            val scrollX = scrollX + paddingLeft
            val scrollY = scrollY + paddingTop
            invalidate(
                dirty.left + scrollX, dirty.top + scrollY,
                dirty.right + scrollX, dirty.bottom + scrollY
            )
        } else {
            super.invalidateDrawable(dr)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        updateDrawableBounds(w, h)
    }

    private fun updateDrawableBounds(w: Int, h: Int) {
        // onDraw will translate the canvas so we draw starting at 0,0.
        // Subtract out padding for the purposes of the calculations below.
        var w = w
        var h = h
        w -= paddingRight + paddingLeft
        h -= paddingTop + paddingBottom
        var right = w
        var bottom = h
        var top = 0
        var left = 0
        if (mIndicator != null) {
            // Maintain aspect ratio. Certain kinds of animated drawables
            // get very confused otherwise.
            val intrinsicWidth = mIndicator!!.intrinsicWidth
            val intrinsicHeight = mIndicator!!.intrinsicHeight
            val intrinsicAspect = intrinsicWidth.toFloat() / intrinsicHeight
            val boundAspect = w.toFloat() / h
            if (intrinsicAspect != boundAspect) {
                if (boundAspect > intrinsicAspect) {
                    // New width is larger. Make it smaller to match height.
                    val width = (h * intrinsicAspect).toInt()
                    left = (w - width) / 2
                    right = left + width
                } else {
                    // New height is larger. Make it smaller to match width.
                    val height = (w * (1 / intrinsicAspect)).toInt()
                    top = (h - height) / 2
                    bottom = top + height
                }
            }
            mIndicator!!.setBounds(left, top, right, bottom)
        }
    }

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawTrack(canvas)
    }

    fun drawTrack(canvas: Canvas) {
        val d: Drawable? = mIndicator
        if (d != null) {
            // Translate canvas so a indeterminate circular progress bar with padding
            // rotates properly in its animation
            val saveCount: Int = canvas.save()
            canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
            d.draw(canvas)
            canvas.restoreToCount(saveCount)
            if (mShouldStartAnimationDrawable && d is Animatable) {
                (d as Animatable).start()
                mShouldStartAnimationDrawable = false
            }
        }
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var dw = 0
        var dh = 0
        val d: Drawable? = mIndicator
        if (d != null) {
            dw = Math.max(mMinWidth, Math.min(mMaxWidth, d.getIntrinsicWidth()))
            dh = Math.max(mMinHeight, Math.min(mMaxHeight, d.getIntrinsicHeight()))
        }
        updateDrawableState()
        dw += paddingLeft + paddingRight
        dh += paddingTop + paddingBottom
        val measuredWidth = resolveSizeAndState(dw, widthMeasureSpec, 0)
        val measuredHeight = resolveSizeAndState(dh, heightMeasureSpec, 0)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        updateDrawableState()
    }

    private fun updateDrawableState() {
        val state = drawableState
        if (mIndicator != null && mIndicator!!.isStateful()) {
            mIndicator!!.setState(state)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun drawableHotspotChanged(x: Float, y: Float) {
        super.drawableHotspotChanged(x, y)
        mIndicator?.setHotspot(x, y)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
        removeCallbacks()
    }

    override fun onDetachedFromWindow() {
        stopAnimation()
        // This should come after stopAnimation(), otherwise an invalidate message remains in the
        // queue, which can prevent the entire view hierarchy from being GC'ed during a rotation
        super.onDetachedFromWindow()
        removeCallbacks()
    }

    private fun removeCallbacks() {
        removeCallbacks(mDelayedHide)
        removeCallbacks(mDelayedShow)
    }
}