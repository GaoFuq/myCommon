package com.gfq.common.dialog

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.gfq.common.system.updateAttributes
import com.gfq.common.view.outline.CornerPosition
import com.gfq.common.view.outline.setCircle
import com.gfq.common.view.outline.setRoundOutline
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.abs

/**
 *  2022/5/17 10:55
 * @auth gaofuq
 * @description
 */
abstract class BaseBottomSheetDialogFragment<T : ViewDataBinding>(
    private val layoutId: Int,
    private val style: Int = 0,
) :
    BottomSheetDialogFragment() {

    lateinit var dialogBinding: T
    private var screenH = Resources.getSystem().displayMetrics.heightPixels
    private var screenW = Resources.getSystem().displayMetrics.widthPixels


    abstract fun initViews()
    abstract fun initLayoutParams()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, style)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialogBinding = DataBindingUtil.inflate<T>(inflater, layoutId, container, false)
        return dialogBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    /**
     * 屏幕宽度百分比的
     */
    fun setWidthPercent(percent: Float) {
        if (percent < 0) return
        dialog?.window?.updateAttributes {
            it.width = (screenW * percent).toInt()
        }
    }


    fun setWidth(fixed: Int) {
        if (fixed < 0) return
        dialog?.window?.updateAttributes {
            it.width = fixed
        }
    }

    fun setRadius(r: Float, pos: CornerPosition = CornerPosition.top) {
        view?.setRoundOutline(r, pos)
    }

    fun setCircle() {
        view?.setCircle()
    }


    /**
     * 需要dialog的宽度小于Activity的宽度。
     * [setWidth],[setWidthPercent]
     * 设置dialog位置居中，并设置左右偏移。x原点在中间。
     */
    fun placeInCenter(xOffset: Int) {
        dialog?.window?.updateAttributes {
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
        dialog?.window?.updateAttributes {
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
        dialog?.window?.updateAttributes {
            it.gravity = Gravity.END
            it.x = abs(marginRight)
        }
    }


    fun setDim(dim: Float) {
        dialog?.window?.updateAttributes {
            it.dimAmount = dim
        }
    }


    override fun onStart() {
        super.onStart()
        view?.post {
            (view?.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)
        }
        initLayoutParams()
    }

    fun show(manager: FragmentManager) {
        show(manager, javaClass.simpleName)
    }

}