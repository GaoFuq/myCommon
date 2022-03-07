package com.gfq.common.system

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.Window
import android.view.WindowManager

/**
 *  2021/12/30 9:29
 * @auth gaofuq
 * @description
 */

inline fun <reified T : Activity> Context.openActivity(vararg params: Pair<String, Any?>) {
    startActivity(createIntent<T>(*params))
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


inline fun Window.updateAttributes(block: (WindowManager.LayoutParams) -> Unit) {
    val params = attributes
    block(params)
    attributes = params
}

fun isActivityDestroyed(mActivity: Activity?): Boolean {
    return mActivity == null || mActivity.isFinishing || mActivity.isDestroyed
}