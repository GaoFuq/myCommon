package com.gfq.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import com.gfq.common.R
import com.gfq.common.base.BaseBindingView
import com.gfq.common.databinding.PrivacyViewBinding

/**
 *  2022/8/23 9:57
 * @auth gaofuq
 * @description 用户协议  隐私政策
 */
class PrivacyView(context: Context, attrs: AttributeSet?) :
    BaseBindingView<PrivacyViewBinding>(context, attrs) {
    override fun layoutResId(): Int = R.layout.privacy_view

    val isCheck = ObservableBoolean(false)

    var iconChecked = R.mipmap.icon_checked
    var iconCheckedNot = R.mipmap.icon_checked_not

    init {
        isCheck.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (isCheck.get()) {
                    vBinding.ivCheck.setImageResource(iconChecked)
                } else {
                    vBinding.ivCheck.setImageResource(iconCheckedNot)
                }
            }
        })
        vBinding.layoutCheck.setOnClickListener {
            isCheck.set(!isCheck.get())
        }
    }
}