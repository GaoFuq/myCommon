package com.gfq.common.net

import com.gfq.common.R
import com.gfq.common.net.interfacee.defimpl.DefShowerDialog
import com.gfq.common.system.ActivityManager

/**
 * 2021/4/19 11:09
 * @auth gaofuq
 * @description
 *
 * 初始化了一个默认的 [IRequestStateShower],[DefShowerDialog]
 */
open class ViewModelRequestWithDialog : ViewModelRequest() {
    init {
        requestDelegate.stateShower = DefShowerDialog(ActivityManager.getAllActivities().last(),R.style.FullTransparentNoDimDialog)
    }
}