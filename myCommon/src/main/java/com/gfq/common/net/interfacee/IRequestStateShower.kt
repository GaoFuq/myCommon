package com.gfq.common.net.interfacee

import com.gfq.common.net.AbsResponse

/**
 * 展示接口请求状态  实现该接口
 */
interface IRequestStateShower {

    fun showLoading(message: String?)

    /**
     * 请求返回结果时回调
     */
    fun <T, Resp : AbsResponse<T>> showComplete(response: Resp?)

    /**
     * 请求返回结果后，但结果是错误的时候回调
     */
    fun <T, Resp : AbsResponse<T>> showCompleteFailed(response: Resp?)

    /**
     * 请求或返回数据失败，发生异常时回调
     */
    fun showError(error: String?)

    fun dismissRequestStateShower()


}