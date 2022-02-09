package com.gfq.common.view

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.gfq.common.system.dpF
import com.google.android.material.shape.*

/**
 *  2022/2/9 17:09
 * @auth gaofuq
 * @description 给view设置一个聊天气泡的drawable背景
 */
enum class MarkerDirect {
    left,
    top,
    right,
    bottom
}

fun View?.setBubbleBackgroundLeft(
    solid: String = "#bebebe",
    radius: Float = dpF(8),
    markerSize: Float = dpF(5),
    markerOffset: Float = 0f,
) = setBubbleBackground(solid, radius, markerSize, MarkerDirect.left, markerOffset)

fun View?.setBubbleBackgroundRight(
    solid: String = "#bebebe",
    radius: Float = dpF(8),
    markerSize: Float = dpF(5),
    markerOffset: Float = 0f,
) = setBubbleBackground(solid, radius, markerSize, MarkerDirect.right, markerOffset)

fun View?.setBubbleBackgroundTop(
    solid: String = "#bebebe",
    radius: Float = dpF(8),
    markerSize: Float = dpF(5),
    markerOffset: Float = 0f,
) = setBubbleBackground(solid, radius, markerSize, MarkerDirect.top, markerOffset)

fun View?.setBubbleBackgroundBottom(
    solid: String = "#bebebe",
    radius: Float = dpF(8),
    markerSize: Float = dpF(5),
    markerOffset: Float = 0f,
) = setBubbleBackground(solid, radius, markerSize, MarkerDirect.bottom, markerOffset)


fun View?.setBubbleBackground(
    solid: String,
    radius: Float,
    markerSize: Float,
    markerDirect: MarkerDirect,
    markerOffset: Float = 0f,
) {
    this ?: return
    (this.parent as? ViewGroup)?.clipChildren = false
    background = bubbleDrawable(solid, radius, markerSize, markerOffset, markerDirect)
}

/**
 * 生成一个带小三角的气泡drawable，类似于聊天气泡
 * 配合 viewGroup 的 android:clipChildren="false" 使用
 */
fun bubbleDrawable(
    solid: String,
    radius: Float,
    markerSize: Float,
    markerOffset: Float = 0f,
    markerDirect: MarkerDirect,
): MaterialShapeDrawable = materialShapeDrawable(
    shape = {
        setAllCorners(RoundedCornerTreatment())
        setAllCornerSizes(radius)
        when (markerDirect) {
            MarkerDirect.left -> {
                setLeftEdge(OffsetEdgeTreatment(TriangleEdgeTreatment(markerSize, false),
                    markerOffset))
            }

            MarkerDirect.right -> {
                setRightEdge(OffsetEdgeTreatment(TriangleEdgeTreatment(markerSize, false),
                    markerOffset))
            }

            MarkerDirect.top -> {
                setTopEdge(OffsetEdgeTreatment(TriangleEdgeTreatment(markerSize, false),
                    markerOffset))
            }

            MarkerDirect.bottom -> {
                setBottomEdge(OffsetEdgeTreatment(TriangleEdgeTreatment(markerSize, false),
                    markerOffset))
            }
        }
    },
    style = {
        setTint(Color.parseColor(solid))
    }
)

inline fun materialShapeDrawable(
    shape: ShapeAppearanceModel.Builder.() -> Unit,
    style: MaterialShapeDrawable.() -> Unit,
): MaterialShapeDrawable {
    return MaterialShapeDrawable(
        ShapeAppearanceModel.builder().also(shape).build()
    ).also(style)
}

