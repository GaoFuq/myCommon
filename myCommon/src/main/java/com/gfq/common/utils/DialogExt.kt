package com.gfq.common.utils

import android.app.Dialog
import android.view.View


/**
 *  2022/3/16 9:31
 * @auth gaofuq
 * @description
 */

/**
 * 设置dialogq全屏且隐藏状态栏。
 */
fun Dialog.setFullHideStatusBar() {
    val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE or
            View.SYSTEM_UI_FLAG_FULLSCREEN
    this.window?.decorView?.systemUiVisibility = uiOptions
}