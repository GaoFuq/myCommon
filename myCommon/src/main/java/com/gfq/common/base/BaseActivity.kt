package com.gfq.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gfq.common.net.RequestDelegate
import com.gfq.common.net.interfacee.defimpl.DefShowerDialogDim
import com.gfq.common.net.interfacee.defimpl.DefShowerDialogDimNt
import com.gfq.common.net.interfacee.defimpl.DefShowerView
import com.gfq.common.system.injectForIntentExtras

/**
 *  2021/12/23 11:26
 * @auth gaofuq
 * @description
 */
abstract class BaseActivity<Binding : ViewDataBinding>(private val layoutId: Int) :
    AppCompatActivity() {

    //默认建议使用View的实现类。显示在Dialog下层，无蒙层，不会改变状态栏的文字颜色
    open val requestDelegate by lazy { RequestDelegate(this, DefShowerView(this)) }

    //建议在已经有 dim!=0 的Dialog显示时使用。半透明黑色蒙层，会改变状态栏的文字颜色。
    open val requestDelegateDim by lazy { RequestDelegate(this, DefShowerDialogDim(this)) }

    //建议在已经有 dim==0 的Dialog显示时使用。全透明蒙层，会改变状态栏的文字颜色
    open val requestDelegateDimNt by lazy { RequestDelegate(this, DefShowerDialogDimNt(this)) }


    lateinit var actBinding: Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectForIntentExtras()
        beforeSetContentView()
        actBinding = DataBindingUtil.setContentView<Binding>(this, layoutId)
        supportActionBar?.hide()
        initView()
    }

    abstract fun initView()
    open fun beforeSetContentView() {}
}