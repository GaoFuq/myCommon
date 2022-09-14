package com.gfq.common.base

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.gfq.common.R
import com.gfq.common.dialog.screenW
import com.gfq.common.net.RequestDelegate
import com.gfq.common.net.interfacee.defimpl.DefShowerDialog
import com.gfq.common.net.interfacee.defimpl.DefShowerDialogNoDim
import com.gfq.common.net.interfacee.defimpl.DefShowerView
import com.gfq.common.system.injectForArguments
import com.gfq.common.system.updateAttributes


abstract class BaseDialogFragment<T : ViewDataBinding>(
    private val layoutId: Int
) : DialogFragment(layoutId) {

    lateinit var dialogBinding: T
    var doOnDismiss:(()->Unit)?=null

    //半透明黑色蒙层，会改变状态栏的文字颜色
    open val requestDelegate by lazy { RequestDelegate(this, DefShowerDialog(requireContext())) }
    //全透明蒙层，会改变状态栏的文字颜色
    open val requestDelegateNoDim by lazy { RequestDelegate(this, DefShowerDialogNoDim(requireContext())) }
    //显示在Dialog下层，无蒙层，不会改变状态栏的文字颜色
    open val requestDelegateByView by lazy { RequestDelegate(this, DefShowerView(requireContext())) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialogBinding = DataBindingUtil.inflate<T>(inflater, layoutId, container, true)
        return dialogBinding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectForArguments()
        setStyle(STYLE_NO_FRAME, R.style.FullTransparentDialog)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        DataBindingUtil.findBinding<T>(requireView())?.let {
            dialogBinding = it
        }
    }

    fun show(a: FragmentActivity) {
        show(a.supportFragmentManager, this.javaClass.simpleName)
    }

    fun show(a: Fragment) {
        a.activity?.supportFragmentManager?.let { show(it, this.javaClass.simpleName) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.updateAttributes {
            it.width = (screenW * 0.7).toInt()
            it.height = -2
//            it.windowAnimations=
        }

        val params =  dialog?.window?.attributes
        setWindowLayoutParams(params)
        dialog?.window?.attributes = params
    }


    override fun onDestroy() {
        super.onDestroy()
        doOnDismiss?.invoke()
    }

    abstract fun initView()
    abstract fun setWindowLayoutParams (param:WindowManager.LayoutParams?)

}