package com.gfq.common.view.outline

import android.view.View

/**
 *  2022/1/21 10:36
 * @auth gaofuq
 * @description
 */

/**
 * 把 View 裁剪为圆形。
 * 以 View 的正中间为圆心裁剪。
 * @param offset 裁剪偏移量，offset > 0 ,扩大裁剪范围，反之缩小。
 */
fun View?.setCircleOutline(offset: Int = 0) {
    if (this == null) return
    clipToOutline = true
    outlineProvider = CircleOutlineProvider(offset)
}

/**
 * 把 View 裁剪为圆角矩形。
 */
fun View?.setRoundOutline(radius: Float, cornerPosition: CornerPosition = CornerPosition.all) {
    if (this == null) return
    val provider =
        RoundCornerOutlineProvider(radius = radius, cornerPosition = cornerPosition)
    clipToOutline = true
    outlineProvider = provider
}

/**
 * 更新 View 圆角半径和圆角位置。
 */
fun View?.updateRoundOutline(radius: Float, cornerPosition: CornerPosition = CornerPosition.all) {
    if (this == null) return
    if (outlineProvider != null && outlineProvider is RoundCornerOutlineProvider) {
        (outlineProvider as RoundCornerOutlineProvider).update(this, radius, cornerPosition)
    } else {
        setRoundOutline(radius, cornerPosition)
    }
}