package com.gfq.common.base

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

//class DataBindingDelegate<T:ViewDataBinding>(private val context: Context): ReadOnlyProperty<Int, T> {
//    override fun getValue(thisRef: Int, property: KProperty<*>): T {
//        return DataBindingUtil.inflate(LayoutInflater.from(context),thisRef,null,false) as T
//    }
//
//    override fun setValue(thisRef: Int, property: KProperty<*>, value: T) {
//
//    }
//}