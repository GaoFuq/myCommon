package com.gfq.common.view.drawable

import android.graphics.Point
import android.graphics.PointF
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 *  2022/4/7 17:44
 * @auth gaofuq
 * @description
 */
object CanvasHelper {
    /**
     * 获取圆上的点的坐标
     */
    fun getPointOnCircle(rx: Float, ry: Float, radius: Float, angle: Float): PointF {
        val point = PointF()
        val pointX = rx + radius * cos(angle * PI / 2)
        val pointY = ry + radius * sin(angle * PI / 2)
        point.set(pointX.toFloat(), pointY.toFloat())
        return point
    }
}