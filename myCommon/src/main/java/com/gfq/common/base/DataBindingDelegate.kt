package com.gfq.common.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.gfq.common.R
import com.permissionx.guolindev.databinding.PermissionxPermissionItemBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ActivityDataBindingDelegate<out VD : ViewDataBinding>(@LayoutRes private val layoutRes: Int) :
    ReadOnlyProperty<Activity, VD?> {

    private var binding: VD? = null

    override fun getValue(thisRef: Activity, property: KProperty<*>): VD? =
        binding ?: DataBindingUtil.setContentView<VD>(thisRef, layoutRes)
            .also { binding = it }

}
