package com.gfq.common.utils


import android.util.Log
import com.gfq.common.WebActivity
import com.gfq.common.system.ActivityManager
import com.gfq.common.system.openActivity
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.atomic.AtomicInteger
import kotlin.jvm.internal.Intrinsics

/**
 *  2022/1/4 13:52
 * @auth gaofuq
 * @description
 *
OkHttpClient.Builder()
.addInterceptor(HttpLogInterceptor(listOf(ClassName::class.java)))
.build()
 *
 */
class HttpLogInterceptor(private val apiServiceList: List<Class<*>>) : Interceptor {

    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val atomicInteger = AtomicInteger(0)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        Intrinsics.checkParameterIsNotNull(chain, "chain")
        val headerSb = StringBuilder()
        val requestBodySb = StringBuilder()

        val request = chain.request()
        val requestBody = request.body
        val response = chain.proceed(request)

        val requestContent = "请求 --> ${request.method}   ${request.url}"

        val headers = request.headers

        //请求头
        headerSb.append("请求头: { ")
        headers.forEach {
            headerSb.append(it.first).append(":").append(it.second).append("; ")
        }
        headerSb.append("}")

        //请求体
        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            var charset = StandardCharsets.UTF_8
            val contentType = requestBody.contentType()
            contentType?.let {
                charset = contentType.charset(charset)
                if (!isIgnoreRequestBody(it, requestBodySb)) {
                    if (isPlaintext(buffer)) {
                        val ttt = buffer.readString(charset!!)
                        requestBodySb.append("请求体：").append(ttt)
                    }
                }
            }
        }


        val responseBody = response.body
        if (responseBody != null) {
            val contentType = responseBody.contentType()
            val responseContent = responseBody.string()
            try {
                log(
                    headerSb.toString(),
                    requestContent,
                    responseContent,
                    requestBodySb.toString()
                )
            } catch (e: Exception) {
                Log.e("error", e.message ?: "")
            }
            return response.newBuilder()
                .body(responseContent.toResponseBody(contentType))
                .build()
        }
        return response
    }





    private fun isIgnoreRequestBody(it: MediaType, requestBodySb: StringBuilder): Boolean {
        var isIgnore = false
        if (it.type == "image") {
            requestBodySb.append("数据为图片，忽略请求体")
            isIgnore = true
        }
        if (it.type == "video") {
            requestBodySb.append("数据为视频，忽略请求体")
            isIgnore = true
        }
        if (it.type == "audio") {
            requestBodySb.append("数据为音频，忽略请求体")
            isIgnore = true
        }
        return isIgnore
    }

    private fun log(
        headers: String,
        requestContent: String,
        responseContent: String,
        requestBodyContent: String,
    ) {
        val serialNumber = atomicInteger.incrementAndGet()
        val serialTag = "$TAG$serialNumber"
        Log.e(serialTag, dvdLine)
        Log.e(serialTag, requestContent)
        Log.e(serialTag, headers)
        Log.e(serialTag, requestBodyContent)

        if (responseContent.startsWith("<!DOCTYPE html>")) {
            Log.e(serialTag, "response is <!DOCTYPE html>")
//            Log.e(serialTag, "返回 -->：\n$responseContent")
            ActivityManager.application.openActivity<WebActivity>("html" to responseContent)
        } else {
            val toJson = gson.toJson(JsonParser.parseString(responseContent))
            Log.e(serialTag, "返回 -->：\n$toJson")
        }
    }

    companion object {
        private const val TAG = "【HTTP】"
        private const val dvdLine =
            "----------------------------------------------------------------------------------------------------------------------"

        fun isPlaintext(buffer: Buffer): Boolean {
            return try {
                val prefix = Buffer()
                val byteCount = if (buffer.size < 64) buffer.size else 64
                buffer.copyTo(prefix, 0, byteCount)
                for (i in 0..15) {
                    if (prefix.exhausted()) {
                        break
                    }
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }
                true
            } catch (e: EOFException) {
                false // Truncated UTF-8 sequence.
            }
        }
    }
}