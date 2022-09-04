package com.gfq.common.toast

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.gfq.common.R
import com.gfq.common.system.ActivityManager
import com.gfq.common.view.setImage
import com.gfq.common.view.visible

class MToast : BaseToast(R.layout.common_toast_with_img) {
    override fun bindView(view: View?, bean: ToastDataBean) {

        val ivToastImg = view?.findViewById<ImageView>(R.id.ivToastImg)
        val tvToastMsg = view?.findViewById<TextView>(R.id.tvToastMsg)
        bean.icon?.let {
            ivToastImg?.setImage(it)
            ivToastImg?.visible()
        }

        val msg = bean.msg
        tvToastMsg?.text = when (msg) {
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
