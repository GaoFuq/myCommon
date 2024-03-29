package com.gfq.common.net.interfacee.defimpl

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.gfq.common.R
import com.gfq.common.helper.actlifecycle.doOnDestroyed
import com.gfq.common.net.AbsResponse
import com.gfq.common.net.interfacee.IRequestStateShower
import com.gfq.common.system.activity
import com.gfq.common.system.fragmentActivity
import com.gfq.common.system.logd
import com.gfq.common.system.logd
import com.gfq.common.utils.mainThread
import com.gfq.common.view.gone
import com.gfq.common.view.visible

/**
 *  2022/9/14 10:44
 * @auth gaofuq
 * @description
 * 默认宽高 100dp，黑色背景，外部区域透明，点击外部不响应。
 * View实现，自动添加到 Activity 里面。
 * 默认展示圆圈 progressBar 和 文本 ，imageView 隐藏。
 * 好处：不会改变状态栏的文字颜色。
 * 问题：显示在 Dialog 的下层，不能显示在 Dialog 上层。
 * 解决思路：给 container 赋值，在 Dialog 里面展示该 View。
 */
open class DefShowerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : FrameLayout(context, attrs), IRequestStateShower {
    companion object {
        private const val TAG = "【DefShowerView】"
    }

    private val inflate =
        LayoutInflater.from(context).inflate(R.layout.default_request_state_shower_view, this, true)
    val layoutLoading = inflate.findViewById<LinearLayout>(R.id.layoutLoading)
    val layoutDim = inflate.findViewById<FrameLayout>(R.id.layoutDim)
    val ivState = inflate.findViewById<ImageView>(R.id.ivState)
    val tvState = inflate.findViewById<TextView>(R.id.tvState)
    val progressBar = inflate.findViewById<ProgressBar>(R.id.progressBar)

    init {
        id = R.id.defShowerViewId
    }

    //容器
    var container: FrameLayout? = null

    override fun showLoading(message: String?) {
        logd("$TAG showLoading: $message")
        mainThread {
            addThisViewIfNeed()
            tvState.text = message
            this.visible()
            progressBar.visible()
        }
    }

    private fun addThisViewIfNeed() {
        if (parent != null) {
            logd("$TAG addThisViewIfNeed parent != null , parent is $parent")
            return
        }
        layoutParams = LayoutParams(-2, -2).apply {
            gravity = Gravity.CENTER
        }
        if (container != null) {
            container?.addView(this)
        } else {
            (context.activity()?.window?.decorView as? FrameLayout)?.addView(this)
        }
    }

    override fun <T, Resp : AbsResponse<T>> showComplete(response: Resp?) {
        mainThread {
            addThisViewIfNeed()
            tvState.text = response?.responseMessage()
            progressBar.gone()
            visible()
        }
    }

    override fun <T, Resp : AbsResponse<T>> showCompleteFailed(response: Resp?) {
        mainThread {
            addThisViewIfNeed()
            tvState.text = response?.responseMessage()
            progressBar.gone()
            visible()
        }
    }

    override fun showError(error: String?) {
        logd("$TAG showError: $error")
        mainThread {
            addThisViewIfNeed()
            tvState.text = error
            progressBar.gone()
            visible()
        }
    }

    override fun dismissRequestStateShower() {
        logd("$TAG dismissRequestStateShower ")
        mainThread { gone() }
    }


    fun setDim() {
        layoutDim.setBackgroundColor(Color.parseColor("#80000000"))
    }
}