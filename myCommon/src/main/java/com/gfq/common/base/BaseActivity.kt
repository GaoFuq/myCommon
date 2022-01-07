package com.gfq.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gfq.common.system.injectForIntentExtras

/**
 *  2021/12/23 11:26
 * @auth gaofuq
 * @description
 */
abstract class BaseActivity<Binding : ViewDataBinding>(private val layoutId:Int) : AppCompatActivity() {
    lateinit var actBinding: Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actBinding = DataBindingUtil.setContentView<Binding>(this, layoutId)
        injectForIntentExtras()
        initView()
    }

    abstract fun initView()

}