package com.gfq.common.view

import android.util.Log
import android.view.View

/**
 *  2022/2/10 10:49
 * @auth gaofuq
 * @description
 */


/**
 * 点击事件防抖动
 */
fun View.setDebounceClick(limitTime: Long = 500L, onClick: (View) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > limitTime) {
            lastClickTime = currentTime
            onClick(it)
        }
    }
}


/**
 * 设置多次点击监听，连击一定次数后触发clickListener
 *
 * @param interval      两次点击的时间间隔
 * @param clickTimes    点击次数
 */
fun View.setMultiClicksListener(
    interval: Long = 1000,
    clickTimes: Int,
    block: () -> Unit
) {
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

