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

fun px2dp(context: Context, px: Int): Int {
    val scale = context.resources.displayMetrics.density;
    return (px / scale + 0.5f).toInt()
}

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


fun Int?.nullOrZero(): Boolean = this == null || this == 0
fun Long?.nullOrZero(): Boolean = this?.toInt().nullOrZero()
fun Float?.nullOrZero(): Boolean = this?.toInt().nullOrZero()
fun Double?.nullOrZero(): Boolean = this?.toInt().nullOrZero()


fun String?.nullToDef(def: String = ""): String {
    if (this == null) return def
    return this
}

fun Int?.nullToDef(def: Int = 0): Int {
    if (this == null) return def
    return this
}

fun Long?.nullToDef(def: Long = 0L): Long {
    if (this == null) return def
    return this
}

fun Float?.nullToDef(def: Float = 0f): Float {
    if (this == null) return def
    return this
}

fun Double?.nullToDef(def: Double = 0.0): Double {
    if (this == null) return def
    return this
}
