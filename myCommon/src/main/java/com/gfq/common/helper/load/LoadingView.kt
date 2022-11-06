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


    private val inflate =
        LayoutInflater.from(context).inflate(R.layout.default_loading_view, this, true)


    val layoutLoading = inflate.findViewById<LinearLayout>(R.id.layoutLoading)

    //最外层的layout，和Activity同宽高
    val layoutDim = inflate.findViewById<FrameLayout>(R.id.layoutDim)

    val ivState = inflate.findViewById<ImageView>(R.id.ivState)
    val tvState = inflate.findViewById<TextView>(R.id.tvState)

    // 设置转圈圈的颜色
    // progressBar.indeterminateTintList = ColorStateList.valueOf(getColor(R.color.theme))
    val progressBar = inflate.findViewById<ProgressBar>(R.id.progressBar)

    init {
        id = R.id.defLoadingViewId
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
        (context.activity()?.window?.decorView as? FrameLayout)?.addView(this)
    }


    fun dismiss() {
        logd("$TAG dismiss ")
        mainThread { gone() }
    }

    fun dismissDelay(delay: Long) {
        logd("$TAG dismissDelay $delay")
        mainThreadDelay(delay) { gone() }
    }


    fun setBackground() {
        layoutDim.setBackgroundColor(Color.parseColor("#80000000"))
    }


    companion object {
        private const val TAG = "【LoadingView】"

        /**
         * 全局获取 LoadingView ,依附于当前显示的 Activity 。
         * 在同一个 Activity 生命周期内调用 withActivity() ,应该得到同一个 LoadingView 对象。
         */
        @JvmStatic
        fun withActivity(context: Context? = ActivityManager.getTopDisplayingActivity()): LoadingView? {
            context ?: return null
            val act = context.activity()
            act ?: return null
            val v = act.findViewById<LoadingView>(R.id.defShowerViewId)
            return v ?: LoadingView(act)
        }

        /**
         * 全局获取 LoadingView ,依附于传入的 container 。
         * 在同一个 container 生命周期内调用 withViewGroup() ,应该得到同一个 LoadingView 对象。
         */
        @JvmStatic
        fun withViewGroup(container: ViewGroup?): LoadingView? {
            container ?: return null
            val v = container.findViewById<LoadingView>(R.id.defShowerViewId)
            return if (v == null) {
                val l = LoadingView(container.context)
                container.addView(l)
                l
            } else {
                v
            }
        }
    }

}
