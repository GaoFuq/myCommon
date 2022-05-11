package com.gfq.common.net


import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope


/**
 * 2021/4/19 11:09
 * @auth gaofuq
 * @description
 *
 * 不需要 loading 弹窗，可以直接继承这个。
 * 需要 loading 弹窗，可以继承 [ViewModelRequestWithDialog]
 *
 */
open class ViewModelRequest() : ViewModel() {
    val requestDelegate = RequestDelegate(viewModelScope)

    fun <T, Resp : AbsResponse<T>> request(
        api: suspend CoroutineScope.() -> Resp?,
        clickView: View? = null,
        isShowDialogLoading: Boolean = true,
        isShowDialogCompleteSuccess: Boolean = false,
        isShowDialogCompleteFailed: Boolean = true,
        isShowDialogError: Boolean = true,
        retryCount: Int = 2,
        success: ((data: T?) -> Unit)? = null,
        failed: ((code: Int?, message: String?) -> Unit)? = null,
        error: ((ApiException) -> Unit)? = null,
        special: ((code: Int?, data: T?,message: String? ) -> Unit)? = null,//特殊情况
    ) {
        requestDelegate.request(api,
            clickView,
            isShowDialogLoading,
            isShowDialogCompleteSuccess,
            isShowDialogCompleteFailed,
            isShowDialogError,
            retryCount,
            success,
            failed,
            error,
            special)
    }



    fun showStateDialog(msg:String?){
        requestDelegate.stateDialog?.showLoading(msg)
    }
}



