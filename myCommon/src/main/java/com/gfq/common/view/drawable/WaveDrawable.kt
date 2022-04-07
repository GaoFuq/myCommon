package com.gfq.common.view.drawable

import android.animation.ValueAnimator
import android.graphics.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 *  2022/4/7 9:43
 * @auth gaofuq
 * @description
 */


class WaveDrawable : AbsWaveStyleDrawable() {

    override fun update(wave: Wave, anim: ValueAnimator) {
        wave.angle += 0.01f
        wave.radius = (startRadius - 20) + anim.animatedFraction * (endRadius - (startRadius - 20))
        wave.paint.alpha =
            255 - ((wave.radius - (startRadius - 20)) / (endRadius - (startRadius - 20)) * 255).toInt()
    }

    override fun drawWave(canvas: Canvas, bounds: Rect, wave: Wave) {
        wave.paint.style = Paint.Style.STROKE
        wave.paint.strokeWidth = 3f
        val x = bounds.exactCenterX()
        val y = bounds.exactCenterY()
        canvas.drawCircle(x, y, wave.radius, wave.paint)
        val point = CanvasHelper.getPointOnCircle(x, y, wave.radius, wave.angle)
        wave.paint.style = Paint.Style.FILL
        canvas.drawCircle(point.x, point.y, 10f, wave.paint)
    }
}