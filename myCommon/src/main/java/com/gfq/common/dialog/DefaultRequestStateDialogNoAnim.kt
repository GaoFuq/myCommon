package com.gfq.common.dialog

import android.content.Context
import android.os.Bundle
import com.gfq.common.R
import com.gfq.common.system.ActivityManager

/**
 *  2022/9/1 12:08
 * @auth gaofuq
 * @description 显示在 Dialog 上的 dialog,解决在两个Dialog相互切换的时候的闪屏问题
 */
open class DefaultRequestStateDialogNoAnim(
    context: Context = ActivityManager.getAllActivities().last(),
    style: Int = 0
) : DefaultRequestStateDialog(context, style) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //解决在两个Dialog相互切换的时候的闪屏问题
        window?.setWindowAnimations(R.style.styleDialogNoAnim)
    }
}