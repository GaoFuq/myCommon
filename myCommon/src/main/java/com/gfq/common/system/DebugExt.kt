package com.gfq.common.system

import android.widget.Toast
import com.orhanobut.logger.Logger
import java.util.*

/**
 *  2022/2/10 10:54
 * @auth gaofuq
 * @description
 */


//inline 方法 ，Logger可以定位到调用栈，但是不能点击跳转。
fun logd(msg: String?) {
    Logger.d(msg ?: "null")
}

fun loge(msg: String?) {
    Logger.e(msg ?: "null")
}

fun logi(any: String?){
    Logger.i(any ?: "null")
}

fun logw(any: String?){
    Logger.w(any ?: "null")
}
