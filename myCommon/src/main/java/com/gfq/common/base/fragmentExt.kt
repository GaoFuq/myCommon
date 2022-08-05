package com.gfq.common.base

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 *  2022/8/5 17:46
 * @auth gaofuq
 * @description
 */
inline fun <reified T : Fragment> createFragment(args: Bundle? = null): T {
    val mFragment: T = T::class.java.newInstance()
    mFragment.arguments = args
    return mFragment
}
