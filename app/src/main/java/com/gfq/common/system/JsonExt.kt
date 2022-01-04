package com.gfq.common.system

/**
 *  2021/12/30 9:38
 * @auth gaofuq
 * @description
 */
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val gson by lazy {
    Gson()
}


inline fun <reified T> String.toBean(): T? {
    return gson.fromJson<T>(this, object : TypeToken<T>() {}.type)
}