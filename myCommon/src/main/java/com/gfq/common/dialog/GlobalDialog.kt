package com.gfq.common.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import com.gfq.common.system.ActivityManager

/**
 *  2022/1/12 11:54
 * @auth gaofuq
 * @description
 * 继承 [GlobalDialog] ,不再显式的传递 context 参数
 * @param style [R.style.xxx]
 */
open class GlobalDialog(
    val mContext: Context = ActivityManager.getAllActivities().last(),
    style: Int = 0,
) : Dialog(mContext, style){
    private fun check(): Boolean {
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

    override fun show() {
        if (check())
            super.show()
    }

    override fun dismiss() {
        if (check())
            super.dismiss()
    }
}