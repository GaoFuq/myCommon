package com.gfq.common.view.outline

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

/**
 * 圆角 OutlineProvider
 * 可以设置四个角的半径。
 * @see [CornerPosition]
 */
class RoundCornerOutlineProvider(
    var radius: Float = 0f,
    var cornerPosition: CornerPosition = CornerPosition.all,
) : ViewOutlineProvider() {

    fun update(
        view:View?,
        radius: Float,
        cornerPosition: CornerPosition = CornerPosition.all,
    ) {
        this.radius = radius
        this.cornerPosition = cornerPosition
        if(view!=null) {
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
        var left = 0
        var top = 0
        var right = v.width
        var bottom = v.height
        val radiusInt = radius.toInt() + 1

        if(radius<=0){
            radius = 0f
        }

        when (cornerPosition) {
            CornerPosition.all -> {
            }
            CornerPosition.leftTop -> {
                right += radiusInt
                bottom += radiusInt
            }
            CornerPosition.leftBottom -> {
                top -= radiusInt
                right += radiusInt
            }
            CornerPosition.rightTop -> {
                left -= radiusInt
                bottom += radiusInt
            }
            CornerPosition.rightBottom -> {
                left -= radiusInt
                top -= radiusInt
            }
            CornerPosition.left -> right += radiusInt
            CornerPosition.top -> bottom += radiusInt
            CornerPosition.right -> left -= radiusInt
            CornerPosition.bottom -> top -= radiusInt
        }
        o.setRoundRect(left, top, right, bottom, radius)
    }

}