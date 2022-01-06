package com.gfq.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.gfq.common.R

/**
 * 圆角 FrameLayout
 * Created by zb on 2018/6/28.
 */

class CornersRelativeLayout : RelativeLayout {

    private var defCornerRadius = 0f //圆角半径像素值
    private var leftTopCornerRadius = 0f
    private var rightTopCornerRadius = 0f
    private var leftBottomCornerRadius = 0f
    private var rightBottomCornerRadius = 0f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initStyle(context, attrs, 0)
    }

    @SuppressLint("CustomViewStyleable")
    private fun initStyle(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.CornersLayout, defStyleAttr, 0
        )
        defCornerRadius = a.getDimension(R.styleable.CornersLayout_layoutRadius, defCornerRadius)
        leftTopCornerRadius =
            a.getDimension(R.styleable.CornersLayout_topLeftRadius, defCornerRadius)
        rightTopCornerRadius =
            a.getDimension(R.styleable.CornersLayout_topRightRadius, defCornerRadius)
        leftBottomCornerRadius =
            a.getDimension(R.styleable.CornersLayout_bottomLeftRadius, defCornerRadius)
        rightBottomCornerRadius =
            a.getDimension(R.styleable.CornersLayout_bottomRightRadius, defCornerRadius)
        a.recycle()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initStyle(context, attrs, defStyleAttr)
    }


    fun setRadius(
        leftTopCornerRadius: Float,
        rightTopCornerRadius: Float,
        rightBottomCornerRadius: Float,
        leftBottomCornerRadius: Float
    ) {
        this.leftTopCornerRadius = leftTopCornerRadius
        this.rightTopCornerRadius = rightTopCornerRadius
        this.leftBottomCornerRadius = leftBottomCornerRadius
        this.rightBottomCornerRadius = rightBottomCornerRadius
        invalidate()
    }

    override fun dispatchDraw(canvas: Canvas) {
        val path = Path()
        val rectF = RectF(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            (width - paddingRight).toFloat(),
            (height - paddingBottom).toFloat()
        )
        val radii = floatArrayOf(
            leftTopCornerRadius,
            leftTopCornerRadius,
            rightTopCornerRadius,
            rightTopCornerRadius,
            leftBottomCornerRadius,
            leftBottomCornerRadius,
            rightBottomCornerRadius,
            rightBottomCornerRadius
        )
        path.addRoundRect(rectF, radii, Path.Direction.CW)

        canvas.drawFilter =
            PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        // 先对canvas进行裁剪
        canvas.clipPath(path)

        super.dispatchDraw(canvas)
    }
}