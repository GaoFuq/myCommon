package com.gfq.common.helper

import android.os.SystemClock
import android.view.*
import android.widget.RelativeLayout
import androidx.core.view.updateLayoutParams
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

/**
 * 需要拖动的 View 父布局必须是 RelativeLayout
 * 并且布局内不能添加rule限制
 * 使用 initPosition() 初始化位置
 *
val moveHelper=MoveHelper(view)
moveHelper.initPosition(Gravity.END or Gravity.BOTTOM, rightMargin = dp(R.dimen.dp12), bottomMargin = dp(R.dimen.dp190))
actBinding.freeTake.setOnTouchListener { _, event ->
    moveHelper.moveWithFinger(event,dp(R.dimen.dp12),dp(R.dimen.dp50),dp(R.dimen.dp12),dp(R.dimen.dp20))
}
 */
class MoveHelper(private val view: View) {


    private var lastX = 0f
    private var lastY = 0f
    private val viewParent = view.parent as? RelativeLayout
    private val slop = ViewConfiguration.get(view.context).scaledTouchSlop
    private var isKeepShape = true//保持宽高不变
    private var mDownTime:Long = 0


    // 限制移动区域
    // 默认限制移动区域在父布局内部
    fun moveWithFinger(
        event: MotionEvent,
        leftMargin: Int = 0,
        topMargin: Int = 0,
        rightMargin: Int = 0,
        bottomMargin: Int = 0,
    ): Boolean {
        viewParent ?: return false

        val x = event.rawX
        val y = event.rawY

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownTime = SystemClock.elapsedRealtime()
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = x - lastX
                val dy = y - lastY
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    this.leftMargin += dx.toInt()
                    this.topMargin += dy.toInt()
                    clampMoveBounds(this, leftMargin, topMargin, rightMargin, bottomMargin)
                }

                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_UP -> {
                val disTime = SystemClock.elapsedRealtime() - mDownTime
                val dx = x - lastX
                val dy = y - lastY
                if (dx.absoluteValue < slop && dy.absoluteValue < slop && disTime < 300) {
                    view.performClick()
                }
            }
        }
        return true
    }

    //限制移动区域
    //在父布局的区域内 margin
    private fun clampMoveBounds(
        param: ViewGroup.MarginLayoutParams,
        leftMargin: Int,
        topMargin: Int,
        rightMargin: Int,
        bottomMargin: Int,
    ) {
        viewParent ?: return
        param.leftMargin = max(param.leftMargin, leftMargin)
        param.rightMargin = rightMargin
        param.topMargin = max(param.topMargin, topMargin)
        param.bottomMargin = bottomMargin

        if (isKeepShape) {
            param.leftMargin = min(param.leftMargin, viewParent.width - view.width - rightMargin)
            param.topMargin = min(param.topMargin, viewParent.height - view.height - topMargin)
        }

    }


    //初始化位置
    //leftMargin,topMargin都相对于父布局的左上角
    fun initPosition(
        gravity: Int,
        leftMargin: Int = 0,
        topMargin: Int = 0,
        rightMargin: Int = 0,
        bottomMargin: Int = 0,
    ) {
        viewParent ?: return
        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            val centerX = viewParent.width / 2 - width / 2
            val centerY = viewParent.height / 2 - height / 2
            val top = 0
            val start = 0
            val bottom = viewParent.height - height
            val end = viewParent.width - width
            when (gravity) {
                Gravity.CENTER -> {
                    this.leftMargin = centerX
                    this.topMargin = centerY
                }
                Gravity.START -> this.leftMargin = start
                Gravity.TOP -> this.topMargin = top
                Gravity.END -> this.leftMargin = end
                Gravity.BOTTOM -> this.topMargin = bottom

                Gravity.CENTER_VERTICAL -> this.topMargin = centerY
                Gravity.CENTER_HORIZONTAL -> this.leftMargin = centerX

                Gravity.CENTER_VERTICAL or Gravity.END -> {
                    this.topMargin = centerY
                    this.leftMargin = end
                }
                Gravity.CENTER_VERTICAL or Gravity.START -> {
                    this.topMargin = centerY
                    this.leftMargin = start
                }
                Gravity.CENTER_HORIZONTAL or Gravity.TOP -> {
                    this.leftMargin = centerX
                    this.topMargin = top
                }
                Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM -> {
                    this.leftMargin = centerX
                    this.topMargin = bottom
                }
                Gravity.START or Gravity.TOP -> {
                    this.leftMargin = 0
                    this.topMargin = 0
                }
                Gravity.START or Gravity.BOTTOM -> {
                    this.leftMargin = 0
                    this.topMargin = bottom
                }
                Gravity.END or Gravity.TOP -> {
                    this.leftMargin = end
                    this.topMargin = 0
                }
                Gravity.END or Gravity.BOTTOM -> {
                    this.leftMargin = end
                    this.topMargin = bottom
                }
            }
            this.leftMargin+=leftMargin
            this.topMargin+=topMargin
            this.leftMargin-=rightMargin
            this.topMargin-=bottomMargin

        }
    }

}
