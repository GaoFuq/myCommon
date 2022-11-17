package com.gfq.common.utils

object FastClickUtil {

    var lastTime = 0L

    @JvmStatic
    fun isFastClick(MIN_CLICK_TIME :Long= 500): Boolean {
        val nowTime = System.currentTimeMillis()
        if (nowTime - lastTime <= MIN_CLICK_TIME) {
            return true
        }
        lastTime = nowTime
        return false
    }

}