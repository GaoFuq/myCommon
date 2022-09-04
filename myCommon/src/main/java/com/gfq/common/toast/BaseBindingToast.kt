package com.gfq.common.toast

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gfq.common.system.ActivityManager
import com.gfq.common.utils.mainThread

abstract class BaseBindingToast<T : ViewDataBinding>(private val layoutId: Int) {
    private var lastToast: Toast? = null
    lateinit var toastBinding: T
    abstract fun bindView(bean: ToastDataBean)

    private fun createToast(
        bean: ToastDataBean,
        gravity: Int = Gravity.CENTER,
        xOffset: Int = 0,
        yOffset: Int = 0,
    ): Toast {
        val currentToast = Toast.makeText(ActivityManager.application, "", Toast.LENGTH_SHORT)
        toastBinding = DataBindingUtil.inflate(
            LayoutInflater.from(ActivityManager.application),
            layoutId,
            null,
            false
        )
        bindView(bean)

        if (lastToast != null) {
            lastToast!!.cancel()
        }
        lastToast = currentToast
        currentToast.view = toastBinding.root
        currentToast.setGravity(gravity, xOffset, yOffset)
        return currentToast
    }

    fun show(msg: Any?, gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0) {
        mainThread { createToast(ToastDataBean(msg = msg),gravity, xOffset, yOffset).show() }
    }

    fun show(
        msg: Any?,
        icon: Any?,
        gravity: Int = Gravity.CENTER,
        xOffset: Int = 0,
        yOffset: Int = 0,
    ) {
        mainThread { createToast(ToastDataBean(msg = msg, icon = icon),gravity, xOffset, yOffset).show() }
    }

    fun show(
        bean: ToastDataBean,
        gravity: Int = Gravity.CENTER,
        xOffset: Int = 0,
        yOffset: Int = 0,
    ) {
        mainThread { createToast(ToastDataBean(bean),gravity, xOffset, yOffset).show() }
    }

    fun cancel() {
        lastToast?.cancel()
    }

}
