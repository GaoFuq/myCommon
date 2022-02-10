package com.gfq.common.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import com.gfq.common.R
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils

/**
 *  2022/2/9 9:45
 * @auth gaofuq
 * @description
 */

/**
 * * 给 View 添加一个 badge（圆形气泡（徽章））。
 * * app需要使用 Theme.MaterialComponents 主题。
 * * 不适合在 recyclerView 的 itemView 里面使用。
 * * 依靠id定位的布局，如：RelativeLayout，ConstraintLayout，
 * 使用该方法后会导致 findViewById 失效。dataBinding 不受影响。
 * * 如果[addBadge]之后需要使用 findViewById 获取该 View，就不要使用该方法。
 * @see [BadgeDrawable]
 * @see [getBadge]
 * @see [removeBadge]
 */
@SuppressLint("UnsafeOptInUsageError")
fun View?.addBadge(
    apply: BadgeDrawable.() -> Unit = {
        badgeGravity = BadgeDrawable.TOP_END
        number = 0
        maxCharacterCount = 3
        backgroundColor = Color.RED
        isVisible = true
    },
) {
    this ?: return
    if (this.getBadge() != null) return

    val badge = BadgeDrawable.create(this.context).apply {
        apply()
    }
    this.setTag(R.id.tag_badge, badge)
    val container by lazy { FrameLayout(this.context) }
    if (this !is TextView) {
        val p = this.parent as? ViewGroup
        val index = p?.indexOfChild(this)
        p?.removeView(this)
        if (index == null) {
            p?.addView(container, this.layoutParams)
        } else {
            p.addView(container, index, this.layoutParams)
        }
        //依靠id定位的布局，如：RelativeLayout，ConstraintLayout
        //会导致findViewById失效,dataBinding不受影响
        if (p !is LinearLayout) {
            container.id = this.id
        }
        container.addView(this, -1, -1)
    }
    doOnPreDraw {
        if (this is TextView) {
            BadgeUtils.attachBadgeDrawable(badge, this)
        } else {
            BadgeUtils.attachBadgeDrawable(badge, this, container)
        }
    }
}

/**
 * 从 View 获取 badge
 * @see [addBadge]
 * @see [removeBadge]
 */
fun View?.getBadge(): BadgeDrawable? {
    this ?: return null
    return this.getTag(R.id.tag_badge) as? BadgeDrawable
}

/**
 * * 从 View 移除 badge 。
 * * findViewById 恢复正常。
 * @see [addBadge]
 * @see [getBadge]
 */
@SuppressLint("UnsafeOptInUsageError")
fun View?.removeBadge() {
    this ?: return
    val badge = this.getTag(R.id.tag_badge) as? BadgeDrawable
    if (badge != null) {
        val p = this.parent as? ViewGroup
        if (p is FrameLayout) {
            this.id = p.id
            p.removeView(this)
            val pp = p.parent as? ViewGroup
            pp?.removeView(p)
            pp?.addView(this, p.layoutParams)
            this.setTag(R.id.tag_badge, null)
            BadgeUtils.detachBadgeDrawable(badge, this)
        } else {
            this.setTag(R.id.tag_badge, null)
            BadgeUtils.detachBadgeDrawable(badge, this)
        }
    }
}

