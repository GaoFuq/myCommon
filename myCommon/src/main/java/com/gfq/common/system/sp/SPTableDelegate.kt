package com.gfq.common.system.sp

import android.annotation.SuppressLint
import android.util.Log
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import com.gfq.common.system.sp.simple.KotlinUseSimple
import java.lang.RuntimeException

/**
 * 属性委托类
 * 示例：[KotlinUseSimple]
 *
 * 使用模板：
 * class AnySP <T> (def:T?) : SPDelegate<T>("AnySp",def)
 * class UserSP: SPDelegate<User>("user")
 *
 * 自定义数据示例：[SPData]
 * 注意：
 * 1.基本数据类型的key和声明的变量名称相同。
 * 2.自定义数据类型的key是[SPData.dataKeyName]
 *
 * 声明：
 * 默认值需要在创建时提供；
 *
 * 使用：(每次调用获取到的数据都是sp表中最新的值)
 * Log.e("tag",isLogin.toString())
 * Log.e("tag",user.toString())
 */
@SuppressLint("ApplySharedPref")
open class SPTableDelegate<T>(
    private val default: T,
    private var spTableName: String = defaultSPTableName,
) : ReadWriteProperty<Any?, T?> {
    private val TAG = "SPDelegate"
    private var dataKeyName: String? = null

    companion object {
        const val defaultSPTableName = "defSPTable"
    }

    init {
        Log.i(TAG, "init spTableName = $spTableName ")
        val spTableCount = SP.getSaveAllSp().getInt(SP.spTableCount, 0)
        Log.i(TAG, "init spTableCount = $spTableCount ")

        val spNameList = mutableListOf<String>()
        for (index in 0 until spTableCount) {
            val existSpTableName = SP.getSaveAllSp().getString("${SP.tableIndex_}$index", null)
            existSpTableName?.let { spNameList.add(it) }
            Log.i(TAG, "key = ${SP.tableIndex_}$index , existSpTableName = $existSpTableName")
        }

        Log.i(TAG, "spCount = $spTableCount spNameList = $spNameList")

        if (spNameList.all { it != spTableName }) {
            SP.getSaveAllSp().edit().putInt(SP.spTableCount, spTableCount + 1).commit()
            SP.getSaveAllSp().edit().putString("${SP.tableIndex_}${spTableCount}", spTableName)
                .commit()
            Log.i(TAG, "更新 spTableCount = ${spTableCount + 1}")
            Log.i(TAG, "保存 ${SP.tableIndex_}${spTableCount} = $spTableName")
        }
        Log.i(TAG, "--------------------------------")
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getOrDefault(dataKeyName ?: property.name)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        if (value is SPData) {
            this.dataKeyName = value.dataKeyName
            this.spTableName = value.spTableName
        } else {
            this.dataKeyName = property.name
        }
        SP.get(spTableName).put(dataKeyName!!, value)
    }

    fun clear() = SP.clear(spTableName)

    fun getCacheSize() = SP.getCacheSize(spTableName)

    /**
     * java 自定义数据类型数据调用该方法获取最新值
     */
    fun getOrDefault(): T {
        if (default is SPData) {
            return SP.get(spTableName).getOrDefault(default.dataKeyName, default)
        } else {
            throw RuntimeException("getOrDefault() 方法返回的数据类型必须实现 SPData 接口")
        }
    }

    /**
     * java 基本类型数据调用该方法获取最新值。
     * @param dataKeyName 必须和定义的字段名一致
     */
    fun getOrDefault(dataKeyName: String): T {
        if (default is SPData) {
            return getOrDefault()
        } else {
            return SP.get(spTableName).getOrDefault(dataKeyName, default)
        }
    }

}

