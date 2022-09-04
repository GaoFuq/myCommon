package com.gfq.common.system.sp

import android.content.SharedPreferences
import android.util.Log
import com.alibaba.fastjson.JSON

/**
 *  2022/1/17 15:36
 * @auth gaofuq
 * @description
 */


internal val KEY_IS_EMPTY_EXCEPTION = "key is empty"
internal val SP_TAG = "【SPDelegate】"


/**
 * 如果[key]存在，则返回对应类型的数据，如果转换数据类型失败，则返回 null。
 * 如果[key]不存在，则返回 null。
 */
@Suppress("UNCHECKED_CAST")
@Throws(IllegalArgumentException::class, NullPointerException::class, Exception::class)
fun <T> SharedPreferences.getOrNull(key: String): T? {
    require(key.isNotEmpty()) { KEY_IS_EMPTY_EXCEPTION }
    all?.forEach {
        if (it.key == key) {
            return try {
                if (it.value is String) {
                    val str = it.value as String
                    if (str.contains(SP.dataClassName)
                        && str.contains(SP.dataClassName)
                        && str.contains(SP.dataValue)
                    ) {
                        val spDataWrapper = JSON.parseObject(str, SPDataWrapper::class.java)
                        Log.i(SP_TAG, "get: dataClassName = ${spDataWrapper.sp_g_f_q_dataClassName}")
                        Log.i(SP_TAG, "get: dataKeyName = ${spDataWrapper.sp_g_f_q_dataKeyName}")
                        Log.i(SP_TAG, "get: dataValue = ${spDataWrapper.sp_g_f_q_dataValue}")
                        JSON.parseObject(spDataWrapper.sp_g_f_q_dataValue,
                            Class.forName(spDataWrapper.sp_g_f_q_dataClassName)) as? T
                    } else {
                        it.value as T
                    }
                } else {
                    it.value as T
                }
            } catch (i: Exception) {
                null
            }
        }
    }
    return null
}


/**
 * 如果[key]存在，则返回对应类型的数据，如果转换数据类型失败，则返回 default。
 * 如果[key]不存在，则返回 default。
 */
@Suppress("UNCHECKED_CAST")
@Throws(IllegalArgumentException::class, NullPointerException::class, Exception::class)
fun <T> SharedPreferences.getOrDefault(key: String, default: T): T {
    return getOrNull(key) ?: default
}



/**
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
                    else -> {
                        val spDataWrapper =
                            SPDataWrapper(value.javaClass.name, key, JSON.toJSONString(value))
                        val jsonStr = JSON.toJSONString(spDataWrapper)
                        Log.i(SP_TAG, "put: $jsonStr")
                        putString(key, jsonStr)
                    }
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
