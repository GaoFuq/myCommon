package com.gfq.common.view.outline

import android.view.View
import com.gfq.common.system.dpF

/**
 *  2022/1/21 10:36
 * @auth gaofuq
 * @description
 */

fun View?.setRoundCorner(dpRadius: Float, direct: Direct = Direct.all) {
    if (this == null) return
    val provider = RoundCornerOutlineProvider(radius = dpF(dpRadius), direct = direct)
    clipToOutline = true
    outlineProvider = provider
}

fun View?.updateCorner(dpRadius: Float, direct: Direct = Direct.all) {
    if (this == null) return
    if (outlineProvider != null && outlineProvider is RoundCornerOutlineProvider) {
        (outlineProvider as RoundCornerOutlineProvider).update(this, dpRadius, direct)
    } else {
        setRoundCorner(dpRadius, direct)
    }
}