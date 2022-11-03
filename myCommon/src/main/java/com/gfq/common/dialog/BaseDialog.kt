package com.gfq.common.dialog

import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.gfq.common.system.ActivityManager
import com.gfq.common.system.loge

/**
 *  2022/1/12 11:54
 * @auth gaofuq
 * @description
 */
open class BaseDialog(
    context: Context,
    style: Int = 0,
) : Dialog(context, style), LifecycleObserver {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addLifecycleObserver()
    }

    private fun addLifecycleObserver() {
        if (context is LifecycleOwner) {
            (context as LifecycleOwner).lifecycle.addObserver(this)
        } else if (context is ContextWrapper) {
            val wrapper = context as ContextWrapper
            loge("${this.javaClass.simpleName} wrapper.baseContext is ${wrapper.baseContext}")
            if (wrapper.baseContext is LifecycleOwner) {
                (wrapper.baseContext as LifecycleOwner).lifecycle.addObserver(this)
            }
        }
    }

    //避免窗口泄漏
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun dismissThis() {
        loge("${this.javaClass.simpleName} before ${ActivityManager.getTopActivity()?.localClassName} destroy dismiss itself")
        dismiss()
    }
}