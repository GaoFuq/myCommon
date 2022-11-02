package com.gfq.common.toast

import android.view.Gravity
import android.widget.Toast
import com.gfq.common.system.ActivityManager

/**
 *  2022/5/17 16:43
 * @auth gaofuq
 * @description
 */
//内存泄漏
private val myToast by lazy {  MToast() }

fun tst(msg: Any?,icon:Any?=null, gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0) {
    myToast.show(msg,icon,gravity, xOffset, yOffset)
}

fun tst(msg: Any?,append:Boolean=false) {
    myToast.show(msg,append)
}


fun toast(msg: String?) {
    Toast.makeText(ActivityManager.application, msg?:"null", Toast.LENGTH_SHORT).show()
}
