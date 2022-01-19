package com.gfq.common.system

/**
 *  2021/12/30 9:38
 * @auth gaofuq
 * @description
 */
import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


val gson by lazy { Gson() }

/**
 * user Gson
 */
fun Any?.toJsonStr(): String = gson.toJson(this)

/**
 * user fastJson
 */
fun Any?.toJSONStr(): String = JSON.toJSONString(this)

/**
 * user Gson
 */
inline fun <reified T> String?.toBean(): T? {
    return gson.fromJson(this, object : TypeToken<T>() {}.type)
}

/**
 * user fastJson
 */
inline fun <reified T> String?.toBEAN(): T? {
    return JSON.parseObject(this, T::class.java)
}

/**
 * Map value should be json string
 */
inline fun <reified T> Map<String, String>?.value2Bean(): T? {
    return this?.values?.firstOrNull()?.toBean<T>()
}

