package com.gfq.common.view

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.gfq.common.system.ActivityManager
import com.github.forjrking.drawable.shapeDrawable
import com.tencent.bugly.crashreport.CrashReport

/**
 *  2021/12/29 11:55
 * @auth gaofuq
 * @description
 */

/**
 * 显示view
 */
fun View.visible() {
    if (!this.isVisible) {
        this.isVisible = true
    }
}

/**
 * 隐藏view
 */
fun View.gone() {
    if (!this.isGone) {
        this.isGone = true
    }
}

/**
 * 不显示view
 */
fun View.invisible() {
    if (!this.isInvisible) {
        this.isInvisible = true
    }
}


/**
 * 点击事件防抖动
 */
inline fun View.setDebounceClick(limitTime: Long = 500L, crossinline onClick: () -> Unit) {
    var lastClickTime = 0L
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > limitTime) {
            lastClickTime = currentTime
            onClick()
        }
    }
}


/**
 * 设置多次点击监听，连击一定次数后触发clickListener
 *
 * @param interval      两次点击的时间间隔
 * @param clickTimes    点击次数
 */
inline fun View.setMultiClicksListener(
    interval: Long = 1000,
    clickTimes: Int,
    crossinline block: () -> Unit
) {
    setOnClickListener(object : View.OnClickListener {
        var firstTime: Long = 0
        var count: Int = 0

        override fun onClick(v: View) {
            val secondTime = System.currentTimeMillis()
            // 判断每次点击的事件间隔是否符合连击的有效范围
            // 不符合时，有可能是连击的开始，否则就仅仅是单击
            if (secondTime - firstTime <= interval) {
                count++
            } else {
                count = 1
            }
            // 延迟，用于判断用户的点击操作是否结束
            firstTime = secondTime
            if (count <= clickTimes) {
                Log.i("setMultiClicksListener", "连续点击次数：$count")
            }
            if (count == clickTimes) {
                block()
            }
        }
    })
}


fun ImageView?.setGif(url: String) {
    this?.let { Glide.with(it).asGif().load(url).into(it) }
}

fun ImageView?.setGif(id: Int) {
    this?.let { Glide.with(it).asGif().load(id).into(it) }
}

fun ImageView?.setImage(imgSource: Any?) {
    if (this == null) return
    if (imgSource == null) return
    try {
        if (this.context is Activity) {
            if (isActivityDestroyed(this.context as Activity)) return
        }

        if (imgSource is Int) {
            this.setImageResource(imgSource)
            if (imgSource == 0) return
        }

        if (imgSource is String && imgSource.endsWith(".gif", true)) {
            Glide.with(context).asGif().load(imgSource).into(this)
            return
        }

        if (imgSource is String && imgSource.startsWith("data:image/png;base64")) {
            val base64 = imgSource.split(",")[1]
            val imageByteArray: ByteArray = Base64.decode(base64, Base64.DEFAULT)
            Glide.with(context).load(imageByteArray).into(this)
            return
        }

        Glide.with(context).load(imgSource).into(this)
    } catch (e: Exception) {
        CrashReport.postCatchedException(e)
    }
}

fun isActivityDestroyed(mActivity: Activity?): Boolean {
    return mActivity == null || mActivity.isFinishing || mActivity.isDestroyed
}

/**
 * 要在 Activity 初始化后立即显示 PopupWindow ，
 * 建议在 onWindowFocusChanged 中调用
 */
fun createPopWindow(
    layoutId: Int, width: Int = -2, height: Int = -2,
    dismissOnTouchOutSide: Boolean = true,
    block: (view: View, pop: PopupWindow) -> Unit = { _, _ -> }
) {
    val view = LayoutInflater.from(ActivityManager.application).inflate(layoutId, null)
    createPopWindow(view, width, height, dismissOnTouchOutSide, block)
}

/**
 * 要在 Activity 初始化后立即显示 PopupWindow ，
 * 建议在 onWindowFocusChanged 中调用
 */
fun createPopWindow(
    view: View, width: Int = -2, height: Int = -2,
    dismissOnTouchOutSide: Boolean = true,
    block: (view: View, pop: PopupWindow) -> Unit = { _, _ -> }
) {
    val pop = PopupWindow(view, width, height)
    pop.setBackgroundDrawable(ColorDrawable())
    pop.isFocusable = true
    pop.isOutsideTouchable = true
    pop.setTouchInterceptor { v: View?, _: MotionEvent? ->
        v?.performClick()
        if (dismissOnTouchOutSide) {
            pop.dismiss()
        }
        true
    }
    block(view, pop)
}

