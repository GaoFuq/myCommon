package com.gfq.common.net.interfacee.defimpl

import android.content.Context
import com.gfq.common.R
import com.gfq.common.databinding.DefaultRequestStateDialogLayoutBinding
import com.gfq.common.dialog.BaseBindingDialog
import com.gfq.common.net.AbsResponse
import com.gfq.common.net.interfacee.IRequestStateShower
import com.gfq.common.system.logd

/**
 * Dialog实现
 * 默认宽高 100dp，黑色背景，点击外部不隐藏。
 * 问题：可能会改变状态栏文字颜色。
 */
open class DefShowerDialog(context: Context) :
    BaseBindingDialog<DefaultRequestStateDialogLayoutBinding>(
        context,
        R.layout.default_request_state_dialog_layout,
        withAnim = false
    ), IRequestStateShower {

    private val TAG = "【DefShowerDialog】"

    override fun initView() {
        setCanceledOnTouchOutside(false)
    }

    override fun showLoading(message: String?) {
        logd("$TAG showLoading : $message")
        dialogBinding.tvState.text = message
        show()
    }

    override fun <T, Resp : AbsResponse<T>> showComplete(response: Resp?) {
        logd("$TAG showComplete : ${response?.responseMessage()}")
        dialogBinding.tvState.text = response?.responseMessage()
        show()
    }

    override fun <T, Resp : AbsResponse<T>> showCompleteFailed(response: Resp?) {
        logd( "$TAG showCompleteFailed  : ${response?.responseMessage()}")
        //默认显示返回的错误信息
        dialogBinding.tvState.text = response?.responseMessage()
        show()
    }


    override fun showError(error: String?) {
        logd( "$TAG showError :  $error")
        dialogBinding.tvState.text = error
        show()
    }


    override fun dismissRequestStateShower() {
        logd( "$TAG dismissRequestStateShower")
        dismiss()
    }



}
