package com.gfq.common.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.atomic.AtomicInteger

/**
 * 2022/3/17 14:04
 * @auth gaofuq
 * @description
 */

/**
 * okClient.addInterceptor(ApiDescInterceptor(XxxApiService::class.java))
 *
 *          @Headers("${ApiDescInterceptor.MethodName}:textApi")
 *          @ApiDesc("测试接口")
 *          fun textApi(){}
 */
class ApiDescInterceptor(private val apiServiceClass:Class<*>) : Interceptor {

    companion object{
        const val MethodName = "MethodName"
        private const val TAG = "【HTTP】"
        private const val dvdLine =
            "----------------------------------------------------------------------------------------------------------------------"
    }

    // <methodName,apiDesc>
    private var apiDescMap: MutableMap<String, String>? = null
    private val atomicInteger = AtomicInteger(0)

    init {
        initApiDescMap()
    }

    //@Headers("${ApiDescInterceptor.MethodName}:methodName")
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val headers = request.headers
        val currentInvokeMethodName = headers.names().firstOrNull {it == MethodName }
        if(currentInvokeMethodName!=null) {
            val serialNumber = atomicInteger.incrementAndGet()
            val currentInvokeMethodDesc = apiDescMap?.get(currentInvokeMethodName).toString()
            val serialTag = "${TAG}$serialNumber"
            Log.e(serialTag, dvdLine)
            Log.e(serialTag, "【 $currentInvokeMethodName : $currentInvokeMethodDesc 】")
            headers.newBuilder().removeAll(MethodName)
        }
        return chain.proceed(request)
    }



    private fun initApiDescMap() {
        if (apiDescMap == null) {
            apiDescMap = mutableMapOf()
            apiServiceClass.declaredMethods.forEach { method ->
                val apiDesc = method.getAnnotation(ApiDesc::class.java)
                apiDesc?.let {
                    apiDescMap!![method.name] = apiDesc.apiDescription
                }
            }
        }
    }

}