package com.gfq.common.dialog

import android.content.res.Resources
import android.view.Gravity
import android.view.View
import androidx.annotation.DimenRes
import androidx.core.view.updateLayoutParams
import com.gfq.common.R
import com.gfq.common.system.updateAttributes
import com.gfq.common.utils.dpF
import com.gfq.common.view.outline.CornerPosition
import com.gfq.common.view.outline.setCircle
import com.gfq.common.view.outline.setRoundOutline
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.math.abs

/**
 *  2022/5/18 14:12
 * @auth gaofuq
 * @description
 */

var screenH = Resources.getSystem().displayMetrics.heightPixels
var screenW = Resources.getSystem().displayMetrics.widthPixels

fun BottomSheetDialog.setMaxHeight(maxHeight: Int) {
    findViewById<View>(R.id.coordinator)?.updateLayoutParams { height = maxHeight }
}

fun BottomSheetDialog.setFixedHeight(fixedHeight: Int) {
    setMaxHeight(fixedHeight)
    behavior.peekHeight = fixedHeight
    behavior.skipCollapsed = true
}

fun BottomSheetDialog.isHideWhenSwipeDown(boo: Boolean) {
    behavior.isHideable = boo
}

fun BottomSheetDialog.setDismissOnTouchOutside(boo: Boolean) {
    setCanceledOnTouchOutside(boo)
    findViewById<View>(R.id.container)?.setOnClickListener {
        if (boo) dismiss() else {//do nothing
        }
    }
}

fun BottomSheetDialog.setRadius(@DimenRes id: Int, pos: CornerPosition = CornerPosition.top) {
    findViewById<View>(R.id.design_bottom_sheet)?.setRoundOutline(context.dpF(id), pos)
}

fun BottomSheetDialog.setCircle() {
    findViewById<View>(R.id.design_bottom_sheet)?.setCircle()
}


/**
 * 屏幕宽度百分比
 */
fun BottomSheetDialog.setWidthPercent(percent: Float) {
    if (percent < 0) return
    window?.updateAttributes {
        it.width = (screenW * percent).toInt()
    }
}


fun BottomSheetDialog.setWidth(fixed: Int) {
    if (fixed < 0) return
    window?.updateAttributes {
        it.width = fixed
    }
}


/**
 * 需要dialog的宽度小于Activity的宽度。
 * [setWidth],[setWidthPercent]
 * 设置dialog位置居中，并设置左右偏移。x原点在中间。
 */
fun BottomSheetDialog.placeInCenter(xOffset: Int) {
    window?.updateAttributes {
        it.gravity = Gravity.CENTER
        it.x = xOffset
    }
}

/**
 * 需要dialog的宽度小于Activity的宽度。
 * [setWidth],[setWidthPercent]
 * 设置dialog位置居左，并设置向右的偏移。x原点在左边。
 */
fun BottomSheetDialog.placeInLeft(marginLeft: Int = 0) {
    window?.updateAttributes {
        it.gravity = Gravity.START
        it.x = abs(marginLeft)
    }
}

/**
 * 需要dialog的宽度小于Activity的宽度。
 * [setWidth],[setWidthPercent]
 * 设置dialog位置居右，并设置向左的偏移。x原点在右边。
 */
fun BottomSheetDialog.placeInRight(marginRight: Int = 0) {
    window?.updateAttributes {
        it.gravity = Gravity.END
        it.x = abs(marginRight)
    }
}


fun BottomSheetDialog.setDim(dim: Float) {
    window?.updateAttributes {
        it.dimAmount = dim
    }
}
