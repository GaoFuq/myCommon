package com.gfq.common.utils

object FastClickUtil {

    private const val MIN_CLICK_TIME = 500

    var lastTime = 0L

    @JvmStatic
    fun isFastClick(): Boolean {
        val nowTime = System.currentTimeMillis()
        if (nowTime - lastTime <= MIN_CLICK_TIME) {
            return true
        }
        lastTime = nowTime
        return false
    }

}