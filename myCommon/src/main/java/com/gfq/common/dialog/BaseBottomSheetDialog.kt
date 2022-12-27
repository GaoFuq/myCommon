package com.gfq.common.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gfq.common.R
import com.gfq.common.helper.actlifecycle.doOnDestroyed
import com.gfq.common.system.ActivityManager
import com.gfq.common.system.fragmentActivity
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


    abstract fun initView()
    open fun initLayoutParams() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        addLifecycleObserver()
        setContentView(dialogBinding.root)
        findViewById<View>(R.id.container)?.setOnClickListener {
            dismiss()
        }
        initView()
    }

    private fun addLifecycleObserver() {
        context.fragmentActivity()?.doOnDestroyed { dismissSelf() }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
        initLayoutParams()
        doOnStart?.invoke()
    }

    override fun dismiss() {
        super.dismiss()
        Log.d(TAG, "dismiss: ")
    }

    override fun show() {
        super.show()
        Log.d(TAG, "show: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    private fun dismissSelf() {
        Log.d(TAG, "before ${ActivityManager.getTopAliveActivity()?.localClassName} destroy dismiss itself")
        dismiss()
    }
}