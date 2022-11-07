package com.gfq.common.helper.load

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import com.gfq.common.R
import com.gfq.common.system.ActivityManager
import com.gfq.common.system.activity
import com.gfq.common.system.logd
import com.gfq.common.utils.mainThread
import com.gfq.common.utils.mainThreadDelay
import com.gfq.common.view.gone
import com.gfq.common.view.visible

open class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {


    val inflate = LayoutInflater.from(context).inflate(R.layout.default_loading_view, this, true)
    val layoutLoading = inflate.findViewById<LinearLayout>(R.id.layoutLoading)
    //最外层的layout，和Activity同宽高
    val layoutDim = inflate.findViewById<FrameLayout>(R.id.layoutDim)

    val ivState = inflate.findViewById<ImageView>(R.id.ivState)
    val tvState = inflate.findViewById<TextView>(R.id.tvState)

    // 设置转圈圈的颜色
    // progressBar.indeterminateTintList = ColorStateList.valueOf(getColor(R.color.theme))
    val progressBar = inflate.findViewById<ProgressBar>(R.id.progressBar)


    private val TAG = "【LoadingView】"

    private var container: ViewGroup? = null

    fun setContainer(viewGroup: ViewGroup): LoadingView {
        this.container = viewGroup
        return this
    }


    fun show(message: String?): LoadingView {
        logd("$TAG show : $message")
        mainThread {
            addThisViewIfNeed()
            tvState.text = message
            this.visible()
        }
        return this
    }

    fun showAppend(message: String?): LoadingView {
        logd("$TAG appendShow : $message")
        mainThread {
            addThisViewIfNeed()
            tvState.append(message)
            this.visible()
        }
        return this
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


    fun dismiss() {
        logd("$TAG dismiss ")
        mainThread {
            (parent as? ViewGroup)?.removeView(this)
        }
    }

    fun dismiss(delay: Long = 0) {
        logd("$TAG dismissDelay $delay")
        mainThreadDelay(delay) {
            (parent as? ViewGroup)?.removeView(this)
        }
    }


    fun setBackground() {
        layoutDim.setBackgroundColor(Color.parseColor("#80000000"))
    }


}
