package com.gfq.common.helper.actlifecycle

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 *  2022/11/2 9:22
 * @auth gaofuq
 * @description
 */
class SimpleActivityLifeObserver(
    activity: FragmentActivity,
    private val doOnCreated: () -> Unit = {},
    private val doOnStarted: () -> Unit = {},
    private val doOnResumed: () -> Unit = {},
    private val doOnStopped: () -> Unit = {},
    private val doOnPaused: () -> Unit = {},
    private val doOnDestroyed: () -> Unit = {},
) : LifecycleObserver {

    init {
        activity.lifecycle.addObserver(this)
    }

    private val TAG = "SimpleActivityLifeObserver"

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onActivityCreated() {
        doOnCreated()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onActivityStarted() {
        doOnStarted()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onActivityResumed() {
        doOnResumed()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onActivityPaused() {
        doOnPaused()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onActivityStopped() {
        doOnStopped()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onActivityDestroyed() {
        doOnDestroyed()
    }
}