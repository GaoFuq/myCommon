package com.gfq.common.view.custom

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import com.gfq.common.R
import com.gfq.common.base.BaseBindingView
import com.gfq.common.databinding.RichItemLayoutBinding

/**
 *  2022/3/8 15:17
 * @auth gaofuq
 * @description
 */
class RichItemLayout(context: Context, attrs: AttributeSet?) :
    BaseBindingView<RichItemLayoutBinding>(context, attrs) {

    override fun layoutResId(): Int = R.layout.rich_item_layout

    init {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.RichItemLayout)
        val icon = arr.getDrawable(R.styleable.RichItemLayout_richItem_icon)
        val rightArrow = arr.getDrawable(R.styleable.RichItemLayout_richItem_rightArrow)

        val title = arr.getString(R.styleable.RichItemLayout_richItem_title)
        val titleSize = arr.getDimension(R.styleable.RichItemLayout_richItem_titleSize, 16f)
        val titleColor = arr.getColor(R.styleable.RichItemLayout_richItem_titleColor, Color.BLACK)
        val titleBold = arr.getBoolean(R.styleable.RichItemLayout_richItem_titleBold, false)

        val content = arr.getString(R.styleable.RichItemLayout_richItem_content)
        val contentSize = arr.getDimension(R.styleable.RichItemLayout_richItem_contentSize, 16f)
        val contentColor =
            arr.getColor(R.styleable.RichItemLayout_richItem_contentColor, Color.GRAY)
        val contentBold = arr.getBoolean(R.styleable.RichItemLayout_richItem_contentBold, true)
        val contentHint = arr.getString(R.styleable.RichItemLayout_richItem_contentHint)
        val contentGravity = arr.getInt(R.styleable.RichItemLayout_richItem_contentGravity, 1)



        vBinding.run {
            ivIcon.setImageDrawable(icon)
            ivArrow.setImageDrawable(rightArrow)

            tvTitle.run {
                text = title
                setTextColor(titleColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize)
                typeface = setTypeFace(titleBold)
            }

            tvContent.run {
                text = content
                setTextColor(contentColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, contentSize)
                typeface = setTypeFace(contentBold)
                contentHint?.let { hint = it }
            }

            if (contentGravity == 0) {
                tvContent.gravity = Gravity.START
            } else {
                tvContent.gravity = Gravity.END
            }
        }

        arr.recycle()
    }


    private fun setTypeFace(isBold: Boolean): Typeface {
        return if (isBold) {
            Typeface.DEFAULT_BOLD
        } else {
            Typeface.DEFAULT
        }
    }
}