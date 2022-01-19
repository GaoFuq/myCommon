package com.gfq.common.system

/**
 *  2021/12/30 9:38
 * @auth gaofuq
 * @description
 */
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.jvm.Throws


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
@Throws(IllegalStateException::class)
inline fun <reified T> String?.toBean(): T? {
    return try {
        gson.fromJson(this, object : TypeToken<T>() {}.type)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * user fastJson
 */
@Throws(JSONException::class)
inline fun <reified T> String?.toBEAN(): T? {
    return try {
        JSON.parseObject(this, T::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Map value should be json string
 */
inline fun <reified T> Map<String, String>?.value2Bean(): T? {
    return this?.values?.firstOrNull()?.toBean<T>()
}

