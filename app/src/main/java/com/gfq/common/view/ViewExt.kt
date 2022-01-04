package com.gfq.common

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

/**
 *  2021/12/29 11:55
 * @auth gaofuq
 * @description
 */

/**
 * 显示view
 */
fun View.visible() {
    if (!this.isVisible) {
        this.isVisible = true
    }
}

/**
 * 隐藏view
 */
fun View.gone() {
    if (!this.isGone) {
        this.isGone = true
    }
}

/**
 * 不显示view
 */
fun View.invisible() {
    if (!this.isInvisible) {
        this.isInvisible = true
    }
}


/**
 * 点击事件防抖动
 */
inline fun View.setDebounceClick(limitTime: Long = 500L, crossinline onClick: () -> Unit) {
    var lastClickTime = 0L
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if( currentTime - lastClickTime >limitTime) {
            lastClickTime = currentTime
            onClick()
        }
    }
}


/**
 * 设置多次点击监听，连击一定次数后触发clickListener
 *
 * @param interval      两次点击的时间间隔
 * @param clickTimes    点击次数
 */
inline fun View.setMultiClicksListener(interval: Long=1000, clickTimes: Int, crossinline block:()->Unit) {
    setOnClickListener(object : View.OnClickListener {
        var firstTime: Long = 0
        var count: Int = 0

        override fun onClick(v: View) {
            val secondTime = System.currentTimeMillis()
            // 判断每次点击的事件间隔是否符合连击的有效范围
            // 不符合时，有可能是连击的开始，否则就仅仅是单击
            if (secondTime - firstTime <= interval) {
                count++
            } else {
                count = 1
            }
            // 延迟，用于判断用户的点击操作是否结束
            firstTime = secondTime
            if (count <= clickTimes) {
                Log.i("setMultiClicksListener", "连续点击次数：$count")
            }
            if (count == clickTimes) {
                block()
            }
        }
    })
}