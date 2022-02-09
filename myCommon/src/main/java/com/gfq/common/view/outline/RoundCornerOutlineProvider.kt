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

    private var view: View? = null


    fun update(
        radius: Float,
        cornerPosition: CornerPosition = CornerPosition.all,
    ) {
        this.radius = radius
        this.cornerPosition = cornerPosition
        view?.invalidateOutline()
    }

    fun update(
        view: View?,
        radius: Float,
        cornerPosition: CornerPosition = CornerPosition.all,
    ) {
        this.view = view
        this.radius = radius
        this.cornerPosition = cornerPosition
        if (view?.clipToOutline == false) {
            view.clipToOutline = true
        }
        if (view?.outlineProvider == null) {
            view?.outlineProvider = this
        }
        view?.invalidateOutline()
    }

    override fun getOutline(v: View?, o: Outline?) {
        if (o == null) return
        if (v == null) return
        var left = 0
        var top = 0
        var right = if (view == null) {
            v.width
        } else {
            view!!.width
        }
        var bottom = if (view == null) {
            v.height
        } else {
            view!!.height
        }
        val radiusInt = radius.toInt() + 1
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