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
import com.gfq.common.system.injectForArguments

/**
 *  2021/4/13 14:02
 * @auth gaofuq
 * @description
 */
abstract class BaseFragment<T : ViewDataBinding>(private val layoutId: Int) : Fragment(layoutId) {

    lateinit var fragBinding: T
    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragBinding = DataBindingUtil.inflate<T>(inflater, layoutId, container, false)
        return fragBinding.root
    }


    abstract fun initViews()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectForArguments()
        navController = try {
            findNavController()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("BaseFragment", "findNavController error")
            null
        }
        initViews()
    }


    fun popBack(destinationId: Int = 0, inclusive: Boolean = false) {
        if (destinationId == 0) {
            navController?.popBackStack()
        } else {
            navController?.popBackStack(destinationId, inclusive)
        }
    }

    fun navigate(actionId: Int, extra: Map<String, Any?>? = null) {
        if (extra != null) {
            val bundle = Bundle()
            extra.forEach { (key, value) ->
                if (value != null) {
                    when (value) {
                        is String -> {
                            bundle.putString(key, value)
                        }
                        is Int -> {
                            bundle.putInt(key, value)
                        }
                        is Boolean -> {
                            bundle.putBoolean(key, value)
                        }
                    }
                }
            }
            navController?.navigate(actionId, bundle)
        } else {
            navController?.navigate(actionId)
        }
    }
}