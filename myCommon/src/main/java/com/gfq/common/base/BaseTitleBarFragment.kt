package com.gfq.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gfq.common.R
import com.gfq.common.databinding.BaseTitleBarFragmentBinding
import com.gfq.common.view.custom.BaseTitleBarLayout

/**
 *  2022/3/21 14:30
 * @auth gaofuq
 * @description
 */
abstract class BaseTitleBarFragment<T : ViewDataBinding>(private val layoutId: Int) :
    BaseFragment<T>(R.layout.base_title_bar_fragment) {

    var titleBarLayout : BaseTitleBarLayout?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val parentBinding = DataBindingUtil.inflate<BaseTitleBarFragmentBinding>(inflater,
            R.layout.base_title_bar_fragment,
            container,
            false)
        titleBarLayout = parentBinding.titleBar
        titleBarLayout?.vBinding?.tvTitleBarTitle?.text = title()
        fragBinding = DataBindingUtil.inflate<T>(inflater, layoutId, parentBinding.llRoot, false)
        return parentBinding.root
    }

    abstract fun title():String
}