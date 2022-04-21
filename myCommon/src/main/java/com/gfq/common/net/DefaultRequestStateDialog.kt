package com.gfq.common.net

import android.app.Activity
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
open class DefaultRequestStateDialog(style:Int=0): GlobalDialog(style), IRequestStateDialog {
    protected val binding = DataBindingUtil.inflate<DefaultRequestStateDialogLayoutBinding>(LayoutInflater.from(context),
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
        Log.e("stateDialog","showLoading")
        binding.tvState.text = message
        show()
    }

    override fun <T, Resp : AbsResponse<T>> showComplete(response: Resp?) {
        Log.e("stateDialog","showComplete ${response?.responseMessage()}")
        //默认不显示任何文本
        if(checkLeak() && !isShowing) {
           show()
        }
    }

    override fun <T, Resp : AbsResponse<T>> showCompleteFailed(response: Resp?) {
        Log.e("stateDialog","showCompleteFailed ${response?.responseMessage()}")
        //默认显示返回的错误信息
        binding.tvState.text = response?.responseMessage()
        if(checkLeak() && !isShowing) {
            show()
        }
    }


    override fun showError(error: ApiException) {
        Log.e("stateDialog","showError")
        binding.tvState.text = error.message
        if(checkLeak() && !isShowing) {
            show()
        }
    }


    override fun dismissStateDialog() {
        if(checkLeak() && isShowing) {
            Log.e("stateDialog","dismissStateDialog")
            dismiss()
        }
    }

    fun checkLeak():Boolean{
        if(context is Activity){
            val act = context as Activity
            return !act.isFinishing && !act.isDestroyed
        }
        return false
    }


}
