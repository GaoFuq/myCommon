package com.gfq.common.view.outline

import android.graphics.Outline
import android.graphics.Rect
import android.view.View
import android.view.ViewOutlineProvider
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import kotlin.math.min

/**
 * 圆形 OutlineProvider，以 View 的正中间为圆心裁剪。
 * @param offset 裁剪偏移量，offset > 0 ,扩大裁剪范围，反之缩小。
 */
class CircleOutlineProvider(var offset: Int = 0) : ViewOutlineProvider() {
    private lateinit var view: View

    fun update(offset: Int) {
        this.offset = offset
        if (::view.isInitialized) {
            if (!view.clipToOutline) {
                view.clipToOutline = true
            }
            if (view.outlineProvider == null) {
                view.outlineProvider = this
            }
            view.invalidateOutline()
        }
    }

    override fun getOutline(v: View, o: Outline) {
        view = v
        if (v.width == v.height) {
            o.setRoundRect(0, 0, v.width, v.width, v.width / 2f)
        } else {
            //取正中间
            val rect = Rect()
            v.getDrawingRect(rect)
            var radius = min(v.width, v.height) / 2 + offset
            if (radius <= 0) {
                radius = 0
            }
            o.setRoundRect(rect.centerX() - radius,
                rect.centerY() - radius,
                rect.centerX() + radius,
                rect.centerY() + radius,
                radius.toFloat())
        }

    }

}
