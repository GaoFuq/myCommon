package com.gfq.common.utils




/**
 *  2022/1/4 13:52
 * @auth gaofuq
 * @description
 */

@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiDesc(val methodName:String,val apiDescription: String)

