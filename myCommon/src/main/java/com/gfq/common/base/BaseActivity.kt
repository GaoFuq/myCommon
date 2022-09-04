package com.gfq.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gfq.common.dialog.DefaultRequestStateDialog
import com.gfq.common.dialog.DefaultRequestStateDialogNoAnim
import com.gfq.common.net.RequestDelegate
import com.gfq.common.system.injectForIntentExtras

/**
 *  2021/12/23 11:26
 * @auth gaofuq
 * @description
 */
abstract class BaseActivity<Binding : ViewDataBinding>(private val layoutId: Int) :
    AppCompatActivity() {

    open val requestDelegate by lazy { RequestDelegate(this, DefaultRequestStateDialogNoAnim(this)) }

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