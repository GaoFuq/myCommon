package com.gfq.common.view.drawable

import android.animation.ObjectAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 *  2022/4/7 9:43
 * @auth gaofuq
 * @description
 */
class WaveDrawable : Drawable() {
    private var mBounds: Rect? = null
    private val paint1 = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint2 = Paint(Paint.ANTI_ALIAS_FLAG)
    private var waveAngle1 = 0f
    private var waveAngle2 = 0f

    private var startRadius: Float = 0f
    private var endRadius: Float = 0f

    private val random = Random(10000)


    private var waveRadius1: Float = 0f
        set(value) {
            field = value
            invalidateSelf()
        }

   private var waveRadius2: Float = 0f
        set(value) {
            field = value
            invalidateSelf()
        }



    fun startWave(startRadius: Float, endRadius: Float = startRadius * 2f) {
        this.startRadius = startRadius
        this.endRadius = endRadius
        startWave1()
    }

    private fun startWave1() {
        waveAngle1= random.nextFloat()
        ObjectAnimator.ofFloat(this, "waveRadius1", startRadius, endRadius).run {
            duration = 3000
            start()
            var flag = true
            addUpdateListener {
                waveAngle1 += 0.01f
                paint1.alpha = 255 - ((waveRadius1 - startRadius) / (endRadius - startRadius) * 255).toInt()
                if (flag && it.animatedFraction > 0.65) {
                    flag = false
                    startWave2()
                }
            }
        }
    }

    private fun startWave2() {
        waveAngle2 = random.nextFloat()
        ObjectAnimator.ofFloat(this, "waveRadius2", startRadius, endRadius).run {
            duration = 3000
            start()
            var flag = true
            addUpdateListener {
                waveAngle2 += 0.01f
                paint2.alpha = 255 - ((waveRadius2 - startRadius) / (endRadius - startRadius) * 255).toInt()
                if (flag && it.animatedFraction > 0.65) {
                    flag = false
                    startWave1()
                }
            }
        }
    }


    override fun draw(canvas: Canvas) {
        mBounds?.let {
            drawWave(canvas,waveRadius1,waveAngle1,paint1)
            drawWave(canvas,waveRadius2,waveAngle2,paint2)
        }
    }

    private fun drawWave(canvas: Canvas, radius: Float, angle: Float,paint: Paint) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        val x = mBounds!!.exactCenterX()
        val y = mBounds!!.exactCenterY()
        canvas.drawCircle(x, y, radius, paint)
        val pointX = x + radius * cos(angle * PI / 2)
        val pointY = y + radius * sin(angle * PI / 2)
        paint.style = Paint.Style.FILL
        canvas.drawCircle(pointX.toFloat(), pointY.toFloat(), 10f, paint)
    }

    override fun onBoundsChange(bounds: Rect?) {
        mBounds = bounds
        paint1.color = Color.parseColor("#FFFFB1C0")
        paint2.color = Color.parseColor("#FFFFB1C0")
    }

    override fun setAlpha(alpha: Int) {

    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }
}