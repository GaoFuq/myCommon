package com.gfq.common.utils



/**
 *  2022/1/4 13:52
 * @auth gaofuq
 * @description 配合@Headers使用
 *
 * @Headers("${ApiDescInterceptor.MethodName}:接口方法名")
 * @ApiDesc("接口描述")
 */

@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiDesc(val apiDescription: String)
