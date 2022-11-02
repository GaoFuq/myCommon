package com.gfq.common.helper.actlifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.gfq.common.system.ActivityManager

/**
 *  2022/9/15 17:19
 * @auth gaofuq
 * @description
 */
open class SimpleActivityLifecycleCallbacks(
    private val needLog: Boolean = false,
    private val logTAG: String = "SimpleActivityLifecycle",
) : Application.ActivityLifecycleCallbacks {
    /**
     * Called as the first step of the Activity being created. This is always called before
     * [Activity.onCreate].
     */
    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        if(needLog) {
            Log.d(logTAG, "onActivityPreCreated: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called when the Activity calls [super.onCreate()][Activity.onCreate].
     */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if(needLog) {
            Log.d(logTAG, "onActivityCreated: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called as the last step of the Activity being created. This is always called after
     * [Activity.onCreate].
     */
    override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
        if(needLog) {
            Log.d(logTAG, "onActivityPostCreated: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called as the first step of the Activity being started. This is always called before
     * [Activity.onStart].
     */
    override fun onActivityPreStarted(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityPreStarted: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called when the Activity calls [super.onStart()][Activity.onStart].
     */
    override fun onActivityStarted(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityStarted: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called as the last step of the Activity being started. This is always called after
     * [Activity.onStart].
     */
    override fun onActivityPostStarted(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityPostStarted: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called as the first step of the Activity being resumed. This is always called before
     * [Activity.onResume].
     */
    override fun onActivityPreResumed(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityPreResumed: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called when the Activity calls [super.onResume()][Activity.onResume].
     */
    override fun onActivityResumed(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityResumed: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called as the last step of the Activity being resumed. This is always called after
     * [Activity.onResume] and [Activity.onPostResume].
     */
    override fun onActivityPostResumed(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityPostResumed: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called as the first step of the Activity being paused. This is always called before
     * [Activity.onPause].
     */
    override fun onActivityPrePaused(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityPrePaused: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called when the Activity calls [super.onPause()][Activity.onPause].
     */
    override fun onActivityPaused(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityPaused: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called as the last step of the Activity being paused. This is always called after
     * [Activity.onPause].
     */
    override fun onActivityPostPaused(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityPostPaused: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called as the first step of the Activity being stopped. This is always called before
     * [Activity.onStop].
     */
    override fun onActivityPreStopped(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityPreStopped: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called when the Activity calls [super.onStop()][Activity.onStop].
     */
    override fun onActivityStopped(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityStopped: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called as the last step of the Activity being stopped. This is always called after
     * [Activity.onStop].
     */
    override fun onActivityPostStopped(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityPostStopped: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called as the first step of the Activity saving its instance state. This is always
     * called before [Activity.onSaveInstanceState].
     */
    override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
        if(needLog) {
            Log.d(logTAG, "onActivityPreSaveInstanceState: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called when the Activity calls
     * [super.onSaveInstanceState()][Activity.onSaveInstanceState].
     */
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        if(needLog) {
            Log.d(logTAG, "onActivitySaveInstanceState: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called as the last step of the Activity saving its instance state. This is always
     * called after[Activity.onSaveInstanceState].
     */
    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
        if(needLog) {
            Log.d(logTAG, "onActivityPostSaveInstanceState: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called as the first step of the Activity being destroyed. This is always called before
     * [Activity.onDestroy].
     */
    override fun onActivityPreDestroyed(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityPreDestroyed: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called when the Activity calls [super.onDestroy()][Activity.onDestroy].
     */
    override fun onActivityDestroyed(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityDestroyed: ${activity.javaClass.simpleName}")
        }
    }

    /**
     * Called as the last step of the Activity being destroyed. This is always called after
     * [Activity.onDestroy].
     */
    override fun onActivityPostDestroyed(activity: Activity) {
        if(needLog) {
            Log.d(logTAG, "onActivityPostDestroyed: ${activity.javaClass.simpleName}")
        }
    }
}