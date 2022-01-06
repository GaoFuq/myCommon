package com.gfq.common.system

/**
 *  2021/12/30 9:38
 * @auth gaofuq
 * @description
 */
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


val gson by lazy { Gson() }

fun Any?.toJsonStr(): String = gson.toJson(this)

inline fun <reified T> String?.toBean(): T? {
    return gson.fromJson(this, object : TypeToken<T>() {}.type)
}

/**
 * Map value should be json string
 */
inline fun <reified T> Map<String, String>?.value2Bean(): T? {
    return this?.values?.firstOrNull()?.toBean<T>()
}
