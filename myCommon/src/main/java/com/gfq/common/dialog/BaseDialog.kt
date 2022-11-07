package com.gfq.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.gfq.common.R
import com.gfq.common.helper.actlifecycle.doOnDestroyed
import com.gfq.common.system.ActivityManager
import com.gfq.common.system.fragmentActivity

/**
 *  2022/1/12 11:54
 * @auth gaofuq
 * @description
 */

/**
 * @param withAnim
 * 是否有进入和退出动画，默认有。
 * 当在Dialog上显示Dialog时，建议设置为false，
 * 解决在两个Dialog相互切换的时候的闪屏问题。
 */
open class BaseDialog(
    context: Context,
    style: Int = 0,
    private val withAnim: Boolean = true,
) : Dialog(context, style) {

    private val TAG = this.javaClass.simpleName

    var doOnStart: (() -> Unit)? = null
    var canSwipeBack = true


    override fun onBackPressed() {
        if (canSwipeBack) {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ")
        addLifecycleObserver()
        window?.setBackgroundDrawable(ColorDrawable())
        if (!withAnim) {
            window?.setWindowAnimations(R.style.styleDialogNoAnim)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
        initLayoutParams()
        doOnStart?.invoke()
    }

    open fun initLayoutParams() {}

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun dismiss() {
        super.dismiss()
        Log.d(TAG, "dismiss: ")
    }

    override fun show() {
        super.show()
        Log.d(TAG, "show: ")
    }

    private fun addLifecycleObserver() {
        context.fragmentActivity()?.doOnDestroyed { dismissSelf() }
    }


    private fun dismissSelf() {
        Log.d(TAG, "before ${ActivityManager.getTopAliveActivity()?.localClassName} destroy dismiss itself")
        dismiss()
    }
}