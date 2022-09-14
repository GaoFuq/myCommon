package com.gfq.common.view

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.gfq.common.R
import com.gfq.common.utils.dpF
import com.google.android.material.shape.*

/**
 *  2022/2/9 17:09
 * @auth gaofuq
 * @description 给view设置一个带尖角的聊天气泡drawable背景
 */

/**
 * 尖角朝向
 */
enum class MarkerDirect {
    left,
    top,
    right,
    bottom
}

/**
 * 设置尖角朝左的气泡背景drawable
 * @see [setBubbleBackground]
 */
fun View.setBubbleBackgroundLeft(
    solid: String = "#bebebe",
    radius: Float = dpF(R.dimen.dp8),
    markerSize: Float = dpF(R.dimen.dp5),
    markerOffset: Float = 0f,
) = setBubbleBackground(solid, radius, markerSize, MarkerDirect.left, markerOffset)

/**
 * 设置尖角朝右的气泡背景drawable
 * @see [setBubbleBackground]
 */
fun View.setBubbleBackgroundRight(
    solid: String = "#bebebe",
    radius: Float =dpF(R.dimen.dp8),
    markerSize: Float = dpF(R.dimen.dp5),
    markerOffset: Float = 0f,
) = setBubbleBackground(solid, radius, markerSize, MarkerDirect.right, markerOffset)

/**
 * 设置尖角朝上的气泡背景drawable
 * @see [setBubbleBackground]
 */
fun View.setBubbleBackgroundTop(
    solid: String = "#bebebe",
    radius: Float =dpF(R.dimen.dp8),
    markerSize: Float = dpF(R.dimen.dp5),
    markerOffset: Float = 0f,
) = setBubbleBackground(solid, radius, markerSize, MarkerDirect.top, markerOffset)

/**
 * 设置尖角朝下的气泡背景drawable
 * @see [setBubbleBackground]
 */
fun View.setBubbleBackgroundBottom(
    solid: String = "#bebebe",
    radius: Float =dpF(R.dimen.dp8),
    markerSize: Float = dpF(R.dimen.dp5),
    markerOffset: Float = 0f,
) = setBubbleBackground(solid, radius, markerSize, MarkerDirect.bottom, markerOffset)

/**
 * 设置气泡背景drawable
 * @param markerSize 尖角朝向的直线距离 px
 * @param markerDirect 尖角朝向
 * @param markerOffset 尖角在该边的水平偏移量，默认不偏移，在中间。
 * * 尖角在的左边或右边[MarkerDirect.left],[MarkerDirect.right]，markerOffset > 0 ,尖角向上偏移，反之向下偏移。
 * * 尖角在上边或下边[MarkerDirect.top],[MarkerDirect.bottom]，markerOffset > 0 ,尖角向右偏移，反之向左偏移。
 */
fun View.setBubbleBackground(
    solid: String,
    radius: Float,
    markerSize: Float,
    markerDirect: MarkerDirect,
    markerOffset: Float = 0f,
) {
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

