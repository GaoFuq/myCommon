package com.gfq.common.net.interfacee.defimpl

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import com.gfq.common.R
import com.gfq.common.net.AbsResponse
import com.gfq.common.net.interfacee.IRequestStateShower
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

    private val TAG = "【DefShowerView】"
    private val inflate =
        LayoutInflater.from(context).inflate(R.layout.default_request_state_shower_view, this, true)
    val rootView = inflate.findViewById<LinearLayout>(R.id.layoutRoot)
    val ivState = inflate.findViewById<ImageView>(R.id.ivState)
    val tvState = inflate.findViewById<TextView>(R.id.tvState)
    val progressBar = inflate.findViewById<ProgressBar>(R.id.progressBar)

    //容器
    var container: FrameLayout? = null

    override fun showLoading(message: String?) {
        Log.e(TAG, "showLoading: $message")
        mainThread {
            layoutParams = LayoutParams(-2, -2).apply {
                gravity = Gravity.CENTER
            }
            tvState.text = message

            if (container != null) {
                container?.addView(this)
            } else if (context is Activity && parent == null) {
                ((context as Activity).window?.decorView as? FrameLayout)?.addView(this)
            }
            this.visible()
            progressBar.visible()
        }
    }

    override fun <T, Resp : AbsResponse<T>> showComplete(response: Resp?) {
        mainThread {
            tvState.text = response?.responseMessage()
            progressBar.gone()
            visible()
        }
    }

    override fun <T, Resp : AbsResponse<T>> showCompleteFailed(response: Resp?) {
        mainThread {
            tvState.text = response?.responseMessage()
            progressBar.gone()
            visible()
        }
    }

    override fun showError(error: String?) {
        Log.e(TAG, "showError: $error")
        mainThread {
            tvState.text = error
            progressBar.gone()
            visible()
        }
    }

    override fun dismissRequestStateShower() {
        Log.e(TAG, "dismissRequestStateShower ")
        mainThread { gone() }
    }


}