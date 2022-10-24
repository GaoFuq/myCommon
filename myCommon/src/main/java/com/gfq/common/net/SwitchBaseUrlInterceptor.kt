package com.gfq.common.net

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 切换 baseUrl 的拦截器
 */
class SwitchBaseUrlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val annotation = request.getMethodAnnotation(OverrideBaseUrl::class.java)
        if (annotation != null) {
            val newRequest = request.newBuilder().url(
                request.url.newBuilder().host(annotation.url).build()
            ).build()
            return chain.proceed(newRequest)
        }
        return chain.proceed(request)
    }
}

