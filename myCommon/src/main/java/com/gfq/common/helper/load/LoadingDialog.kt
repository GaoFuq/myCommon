package com.gfq.common.helper.load

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.gfq.common.R
import com.gfq.common.dialog.BaseDialog
import com.gfq.common.system.logd
import com.gfq.common.utils.mainThreadDelay

open class LoadingDialog(context: Context) : BaseDialog(context, withAnim = false) {

    private val TAG = "【LoadingDialog】"

    val inflate = LayoutInflater.from(context).inflate(R.layout.default_loading_dialog, null, false)
    val ivState = inflate.findViewById<ImageView>(R.id.ivState)

    val tvState = inflate.findViewById<TextView>(R.id.tvState)

    // 设置转圈圈的颜色
    // progressBar.indeterminateTintList = ColorStateList.valueOf(getColor(R.color.theme))
    val progressBar = inflate.findViewById<ProgressBar>(R.id.progressBar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)
        setContentView(inflate)

    }


    fun show(message: String?) {
        logd("$TAG show : $message")
        tvState.text = message
        show()
    }

    fun showAppend(message: String?) {
        logd("$TAG appendShow : $message")
        tvState.append(message)
        show()
    }

    fun dismissDelay(delay: Long) {
        logd("$TAG dismissDelay $delay")
        mainThreadDelay(delay) { dismiss() }
    }



}
