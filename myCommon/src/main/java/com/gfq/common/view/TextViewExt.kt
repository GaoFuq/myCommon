@file:Suppress("NOTHING_TO_INLINE")
package com.gfq.common.view

/**
 *  2021/12/30 18:05
 * @auth gaofuq
 * @description
 */
import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

inline fun TextView.setDelLine() {
    this.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
}

inline fun TextView.setBold() {
    this.paint.flags = Paint.FAKE_BOLD_TEXT_FLAG
}

inline fun TextView.setUnderLine() {
    this.paint.flags = Paint.UNDERLINE_TEXT_FLAG
}
