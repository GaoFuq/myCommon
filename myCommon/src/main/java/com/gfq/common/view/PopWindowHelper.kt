package com.gfq.common.view

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.gfq.common.system.ActivityManager

/**
 *  2022/2/10 10:50
 * @auth gaofuq
 * @description
 */


/**
 * 要在 Activity 初始化后立即显示 PopupWindow ，
 * 建议在 onWindowFocusChanged 中调用
 */
fun createPopWindow(
    layoutId: Int, width: Int = -2, height: Int = -2,
    block: (view: View, pop: PopupWindow) -> Unit = { _, _ -> }
) :PopupWindow{
  val view = LayoutInflater.from(ActivityManager.application).inflate(layoutId, null)
  return  createPopWindow(view, width, height, block)
}

/**
 * 要在 Activity 初始化后立即显示 PopupWindow ，
 * 建议在 onWindowFocusChanged 中调用
 */
fun createPopWindow(
    view: View, width: Int = -2, height: Int = -2,
    block: (view: View, pop: PopupWindow) -> Unit = { _, _ -> }
) :PopupWindow{
    val pop = PopupWindow(view, width, height)
    pop.setBackgroundDrawable(ColorDrawable())
    pop.isFocusable = true
    pop.isOutsideTouchable = true
    block(view, pop)
    return pop
}
