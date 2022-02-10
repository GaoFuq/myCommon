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
 * @param offset px 裁剪偏移量，offset > 0 ,扩大裁剪范围，反之缩小。
 */
fun View?.setCircleOutline(offset: Int = 0) {
    if (this == null) return
    clipToOutline = true
    outlineProvider = CircleOutlineProvider(offset)
}
/**
 * 更新 View 的圆形裁剪偏移量。
 * @param offset px 裁剪偏移量，offset > 0 ,扩大裁剪范围，反之缩小。
 */
fun View?.updateCircleOutline(offset: Int) {
    if (this == null) return
    if (outlineProvider != null && outlineProvider is CircleOutlineProvider) {
        val outline = outlineProvider as CircleOutlineProvider
        outline.update(offset)
    }
}

/**
 * 把 View 裁剪为圆角矩形。
 * @param radius 圆角半径 px
 * @param cornerPosition 角的位置
 */
fun View?.setRoundOutline(radius: Float, cornerPosition: CornerPosition = CornerPosition.all) {
    if (this == null) return
    val provider =
        RoundCornerOutlineProvider(radius = radius, cornerPosition = cornerPosition)
    clipToOutline = true
    outlineProvider = provider
}

/**
 * 更新 View 的圆角半径。
 * @param radius 圆角半径 px
 */
fun View?.updateRoundOutline(radius: Float) {
    if (this == null) return
    if (outlineProvider != null && outlineProvider is RoundCornerOutlineProvider) {
        val outline = outlineProvider as RoundCornerOutlineProvider
        outline.update( radius, outline.cornerPosition)
    }
}