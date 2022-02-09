package com.gfq.common.view.outline

import android.graphics.Outline
import android.graphics.Rect
import android.view.View
import android.view.ViewOutlineProvider
import kotlin.math.min

/**
 * 圆形 OutlineProvider，以 View 的正中间为圆心裁剪。
 * @param offset 裁剪偏移量，offset > 0 ,扩大裁剪范围，反之缩小。
 */
class CircleOutlineProvider(val offset:Int = 0) : ViewOutlineProvider() {

    override fun getOutline(v: View?, o: Outline?) {
        if (o == null) return
        if (v == null) return
        if (v.width == v.height) {
            o.setRoundRect(0, 0, v.width, v.width, v.width / 2f)
        } else {
            //取正中间
            val rect = Rect()
            v.getDrawingRect(rect)
            val radius = min(v.width,v.height) /2 +offset
            o.setRoundRect(rect.centerX()-radius, rect.centerY()-radius, rect.centerX()+radius, rect.centerY()+radius, radius.toFloat())
        }

    }

}
