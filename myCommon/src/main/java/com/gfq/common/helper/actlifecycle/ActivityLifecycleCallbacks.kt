package com.gfq.common.helper.actlifecycle

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.widget.doAfterTextChanged
import com.gfq.common.system.ActivityManager

/**
 *  2022/11/1 16:01
 * @auth gaofuq
 * @description
 */

inline fun Activity.doOnActivityCreated(
    crossinline action: (activity: Activity,savedInstanceState: Bundle?) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityCreated = action)

inline fun Activity.doOnActivityStarted(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityStarted = action)

inline fun Activity.doOnActivityResumed(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityResumed = action)


inline fun Activity.doOnActivityPaused(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPaused = action)


inline fun Activity.doOnActivityStopped(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityStopped = action)

inline fun Activity.doOnActivityDestroyed(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityDestroyed = action)

//----------- pre
inline fun Activity.doOnActivityPreCreated(
    crossinline action: (activity: Activity,savedInstanceState: Bundle?) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPreCreated = action)

inline fun Activity.doOnActivityPreStarted(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPreStarted = action)

inline fun Activity.doOnActivityPreResumed(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPreResumed = action)


inline fun Activity.doOnActivityPrePaused(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPrePaused = action)


inline fun Activity.doOnActivityPreStopped(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPreStopped = action)

inline fun Activity.doOnActivityPreDestroyed(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPreDestroyed = action)


//----------- Post
inline fun Activity.doOnActivityPostCreated(
    crossinline action: (activity: Activity,savedInstanceState: Bundle?) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPostCreated = action)

inline fun Activity.doOnActivityPostStarted(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPostStarted = action)

inline fun Activity.doOnActivityPostResumed(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPostResumed = action)


inline fun Activity.doOnActivityPostPaused(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPostPaused = action)


inline fun Activity.doOnActivityPostStopped(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPostStopped = action)

inline fun Activity.doOnActivityPostDestroyed(
    crossinline action: (activity: Activity) -> Unit,
): Application.ActivityLifecycleCallbacks = addLifecycleCallback(onActivityPostDestroyed = action)



inline fun Activity.addLifecycleCallback(
    crossinline onActivityCreated: (activity: Activity, savedInstanceState: Bundle?) -> Unit = {_,_->},
    crossinline onActivityPreCreated: (activity: Activity, savedInstanceState: Bundle?) -> Unit = {_,_->},
    crossinline onActivityPostCreated: (activity:Activity,savedInstanceState: Bundle?) -> Unit = {_,_->},
    crossinline onActivityPreStarted: (activity: Activity) -> Unit = {},
    crossinline onActivityStarted: (activity: Activity) -> Unit = {},
    crossinline onActivityPostStarted: (activity: Activity) -> Unit = {},
    crossinline onActivityPreResumed: (activity: Activity) -> Unit = {},
    crossinline onActivityResumed: (activity: Activity) -> Unit = {},
    crossinline onActivityPostResumed: (activity: Activity) -> Unit = {},
    crossinline onActivityPrePaused: (activity: Activity) -> Unit = {},
    crossinline onActivityPaused: (activity: Activity) -> Unit = {},
    crossinline onActivityPostPaused: (activity: Activity) -> Unit = {},
    crossinline onActivityPreStopped: (activity: Activity) -> Unit = {},
    crossinline onActivityStopped: (activity: Activity) -> Unit = {},
    crossinline onActivityPostStopped: (activity: Activity) -> Unit = {},
    crossinline onActivityPreDestroyed: (activity: Activity) -> Unit = {},
    crossinline onActivityDestroyed: (activity: Activity) -> Unit = {},
    crossinline onActivityPostDestroyed: (activity: Activity) -> Unit = {},
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
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                onActivityPreCreated(activity, savedInstanceState)
            }else{
                onActivityCreated(activity, savedInstanceState)
            }
        }

        override fun onActivityPrePaused(activity: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                onActivityPrePaused(activity)
            }else{
                onActivityPaused(activity)
            }
        }

        override fun onActivityPreResumed(activity: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                onActivityPreResumed(activity)
            }else{
                onActivityResumed(activity)
            }
        }

        override fun onActivityPreStarted(activity: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                onActivityPreStarted(activity)
            }else{
                onActivityStarted(activity)
            }
        }

        override fun onActivityPreStopped(activity: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                onActivityPreStopped(activity)
            }else{
                onActivityStopped(activity)
            }
        }

        override fun onActivityPreDestroyed(activity: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                onActivityPreDestroyed(activity)
            }else{
                onActivityDestroyed(activity)
            }
        }

        override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                onActivityPostCreated(activity, savedInstanceState)
            }else{
                onActivityCreated(activity,savedInstanceState)
            }
        }

        override fun onActivityPostPaused(activity: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                onActivityPostPaused(activity)
            } else {
                onActivityPaused(activity)
            }
        }

        override fun onActivityPostResumed(activity: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                onActivityPostResumed(activity)
            } else {
                onActivityResumed(activity)
            }
        }

        override fun onActivityPostStarted(activity: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                onActivityPostStarted(activity)
            } else {
                onActivityStarted(activity)
            }
        }

        override fun onActivityPostStopped(activity: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                onActivityPostStopped(activity)
            } else {
                onActivityStopped(activity)
            }
        }

        override fun onActivityPostDestroyed(activity: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                onActivityPostDestroyed(activity)
            } else {
                onActivityDestroyed(activity)
            }
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.registerActivityLifecycleCallbacks(callbacks)
    } else {
        ActivityManager.application.registerActivityLifecycleCallbacks(callbacks)
    }
    return callbacks
}
