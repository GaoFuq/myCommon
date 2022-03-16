package com.gfq.common.net


import android.net.ParseException
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 * 2021/4/19 11:09
 * @auth gaofuq
 * @description
 *
 * 一个 [BaseRequestViewModel] 持有一个空的 [IRequestStateDialog] 。
 * 连续发起多个请求，[IRequestStateDialog] 显示隐藏只会走一次，无法反映出结果是成功还是异常
 *
 * 不需要 loading 弹窗，可以直接继承这个。
 * 需要 loading 弹窗，可以继承 [BaseRequestViewModelWithStateDialog]
 *
 */
open class BaseRequestViewModel() : ViewModel() {

    /**
     * 请求状态弹窗 @see [RequestState]
     */
    var requestStateDialog: IRequestStateDialog? = null

    /**
     * 结果成功返回时，dialog隐藏的延迟时间（显示的时间），用于展示结果信息
     */
    var completeDismissDelay: Long = 0

    /**
     * 请求或返回结果是异常时，dialog隐藏的延迟时间（显示的时间），用于展示异常信息
     */
    var errorDismissDelay: Long = 0

    /**
     * loading 显示的最少时间，用于展示loading
     */
    var minimumLoadingTime: Long = 0

    private var loadingTime: Long = 0

    companion object {
        const val UNAUTHORIZED = 401
        const val FORBIDDEN = 403
        const val NOT_FOUND = 404
        const val REQUEST_TIMEOUT = 408
        const val INTERNAL_SERVER_ERROR = 500
        const val BAD_GATEWAY = 502
        const val SERVICE_UNAVAILABLE = 503
        const val GATEWAY_TIMEOUT = 504


        const val UNKNOWN_ERROR = 666_111_0
        const val NOT_AUTH = 666_111_1
        const val IO_ERROR = 666_111_2
        const val DATA_PARSE_ERROR = 666_111_3


        const val NOT_FOUND_str = "服务器不可访问"
        const val REQUEST_TIMEOUT_str = "请求超时"
        const val INTERNAL_SERVER_ERROR_str = "服务器错误"
        const val BAD_GATEWAY_str = "网关错误"
        const val SERVICE_UNAVAILABLE_str = "访问了不可获取的资源"
        const val GATEWAY_TIMEOUT_str = "网关超时"


        const val UNKNOWN_ERROR_str = "未知错误"
        const val NOT_AUTH_str = "未授权"
        const val IO_ERROR_str = "网络连接失败"
        const val IO_ERROR_TIMEOUT_str = "网络连接超时"
        const val IO_ERROR_UNKNOWN_HOST_str = "未知主机错误"
        const val DATA_PARSE_ERROR_str = "数据解析错误"

    }

    private var requestCount = 0


    override fun onCleared() {
    }

    /**
     * @param clickView 点击触发请求的view。传入该view，控制enable属性。
     */
    open fun <T, Resp : AbsResponse<T>> request(
        api: suspend CoroutineScope.() -> Resp?,
        clickView: View? = null,
        success: ((data: T?) -> Unit)? = null,
        failed: ((code: Int?, message: String?) -> Unit)? = null,
        error: ((ApiException) -> Unit)? = null,
        special: ((code: Int?, message: String?, data: T?) -> Unit)? = null,//特殊情况
    ) {
        clickView?.isEnabled = false
        requestCount++
        if (requestCount == 1) {
            loadingTime = System.currentTimeMillis()
            requestStateDialog?.showLoading()
        }

        viewModelScope.launch {
            flow { emit(api()) }    //网络请求
                .flowOn(Dispatchers.IO)
                .catch { e: Throwable? ->//异常捕获处理
                    clickView?.isEnabled = true
                    val apiException = handleException(e)
                    requestCount--
                    updateRequestStateDialogIfNeed<T, Resp>(RequestState.error,
                        apiException = apiException)
                    error?.invoke(apiException)
                    requestStateDialog?.let { delay(errorDismissDelay) }
                    updateRequestStateDialogIfNeed<T, Resp>(RequestState.dismiss)
                } //数据请求返回处理  emit(block()) 返回的数据
                .collect {
                    clickView?.isEnabled = true
                    requestCount--
                    if(it?.isSuccess()==true||it?.isSpecial()==true){
                        //回调请求完成-成功，默认不显示文本
                        updateRequestStateDialogIfNeed<T, Resp>(RequestState.complete, it)
                    }else{
                        //回调请求完成-失败，默认显示错误文本
                        updateRequestStateDialogIfNeed<T, Resp>(RequestState.completeFailed, it)
                    }

                    handleResponse(it, success, special, failed)
                    requestStateDialog?.let { delay(completeDismissDelay) }
                    updateRequestStateDialogIfNeed<T, Resp>(RequestState.dismiss)
                }
        }
    }

