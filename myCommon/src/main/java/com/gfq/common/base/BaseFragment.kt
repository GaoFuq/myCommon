package com.gfq.common.base

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
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
import com.gfq.common.net.interfacee.defimpl.DefShowerDialog
import com.gfq.common.net.interfacee.defimpl.DefShowerDialogDim0
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
    open val reqDel by lazy { context?.let { RequestDelegate(this, DefShowerView(it)) }}

    //建议在已经有 dim!=0 的Dialog显示时使用。半透明黑色蒙层，会改变状态栏的文字颜色。
    open val reqDelDim by lazy { context?.let { RequestDelegate(this, DefShowerDialog(it)) }}

    //建议在已经有 dim==0 的Dialog显示时使用。全透明蒙层，会改变状态栏的文字颜色
    open val reqDelDim0 by lazy { context?.let { RequestDelegate(this, DefShowerDialogDim0(it)) }}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "onCreateView: " )
        fragBinding = DataBindingUtil.inflate<T>(inflater, layoutId, container, false)
        return fragBinding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: " )
        doOnStart?.invoke()
    }
    abstract fun initView()
    abstract fun initClick()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: " )
        injectForArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: " )
        navController = try {
            findNavController()
        } catch (e: Exception) {
            Log.d(TAG, "onViewCreated: can not find NavController")
            null
        }
        initView()
        initClick()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(TAG, "onViewStateRestored: " )
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


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: " )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: " )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: " )
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach: " )
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: " )
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: " )
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: " )
    }

}