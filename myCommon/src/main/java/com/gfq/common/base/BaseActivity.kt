package com.gfq.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gfq.common.net.RequestDelegate
import com.gfq.common.net.interfacee.defimpl.DefShowerDialog
import com.gfq.common.net.interfacee.defimpl.DefShowerDialogNoDim
import com.gfq.common.net.interfacee.defimpl.DefShowerView
import com.gfq.common.system.injectForIntentExtras

/**
 *  2021/12/23 11:26
 * @auth gaofuq
 * @description
 */
abstract class BaseActivity<Binding : ViewDataBinding>(private val layoutId: Int) :
    AppCompatActivity() {

    //半透明黑色蒙层，会改变状态栏的文字颜色
    open val requestDelegate by lazy { RequestDelegate(this, DefShowerDialog(this)) }
    //全透明蒙层，会改变状态栏的文字颜色
    open val requestDelegateNoDim by lazy { RequestDelegate(this, DefShowerDialogNoDim(this)) }
    //显示在Dialog下层，无蒙层，不会改变状态栏的文字颜色
    open val requestDelegateByView by lazy { RequestDelegate(this, DefShowerView(this)) }

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