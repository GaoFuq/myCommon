package com.gfq.common.helper.actlifecycle

import android.app.Activity
import androidx.fragment.app.FragmentActivity

/**
 *  2022/11/3 15:21
 * @auth gaofuq
 * @description
 */
fun FragmentActivity.doOnCreated(action: () -> Unit) {
    SimpleActivityLifecycleObserver(this, doOnCreated = action)
}

fun FragmentActivity.doOnStarted(action: () -> Unit) {
    SimpleActivityLifecycleObserver(this, doOnStarted = action)
}

fun FragmentActivity.doOnResumed(action: () -> Unit) {
    SimpleActivityLifecycleObserver(this, doOnResumed = action)
}

fun FragmentActivity.doOnPaused(action: () -> Unit) {
    SimpleActivityLifecycleObserver(this, doOnPaused = action)
}

fun FragmentActivity.doOnStopped(action: () -> Unit) {
    SimpleActivityLifecycleObserver(this, doOnStopped = action)
}

fun FragmentActivity.doOnDestroyed(action: () -> Unit) {
    SimpleActivityLifecycleObserver(this, doOnDestroyed = action)
}