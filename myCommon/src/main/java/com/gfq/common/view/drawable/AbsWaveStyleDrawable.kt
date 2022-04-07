package com.gfq.common.view.drawable

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import kotlin.random.Random

/**
 *  2022/4/7 9:43
 * @auth gaofuq
 * @description
 */
/**
 *  类似波纹样式的动画抽象类。
 * 多个相同的动画，按时间延迟，依次做无限循环播放。
 */
abstract class AbsWaveStyleDrawable : Drawable() {
    private var mBounds: Rect? = null

    protected var startRadius: Float = 0f
    protected var endRadius: Float = 0f

    private val random = Random(10000)

    protected val waveList = mutableListOf<Wave>()
    private var waveCount = 0
    private var waveColor = 0
    protected var waveDuration = 3000L

    private var wave: Float = 0f
        set(value) {
            field = value
            invalidateSelf()
        }

    abstract fun update(wave: Wave, anim: ValueAnimator)
    abstract fun drawWave(canvas: Canvas, bounds: Rect, wave: Wave)

    open fun startWave(
        startRadius: Float,
        endRadius: Float = startRadius * 2f,
        waveDuration: Long = 3000L,
        color: Int = Color.parseColor("#FFFFB1C0"),
        waveCount: Int = 3,
    ) {
        this.startRadius = startRadius
        this.endRadius = endRadius
        this.waveDuration = waveDuration
        this.waveCount = waveCount
        this.waveColor = color
        startWave()
    }


    private fun startWave() {
        repeat(waveCount) {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = waveColor
            val wave = Wave(0f, random.nextFloat(), paint)
            waveList.add(wave)

            ObjectAnimator.ofFloat(this, "wave", startRadius - 20, endRadius).run {
                duration = waveDuration
                repeatCount = -1
                startDelay = waveDuration / waveCount * it
                start()
                addUpdateListener { anim -> update(wave, anim) }
            }
        }
    }


    override fun draw(canvas: Canvas) {
        mBounds?.let {
            waveList.forEach {
                drawWave(canvas, mBounds!!, it)
            }
        }
    }


    override fun onBoundsChange(bounds: Rect?) {
        mBounds = bounds
    }

    override fun setAlpha(alpha: Int) {

    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }
}