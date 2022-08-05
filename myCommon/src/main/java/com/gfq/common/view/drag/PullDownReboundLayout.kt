package com.gfq.common.view.drag

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.customview.widget.ViewDragHelper

/**
 *  2022/5/23 17:40
 * @auth gaofuq
 * @description ViewDragHelper 示例
 */
internal class PullDownReboundLayout(
    context: Context,
    attrs: AttributeSet?,
) : LinearLayout(context, attrs) {

    var maxTopOffset = 0
    var maxBottomOffset = 0

    private val callback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        // 确定是否要拖拽
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return true
        }

        // clamp 夹钳, 钳制view的横向位置
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return 0
        }

        // clamp 夹钳, 钳制view的纵向位置
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            if (top <= -maxTopOffset) {
                return -maxTopOffset
            }
            if (top >= height + maxBottomOffset) {
                return height + maxBottomOffset
            }

            return top
        }

        // 开始拖拽
        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            //记录拖拽view的初始位置
        }

        // 拖拽中
        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int,
        ) {

        }

        //松手，拖拽结束
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            //返回到该view的初始位置
            dragHelper.settleCapturedViewAt(0, 0)
            invalidate()
        }

    }

    val dragHelper = ViewDragHelper.create(this, callback)


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return dragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            invalidate()
        }
    }
}