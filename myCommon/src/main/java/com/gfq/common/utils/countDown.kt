package com.gfq.common.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * 倒计时的实现
 */
fun FragmentActivity.countDown(
    time: Int = 5,
    start: (scope: CoroutineScope) -> Unit,
    end: () -> Unit,
    next: (time: Int) -> Unit
) {
    lifecycleScope.launch {
        flow {
            (time downTo 0).forEach {
                delay(1000)
                emit(it)
            }
        }.onStart {
            // 倒计时开始 ，在这里可以让Button 禁止点击状态
            start(this@launch)

        }.onCompletion {
            // 倒计时结束 ，在这里可以让Button 恢复点击状态
            end()

        }.catch {
            //错误
        }.collect {
            next(it)
        }
    }
}

fun Fragment.countDown(
    time: Int = 5,
    start: (scope: CoroutineScope) -> Unit,
    end: () -> Unit,
    next: (time: Int) -> Unit
) {
    lifecycleScope.launch {
        flow {
            (time downTo 0).forEach {
                delay(1000)
                emit(it)
            }
        }.onStart {
            // 倒计时开始 ，在这里可以让Button 禁止点击状态
            start(this@launch)
        }.onCompletion {
            // 倒计时结束 ，在这里可以让Button 恢复点击状态
            end()
        }.catch {
            //错误
        }.collect {
            next(it)
        }
    }
}