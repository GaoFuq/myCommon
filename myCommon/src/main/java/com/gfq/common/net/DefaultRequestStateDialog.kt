package com.gfq.common.net

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.gfq.common.R
import com.gfq.common.databinding.DefaultRequestStateDialogLayoutBinding
import com.gfq.common.system.dp
import com.gfq.common.system.updateAttributes

/**
 * [AbsRequestViewModel] 中使用的默认 dialog 。
 * 默认只显示 [RequestState.loading] 状态。
 * 默认宽高 100dp,遮罩透明，点击外部不隐藏
 */
class DefaultRequestStateDialog: GlobalDialog(), IRequestStateDialog {
    private val binding = DataBindingUtil.inflate<DefaultRequestStateDialogLayoutBinding>(LayoutInflater.from(context),
        R.layout.default_request_state_dialog_layout,
        null,
        false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable())
        window?.updateAttributes {
            it.width = dp(100)
            it.height = dp(100)
            it.dimAmount=0f
        }

    }

    override fun showLoading(message: String?) {
        Log.d("stateDialog","showLoading")
        binding.tvState.text = message
        show()
    }

    override fun <T, Resp : BaseResp<T>> showComplete(response: Resp?) {
//        binding.tvState.text = (response as MyBaseResp<*>).msg
        Log.d("stateDialog","showComplete")
        dismissStateDialog()
    }


    override fun showError(error: ApiException) {
//        binding.tvState.text = error.message
        Log.d("stateDialog","showError")
        dismissStateDialog()
    }


    override fun dismissStateDialog() {
        if(isShowing) {
            Log.d("stateDialog","dismissStateDialog")
            dismiss()
        }
    }



}
