package com.gfq.common.system

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.orhanobut.logger.Logger
import java.io.File

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

private val homePressedReceiver by lazy {
    HomePressedReceiver()
}

fun Context?.registerHomePressedReceiver(
    onHomePressed: (() -> Unit)? = null,
    onHomeLongPressed: (() -> Unit)? = null,
) {
    if (this == null) return
    val filter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
    homePressedReceiver.onHomePressed = onHomePressed
    homePressedReceiver.onHomeLongPressed = onHomeLongPressed
    registerReceiver(homePressedReceiver, filter);
}

fun Context?.unRegisterHomePressedReceiver() {
    if (this == null) return
    unregisterReceiver(homePressedReceiver)
}

fun toast(msg: String?) {
    Toast.makeText(ActivityManager.application, msg, Toast.LENGTH_SHORT).show()
}

fun log(msg: String?) {
    Logger.e(msg ?: "null")
}

inline fun Window.updateAttributes(block: (WindowManager.LayoutParams) -> Unit) {
    val params = attributes
    block(params)
    attributes = params
}