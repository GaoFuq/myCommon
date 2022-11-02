package com.gfq.common.helper.actlifecycle

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import com.gfq.common.system.ActivityManager

/**
 *  2022/11/1 16:01
 * @auth gaofuq
 * @description
 */

fun Activity.doOnActivityCreated(
    action: (activity: Activity, savedInstanceState: Bundle?) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityCreated = action)

fun Activity.doOnActivityStarted(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityStarted = action)

fun Activity.doOnActivityResumed(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityResumed = action)


fun Activity.doOnActivityPaused(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPaused = action)


fun Activity.doOnActivityStopped(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityStopped = action)

fun Activity.doOnActivityDestroyed(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityDestroyed = action)

//----------- pre
fun Activity.doOnActivityPreCreated(
    action: (activity: Activity, savedInstanceState: Bundle?) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPreCreated = action)

fun Activity.doOnActivityPreStarted(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPreStarted = action)

fun Activity.doOnActivityPreResumed(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPreResumed = action)


fun Activity.doOnActivityPrePaused(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPrePaused = action)


fun Activity.doOnActivityPreStopped(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPreStopped = action)

fun Activity.doOnActivityPreDestroyed(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPreDestroyed = action)


//----------- Post
fun Activity.doOnActivityPostCreated(
    action: (activity: Activity, savedInstanceState: Bundle?) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPostCreated = action)

fun Activity.doOnActivityPostStarted(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPostStarted = action)

fun Activity.doOnActivityPostResumed(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPostResumed = action)


fun Activity.doOnActivityPostPaused(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPostPaused = action)


fun Activity.doOnActivityPostStopped(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPostStopped = action)

fun Activity.doOnActivityPostDestroyed(
    action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPostDestroyed = action)


fun Activity.addLifecycleCallback(
    onActivityCreated: (activity: Activity, savedInstanceState: Bundle?) -> Unit = { _, _ -> },
    onActivityPreCreated: (activity: Activity, savedInstanceState: Bundle?) -> Unit = { _, _ -> },
    onActivityPostCreated: (activity: Activity, savedInstanceState: Bundle?) -> Unit = { _, _ -> },
    onActivityPreStarted: (activity: Activity) -> Unit = {},
    onActivityStarted: (activity: Activity) -> Unit = {},
    onActivityPostStarted: (activity: Activity) -> Unit = {},
    onActivityPreResumed: (activity: Activity) -> Unit = {},
    onActivityResumed: (activity: Activity) -> Unit = {},
    onActivityPostResumed: (activity: Activity) -> Unit = {},
    onActivityPrePaused: (activity: Activity) -> Unit = {},
    onActivityPaused: (activity: Activity) -> Unit = {},
    onActivityPostPaused: (activity: Activity) -> Unit = {},
    onActivityPreStopped: (activity: Activity) -> Unit = {},
    onActivityStopped: (activity: Activity) -> Unit = {},
    onActivityPostStopped: (activity: Activity) -> Unit = {},
    onActivityPreDestroyed: (activity: Activity) -> Unit = {},
    onActivityDestroyed: (activity: Activity) -> Unit = {},
    onActivityPostDestroyed: (activity: Activity) -> Unit = {},
): Application.ActivityLifecycleCallbacks {
    val callbacks = object : SimpleActivityLifecycleCallbacks() {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            onActivityCreated(activity, savedInstanceState)
        }

        override fun onActivityStarted(activity: Activity) {
            onActivityStarted(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            onActivityResumed(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            onActivityPaused(activity)
        }

        override fun onActivityStopped(activity: Activity) {
            onActivityStopped(activity)
        }

        override fun onActivityDestroyed(activity: Activity) {
            onActivityDestroyed(activity)
        }

        override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
            onActivityPreCreated(activity, savedInstanceState)
        }

        override fun onActivityPrePaused(activity: Activity) {
            onActivityPrePaused(activity)
        }

        override fun onActivityPreResumed(activity: Activity) {
            onActivityPreResumed(activity)
        }

        override fun onActivityPreStarted(activity: Activity) {
            onActivityPreStarted(activity)
        }

        override fun onActivityPreStopped(activity: Activity) {
            onActivityPreStopped(activity)
        }

        override fun onActivityPreDestroyed(activity: Activity) {
            onActivityPreDestroyed(activity)
        }

        override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
            onActivityPostCreated(activity, savedInstanceState)
        }

        override fun onActivityPostPaused(activity: Activity) {
            onActivityPostPaused(activity)
        }

        override fun onActivityPostResumed(activity: Activity) {
            onActivityPostResumed(activity)
        }

        override fun onActivityPostStarted(activity: Activity) {
            onActivityPostStarted(activity)
        }

        override fun onActivityPostStopped(activity: Activity) {
            onActivityPostStopped(activity)
        }

        override fun onActivityPostDestroyed(activity: Activity) {
            onActivityPostDestroyed(activity)
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.registerActivityLifecycleCallbacks(callbacks)
    } else {
        ActivityManager.application.registerActivityLifecycleCallbacks(callbacks)
    }
    return callbacks
}
