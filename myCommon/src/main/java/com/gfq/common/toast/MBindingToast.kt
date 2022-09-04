package com.gfq.common.toast

import com.gfq.common.R
import com.gfq.common.databinding.CommonToastWithImgBinding
import com.gfq.common.system.ActivityManager
import com.gfq.common.view.setImage
import com.gfq.common.view.visible

object MBindingToast : BaseBindingToast<CommonToastWithImgBinding>(R.layout.common_toast_with_img) {
    override fun bindView(bean: ToastDataBean) {

        bean.icon?.let {
            toastBinding.ivToastImg.setImage(it)
            toastBinding.ivToastImg.visible()
        }

        val msg = bean.msg
        toastBinding.tvToastMsg.text = when (msg) {
            is CharSequence -> msg
            is Int -> {
                try {
                    ActivityManager.application.getString(msg)
                } catch (e: Exception) {
                    msg.toString()
                }
            }
            else -> msg.toString()
        }
    }
}
