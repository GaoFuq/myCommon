@file:Suppress("NOTHING_TO_INLINE")

package com.gfq.common.view

/**
 *  2021/12/30 18:05
 * @auth gaofuq
 * @description
 */
import android.graphics.Paint
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat

inline fun TextView.styleDeleteLine() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

inline fun TextView.styleNotDeleteLine() {
    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}


inline fun TextView.styleBold() {
    paintFlags = paintFlags or Paint.FAKE_BOLD_TEXT_FLAG
}

inline fun TextView.styleNotBold() {
    paintFlags = paintFlags and Paint.FAKE_BOLD_TEXT_FLAG.inv()
}

inline fun TextView.styleUnderLine() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

inline fun TextView.styleNotUnderLine() {
    paintFlags = paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
}

/**
 * @param drawablePosition   在[TextView]上调用 drawableStart、drawableTop、drawableEnd、drawableBottom 的标记。
 * 分别对应：1、2、3、4
 * @param width                 图片宽度
 * @param height                图片高度
 */
fun TextView.setDrawable(drawablePosition: Int, @DrawableRes resId: Int, width: Int, height: Int) {
    val drawable =  ResourcesCompat.getDrawable(resources,resId,null)
    drawable?.setBounds(0, 0, width, height)// 这里使用了setCompoundDrawables()方法，必须设置图片大小
    when (drawablePosition) {
        1 -> {
            this.setCompoundDrawables(drawable, null, null, null)
        }
        2 -> {
            this.setCompoundDrawables(null, drawable, null, null)
        }
        3 -> {
            this.setCompoundDrawables(null, null, drawable, null)
        }
        4 -> {
            this.setCompoundDrawables(null, null, null, drawable)
        }
    }
}