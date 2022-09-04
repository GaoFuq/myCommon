package com.gfq.common.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 不需要手动切换线程，可以直接在[doSomething]更新UI的定时器。
 * @param condition 定时任务执行条件，true执行，false结束。
 * @param interval 每次执行任务的时间间隔，毫秒。
 * @param startDelay 第一次开始任务时的延迟，毫秒。
 * @param doSomethingBeforeDelay 每次执行任务前的延迟，毫秒。
 * @param context [Dispatchers.IO],[Dispatchers.Main],[Dispatchers.Default]
 */
inline fun CoroutineScope.scheduleAtFixedRate(
    context: CoroutineContext = EmptyCoroutineContext,
    interval: Long,
    startDelay: Long,
    doSomethingBeforeDelay: Long = 0,
    autoCancelOnFinish: Boolean = true,
    crossinline condition: () -> Boolean,
    crossinline doSomething: () -> Unit,
) {
    launch(context) {
        delay(startDelay)
        while (isActive && condition()) {
            if (doSomethingBeforeDelay > 0) {
                delay(doSomethingBeforeDelay)
            }
            doSomething()
            delay(interval)
        }
        if (autoCancelOnFinish) {
            cancel()
        }
    }
}

inline fun FragmentActivity.scheduleAtFixedRate(
    context: CoroutineContext = EmptyCoroutineContext,
    interval: Long,
    startDelay: Long,
    doSomethingBeforeDelay: Long = 0,
    autoCancelOnFinish: Boolean = true,
    crossinline condition: () -> Boolean,
    crossinline doSomething: () -> Unit,
) {
    this.lifecycleScope.scheduleAtFixedRate(
        context,
        interval,
        startDelay,
        doSomethingBeforeDelay,
        autoCancelOnFinish,
        condition,
        doSomething
    )
}

inline fun Fragment.scheduleAtFixedRate(
    context: CoroutineContext = EmptyCoroutineContext,
    interval: Long,
    startDelay: Long,
    doSomethingBeforeDelay: Long = 0,
    autoCancelOnFinish: Boolean = true,
    crossinline condition: () -> Boolean,
    crossinline doSomething: () -> Unit,
) {
    this.lifecycleScope.scheduleAtFixedRate(
        context,
        interval,
        startDelay,
        doSomethingBeforeDelay,
        autoCancelOnFinish,
        condition,
        doSomething
    )
}