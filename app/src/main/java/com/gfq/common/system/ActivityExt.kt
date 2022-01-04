package com.gfq.common.system

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.gfq.common.createIntent
import java.io.File
import java.lang.Deprecated

/**
 *  2021/12/30 9:29
 * @auth gaofuq
 * @description
 */

inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any?>) {
    startActivity(createIntent<T>(*params))
}

/**
 * 广播通知相册有图片更新
 */
@kotlin.Deprecated("Callers should migrate to inserting items directly into MediaStore, where they will be automatically scanned after each mutation.")
fun Context.sendBroadcastNotifyGalleryUpdate(file: File?) {
    file?.let {
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val uri = Uri.fromFile(it)
        intent.data = uri
        sendBroadcast(intent)
    }
}

fun Context.notifyGalleryUpdate(file: File?) {
    file?.let {
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val uri = Uri.fromFile(it)
        intent.data = uri
        sendBroadcast(intent)
    }
}