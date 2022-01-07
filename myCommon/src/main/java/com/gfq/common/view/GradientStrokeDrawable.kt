package com.gfq.common.view

import android.graphics.*
import android.graphics.drawable.Drawable
import com.gfq.common.system.dp

/**
 * 代码设置 渐变色的边框背景drawable
 */
class GradientStrokeDrawable(val width: Int, val height: Int): Drawable() {

    var mPaint: Paint = Paint()

    var startColor: Int = Color.parseColor("#51BCDF")
        set(value) {
            field = value
            invalidateSelf()
        }

    var endColor: Int = Color.parseColor("#5CE2A3")
        set(value) {
            field = value
            invalidateSelf()
        }

    /**
     * 圆角半径 单位dp
     */
    var radius = dp(0)
        set(value) {
            field = dp(value)
            invalidateSelf()
        }

    /**
     * 边框宽度 单位dp
     */
    var strokeWidth = dp(1)
        set(value) {
            field = dp(value)
            invalidateSelf()
        }

    override fun draw(canvas: Canvas) {
        val halfWidth = strokeWidth / 2f
        val rect = RectF(halfWidth, halfWidth, width.toFloat() - halfWidth,
            height.toFloat() - halfWidth)
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth.toFloat()
        mPaint.shader = LinearGradient(0f, 0f, width.toFloat(), height.toFloat(),
            intArrayOf(startColor, endColor), null, Shader.TileMode.CLAMP)
        canvas.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), mPaint)
    }

    override fun setAlpha(alpha: Int) {
        startColor = alphaColor(startColor, alpha)
        endColor = alphaColor(endColor, alpha)
    }

    private fun alphaColor(color: Int, alpha: Int): Int {
        val r: Int = Color.red(color)
        val g: Int = Color.green(color)
        val b: Int = Color.blue(color)
        return Color.argb(alpha, r, g, b)
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

}