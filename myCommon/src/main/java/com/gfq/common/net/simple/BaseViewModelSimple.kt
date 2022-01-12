package com.gfq.common.net.simple

import com.gfq.common.net.AbsRequestWithStateDialogViewModel
import com.gfq.common.net.BaseResp

internal open class BaseViewModelSimple : AbsRequestWithStateDialogViewModel() {

    @Suppress("UNCHECKED_CAST")
    override fun <T, Resp : BaseResp<T>> handleResponse(
        response: Resp?,
        success: ((data: T?) -> Unit)?,
        special: ((data: T?) -> Unit)?,
        failed: ((code: Int, message: String?) -> Unit)?,
    ) {
       (response as? BaseRespSimple<T>)?.let {
            if (it.code == 0) {
                success?.invoke(it.data)
            }
        }
    }


}
