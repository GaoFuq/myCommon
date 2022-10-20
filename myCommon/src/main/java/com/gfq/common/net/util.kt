package com.gfq.common.net

import okhttp3.Request
import retrofit2.Invocation

/**
 * 获取方法上的注解
 */
fun <T:Annotation> Request.getMethodAnnotation(annotationClass: Class<T>):T?{
    return tag(Invocation::class.java)?.method()?.getAnnotation(annotationClass)
}