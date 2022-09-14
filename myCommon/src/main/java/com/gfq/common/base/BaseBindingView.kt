package com.gfq.common.base

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.gfq.common.net.RequestDelegate
import com.gfq.common.net.interfacee.defimpl.DefShowerDialog
import com.gfq.common.net.interfacee.defimpl.DefShowerDialogNoDim
import com.gfq.common.net.interfacee.defimpl.DefShowerView

/**
 *  2022/3/3 10:36
 * @auth gaofuq
 * @description
 */
abstract class BaseBindingView<binding : ViewDataBinding>(
    context: Context,
    attrs: AttributeSet?,
) : FrameLayout(context, attrs) {

    abstract fun layoutResId(): Int


    //半透明黑色蒙层，会改变状态栏的文字颜色
    open val requestDelegate by lazy {
        this.findViewTreeLifecycleOwner()
            ?.let { RequestDelegate(it, DefShowerDialog(context)) }
    }

    //全透明蒙层，会改变状态栏的文字颜色
    open val requestDelegateNoDim by lazy {
        this.findViewTreeLifecycleOwner()
            ?.let { RequestDelegate(it, DefShowerDialogNoDim(context)) }
    }

    //显示在Dialog下层，无蒙层，不会改变状态栏的文字颜色
    open val requestDelegateByView by lazy {
        this.findViewTreeLifecycleOwner()
            ?.let {
                if (context is Activity) {
                    RequestDelegate(it, DefShowerView(context))
                }
            }
    }


    val vBinding by lazy {
        DataBindingUtil.inflate<binding>(
            LayoutInflater.from(context),
            layoutResId(),
            this,
            true
        )
    }

    init {
        vBinding
    }
}
