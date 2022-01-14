package com.gfq.common.system

/**
 *  2021/12/29 15:33
 * @auth gaofuq
 * @description
 */

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 管理包里面所有的sp表单 。
 * @see [SPDelegate]
 * 使用步骤：
 *
 * 1.定义sp表
 * class SimpleSP <T> (key:String,def:T?) : SPDelegate<T>("SimpleSP",key,def)
 *
 * 2.定义字段名称和类型
 * var simple by SimpleSP("simple",false)
 * var simple by SimpleSP("simple","")
 * var simple by SimpleSP("simple",0)
 *
 * 3.获取值 Log.e("tag",simple.toString())
 * 4.赋值   simple        = true
 * 4.赋值   simple        = "下次一定"
 * 4.赋值   simple        = 666
 *
 * API
 *  @see [SP.get]
 *  @see [SP.getAllSPFormNames]
 *  @see [SP.getAllSPFormData]
 *  @see [SP.clear]
 *  @see [SP.clearAll]
 *
 */
object SP {

    /**
     * sp表单数量
     */
    internal const val spFormCount = "spFormCount"

    /**
     * sp表单索引
     */
    internal const val formIndex_ = "form_index_"

    /**
     * 储存所有sp表单的sp表单的名称后缀
     */
    internal const val nameSuffix = ".sp_all_info666"

    /**
     * 获取储存了包里所有sp表单信息的sp
     *
     * 储存包里面所有的sp表单 数量 名称 和 名称对应的编号
     * key: [spFormCount]       value: sp表单数量
     * key: [formIndex_]+index  value: sp表单名称
     */
    internal fun getSaveAllSp() = get(ActivityManager.application.packageName + nameSuffix)

    /**
     * 通过sp名称获取[SharedPreferences]实例
     */
    fun get(name: String) = ActivityManager.application.getSharedPreferences(name, 0)!!

    @RequiresApi(Build.VERSION_CODES.N)
    fun deleteSP(name: String) = ActivityManager.application.deleteSharedPreferences(name)


    /**
     * 获取所有SP表单名称的集合
     */
    fun getAllSPFormNames(): List<String> {
        val spCount = getSaveAllSp().getInt(spFormCount, 0)
        val list = mutableListOf<String>()
        for (index in 0 until spCount) {
            val existName = getSaveAllSp().getString("${formIndex_}$index", "")
            existName?.let { list.add(it) }
        }
        return list
    }

    /**
     * 获取所有SP表单的数据Map
     * key：sp表单名称
     * value：sp表单所有数据
     */
    fun getAllSPFormData(): Map<String, Any?> {
        val list = getAllSPFormNames()
        val map = mutableMapOf<String, Map<String, Any?>>()
        list.forEach { name ->
            map[name] = get(name).all
        }
        return map
    }

    /**
     * 清空指定名称的 sp 表单里面的数据
     */
    fun clear(name: String) {
        get(name).edit().clear().apply()
    }


    /**
     * 清空所有 sp 表单里面的数据
     */
    fun clearAll() {
        getAllSPFormNames().forEach { name ->
            get(name).edit().clear().apply()
        }
    }

    /**
     * 删除指定名称的 sp 表单
     * 有问题
     */
    internal fun delete(name: String) {
        val spFile =
            File("data/data/${ActivityManager.application.packageName}/shared_prefs", "$name.xml")
        if (spFile.exists()) {
            val deleteSuccess = spFile.delete()
            if (deleteSuccess) {
                val spFormIndex = getSaveAllSp().getString(name, null)
                getSaveAllSp().remove(name)
                spFormIndex?.let {
                    //在所有sp信息数据表中，把指定名称的sp表单信息删除
                    getSaveAllSp().remove(it)
                }
                val count = getSaveAllSp().getInt(spFormCount, 0)
                if (count > 0) {
                    getSaveAllSp().put(spFormCount, count - 1)
                }
            }
        }
    }

    /**
     * 删除所有 sp 表单
     * 有问题
     */
    internal fun deleteAll() {
        getAllSPFormNames().forEach { name ->
            delete(name)
        }
    }

}

/**
 * 属性委托类
 */
@SuppressLint("ApplySharedPref")
open class SPDelegate<T>(
    private val name: String,
    private val key: String,
    private val default: T? = null,
) : ReadWriteProperty<Any?, T?> {

    init {
        Log.i("SPDelegate", "init name = $name ")
        val spCount = SP.getSaveAllSp().getInt(SP.spFormCount, 0)
        Log.i("SPDelegate", "init spCount = $spCount ")

        val spNameList = mutableListOf<String>()
        for (index in 0 until spCount) {
            val existName = SP.getSaveAllSp().getString("${SP.formIndex_}$index", null)
            existName?.let { spNameList.add(it) }
            Log.i("SPDelegate", "key = ${SP.formIndex_}$index , existName = $existName")
        }

        Log.i("SPDelegate", "spCount = $spCount spNameList = $spNameList")

        if (spNameList.all { it != name }) {
            SP.getSaveAllSp().edit().putInt(SP.spFormCount, spCount + 1).commit()
            SP.getSaveAllSp().edit().putString("${SP.formIndex_}${spCount}", name).commit()
            Log.i("SPDelegate", "更新 spFormCount = ${spCount + 1}")
            Log.i("SPDelegate", "保存 ${SP.formIndex_}${spCount} = $name")
        }
        Log.i("SPDelegate", "--------------------------------")
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return SP.get(name).get(key, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        SP.get(name).put(key, value)
    }
}


private val KEY_IS_EMPTY_EXCEPTION = "key is empty"

/**
 * 如果[key]存在，则返回对应类型的数据，如果转换数据类型失败，则返回[default]。
 * 如果[key]不存在，则返回[default]；
 */
@Suppress("UNCHECKED_CAST")
@Throws(IllegalArgumentException::class, NullPointerException::class)
fun <T> SharedPreferences.get(key: String, default: T): T {
    require(key.isNotEmpty()) { KEY_IS_EMPTY_EXCEPTION }
    all?.forEach {
        if (it.key == key) {
            return try {
                it.value as T
            } catch (i: Exception) {
                default
            }
        }
    }
    return default
}

/**
 * 支持 String ,Boolean , Int ,Long , Float
 * 不支持自定义数据类型
 *
 * 如果[key]已经存在，则会覆盖数据
 * @param value     如果为 null，则会移除对应的数据。
 */
@Throws(IllegalArgumentException::class, NullPointerException::class)
fun SharedPreferences.put(key: String, value: Any?) {
    require(key.isNotEmpty()) { KEY_IS_EMPTY_EXCEPTION }
    if (value == null) {
        remove(key)
    } else {
        try {
            with(edit()) {
                when (value) {
                    is String -> putString(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Float -> putFloat(key, value)
                    else -> null
                }?.apply()
            }
        } catch (i: Exception) {
            i.printStackTrace()
        }
    }
}

/**
 * 移除某个key对应的那一条数据
 * @param key
 */
@Throws(IllegalArgumentException::class)
fun SharedPreferences.remove(key: String) {
    require(key.isNotEmpty()) { KEY_IS_EMPTY_EXCEPTION }
    try {
        edit().remove(key).apply()
    } catch (i: Exception) {
        i.printStackTrace()
    }
}



