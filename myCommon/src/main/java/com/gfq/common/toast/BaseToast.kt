package com.gfq.common.toast

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.gfq.common.system.ActivityManager
import com.gfq.common.utils.mainThread

abstract class BaseToast(private val layoutId: Int) {
    private var lastToast: Toast? = null
    abstract fun bindView(view: View?, bean: ToastDataBean)
    var view: View? = null

    private fun createToast(
        bean: ToastDataBean,
        gravity: Int = Gravity.CENTER,
        xOffset: Int = 0,
        yOffset: Int = 0,
        append: Boolean = false,
    ): Toast {

        if(append && lastToast!=null&& view!=null){
            appendMsg(bean.msg)
            return lastToast!!
        }else{
            val currentToast = Toast.makeText(ActivityManager.application, "", Toast.LENGTH_SHORT)
            view = LayoutInflater.from(ActivityManager.application).inflate(layoutId, null, false)
            bindView(view, bean)

            if (lastToast != null) {
                lastToast!!.cancel()
            }
            lastToast = currentToast
            currentToast.view = view
            currentToast.setGravity(gravity, xOffset, yOffset)
            return currentToast
        }

    }

    fun show(msg: Any?, gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0) {
        mainThread { createToast(ToastDataBean(msg = msg), gravity, xOffset, yOffset).show() }
    }

    fun show(
        msg: Any?,
        icon: Any?,
        gravity: Int = Gravity.CENTER,
        xOffset: Int = 0,
        yOffset: Int = 0,
    ) {
        mainThread {
            createToast(ToastDataBean(msg = msg, icon = icon),
                gravity,
                xOffset,
                yOffset).show()
        }
    }


    fun show(
        msg: Any?,
        append: Boolean = false,
    ) {
        mainThread {
            createToast(ToastDataBean(msg = msg, icon = null),
                Gravity.CENTER,
                0,
                0,
                append).show()
        }
    }

    fun show(
        bean: ToastDataBean,
        gravity: Int = Gravity.CENTER,
        xOffset: Int = 0,
        yOffset: Int = 0,
    ) {
        mainThread { createToast(ToastDataBean(bean), gravity, xOffset, yOffset).show() }
    }

    fun cancel() {
        lastToast?.cancel()
    }

    open fun appendMsg(msg:Any?){}
}
