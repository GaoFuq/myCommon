package com.gfq.common.dialog

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gfq.common.R

/**
 *  2022/10/24 15:25
 * @auth gaofuq
 * @description
 */

/**
 * @param withAnim
 * 是否有进入和退出动画，默认有。
 * 当在Dialog上显示Dialog时，建议设置为false，
 * 解决在两个Dialog相互切换的时候的闪屏问题。
 */
abstract class BaseBindingDialog<T : ViewDataBinding>(
    context: Context,
    layoutId: Int,
    withAnim: Boolean = true,
) :
    BaseDialog(context, withAnim = withAnim) {

    val dialogBinding = DataBindingUtil.inflate<T>(
        LayoutInflater.from(context),
        layoutId,
        null,
        false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dialogBinding.root)
        initViews()
    }

    abstract fun initViews()

}