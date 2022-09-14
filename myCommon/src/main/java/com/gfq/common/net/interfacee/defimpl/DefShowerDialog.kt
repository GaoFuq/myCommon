package com.gfq.common.net.interfacee.defimpl

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.gfq.common.R
import com.gfq.common.databinding.DefaultRequestStateDialogLayoutBinding
import com.gfq.common.dialog.GlobalDialog
import com.gfq.common.net.AbsResponse
import com.gfq.common.net.interfacee.IRequestStateShower
import com.gfq.common.system.ActivityManager

/**
 * Dialog实现
 * 默认宽高 100dp，黑色背景，点击外部不隐藏。
 * 问题：可能会改变状态栏文字颜色。
 */
open class DefShowerDialog(
    context: Context = ActivityManager.getAllActivities().last(),
    style: Int = 0
) : GlobalDialog(context, style), IRequestStateShower, LifecycleObserver {
    private val TAG = "【RequestStateShower】"
    protected val binding =
        DataBindingUtil.inflate<DefaultRequestStateDialogLayoutBinding>(
            LayoutInflater.from(context),
            R.layout.default_request_state_dialog_layout,
            null,
            false
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable())
        //解决在两个Dialog相互切换的时候的闪屏问题
        window?.setWindowAnimations(R.style.styleDialogNoAnim)
        if (mContext is LifecycleOwner) {
            mContext.lifecycle.addObserver(this)
        }
    }

    //不可通过返回按钮隐藏
//    override fun onBackPressed() {
//
//    }

    override fun showLoading(message: String?) {
        Log.e(TAG, "showLoading")
        checkAndShow(message)
    }

    override fun <T, Resp : AbsResponse<T>> showComplete(response: Resp?) {
        Log.e(TAG, "showComplete ${response?.responseMessage()}")
        checkAndShow(response?.responseMessage())
    }

    override fun <T, Resp : AbsResponse<T>> showCompleteFailed(response: Resp?) {
        Log.e(TAG, "showCompleteFailed ${response?.responseMessage()}")
        //默认显示返回的错误信息
        checkAndShow(response?.responseMessage())
    }


    override fun showError(error: String?) {
        Log.e(TAG, "showError")
        checkAndShow(error)
    }


    override fun dismissRequestStateShower() {
        Log.e(TAG, "dismissRequestStateShower")
        dismiss()
    }

    fun check(): Boolean {
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
        if (check() && !isShowing) {
            show()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun dismissThis() {
        Log.e(TAG, "${mContext.javaClass.simpleName} on destroy dismissThis")
        dismiss()
    }
}
