package com.quenstin.basicmodel.widget.loading

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable

/**
 * Created by hdyjzgq
 * data on 4/21/21
 * function is ：定义指示器
 */
abstract class Indicator : Drawable(), Animatable {

    companion object {
        val ZERO_BOUNDS_RECT = Rect()
    }

    private val mUpdateListeners = HashMap<ValueAnimator, ValueAnimator.AnimatorUpdateListener>()
    private var mAnimators = ArrayList<ValueAnimator>()
    private var mAlpha = 255
    private var mDrawBounds = ZERO_BOUNDS_RECT
    private var mHasAnimators=false

    private val mPaint = Paint()

    init {
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
    }

    fun getColor(): Int = mPaint.color
    fun setColor(color: Int) {
        mPaint.color = color
    }

    override fun draw(canvas: Canvas) {
        draw(canvas, mPaint)
    }

    abstract fun draw(canvas: Canvas?, paint: Paint?)

    abstract fun onCreateAnimators(): ArrayList<ValueAnimator>

    override fun setAlpha(alpha: Int) {
        mAlpha = alpha
    }

    override fun getAlpha(): Int {
        return mAlpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun start() {
        ensureAnimators()

        if (isStarted()) {
            return;
        }
        startAnimators()
        invalidateSelf()
    }

    override fun stop() {
        stopAnimators()
    }



    /**
     * 开始动画
     */
    private fun startAnimators() {
        mAnimators.forEachIndexed { _, valueAnimator ->
            val updateListener = mUpdateListeners[valueAnimator]
            if (updateListener != null) {
                valueAnimator.addUpdateListener(updateListener)
            }
            valueAnimator.start()
        }
    }

    /**
     * 结束
     */
    private fun stopAnimators(){
        if (mAnimators.isNotEmpty()){
            mAnimators.forEach {
                if (it.isStarted){
                    it.removeAllUpdateListeners()
                    it.end()
                }
            }
        }
    }

    private fun ensureAnimators(){
        if (!mHasAnimators){
            mAnimators=onCreateAnimators()
            mHasAnimators=true
        }
    }

    private fun isStarted(): Boolean {
        for (animator in mAnimators) {
            return animator.isStarted
        }
        return false
    }

    override fun isRunning(): Boolean {
        for (animator in mAnimators) {
            return animator.isRunning
        }
        return false
    }

    /**
     * Your should use this to add AnimatorUpdateListener when
     * create animator , otherwise , animator doesn't work when
     * the animation restart .
     * @param updateListener
     */
    open fun addUpdateListener(
        animator: ValueAnimator?,
        updateListener: ValueAnimator.AnimatorUpdateListener?
    ) {
        mUpdateListeners[animator!!] = updateListener!!
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        setDrawBounds(bounds)
    }

    open fun setDrawBounds(drawBounds: Rect) {
        setDrawBounds(drawBounds.left, drawBounds.top, drawBounds.right, drawBounds.bottom)
    }

    open fun setDrawBounds(left: Int, top: Int, right: Int, bottom: Int) {
        this.mDrawBounds = Rect(left, top, right, bottom)
    }

    open fun postInvalidate() {
        invalidateSelf()
    }

    open fun getDrawBounds(): Rect? {
        return mDrawBounds
    }

    open fun getWidth(): Int {
        return mDrawBounds.width()
    }

    open fun getHeight(): Int {
        return mDrawBounds.height()
    }

    open fun centerX(): Int {
        return mDrawBounds.centerX()
    }

    open fun centerY(): Int {
        return mDrawBounds.centerY()
    }

    open fun exactCenterX(): Float {
        return mDrawBounds.exactCenterX()
    }

    open fun exactCenterY(): Float {
        return mDrawBounds.exactCenterY()
    }
}