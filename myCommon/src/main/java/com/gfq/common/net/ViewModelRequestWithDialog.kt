package com.gfq.common.net

import com.gfq.common.R
import com.gfq.common.dialog.DefaultRequestStateDialog
import com.gfq.common.system.ActivityManager

/**
 * 2021/4/19 11:09
 * @auth gaofuq
 * @description
 *
 * 初始化了一个默认的 [IRequestStateDialog],[DefaultRequestStateDialog]
 */
open class ViewModelRequestWithDialog : ViewModelRequest() {
    init {
        requestDelegate.stateDialog = DefaultRequestStateDialog(ActivityManager.getAllActivities().last(),R.style.FullTransparentNoDimDialog)
    }
}