package com.gfq.common.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gfq.common.R
import com.gfq.common.net.RequestDelegate
import com.gfq.common.net.interfacee.defimpl.DefShowerDialogDim
import com.gfq.common.net.interfacee.defimpl.DefShowerDialogDimNt
import com.gfq.common.net.interfacee.defimpl.DefShowerView
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 *  2022/5/17 10:55
 * @auth gaofuq
 * @description
 */
abstract class BaseBottomSheetDialog<T : ViewDataBinding>(
    val mContext: Context,
    private val layoutId: Int,
    style: Int = 0,
) :
    BottomSheetDialog(mContext, style) {

    lateinit var dialogBinding: T

    //默认建议使用View的实现类。显示在Dialog下层，无蒙层，不会改变状态栏的文字颜色
    open val requestDelegate by lazy { RequestDelegate(null, DefShowerView(mContext)) }

    //建议在已经有 dim!=0 的Dialog显示时使用。半透明黑色蒙层，会改变状态栏的文字颜色。
    open val requestDelegateDim by lazy { RequestDelegate(null, DefShowerDialogDim(mContext)) }

    //建议在已经有 dim==0 的Dialog显示时使用。全透明蒙层，会改变状态栏的文字颜色
    open val requestDelegateDimNt by lazy { RequestDelegate(null, DefShowerDialogDimNt(mContext)) }

    abstract fun initViews()
    open fun initLayoutParams() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBinding =
            DataBindingUtil.inflate<T>(LayoutInflater.from(mContext), layoutId, null, false)
        setContentView(dialogBinding.root)
        findViewById<View>(R.id.container)?.setOnClickListener {
            dismiss()
        }
        initViews()
    }

    override fun onStart() {
        super.onStart()
        initLayoutParams()
    }

}