package com.gfq.common.net.simple

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import com.gfq.common.net.buildMultipartList
import com.gfq.common.net.buildMultipartBody
import com.gfq.common.net.buildRequestBodyMap

/**
 *  2022/4/22 9:07
 * @auth gaofuq
 * @description
 */
internal interface ApiServiceSimple {

    //表单形式
    @POST("")
    @FormUrlEncoded
    suspend fun doPost1(@Field("param") param: Any?): BaseResponseSimple<Any?>

    //表单形式
    @POST("")
    @FormUrlEncoded
    suspend fun doPost2(@FieldMap param: MutableMap<String, Any?>): BaseResponseSimple<Any?>

    //body形式
    @POST("")
    suspend fun doPost3(@Body body: Map<String, @JvmSuppressWildcards Any>): BaseResponseSimple<Any?>


    /**
     * @see [buildMultipartList]
     */
    @POST("")
    @Multipart
    suspend fun uploadFileWithPart(
        //服务端以 MultipartFile 或 MultipartFile[] 接收
        @Part() params: List<MultipartBody.Part>,
    ): BaseResponseSimple<Any?>

    /**
     * @see [buildRequestBodyMap]
     */
    @POST("")
    @Multipart
    suspend fun uploadFileWithRequestBodyMap(
        //服务端以 Map<String,RequestBody> 接收
        @PartMap params: MutableMap<String, @JvmSuppressWildcards RequestBody>,
    ): BaseResponseSimple<Any?>

    /**
     * @see [buildMultipartBody]
     */
    @POST("")
    @Multipart
    suspend fun uploadFileWithBody(
        //服务端以 MultipartBody 接收
        @Body multipartBody: MultipartBody,
    ): BaseResponseSimple<Any?>


}