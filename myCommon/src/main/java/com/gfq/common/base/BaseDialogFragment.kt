package com.gfq.common.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.*
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.gfq.common.R
import com.gfq.common.dialog.screenW
import com.gfq.common.net.RequestDelegate
import com.gfq.common.net.interfacee.defimpl.DefShowerDialog
import com.gfq.common.net.interfacee.defimpl.DefShowerDialogDim0
import com.gfq.common.net.interfacee.defimpl.DefShowerView
import com.gfq.common.system.injectForArguments
import com.gfq.common.system.updateAttributes


abstract class BaseDialogFragment<T : ViewDataBinding>(
    private val layoutId: Int
) : DialogFragment(layoutId) {
    private val TAG = "【${this::class.java.simpleName}】"
    lateinit var dialogBinding: T
    var doOnDismiss:(()->Unit)?=null
    var doOnStart:(()->Unit)?=null

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
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d(TAG, "onCreateView: ")
        dialogBinding = DataBindingUtil.inflate<T>(inflater, layoutId, container, true)
        return dialogBinding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        injectForArguments()
        setStyle(STYLE_NO_FRAME, R.style.FullTransparentDialog)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(TAG, "onViewStateRestored: ")
        DataBindingUtil.findBinding<T>(requireView())?.let {
            dialogBinding = it
        }
    }

    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager)
    }

    fun show(fragment: Fragment) {
        fragment.fragmentManager?.apply {
            show(this)
        }
    }

    /**
     * bug：Can not perform this action after onSaveInstanceState
     * onSaveInstanceState方法是在该Activity即将被销毁前调用，来保存Activity数据的，如果在保存玩状态后再给它添加Fragment就会出错
     * 解决方法就是把show()方法中的 commit（）方法替换成 commitAllowingStateLoss()、或者直接try
     */
    fun show(fragmentManager: FragmentManager) {
        Log.d(TAG, "show: ")
        val tag = this::class.java.simpleName
        val fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment == null && !this.isAdded) {
            try {
                // 在每个add事务前增加一个remove事务，防止连续的add。造成java.lang.IllegalStateException: Fragment already added
                fragmentManager.beginTransaction().remove(this).commit()
                this.show(fragmentManager, tag)
            } catch (e: Exception) {// 相当于重写了 show() 方法，至于其中的 mDismissed、mShownByMe 这两个变量的值，在 try 中已经设置好了。
                fragmentManager.beginTransaction().add(this, tag).commitAllowingStateLoss()
            }
        }
    }

    override fun dismiss() {
        Log.d(TAG, "dismiss: ")
        // 防止横竖屏切换时 getFragmentManager置空引起的问题：
        // Attempt to invoke virtual method 'android.app.FragmentTransaction
        // android.app.FragmentManager.beginTransaction()' on a null object reference
        fragmentManager ?: return
        try {
            super.dismiss()
        } catch (e: Exception) {
            dismissAllowingStateLoss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        initView()
        initClick()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
        dialog?.window?.updateAttributes {
            it.width = (screenW * 0.7).toInt()
            it.height = -2
//            it.windowAnimations=
        }

        val params =  dialog?.window?.attributes
        setWindowLayoutParams(params)
        dialog?.window?.attributes = params

        doOnStart?.invoke()
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        doOnDismiss?.invoke()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: ")
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Log.d(TAG, "onCancel: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d(TAG, "onDismiss: ")
    }



    abstract fun initView()
    abstract fun initClick()
    abstract fun setWindowLayoutParams (param:WindowManager.LayoutParams?)

}