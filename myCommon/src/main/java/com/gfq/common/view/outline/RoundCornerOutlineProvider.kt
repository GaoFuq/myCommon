package com.gfq.common.view.outline

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

class RoundCornerOutlineProvider(
    var radius: Float = 0f,
    var direct: Direct = Direct.all,
) : ViewOutlineProvider() {

    private var view: View? = null


    fun update(
        radius: Float,
        direct: Direct = Direct.all,
    ) {
        this.radius = radius
        this.direct = direct
        view?.invalidateOutline()
    }

    fun update(
        view: View?,
        radius: Float,
        direct: Direct = Direct.all,
    ) {
        this.view = view
        this.radius = radius
        this.direct = direct
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
        when (direct) {
            Direct.all -> {
            }
            Direct.leftTop -> {
                right += radiusInt
                bottom += radiusInt
            }
            Direct.leftBottom -> {
                top -= radiusInt
                right += radiusInt
            }
            Direct.rightTop -> {
                left -= radiusInt
                bottom += radiusInt
            }
            Direct.rightBottom -> {
                left -= radiusInt
                top -= radiusInt
            }
            Direct.left -> right += radiusInt
            Direct.top -> bottom += radiusInt
            Direct.right -> left -= radiusInt
            Direct.bottom -> top -= radiusInt
        }
        o.setRoundRect(left, top, right, bottom, radius)
    }

}