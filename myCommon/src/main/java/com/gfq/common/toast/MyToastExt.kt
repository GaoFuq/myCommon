package com.gfq.common.toast

import android.view.Gravity

/**
 *  2022/5/17 16:43
 * @auth gaofuq
 * @description
 */

fun toast(msg: Any?,icon:Any?=null, gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0) {
    MyToast.show(msg,icon,gravity, xOffset, yOffset)
}