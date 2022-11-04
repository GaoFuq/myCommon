package com.gfq.common.system

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.gfq.common.helper.actlifecycle.doOnDestroyed

/**
 *  2021/12/30 9:29
 * @auth gaofuq
 * @description
 */

inline fun <reified T : Activity> Context.openActivity(bundle: Bundle? = null) {
    startActivity(createIntent<T>(bundle))
}


inline fun <reified T : Activity> Fragment.openActivity(bundle: Bundle? = null) {
    startActivity(this.context?.createIntent<T>(bundle))
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

fun Context.activity(): Activity? {
    return if (this is Activity) {
        this
    } else if (this is ContextWrapper) {
        if (this.baseContext is Activity) {
            this.baseContext as Activity
        }else {
            null
        }
    } else {
        null
    }
}

fun Context.fragmentActivity(): FragmentActivity? {
    return if (this is FragmentActivity) {
        this
    } else if (this is ContextWrapper) {
        if (this.baseContext is FragmentActivity) {
            this.baseContext as FragmentActivity
        }else {
            null
        }
    } else {
        null
    }
}