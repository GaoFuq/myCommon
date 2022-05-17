package com.gfq.common.dialog

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gfq.common.system.dpF
import com.gfq.common.system.updateAttributes
import com.gfq.common.view.outline.CornerPosition
import com.gfq.common.view.outline.setCircle
import com.gfq.common.view.outline.setRoundOutline
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.math.abs

/**
 *  2022/5/17 10:55
 * @auth gaofuq
 * @description
 */
abstract class BaseBottomSheetDialog<T : ViewDataBinding>(
    private val mCtx: Context,
    private val layoutId: Int,
    style: Int = 0,
) :
    BottomSheetDialog(mCtx, style) {

    lateinit var dialogBinding: T
    private var screenH = Resources.getSystem().displayMetrics.heightPixels
    private var screenW = Resources.getSystem().displayMetrics.widthPixels

    abstract fun initViews()
    abstract fun initLayoutParams()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBinding = DataBindingUtil.inflate<T>(LayoutInflater.from(mCtx), layoutId, null, false)
        setContentView(dialogBinding.root)
        initViews()
    }


    fun setRadius(r: Float, pos: CornerPosition = CornerPosition.top) {
        dialogBinding.root.setRoundOutline(dpF(r), CornerPosition.top)
    }

    fun setCircle() {
        dialogBinding.root.setCircle()
    }




    /**
     * 屏幕宽度百分比
     */
    fun setWidthPercent(percent: Float) {
        if (percent < 0) return
        window?.updateAttributes {
            it.width = (screenW * percent).toInt()
        }
    }


    fun setWidth(fixed: Int) {
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
    fun placeInCenter(xOffset: Int) {
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
    fun placeInLeft(marginLeft: Int = 0) {
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
    fun placeInRight(marginRight: Int = 0) {
        window?.updateAttributes {
            it.gravity = Gravity.END
            it.x = abs(marginRight)
        }
    }


    fun setDim(dim: Float) {
        window?.updateAttributes {
            it.dimAmount = dim
        }
    }


    override fun onStart() {
        super.onStart()
        dialogBinding.root.post {
            (dialogBinding.root.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)
        }
        initLayoutParams()
    }

}