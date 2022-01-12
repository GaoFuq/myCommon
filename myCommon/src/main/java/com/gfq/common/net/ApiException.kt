package com.gfq.common.net

data class ApiException(
    val code: Int? = null,
    val customCode: Int? = null,
    val message: String? = null,
)