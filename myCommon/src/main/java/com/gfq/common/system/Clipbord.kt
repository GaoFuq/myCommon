package com.gfq.common.system

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ClipboardManager.OnPrimaryClipChangedListener
import android.content.Context
import androidx.core.content.getSystemService

/**
 *  2022/1/6 14:35
 * @auth gaofuq
 * @description
 */
fun Context?.copyText2Clipboard(text: CharSequence?) =
    this?.getSystemService<ClipboardManager>()
        ?.setPrimaryClip(ClipData.newPlainText(packageName, text))


fun Context?.copyText2Clipboard(label: CharSequence?, text: CharSequence?) =
    this?.getSystemService<ClipboardManager>()?.setPrimaryClip(ClipData.newPlainText(label, text))


fun Context?.clearClipboard() =
    this?.getSystemService<ClipboardManager>()?.setPrimaryClip(ClipData.newPlainText(null, ""))


fun Context?.getClipboardLabel(): CharSequence =
    this?.getSystemService<ClipboardManager>()?.primaryClipDescription?.label ?: ""

/**
 * Return the text for clipboard.
 *
 * @return the text for clipboard
 */
fun Context?.getClipboardText(): CharSequence {
    val clip = this?.getSystemService<ClipboardManager>()?.primaryClip
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
fun Context?.addPrimaryClipChangedListener(listener: OnPrimaryClipChangedListener?) =
    this?.getSystemService<ClipboardManager>()?.addPrimaryClipChangedListener(listener)


/**
 * Remove the clipboard changed listener.
 */
fun Context?.removePrimaryClipChangedListener(listener: OnPrimaryClipChangedListener?) =
    this?.getSystemService<ClipboardManager>()?.removePrimaryClipChangedListener(listener)

