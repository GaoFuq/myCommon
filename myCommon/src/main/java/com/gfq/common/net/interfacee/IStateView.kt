package com.gfq.common.net.interfacee

import android.view.View
import android.widget.FrameLayout
import com.gfq.common.R

/**
 *  2022/8/10 16:01
 * @auth gaofuq
 * @description
 * abstract class BaseStateView : IStateView {
 * override val stateViewContainer: FrameLayout
 * get() = TODO("Not yet implemented")
 * override val stateEmptyView: View
 * get() = TODO("Not yet implemented")
 * override val stateErrorView: View
 * get() = TODO("Not yet implemented")
 * }
 *
 * class CC : FragmentActivity() {
 * val delegate = RequestDelegate(this, object : BaseStateView() {
 * override fun click2Refresh() {
 * refresh()
 * }
 * })
 *
 * fun refresh() {
 * //        delegate.request({})
 * }
 * }
 */
interface IStateView {
    val stateViewContainer: FrameLayout
    val stateEmptyView: View
    val stateErrorView: View

    fun clickAction(){}

    fun showStateEmptyView() {
        reset()
        stateViewContainer.addView(stateEmptyView)
    }

    fun showStateErrorView() {
        reset()
        stateViewContainer.addView(stateErrorView)
    }


    fun reset() {
        stateViewContainer.removeView(stateEmptyView)
        stateViewContainer.removeView(stateErrorView)
        val temp = stateEmptyView.findViewWithTag<View>(R.string.stateViewRefreshTag)
        if (!temp.hasOnClickListeners()) {
            temp.setOnClickListener { clickAction() }
        }
        val temp2 = stateErrorView.findViewWithTag<View>(R.string.stateViewRefreshTag)
        if (!temp2.hasOnClickListeners()) {
            temp2.setOnClickListener { clickAction() }
        }
    }
}