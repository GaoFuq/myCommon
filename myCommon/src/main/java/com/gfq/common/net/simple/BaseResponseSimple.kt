package com.gfq.common.net.simple

import com.gfq.common.net.AbsResponse

internal open class BaseResponseSimple<T> : AbsResponse<T>() {
    val status: Int? = null
    val msg: String? = null
    val `data`: T? = null

    override fun responseCode(): Int? =status
    override fun isSuccess(): Boolean = status == 200
    override fun responseData(): T?  = data
    override fun responseMessage(): String? =msg

    override fun isSpecial(): Boolean  = status == 1 || status == 2

    override fun handleSpecial(code: Int?, data: T?, message: String?) {
        when(code){
            1->{}
            2->{}
            else->{}
        }
    }
}