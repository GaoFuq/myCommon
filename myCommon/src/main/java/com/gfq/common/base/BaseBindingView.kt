package com.gfq.common.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.gfq.common.net.RequestDelegate
import com.gfq.common.net.interfacee.defimpl.DefShowerDialog
import com.gfq.common.net.interfacee.defimpl.DefShowerDialogDim0
import com.gfq.common.net.interfacee.defimpl.DefShowerView

/**
 *  2022/3/3 10:36
 * @auth gaofuq
 * @description
 */
abstract class BaseBindingView<binding : ViewDataBinding> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?=null,
) : FrameLayout(context, attrs) {

    abstract fun layoutResId(): Int


    //默认建议使用View的实现类。显示在Dialog下层，无蒙层，不会改变状态栏的文字颜色
    open val reqDel by lazy { findViewTreeLifecycleOwner()?.let { RequestDelegate(it, DefShowerView(context)) }}

    //建议在已经有 dim!=0 的Dialog显示时使用。半透明黑色蒙层，会改变状态栏的文字颜色。
    open val reqDelDim by lazy { findViewTreeLifecycleOwner()?.let { RequestDelegate(it, DefShowerDialog(context)) }}

    //建议在已经有 dim==0 的Dialog显示时使用。全透明蒙层，会改变状态栏的文字颜色
    open val reqDelDim0 by lazy { findViewTreeLifecycleOwner()?.let { RequestDelegate(it, DefShowerDialogDim0(context)) }}




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
