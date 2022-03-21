package com.gfq.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gfq.common.system.injectForIntentExtras
import com.gfq.common.view.custom.BaseTitleBarLayout

/**
 *  2022/3/21 14:19
 * @auth gaofuq
 * @description
 */
abstract class BaseTitleBarActivity<Binding : ViewDataBinding>(private val layoutId: Int) :
    AppCompatActivity() {

    lateinit var actBinding: Binding

    var titleBarLayout : BaseTitleBarLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectForIntentExtras()
        beforeSetContentView()

        val linearLayout = LinearLayout(this)
        setContentView(linearLayout)
        linearLayout.orientation = LinearLayout.VERTICAL
        titleBarLayout = BaseTitleBarLayout(this,null)
        linearLayout.addView(titleBarLayout,-1,-2)
        titleBarLayout?.vBinding?.tvTitleBarTitle?.text = title()

        actBinding = DataBindingUtil.inflate<Binding>(LayoutInflater.from(this),layoutId,linearLayout,true)
        initView()
    }

    abstract fun title():String
    abstract fun initView()

    open fun beforeSetContentView() {}
}