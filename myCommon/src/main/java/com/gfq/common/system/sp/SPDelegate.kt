package com.gfq.common.system.sp

import android.annotation.SuppressLint
import android.util.Log
import com.gfq.common.system.sp.simple.SPCache
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 属性委托类
 * 示例：[SPCache]
 *
 * 使用模板：
 * class AnySP <T> (def:T?) : SPDelegate<T>("AnySp",def)
 * class UserSP: SPDelegate<User>("user")
 *
 * 自定义数据量示例：[SPData]
 * 注意：
 * 1.基本数据类型的key和声明的变量名称相同。
 * 2.自定义数据类型的key是[SPData.dataKeyName]
 *
 * 声明：
 * 1.自定义数据类的默认值是 null ；
 * 2.基本类型数据的默认值需要手动指定。
 * var isLogin by AnySP(def = false)
 * var user by UserSP()//user的默认值=null
 *
 * 使用：(每次调用获取到的数据都是sp表中最新的值)
 * Log.e("tag",isLogin.toString())
 * Log.e("tag",user.toString())
 *
 * 赋值：(实现SPData的类调用save方法，会直接把sp中对应的值覆盖掉)
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
    private var spTableName: String = "defTable",
    private val default: T? = null,
) : ReadWriteProperty<Any?, T?> {
    private val TAG = "SPDelegate"
    private var dataKeyName: String? = null

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

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return if (dataKeyName == null) {
            SP.get(spTableName).get(property.name, default)
        } else {
            SP.get(spTableName).get(dataKeyName!!, default)
        }
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
}

