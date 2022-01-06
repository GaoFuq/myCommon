package com.gfq.common.system

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ClipboardManager.OnPrimaryClipChangedListener
import android.content.Context

/**
 *  2022/1/6 14:35
 * @auth gaofuq
 * @description
 */
fun Context.copyText2Clipboard(text: CharSequence?) {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    cm?.setPrimaryClip(ClipData.newPlainText(packageName, text))
}

fun Context.copyText2Clipboard(label: CharSequence?, text: CharSequence?) {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    cm?.setPrimaryClip(ClipData.newPlainText(label, text))
}

fun Context.clearClipboard() {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    cm?.setPrimaryClip(ClipData.newPlainText(null, ""))
}

fun Context.getClipboardLabel(): CharSequence {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val des = cm?.primaryClipDescription
    return des?.label ?: ""
}

/**
 * Return the text for clipboard.
 *
 * @return the text for clipboard
 */
fun Context.getClipboardText(): CharSequence {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clip = cm?.primaryClip
    if (clip != null && clip.itemCount > 0) {
        val text = clip.getItemAt(0).coerceToText(this)
        if (text != null) {
            return text
        }
    }
    return ""
}

/**
 * Add the clipboard changed listener.
 */
fun Context.addPrimaryClipChangedListener(listener: OnPrimaryClipChangedListener?) {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    cm?.addPrimaryClipChangedListener(listener)
}

/**
 * Remove the clipboard changed listener.
 */
fun Context.removePrimaryClipChangedListener(listener: OnPrimaryClipChangedListener?) {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    cm?.removePrimaryClipChangedListener(listener)
}
