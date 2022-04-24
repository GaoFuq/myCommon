package com.gfq.common.net

import android.app.Activity
import android.content.ContextWrapper
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.gfq.common.R
import com.gfq.common.databinding.DefaultRequestStateDialogLayoutBinding

/**
 * [ViewModelRequest] 中使用的默认 dialog 。
 * 默认宽高 100dp，遮罩透明，黑色背景，点击外部不隐藏。
 */
open class DefaultRequestStateDialog(style: Int = 0) : GlobalDialog(style = style), IRequestStateDialog {
    protected val binding =
        DataBindingUtil.inflate<DefaultRequestStateDialogLayoutBinding>(LayoutInflater.from(context),
            R.layout.default_request_state_dialog_layout,
            null,
            false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable())
        window?.setDimAmount(0f)
    }

    //不可通过返回按钮隐藏
//    override fun onBackPressed() {
//
//    }

    override fun showLoading(message: String?) {
        Log.e("stateDialog", "showLoading")
        checkAndShow(message)
    }

    override fun <T, Resp : AbsResponse<T>> showComplete(response: Resp?) {
        Log.e("stateDialog", "showComplete ${response?.responseMessage()}")
        checkAndShow(response?.responseMessage())
    }

    override fun <T, Resp : AbsResponse<T>> showCompleteFailed(response: Resp?) {
        Log.e("stateDialog", "showCompleteFailed ${response?.responseMessage()}")
        //默认显示返回的错误信息
        checkAndShow(response?.responseMessage())
    }


    override fun showError(error: String?) {
        Log.e("stateDialog", "showError")
        checkAndShow(error)
    }


    override fun dismissStateDialog() {
        Log.e("stateDialog", "dismissStateDialog")
        dismiss()
    }

    fun checkLeak(): Boolean {
        if (mContext is Activity) {
            return !mContext.isFinishing && !mContext.isDestroyed
        }

        if (context is ContextWrapper) {
            val wrapper = context as ContextWrapper
            if (wrapper.baseContext is Activity) {
                val act = wrapper.baseContext as Activity
                return !act.isFinishing && !act.isDestroyed
            }
        }
        return false
    }

    fun checkAndShow(msg: String?) {
        binding.tvState.text = msg
        if (checkLeak() && !isShowing) {
            show()
        }
    }

}
