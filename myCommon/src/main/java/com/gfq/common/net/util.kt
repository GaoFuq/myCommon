package com.gfq.common.net

import android.webkit.WebSettings
import com.gfq.common.system.ActivityManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation

/**
 * 获取方法上的注解
 */
fun <T : Annotation> Request.getMethodAnnotation(annotationClass: Class<T>): T? {
    return tag(Invocation::class.java)?.method()?.getAnnotation(annotationClass)
}

class AddUserAgentAndAuthorization(private val Authorization: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val r = chain.request().newBuilder()
            .removeHeader("User-Agent")
            .removeHeader("Authorization")
            .addHeader("User-Agent", getUserAgent())
            .addHeader("Authorization", Authorization)
            .build()
        return chain.proceed(r)
    }
}

fun getUserAgent(): String {
    val userAgent = WebSettings.getDefaultUserAgent(ActivityManager.application)
    val sb = StringBuffer()
    var i = 0
    val length = userAgent.length
    while (i < length) {
        val c = userAgent[i]
        if (c <= '\u001f' || c >= '\u007f') {
            sb.append(String.format("\\u%04x", c.code))
        } else {
            sb.append(c)
        }
        i++
    }
    return sb.toString()
}