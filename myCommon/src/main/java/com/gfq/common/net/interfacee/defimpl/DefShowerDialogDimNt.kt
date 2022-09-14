package com.gfq.common.net.interfacee.defimpl

import android.content.Context
import android.os.Bundle
import com.gfq.common.system.ActivityManager

/**
 *  2022/9/1 12:08
 * @auth gaofuq
 * @description
 *
 * Dialog实现
 * 默认宽高 100dp，黑色背景，点击外部不隐藏。
 * 问题：1.可能会改变状态栏文字颜色。
 */
open class DefShowerDialogDimNt(
    context: Context = ActivityManager.getAllActivities().last(),
    style: Int = 0
) : DefShowerDialogDim(context, style) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //解决在两个Dialog相互切换的时候的闪屏问题
//        window?.setWindowAnimations(R.style.styleDialogNoAnim)
        window?.setDimAmount(0f)
    }
}