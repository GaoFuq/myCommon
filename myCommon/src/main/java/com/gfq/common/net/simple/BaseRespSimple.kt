package com.gfq.common.net.simple

import com.gfq.common.net.BaseResp

internal open class BaseRespSimple<T> : BaseResp<T>() {
    val code: Int? = null
    val msg: String? = null
    val `data`: T? = null
}