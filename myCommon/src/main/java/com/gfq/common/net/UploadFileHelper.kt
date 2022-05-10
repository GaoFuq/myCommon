package com.gfq.common.net

import com.gfq.common.utils.getFileName
import com.gfq.common.utils.getMimeType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


/**
 *  2022/4/22 10:01
 * @auth gaofuq
 * @description
 */

object UploadFileHelper {

    @JvmStatic
    fun buildMultipartList(
        fileKeyName: String,
        params: Map<String, String>?=null,
        fileValuePaths: List<String>,
        onProgress: ((progress: Long) -> Unit)? = null,
    ): List<MultipartBody.Part> {
        val parts: MutableList<MultipartBody.Part> = ArrayList()
        //拼接参数键值对
        params?.forEach { (key, value) ->
            parts.add(MultipartBody.Part.createFormData(key, value))
        }
        //拼接文件
        for (index in fileValuePaths.indices) {
            val path = fileValuePaths[index]
            val requestBody = File(path).asRequestBody(path.getMimeType()?.toMediaType())
            val part = if (onProgress == null) {
                MultipartBody.Part.createFormData(fileKeyName, path, requestBody)
            } else {
                MultipartBody.Part.createFormData(fileKeyName,
                    path,
                    RequestBodyProgress(requestBody, onProgress))
            }
            parts.add(part)
        }
        return parts
    }

    @JvmStatic
    fun buildMultipartBody(
        fileKeyName: String,
        params: Map<String, String>?=null,
        fileValuePaths: List<String>,
        onProgress: ((progress: Long) -> Unit)? = null,
    ): MultipartBody {
        val multipartBodybuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        //拼接参数键值对
        params?.forEach { (key, value) ->
            multipartBodybuilder.addFormDataPart(key,value)
        }
        //拼接文件
        for (index in fileValuePaths.indices) {
            val path = fileValuePaths[index]
            val requestBody = File(path).asRequestBody(path.getMimeType()?.toMediaType())
            if (onProgress == null) {
                multipartBodybuilder.addFormDataPart(fileKeyName,path.getFileName(),requestBody)
            } else {
                multipartBodybuilder.addFormDataPart(fileKeyName,path.getFileName(),RequestBodyProgress(requestBody, onProgress))
            }
        }

        return multipartBodybuilder.build()
    }

    @JvmStatic
    fun buildRequestBodyMap(
        fileKeyName: String,
        params: Map<String, String>?=null,
        fileValuePaths: List<String>,
        onProgress: ((progress: Long) -> Unit)? = null,
    ): Map<String, RequestBody> {
        val bodyMap: MutableMap<String, RequestBody> = HashMap()
        //拼接参数键值对
        params?.forEach { (key, value) ->
            bodyMap[key] = value.toRequestBody("text/plain; charset=utf-8".toMediaTypeOrNull())
        }
        //拼接文件
        for (index in fileValuePaths.indices) {
            val path = fileValuePaths[index]
            val requestBody = File(path).asRequestBody(path.getMimeType()?.toMediaType())
            val key = "form-data; name=$fileKeyName; filename=${path.getFileName()}"
            if (onProgress == null) {
                bodyMap[key] = requestBody
            } else {
                bodyMap[key] = RequestBodyProgress(requestBody, onProgress)
            }
        }
        return bodyMap
    }
}
