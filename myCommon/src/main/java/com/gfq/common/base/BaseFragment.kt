package com.gfq.common.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gfq.common.net.RequestDelegate
import com.gfq.common.net.interfacee.defimpl.DefShowerDialogDim
import com.gfq.common.net.interfacee.defimpl.DefShowerDialogDimNt
import com.gfq.common.net.interfacee.defimpl.DefShowerView
import com.gfq.common.system.injectForArguments

/**
 * 2021/4/13 14:02
 * @auth gaofuq
 * @description
 */
abstract class BaseFragment<T : ViewDataBinding>(private val layoutId: Int) : Fragment(layoutId) {

    lateinit var fragBinding: T
    var navController: NavController? = null
    var doOnStart:(()->Unit)?=null
    private val TAG = "【${javaClass.simpleName}】"

    //默认建议使用View的实现类。显示在Dialog下层，无蒙层，不会改变状态栏的文字颜色
    open val requestDelegate by lazy { RequestDelegate(this, DefShowerView(requireContext())) }

    //建议在已经有 dim!=0 的Dialog显示时使用。半透明黑色蒙层，会改变状态栏的文字颜色。
    open val requestDelegateDim by lazy { RequestDelegate(this, DefShowerDialogDim(requireContext())) }

    //建议在已经有 dim==0 的Dialog显示时使用。全透明蒙层，会改变状态栏的文字颜色
    open val requestDelegateDimNt by lazy { RequestDelegate(this, DefShowerDialogDimNt(requireContext())) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragBinding = DataBindingUtil.inflate<T>(inflater, layoutId, container, false)
        return fragBinding.root
    }

    override fun onStart() {
        super.onStart()
        doOnStart?.invoke()
    }
    abstract fun initViews()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectForArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = try {
            findNavController()
        } catch (e: Exception) {
            Log.d(TAG, "onViewCreated: can not find NavController")
            null
        }
        initViews()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        DataBindingUtil.findBinding<T>(requireView())?.let {
            fragBinding = it
        }
    }

    fun popBack(destinationId: Int = 0, inclusive: Boolean = false) {
        if (navController == null) {
            Log.d(TAG, "popBack: can not find NavController")
            return
        }
        if (destinationId == 0) {
            navController?.popBackStack()
        } else {
            navController?.popBackStack(destinationId, inclusive)
        }
    }

    fun navigate(actionId: Int, bundle: Bundle? = null) {
        if (navController == null) {
            Log.d(TAG, "navigate: can not find NavController")
            return
        }
        navController?.navigate(actionId, bundle)
    }
}