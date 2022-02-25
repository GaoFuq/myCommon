package com.gfq.common.net

/**
 * 请求状态弹窗需要实现该接口
 * @see [DefaultRequestStateDialog]
 */
interface IRequestStateDialog {

    fun showLoading(message: String? = "加载中...")

    fun <T, Resp : AbsResponse<T>> showComplete(response: Resp?)

    fun showError(error: ApiException)

    fun dismissStateDialog()
}