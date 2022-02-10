package com.gfq.common.view

import android.app.Activity
import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.gfq.common.system.dpF
import com.gfq.common.system.isActivityDestroyed
import com.gfq.common.view.outline.setCircleOutline
import com.gfq.common.view.outline.setRoundOutline
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.tencent.bugly.crashreport.CrashReport

/**
 *  2022/2/10 10:48
 * @auth gaofuq
 * @description
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

fun ShapeableImageView.setRoundCorner(radius: Float) {
    shapeAppearanceModel = ShapeAppearanceModel.Builder().setAllCorners(RoundedCornerTreatment())
        .setAllCornerSizes(radius).build()
}

fun ImageView.setRoundCorner(radius: Float) = setRoundOutline(radius)
fun ImageView.setCircle() = setCircleOutline()