    /**
     * 默认只处理成功的返回
     */
    open fun <T, Resp : AbsResponse<T>> handleResponse(
        response: Resp?,
        success: ((data: T?) -> Unit)?,
        special: ((code: Int?, message: String?, data: T?) -> Unit)?,
        failed: ((code: Int?, message: String?) -> Unit)?,
    ) {
        when {
            response?.isSpecial() == true -> {
                special?.invoke(
                    response.responseCode(),
                    response.responseMessage(),
                    response.responseData())
            }
            response?.isSuccess() == true -> {
                success?.invoke(response.responseData())
            }
            else -> {
                failed?.invoke(response?.responseCode(), response?.responseMessage())
            }
        }
    }


    private suspend fun <T, Resp : AbsResponse<T>> updateRequestStateDialogIfNeed(
        dialogState: RequestState,
        response: Resp? = null,
        apiException: ApiException = ApiException(),
    ) {
        if (requestStateDialog == null) return
        when (dialogState) {
            RequestState.loading -> {
                if (requestCount == 1) {
                    requestStateDialog?.showLoading()
                }

            }
            RequestState.complete -> {
                if (requestCount == 0) {
                    val remainingTime =
                        minimumLoadingTime - (System.currentTimeMillis() - loadingTime)
                    if (remainingTime > 0) {
                        delay(remainingTime)
                    }
                    requestStateDialog?.showComplete(response)
                }
            }
            RequestState.completeFailed -> {
                if (requestCount == 0) {
                    val remainingTime =
                        minimumLoadingTime - (System.currentTimeMillis() - loadingTime)
                    if (remainingTime > 0) {
                        delay(remainingTime)
                    }
                    requestStateDialog?.showCompleteFailed(response)
                }
            }
            RequestState.error -> {
                if (requestCount == 0) {
                    val remainingTime =
                        minimumLoadingTime - (System.currentTimeMillis() - loadingTime)
                    if (remainingTime > 0) {
                        delay(remainingTime)
                    }
                    requestStateDialog?.showError(apiException)
                }
            }
            RequestState.dismiss -> {
                if (requestCount == 0) {
                    requestStateDialog?.dismissStateDialog()
                }
            }
        }
    }


    private fun handleException(e: Throwable?): ApiException {

        if (e == null) return ApiException(customCode = UNKNOWN_ERROR, message = UNKNOWN_ERROR_str)
        Log.e("BaseRequestViewModel",e.message.toString())
        var code: Int? = null
        var message = UNKNOWN_ERROR_str
        var customCode = UNKNOWN_ERROR

        when (e) {
            is HttpException -> {
                code = e.code()
                customCode = e.code()
                when (e.code()) {
                    UNAUTHORIZED, FORBIDDEN -> {
                        customCode = NOT_AUTH
                        message = NOT_AUTH_str
                    }
                    NOT_FOUND -> {
                        message = NOT_FOUND_str
                    }
                    REQUEST_TIMEOUT -> {
                        message = REQUEST_TIMEOUT_str
                    }
                    INTERNAL_SERVER_ERROR -> {
                        message = INTERNAL_SERVER_ERROR_str
                    }
                    BAD_GATEWAY -> {
                        message = BAD_GATEWAY_str
                    }
                    SERVICE_UNAVAILABLE -> {
                        message = SERVICE_UNAVAILABLE_str
                    }
                    GATEWAY_TIMEOUT -> {
                        message = GATEWAY_TIMEOUT_str
                    }
                }
            }
            is UnknownHostException -> {
                message = IO_ERROR_UNKNOWN_HOST_str
                customCode = IO_ERROR
            }
            is SocketTimeoutException -> {
                message = IO_ERROR_TIMEOUT_str
                customCode = IO_ERROR
            }


            is JSONException,
            is ParseException,
            -> {
                message = DATA_PARSE_ERROR_str
                customCode = DATA_PARSE_ERROR
            }
            else -> {
                customCode = UNKNOWN_ERROR
                message = UNKNOWN_ERROR_str
            }

        }

        return ApiException(code, customCode, message)
    }

}






