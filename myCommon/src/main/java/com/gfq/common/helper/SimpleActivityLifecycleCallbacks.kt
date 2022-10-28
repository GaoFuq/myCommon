package com.gfq.common.helper

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 *  2022/9/15 17:19
 * @auth gaofuq
 * @description
 */
open class SimpleActivityLifecycleCallbacks:Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}