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

/**
 * @link https://juejin.cn/post/7165427955902971918
控件的可见性受到诸多因素的影响，下面是影响控件可见性的十大因素：
1.手机电源开关
2.Home 键
3.动态替换的 Fragment 遮挡了原有控件
4.ScrollView, NestedScrollView 的滚动
5.ViewPager, ViewPager2 的滚动
6.RecyclerView 的滚动
7.被 Dialog 遮挡
8.Activity 切换
9.同一 Activity 中 Fragment 的切换
10.手动调用 View.setVisibility(View.GONE)
11.被输入法遮盖
 * View 可见性检测，最大用处在于检测 Fragment 的可见性
 */
fun View.onVisibilityChange(
    viewGroups: List<ViewGroup> = emptyList(), // 会被插入 Fragment 的容器集合
    needScrollListener: Boolean = true,
    block: (view: View, isVisible: Boolean) -> Unit
) {
    val KEY_VISIBILITY = "KEY_VISIBILITY".hashCode()
    val KEY_HAS_LISTENER = "KEY_HAS_LISTENER".hashCode()
    // 若当前控件已监听可见性，则返回
    if (getTag(KEY_HAS_LISTENER) == true) return

    // 检测可见性
    val checkVisibility = {
        // 获取上一次可见性
        val lastVisibility = getTag(KEY_VISIBILITY) as? Boolean
        // 判断控件是否出现在屏幕中
        val isInScreen = this.isInScreen
        // 首次可见性变更
        if (lastVisibility == null) {
            if (isInScreen) {
                block(this, true)
                setTag(KEY_VISIBILITY, true)
            }
        }
        // 非首次可见性变更
        else if (lastVisibility != isInScreen) {
            block(this, isInScreen)
            setTag(KEY_VISIBILITY, isInScreen)
        }
    }

    // 全局重绘监听器
    class LayoutListener : ViewTreeObserver.OnGlobalLayoutListener {
        // 标记位用于区别是否是遮挡case
        var addedView: View? = null
        override fun onGlobalLayout() {
            // 遮挡 case
            if (addedView != null) {
                // 插入视图矩形区域
                val addedRect = Rect().also { addedView?.getGlobalVisibleRect(it) }
                // 当前视图矩形区域
                val rect = Rect().also { this@onVisibilityChange.getGlobalVisibleRect(it) }
                // 如果插入视图矩形区域包含当前视图矩形区域，则视为当前控件不可见
                if (addedRect.contains(rect)) {
                    block(this@onVisibilityChange, false)
                    setTag(KEY_VISIBILITY, false)
                } else {
                    block(this@onVisibilityChange, true)
                    setTag(KEY_VISIBILITY, true)
                }
            }
            // 非遮挡 case
            else {
                checkVisibility()
            }
        }
    }

    val layoutListener = LayoutListener()
    // 编辑容器监听其插入视图时机
    viewGroups.forEachIndexed { index, viewGroup ->
        viewGroup.setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View?, child: View?) {
                // 当控件插入，则置标记位
                layoutListener.addedView = child
            }

            override fun onChildViewRemoved(parent: View?, child: View?) {
                // 当控件移除，则置标记位
                layoutListener.addedView = null
            }
        })
    }
    viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
    // 全局滚动监听器
    var scrollListener:ViewTreeObserver.OnScrollChangedListener? = null
    if (needScrollListener) {
        scrollListener = ViewTreeObserver.OnScrollChangedListener { checkVisibility() }
        viewTreeObserver.addOnScrollChangedListener(scrollListener)
    }
    // 全局焦点变化监听器
    val focusChangeListener = ViewTreeObserver.OnWindowFocusChangeListener { hasFocus ->
        val lastVisibility = getTag(KEY_VISIBILITY) as? Boolean
        val isInScreen = this.isInScreen
        if (hasFocus) {
            if (lastVisibility != isInScreen) {
                block(this, isInScreen)
                setTag(KEY_VISIBILITY, isInScreen)
            }
        } else {
            if (lastVisibility == true) {
                block(this, false)
                setTag(KEY_VISIBILITY, false)
            }
        }
    }
    viewTreeObserver.addOnWindowFocusChangeListener(focusChangeListener)
    // 为避免内存泄漏，当视图被移出的同时反注册监听器
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View?) {
        }

        override fun onViewDetachedFromWindow(v: View?) {
            v ?: return
            // 有时候 View detach 后，还会执行全局重绘，为此退后反注册
            post {
                try {
                    v.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
                } catch (_: java.lang.Exception) {
                    v.viewTreeObserver.removeGlobalOnLayoutListener(layoutListener)
                }
                v.viewTreeObserver.removeOnWindowFocusChangeListener(focusChangeListener)
                if(scrollListener !=null) v.viewTreeObserver.removeOnScrollChangedListener(scrollListener)
                viewGroups.forEach { it.setOnHierarchyChangeListener(null) }
            }
            removeOnAttachStateChangeListener(this)
        }
    })
    // 标记已设置监听器
    setTag(KEY_HAS_LISTENER, true)
}

