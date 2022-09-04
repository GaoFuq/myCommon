package com.gfq.common.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.gfq.common.dialog.DefaultRequestStateDialogNoAnim
import com.gfq.common.net.RequestDelegate

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

    open val requestDelegate by lazy {
        this.findViewTreeLifecycleOwner()
            ?.let { RequestDelegate(it, DefaultRequestStateDialogNoAnim(context)) }
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
