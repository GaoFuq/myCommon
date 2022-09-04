package com.gfq.common.utils

import android.app.Activity
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import com.gfq.common.R
import com.gyf.immersionbar.ImmersionBar

/**
 *  2022/8/18 17:53
 * @auth gaofuq
 * @description
 */

fun Fragment.setImmerseAndDarkFont(darkFont: Boolean = true) {
    ImmersionBar.with(this).statusBarDarkFont(darkFont).init()
}

fun Fragment.setNotImmerseAndDarkFont(
    darkFont: Boolean = true,
    @ColorRes barColor: Int = R.color.white
) {
    ImmersionBar.with(this)
        .statusBarDarkFont(darkFont)
        .fitsSystemWindows(true)
        .statusBarColor(barColor).init()
}

fun Activity.setImmerseAndDarkFont(darkFont: Boolean = true) {
    ImmersionBar.with(this).statusBarDarkFont(darkFont).init()
}

fun Activity.setNotImmerseAndDarkFont(
    darkFont: Boolean = true,
    @ColorRes barColor: Int = R.color.white
) {
    ImmersionBar.with(this)
        .statusBarDarkFont(darkFont)
        .fitsSystemWindows(true)
        .statusBarColor(barColor).init()
}
