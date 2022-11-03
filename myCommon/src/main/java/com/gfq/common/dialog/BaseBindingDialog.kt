package com.gfq.common.dialog

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gfq.common.R

/**
 *  2022/10/24 15:25
 * @auth gaofuq
 * @description
 */
abstract class BaseBindingDialog<T : ViewDataBinding>(
    context: Context,
    layoutId: Int,
    private val withAnim: Boolean = true,
) :
    BaseDialog(context) {

    var doOnStart: (() -> Unit)? = null

    val dialogBinding = DataBindingUtil.inflate<T>(
        LayoutInflater.from(context),
        layoutId,
        null,
        false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dialogBinding.root)
        window?.setBackgroundDrawable(ColorDrawable())
        if (!withAnim) {
            window?.setWindowAnimations(R.style.styleDialogNoAnim)
        }
        initViews()
    }

    abstract fun initViews()
    open fun initLayoutParams() {}


    override fun onStart() {
        super.onStart()
        initLayoutParams()
        doOnStart?.invoke()
    }


}