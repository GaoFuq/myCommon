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
open class DefShowerDialogDim0(
    context: Context,
) : DefShowerDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setDimAmount(0f)
    }
}