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
import com.gfq.common.system.fillParams
import com.gfq.common.system.injectForArguments
import com.gfq.common.system.log

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectForArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = try {
            findNavController()
        } catch (e: Exception) {
            log("can not find NavController")
            null
        }
        initViews()
    }


    fun popBack(destinationId: Int = 0, inclusive: Boolean = false) {
        if (navController == null) {
            log("can not find NavController")
            return
        }
        if (destinationId == 0) {
            navController?.popBackStack()
        } else {
            navController?.popBackStack(destinationId, inclusive)
        }
    }

    fun navigate(actionId: Int, vararg params: Pair<String, Any?>) {
        if (navController == null) {
            log("can not find NavController")
            return
        }
        if (params.isNotEmpty()) {
            val bundle = Bundle()
            bundle.fillParams(params)
            navController?.navigate(actionId, bundle)
        } else {
            navController?.navigate(actionId)
        }
    }
}