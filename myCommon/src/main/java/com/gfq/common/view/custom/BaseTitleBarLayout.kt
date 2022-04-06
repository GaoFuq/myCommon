package com.gfq.common.view.custom

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.gfq.common.R
import com.gfq.common.base.BaseBindingView
import com.gfq.common.databinding.BaseTitleBarLayoutBinding

/**
 *  2022/3/7 16:45
 * @auth gaofuq
 * @description
 */
class BaseTitleBarLayout(context: Context, attrs: AttributeSet?) :
    BaseBindingView<BaseTitleBarLayoutBinding>(context, attrs) {

    override fun layoutResId(): Int = R.layout.base_title_bar_layout

    var rightTextView: TextView? = null
    var rightImageView: ImageView? = null

    init {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.BaseTitleBarLayout)

        val backIcon = arr.getResourceId(R.styleable.BaseTitleBarLayout_titleBar_backIcon, 0)

        val title = arr.getString(R.styleable.BaseTitleBarLayout_titleBar_title)
        val titleColor =
            arr.getColor(R.styleable.BaseTitleBarLayout_titleBar_titleColor, Color.BLACK)
        val titleSize = arr.getDimensionPixelSize(R.styleable.BaseTitleBarLayout_titleBar_titleSize, 16)
        val titleBold = arr.getBoolean(R.styleable.BaseTitleBarLayout_titleBar_titleBold, true)
        val titleBackground =
            arr.getDrawable(R.styleable.BaseTitleBarLayout_titleBar_titleBackground)

        val rightText = arr.getString(R.styleable.BaseTitleBarLayout_titleBar_rightText)
        val rightTextColor =
            arr.getColor(R.styleable.BaseTitleBarLayout_titleBar_rightTextColor, Color.BLACK)
        val rightTextSize =
            arr.getDimensionPixelSize(R.styleable.BaseTitleBarLayout_titleBar_rightTextSize, 14)
        val rightTextBold =
            arr.getBoolean(R.styleable.BaseTitleBarLayout_titleBar_rightTextBold, false)
        val rightTextBackground =
            arr.getDrawable(R.styleable.BaseTitleBarLayout_titleBar_rightTextBackground)

        val rightIcon = arr.getResourceId(R.styleable.BaseTitleBarLayout_titleBar_rightIcon, 0)

        arr.recycle()

        vBinding.run {
            ivTitleBarBack.setImageResource(backIcon)
            tvTitleBarTitle.run {
                text = title
                setTextColor(titleColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX,titleSize.toFloat())
                typeface = setTypeFace(titleBold)
                background = titleBackground
            }

            rightText?.let {
                addViewToRight(TextView(context).apply {
                    text = rightText
                    setTextColor(rightTextColor)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX,rightTextSize.toFloat())
                    typeface = setTypeFace(rightTextBold)
                    background = rightTextBackground
                    gravity = Gravity.CENTER
                    rightTextView = this
                })
            }

            if (rightIcon > 0) {
                addViewToRight(ImageView(context).apply {
                    rightImageView = this
                    setImageResource(rightIcon)
                })
            }
        }



        if (context is Activity) {
            vBinding.ivTitleBarBack.setOnClickListener { context.finish() }
        }
    }


    private fun setTypeFace(isBold: Boolean): Typeface {
        return if (isBold) {
            Typeface.DEFAULT_BOLD
        } else {
            Typeface.DEFAULT
        }
    }



    fun addViewToRight(view: View) {
        vBinding.titleBarRightContainer.removeAllViews()
        vBinding.titleBarRightContainer.addView(view, -2, -1)
    }

}