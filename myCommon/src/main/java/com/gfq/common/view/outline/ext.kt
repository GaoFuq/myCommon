package com.gfq.common.view.outline

import android.view.View
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapeAppearanceModel.PILL

/**
 *  2022/1/21 10:36
 * @auth gaofuq
 * @description
 */


/**
 * 设置View为圆形裁剪 或 更新裁剪偏移量。
 * 以 View 的正中间为圆心裁剪为圆形。
 * @param offset px 裁剪偏移量，offset > 0 ,扩大裁剪范围，反之缩小。
 */
fun View.setCircleOutline(offset: Int=0) {
    if(outlineProvider==null||outlineProvider !is CircleOutlineProvider){
        clipToOutline = true
        outlineProvider = CircleOutlineProvider(offset)
    }else {
        val outline = outlineProvider as CircleOutlineProvider
        outline.update(offset)
    }
}


/**
 * 设置/更新 View 的圆角半径。
 * @param radius 圆角半径 px
 */
fun View.setRoundOutline(radius: Float,cornerPosition: CornerPosition = CornerPosition.all) {
    if(outlineProvider==null||outlineProvider !is RoundCornerOutlineProvider){
        val provider = RoundCornerOutlineProvider(radius = radius, cornerPosition = cornerPosition)
        clipToOutline = true
        outlineProvider = provider
    }else {
        val outline = outlineProvider as RoundCornerOutlineProvider
        outline.update(radius, outline.cornerPosition)
    }
}

/**
 * 设置View为圆角矩形。
 * 如果View是[ShapeableImageView],则所有角都做圆角处理。
 * 其他View通过设置outline实现。
 */
fun View.setRoundCorner(radius: Float) {
    if (this is ShapeableImageView) {
        shapeAppearanceModel =
            shapeAppearanceModel.toBuilder().setAllCorners(RoundedCornerTreatment())
                .setAllCornerSizes(radius).build()
    } else {
        setRoundOutline(radius)
    }
}

/**
 * 设置View为圆形
 * 如果View是[ShapeableImageView],则所有角都做圆角处理。
 * 其他View通过设置outline实现。
 */
fun View.setCircle() {
    if (this is ShapeableImageView) {
        shapeAppearanceModel =
            shapeAppearanceModel.toBuilder().setAllCorners(RoundedCornerTreatment())
                .setAllCornerSizes(PILL).build()
    } else {
        setCircleOutline()
    }
}