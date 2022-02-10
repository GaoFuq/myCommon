package com.gfq.common.view

import android.app.Activity
import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.gfq.common.system.isActivityDestroyed
import com.tencent.bugly.crashreport.CrashReport

/**
 *  2022/2/10 10:48
 * @auth gaofuq
 * @description
 */

fun ImageView?.setGif(url: String) {
    this?.let { Glide.with(it).asGif().load(url).into(it) }
}

fun ImageView?.setGif(id: Int) {
    this?.let { Glide.with(it).asGif().load(id).into(it) }
}

fun ImageView?.setImage(imgSource: Any?) {
    if (this == null) return
    if (imgSource == null) return
    try {
        if (this.context is Activity) {
            if (isActivityDestroyed(this.context as Activity)) return
        }

        if (imgSource is Int) {
            this.setImageResource(imgSource)
            if (imgSource == 0) return
        }

        if (imgSource is String && imgSource.endsWith(".gif", true)) {
            Glide.with(context).asGif().load(imgSource).into(this)
            return
        }

        if (imgSource is String && imgSource.startsWith("data:image/png;base64")) {
            val base64 = imgSource.split(",")[1]
            val imageByteArray: ByteArray = Base64.decode(base64, Base64.DEFAULT)
            Glide.with(context).load(imageByteArray).into(this)
            return
        }

        Glide.with(context).load(imgSource).into(this)
    } catch (e: Exception) {
        CrashReport.postCatchedException(e)
    }
}
