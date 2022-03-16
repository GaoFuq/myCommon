package com.gfq.common.net

import com.gfq.common.R

/**
 * 2021/4/19 11:09
 * @auth gaofuq
 * @description
 *
 * 初始化了一个默认的 [IRequestStateDialog],[DefaultRequestStateDialog]
 */
abstract class BaseRequestViewModelWithStateDialog : BaseRequestViewModel() {
    init {
        requestStateDialog = DefaultRequestStateDialog(R.style.FullTransparentNoDimDialog)
        completeDismissDelay = 1000
        errorDismissDelay = 1500
        minimumLoadingTime = 800
    }
}