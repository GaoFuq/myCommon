package com.gfq.common.net.interfacee.defimpl

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.gfq.common.R
import com.gfq.common.helper.actlifecycle.doOnDestroyed
import com.gfq.common.net.AbsResponse
import com.gfq.common.net.interfacee.IRequestStateShower
import com.gfq.common.system.activity
import com.gfq.common.system.fragmentActivity
import com.gfq.common.system.logd
import com.gfq.common.system.logd
import com.gfq.common.utils.mainThread
import com.gfq.common.view.gone
import com.gfq.common.view.visible

/**
 *  2022/9/14 10:44
 * @auth gaofuq
 * @description
 * 默认宽高 100dp，黑色背景，外部区域透明，点击外部不响应。
 * PopupWindow实现，自动添加到 Activity 里面。
 */
open class DefShowerPop: PopupWindow(), IRequestStateShower {
    override fun showLoading(message: String?) {

    }

    override fun <T, Resp : AbsResponse<T>> showComplete(response: Resp?) {
    }

    override fun <T, Resp : AbsResponse<T>> showCompleteFailed(response: Resp?) {
    }

    override fun showError(error: String?) {
    }

    override fun dismissRequestStateShower() {
    }

}