package com.gfq.common.dialog

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.gfq.common.R
import com.gfq.common.helper.actlifecycle.doOnDestroyed
import com.gfq.common.system.ActivityManager
import com.gfq.common.system.loge
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 *  2022/5/17 10:55
 * @auth gaofuq
 * @description
 */
abstract class BaseBottomSheetDialog<T : ViewDataBinding>(
    context: Context,
    layoutId: Int,
    style: Int = 0,
) :
    BottomSheetDialog(context, style) {

    private val TAG = this.javaClass.simpleName

    var dialogBinding = DataBindingUtil.inflate<T>(
        LayoutInflater.from(context),
        layoutId,
        null,
        false
    )


    var doOnStart: (() -> Unit)? = null


    abstract fun initViews()
    open fun initLayoutParams() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loge("$TAG onCreate")
        addLifecycleObserver()
        setContentView(dialogBinding.root)
        findViewById<View>(R.id.container)?.setOnClickListener {
            dismiss()
        }
        initViews()
    }

    private fun addLifecycleObserver() {
        if (context is FragmentActivity) {
            (context as FragmentActivity).doOnDestroyed { dismissSelf() }
        } else if (context is ContextWrapper) {
            val wrapper = context as ContextWrapper
            loge("$TAG wrapper.baseContext is ${wrapper.baseContext}")
            if (wrapper.baseContext is FragmentActivity) {
                (wrapper.baseContext as FragmentActivity).doOnDestroyed { dismissSelf() }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loge("$TAG onStart")
        initLayoutParams()
        doOnStart?.invoke()
    }

    override fun dismiss() {
        super.dismiss()
        loge("$TAG dismiss")
    }

    override fun show() {
        super.show()
        loge("$TAG show")
    }

    override fun onStop() {
        super.onStop()
        loge("$TAG onStop")
    }

    private fun dismissSelf() {
        loge("$TAG before ${ActivityManager.getTopActivity()?.localClassName} destroy dismiss itself")
        dismiss()
    }
}