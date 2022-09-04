package com.gfq.common.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment

fun Context.dpF(@DimenRes id: Int): Float = this.resources.getDimension(id)
fun Context.dp(@DimenRes id: Int): Int = dpF(id).toInt()

fun Fragment.dp(@DimenRes id: Int): Int = this.context?.dpF(id)?.toInt() ?: 0
fun Fragment.dpF(@DimenRes id: Int): Float = this.context?.dpF(id)?:0F

fun View.dp(@DimenRes id: Int): Int = this.context?.dpF(id)?.toInt() ?: 0
fun View.dpF(@DimenRes id: Int): Float = this.context?.dpF(id)?:0F


@Deprecated(message = "废弃，因为不能适配不同的屏幕密度", replaceWith = ReplaceWith("dpF(@DimenRes id: Int)"))
internal fun dpF(n: Number?): Float {
    if (n == null) return 0f
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        n.toFloat(),
        Resources.getSystem().displayMetrics
    )
}

@Deprecated(message = "废弃，因为不能适配不同的屏幕密度", replaceWith = ReplaceWith("dp(@DimenRes id: Int)"))
internal fun dp(n: Number?): Int = dpF(n).toInt()
