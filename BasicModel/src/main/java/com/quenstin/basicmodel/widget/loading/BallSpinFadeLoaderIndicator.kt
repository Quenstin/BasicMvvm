package com.quenstin.basicmodel.widget.loading

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint

/**
 * Created by hdyjzgq
 * data on 4/21/21
 * function is ：
 */
class BallSpinFadeLoaderIndicator : Indicator() {

    companion object {
        val SCALE = 1.0f
        val ALPHA = 255
    }

    private val scaleFloats = arrayOf(SCALE, SCALE, SCALE, SCALE, SCALE, SCALE, SCALE, SCALE)
    private val alphas = arrayOf(ALPHA, ALPHA, ALPHA, ALPHA, ALPHA, ALPHA, ALPHA, ALPHA)



    override fun draw(canvas: Canvas?, paint: Paint?) {
        val radius=getWidth()/10
        scaleFloats.forEachIndexed { index, _ ->
            canvas?.apply {
                save()
                val point=circleAt(width, height, width / 2 - radius, index * (Math.PI / 4))
                if (point != null) {
                    translate(point.x, point.y)
                }
                scale(scaleFloats[index], scaleFloats[index])
                paint?.alpha=alphas[index]
                if (paint != null) {
                    drawCircle(0f, 0f, radius.toFloat(), paint)
                }
                restore()
            }
        }
    }

    override fun onCreateAnimators(): ArrayList<ValueAnimator> {
        val animators: ArrayList<ValueAnimator> = ArrayList()
        val delays = intArrayOf(0, 120, 240, 360, 480, 600, 720, 780, 840)
        for (i in 0..7) {
            val scaleAnim = ValueAnimator.ofFloat(1f, 0.4f, 1f)
            scaleAnim.duration = 1000
            scaleAnim.repeatCount = -1
            scaleAnim.startDelay = delays[i].toLong()
            addUpdateListener(scaleAnim) { animation ->
                scaleFloats[i] = animation.animatedValue as Float
                postInvalidate()
            }
            val alphaAnim = ValueAnimator.ofInt(255, 77, 255)
            alphaAnim.duration = 1000
            alphaAnim.repeatCount = -1
            alphaAnim.startDelay = delays[i].toLong()
            addUpdateListener(alphaAnim) { animation ->
                alphas[i] = animation.animatedValue as Int
                postInvalidate()
            }
            animators.add(scaleAnim)
            animators.add(alphaAnim)
        }
        return animators
    }

    private fun circleAt(width: Int, height: Int, radius: Int, angle: Double): Point? {
        val x = (width / 2 + radius * Math.cos(angle)).toFloat()
        val y = (height / 2 + radius * Math.sin(angle)).toFloat()
        return Point(x, y)
    }

    class Point(var x: Float, var y: Float)
}