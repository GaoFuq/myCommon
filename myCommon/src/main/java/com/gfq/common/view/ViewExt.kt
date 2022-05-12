package com.gfq.common.view

import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

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
 * 是否滑动到底部
 */
fun RecyclerView.isScrolled2Bottom() = !canScrollVertically(1)

/**
 * 是否滑动到顶部
 */
fun RecyclerView.isScrolled2Top() = !canScrollVertically(-1)



/**
 * 是否正在使用手指滑动
 */
fun RecyclerView.isScrollingByHand() = scrollState == 1

/**
 * 是否本身在做惯性滑动
 */
fun RecyclerView.isScrollingBySelf() = scrollState == 2

/**
 * 滑动是否已经停止
 */
fun RecyclerView.isScrollingStopped() = scrollState == 0

