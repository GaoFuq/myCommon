@file:Suppress("NOTHING_TO_INLINE")

package com.gfq.common.view

/**
 *  2021/12/30 18:05
 * @auth gaofuq
 * @description
 */
import android.graphics.Paint
import android.widget.TextView

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
