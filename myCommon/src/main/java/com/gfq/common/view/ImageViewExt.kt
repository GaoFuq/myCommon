package com.gfq.common.view

import android.app.Activity
import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.gfq.common.system.isActivityDestroyed
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.*

import com.gfq.common.view.outline.setRoundCorner
import com.gfq.common.view.outline.setCircle

/**
 *  2022/2/10 10:48
 * @auth gaofuq
 * @description
 * 
 * ShapeableImageView 相关方法：
 * @see [setRoundCorner]
 * @see [setCircle]
 * @see [setTopLeftCornerSize]
 * @see [setTopRightCornerSize]
 * @see [setBottomLeftCornerSize]
 * @see [setBottomRightCornerSize]
 */

fun ImageView.setGif(resourceId: Int, placeholder: Int = 0, error: Int = 0) {
    Glide.with(this).asGif().load(resourceId).placeholder(placeholder).error(error).into(this)
}

fun ImageView.setImage(imgSource: Any?, placeholder: Int = 0, error: Int = 0) {
    if (this.context is Activity) {
        if (isActivityDestroyed(this.context as Activity)) return
    }

    if (imgSource is String && imgSource.endsWith(".gif", true)) {
        Glide.with(this).asGif().load(imgSource).placeholder(placeholder).error(error).into(this)
        return
    }

    if (imgSource is String && imgSource.startsWith("data:image/png;base64")) {
        val base64 = imgSource.split(",")[1]
        val imageByteArray: ByteArray = Base64.decode(base64, Base64.DEFAULT)
        Glide.with(context).load(imageByteArray).placeholder(placeholder).error(error).into(this)
        return
    }

    Glide.with(this).load(imgSource).placeholder(placeholder).error(error).into(this)
}


/**
 * 单独设置左上角的圆角半径，其他角的样式和大小保持原来的不变
 * @see [AbsoluteCornerSize]
 * @see [RelativeCornerSize]
 */
fun ShapeableImageView.setTopLeftCornerSize(cornerSize: CornerSize) {
    shapeAppearanceModel = shapeAppearanceModel.toBuilder()
        .setTopLeftCornerSize(cornerSize)
        .build()
}

/**
 * 单独设置右上角的圆角半径，其他角的样式和大小保持原来的不变
 * @see [AbsoluteCornerSize]
 * @see [RelativeCornerSize]
 */
fun ShapeableImageView.setTopRightCornerSize(cornerSize: CornerSize) {
    shapeAppearanceModel = shapeAppearanceModel.toBuilder()
        .setTopRightCornerSize(cornerSize)
        .build()
}

/**
 * 单独设置左下角的圆角半径，其他角的样式和大小保持原来的不变
 * @see [AbsoluteCornerSize]
 * @see [RelativeCornerSize]
 */
fun ShapeableImageView.setBottomLeftCornerSize(cornerSize: CornerSize) {
    shapeAppearanceModel = shapeAppearanceModel.toBuilder()
        .setBottomLeftCornerSize(cornerSize)
        .build()
}

/**
 * 单独设置右下角的圆角半径，其他角的样式和大小保持原来的不变
 * @see [AbsoluteCornerSize]
 * @see [RelativeCornerSize]
 */
fun ShapeableImageView.setBottomRightCornerSize(cornerSize: CornerSize) {
    shapeAppearanceModel = shapeAppearanceModel.toBuilder()
        .setBottomRightCornerSize(cornerSize)
        .build()
}

