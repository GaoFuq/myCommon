package com.gfq.common.net

/**
 * 使用传入的 url 参数覆盖掉默认配置的 url
 */
@Target(AnnotationTarget.FUNCTION)
annotation class OverrideBaseUrl(val url:String)