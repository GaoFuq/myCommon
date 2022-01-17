package com.gfq.common.system.sp

import android.annotation.SuppressLint
import android.util.Log
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 属性委托类
 *
 *
 * 使用模板：[SPData]
 * class AnySP <T> (key:String,def:T?) : SPDelegate<T>("AnySp",key,def)
 * class UserSP (key:String) : SPDelegate<User>("user",key)
 *
 * 示例：
 * data class User(var name:String):SPData()
 *
 * 声明：
 * var isLogin by AnySP("isLogin",false)
 * var user by UserSP(key = "user")
 * var user1 by UserSP(key = "user1")
 *
 * 使用：(每次调用获取到的数据都是sp表中最新的值)
 * Log.e("tag",isLogin.toString())
 * Log.e("tag",user.toString())
 *
 * 赋值：(实现SPData的类掉用save方法，会直接把sp中对应的值覆盖掉)
 * isLogin = true
 * user = User("name")
 * User("name").save()
 *
 * 自定义数据类-新数据的保存：
 * val uData = User("name")
 * uData.save()
 *
 * 自定义数据类-老数据的更新：
 * val uData = User("name")
 * uData.save()
 */
@SuppressLint("ApplySharedPref")
open class SPDelegate<T>(
    private val spFormName: String,
    private val key: String,
    private val default: T? = null,
) : ReadWriteProperty<Any?, T?> {
    private val TAG = "SPDelegate"
    init {
        Log.i(TAG, "init spFormName = $spFormName ")
        val spFormCount = SP.getSaveAllSp().getInt(SP.spFormCount, 0)
        Log.i(TAG, "init spFormCount = $spFormCount ")

        val spNameList = mutableListOf<String>()
        for (index in 0 until spFormCount) {
            val existSpFormName = SP.getSaveAllSp().getString("${SP.formIndex_}$index", null)
            existSpFormName?.let { spNameList.add(it) }
            Log.i(TAG, "key = ${SP.formIndex_}$index , existSpFormName = $existSpFormName")
        }

        Log.i(TAG, "spCount = $spFormCount spNameList = $spNameList")

        if (spNameList.all { it != spFormName }) {
            SP.getSaveAllSp().edit().putInt(SP.spFormCount, spFormCount + 1).commit()
            SP.getSaveAllSp().edit().putString("${SP.formIndex_}${spFormCount}", spFormName).commit()
            Log.i(TAG, "更新 spFormCount = ${spFormCount + 1}")
            Log.i(TAG, "保存 ${SP.formIndex_}${spFormCount} = $spFormName")
        }
        Log.i(TAG, "--------------------------------")
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return SP.get(spFormName).get(key, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        SP.get(spFormName).put(key, value)
    }
}

