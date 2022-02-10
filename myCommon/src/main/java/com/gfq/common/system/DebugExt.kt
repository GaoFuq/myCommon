package com.gfq.common.system

import android.widget.Toast
import com.orhanobut.logger.Logger

/**
 *  2022/2/10 10:54
 * @auth gaofuq
 * @description
 */
fun toast(msg: String?) {
    Toast.makeText(ActivityManager.application, msg, Toast.LENGTH_SHORT).show()
}

fun logd(msg: String?) {
    Logger.d(msg ?: "null")
}

fun loge(msg: String?) {
    Logger.e(msg ?: "null")
}

fun log(any: Any?){
    Logger.d(any ?: "null")
}
