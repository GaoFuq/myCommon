package com.gfq.common.net

/**
 * 请求状态弹窗需要实现该接口
 * @see [DefaultRequestStateDialog]
 */
interface IRequestStateDialog {

    fun showLoading(message: String? = "加载中...")

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

    fun dismissStateDialog()
}