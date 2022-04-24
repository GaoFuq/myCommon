package com.gfq.common.net

data class ApiException(
    val code: Int? = null,
    val message: String? = "出错了"
)