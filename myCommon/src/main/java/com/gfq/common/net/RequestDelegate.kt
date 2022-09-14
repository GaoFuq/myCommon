package com.gfq.common.net

import android.net.ParseException
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.gfq.common.R
import com.gfq.common.net.interfacee.IRequestStateShower
import com.gfq.common.net.interfacee.IStateView
import com.gfq.common.utils.getString
import com.google.gson.JsonParseException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.net.*

/**
 *  2022/4/21 17:24
 * @auth gaofuq
 * @description
 */
/**
 * 连续发起多个请求，[IRequestStateShower] 显示隐藏只会走一次。
 *
 * 无参的构造方法，会使用 GlobalScope 发起请求。
 * 有必要的情况下，需要手动取消 job 。
 */
class RequestDelegate(
    var scope: CoroutineScope = GlobalScope,

    var stateView: IStateView? = null,

    /**
     * 请求状态弹窗 @see [RequestState]
     */
    var stateShower: IRequestStateShower? = null,


    /**
     * 结果成功返回时，dialog隐藏的延迟时间（显示的时间），用于展示结果信息
     */
    var completeDismissDelay: Long = 1000,

    /**
     * 请求或返回结果是异常时，dialog隐藏的延迟时间（显示的时间），用于展示异常信息
     */
    var errorDismissDelay: Long = 1500,

    /**
     * loading 显示的最少时间，用于展示loading
     */
    var minimumLoadingTime: Long = 800,
) {

    private val TAG = "【${javaClass.simpleName}】"


    constructor(
        lifecycleOwner: LifecycleOwner,
    ) : this(stateShower = null, stateView = null) {
        this.scope = lifecycleOwner.lifecycleScope
    }

    constructor(
        lifecycleOwner: LifecycleOwner,
        stateShower: IRequestStateShower,
    ) : this(stateShower = stateShower, stateView = null) {
        this.scope = lifecycleOwner.lifecycleScope
    }

    constructor(
        lifecycleOwner: LifecycleOwner,
        stateShower: IRequestStateShower,
        stateView: IStateView,
    ) : this(stateShower = stateShower, stateView = stateView) {
        this.scope = lifecycleOwner.lifecycleScope
    }


    private var loadingTime: Long = 0 //实际loading时间
    private var requestCount = 0
    private var isShowDialogLoading: Boolean = true
    private var isShowDialogCompleteSuccess: Boolean = false
    private var isShowDialogCompleteFailed: Boolean = true
    private var isShowDialogError: Boolean = true

    /**
    * 无参的构造方法，会使用 GlobalScope 发起请求。
    * 有必要的情况下，需要手动取消 job 。
    */
    var job: Job? = null
    var loadingText = getString(R.string.request_loading_state_default_text)
    var errorText = getString(R.string.request_error_state_default_text)


    /**
     * 在 DialogFragment 中使用这个方法请求接口。
     * 因为 DialogFragment 在 dismiss 后，由构造方法传入的 scope 会失活，既是 scope.isActive == false。
     * 所以需要在请求时传入新的 LifecycleOwner。
     */
    fun <T, Resp : AbsResponse<T>> request(
        lifecycleOwner: LifecycleOwner,
        api: suspend CoroutineScope.() -> Resp?,
        clickView: View? = null,
        loadingText: String = getString(R.string.request_loading_state_default_text),
        isShowDialogLoading: Boolean = true,
        isShowDialogCompleteSuccess: Boolean = false,
        isShowDialogCompleteFailed: Boolean = true,
        isShowDialogError: Boolean = true,
        retryCount: Int = 0,
        retryDelay: Long = 0L,
        retryCondition: (Throwable) -> Boolean = { it is IOException },
        success: ((data: T?) -> Unit)? = null,
        failed: ((code: Int?, message: String?) -> Unit)? = null,
        error: ((ApiException) -> Unit)? = null,
        special: ((code: Int?, data: T?, message: String?) -> Unit)? = null,//特殊情况
        handleResponseBySelf: ((Resp?) -> Unit)? = null,//更特殊的情况，自己处理。success，failed，error，special都不会走。
    ) {
        this.scope = lifecycleOwner.lifecycleScope
        request(api,
            clickView,
            loadingText,
            isShowDialogLoading,
            isShowDialogCompleteSuccess,
            isShowDialogCompleteFailed,
            isShowDialogError,
            retryCount,
            retryDelay,
            retryCondition,
            success,
            failed,
            error,
            special,
            handleResponseBySelf)
    }

    /**
     * @param clickView 点击触发请求的view。传入该view，控制enable属性。
     * @sample special 不为空时，会代替[AbsResponse.handleSpecial]执行。
     */
    fun <T, Resp : AbsResponse<T>> request(
        api: suspend CoroutineScope.() -> Resp?,
        clickView: View? = null,
        loadingText: String = getString(R.string.request_loading_state_default_text),
        isShowDialogLoading: Boolean = true,
        isShowDialogCompleteSuccess: Boolean = false,
        isShowDialogCompleteFailed: Boolean = true,
        isShowDialogError: Boolean = true,
        retryCount: Int = 0,
        retryDelay: Long = 0L,
        retryCondition: (Throwable) -> Boolean = { it is IOException },
        success: ((data: T?) -> Unit)? = null,
        failed: ((code: Int?, message: String?) -> Unit)? = null,
        error: ((ApiException) -> Unit)? = null,
        special: ((code: Int?, data: T?, message: String?) -> Unit)? = null,//特殊情况
        handleResponseBySelf: ((Resp?) -> Unit)? = null,//更特殊的情况，自己处理。success，failed，error，special都不会走。
    ) {
        clickView?.isEnabled = false
        this.loadingText = loadingText
        this.isShowDialogLoading = isShowDialogLoading
        this.isShowDialogCompleteSuccess = isShowDialogCompleteSuccess
        this.isShowDialogCompleteFailed = isShowDialogCompleteFailed
        this.isShowDialogError = isShowDialogError

        requestCount++
        if (requestCount == 1) {
            loadingTime = System.currentTimeMillis()
            if (isShowDialogLoading) {
                stateShower?.showLoading(loadingText)
            }
        }
        if (!scope.isActive) return

        job = scope.launch {
            flow { emit(api()) }    //网络请求
                .flowOn(Dispatchers.IO)
                .retryWhen { cause, attempt ->
                    delay(retryDelay)
                    val boo = attempt < retryCount && retryCondition(cause)
                    if (boo) {
                        Log.e(TAG, cause.message.toString() + " retry - $attempt")
                    }
                    boo
                }
                .catch { e: Throwable? ->//异常捕获处理
                    clickView?.isEnabled = true
                    showStateViewIfNeed<T, Resp>(e, null)
                    val apiException = handleException(e)
                    requestCount--
                    updateRequestStateDialogIfNeed<T, Resp>(
                        RequestState.error,
                        apiException = apiException
                    )
                    error?.invoke(apiException)
                    stateShower?.let { delay(errorDismissDelay) }
                    updateRequestStateDialogIfNeed<T, Resp>(RequestState.dismiss)
                } //数据请求返回处理  emit(block()) 返回的数据
                .collect {
                    clickView?.isEnabled = true
                    showStateViewIfNeed<T, Resp>(null, it)
                    requestCount--
                    if (it?.isSuccess() == true) {
                        //回调请求完成-成功，默认不显示dialog
                        updateRequestStateDialogIfNeed<T, Resp>(RequestState.complete, it)
                    } else {
                        //回调请求完成-失败，默认显示dialog错误文本
                        updateRequestStateDialogIfNeed<T, Resp>(RequestState.completeFailed, it)
                    }
                    handleResponse(it, success, special, failed, handleResponseBySelf)
                    stateShower?.let { delay(completeDismissDelay) }
                    updateRequestStateDialogIfNeed<T, Resp>(RequestState.dismiss)
                }
        }
    }


    private fun <T, Resp : AbsResponse<T>> showStateViewIfNeed(e: Throwable?, resp: Resp?) {
        stateView?.let {
            if (e != null) {
                it.showStateErrorView()
            } else {
                if (resp == null) {
                    it.showStateEmptyView()
                    return@let
                }
                if (resp.responseData() == null) {
                    it.showStateEmptyView()
                    return@let
                }
                if (resp.responseData() is Collection<*>) {
                    val temp = resp.responseData() as Collection<*>
                    if (temp.isNullOrEmpty()) {
                        it.showStateEmptyView()
                    }
                }
            }
        }
    }

    /**
     * 默认只处理成功的返回
     */
    fun <T, Resp : AbsResponse<T>> handleResponse(
        response: Resp?,
        success: ((data: T?) -> Unit)?,
        special: ((code: Int?, data: T?, message: String?) -> Unit)?,
        failed: ((code: Int?, message: String?) -> Unit)?,
        handleResponseBySelf: ((Resp?) -> Unit)? = null,//更特殊的情况，自己处理
    ) {
        if (handleResponseBySelf != null) {
            handleResponseBySelf.invoke(response)
            return
        }
        when {
            response?.isSpecial() == true -> {
                if (special == null) {
                    response.handleSpecial(
                        response.responseCode(),
                        response.responseData(),
                        response.responseMessage()
                    )
                } else {
                    special(
                        response.responseCode(),
                        response.responseData(),
                        response.responseMessage()
                    )
                }
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
        apiException: ApiException? = null,
    ) {
        if (stateShower == null) return
        when (dialogState) {
            RequestState.loading -> {
                if (requestCount == 1) {
                    stateShower?.showLoading(loadingText)
                }
            }
            RequestState.complete -> {
                if (requestCount == 0) {
                    val remainingTime =
                        minimumLoadingTime - (System.currentTimeMillis() - loadingTime)
                    if (remainingTime > 0) {
                        delay(remainingTime)
                    }
                    if (isShowDialogCompleteSuccess) {
                        stateShower?.showComplete(response)
                    }
                }
            }
            RequestState.completeFailed -> {
                if (requestCount == 0) {
                    val remainingTime =
                        minimumLoadingTime - (System.currentTimeMillis() - loadingTime)
                    if (remainingTime > 0) {
                        delay(remainingTime)
                    }
                    if (isShowDialogCompleteFailed) {
                        stateShower?.showCompleteFailed(response)
                    }
                }
            }
            RequestState.error -> {
                if (requestCount == 0) {
                    val remainingTime =
                        minimumLoadingTime - (System.currentTimeMillis() - loadingTime)
                    if (remainingTime > 0) {
                        delay(remainingTime)
                    }
                    if (isShowDialogError) {
                        stateShower?.showError(apiException?.message ?: errorText)
                    }
                }
            }
            RequestState.dismiss -> {
                if (requestCount == 0) {
                    stateShower?.dismissRequestStateShower()
                }
            }
        }
    }


    private fun handleException(e: Throwable?): ApiException {

        if (e == null) return ApiException(message = ERROR_str)
        Log.e(TAG, "handleException " + e.message.toString())
        var code: Int? = null
        var message = e.message

        when (e) {
            is ConnectException -> {
                message = IO_ERROR_str
            }
            is HttpException -> {
                code = e.code()
                when (e.code()) {
                    HttpURLConnection.HTTP_BAD_REQUEST -> {
                        message = HTTP_BAD_REQUEST_MSG
                    }
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        message = HTTP_UNAUTHORIZED_MSG
                    }
                    HttpURLConnection.HTTP_FORBIDDEN -> {
                        message = HTTP_FORBIDDEN_MSG
                    }
                    HttpURLConnection.HTTP_NOT_FOUND -> {
                        message = HTTP_NOT_FOUND_MSG
                    }
                    HttpURLConnection.HTTP_BAD_METHOD -> {
                        message = HTTP_BAD_METHOD_MSG
                    }
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                        message = HTTP_INTERNAL_ERROR_MSG
                    }
                    HttpURLConnection.HTTP_BAD_GATEWAY -> {
                        message = HTTP_BAD_GATEWAY_MSG
                    }
                    HttpURLConnection.HTTP_UNAVAILABLE -> {
                        message = HTTP_UNAVAILABLE_MSG
                    }
                    HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> {
                        message = HTTP_GATEWAY_TIMEOUT_MSG
                    }
                    HttpURLConnection.HTTP_VERSION -> {
                        message = HTTP_VERSION_MSG
                    }
                    in 400..500 -> {
                        message = HTTP_BAD_REQUEST_MSG
                    }
                    in 500..600 -> {
                        message = HTTP_INTERNAL_ERROR_MSG
                    }
                }
            }

            is UnknownHostException -> {
                message = IO_ERROR_UNKNOWN_HOST_str
            }
            is SocketTimeoutException -> {
                message = IO_ERROR_TIMEOUT_str
            }


            is JSONException,
            is ParseException,
            is JsonParseException,
            -> {
                message = DATA_PARSE_ERROR_str
            }
            else -> {
                message = ERROR_str
            }

        }


        return ApiException(code, message)
    }


    fun cancel() {
        job?.cancel()
    }


    companion object {

        const val ERROR_str = "出错了"

        const val IO_ERROR_str = "网络连接失败"
        const val IO_ERROR_TIMEOUT_str = "网络连接超时"
        const val IO_ERROR_UNKNOWN_HOST_str = "未知主机错误"
        const val DATA_PARSE_ERROR_str = "数据解析错误"


        const val HTTP_BAD_REQUEST_MSG = "错误的请求"
        const val HTTP_UNAUTHORIZED_MSG = "没有访问权限"
        const val HTTP_FORBIDDEN_MSG = "禁止访问"

        const val HTTP_NOT_FOUND_MSG = "找不到资源\n not found"

        //对于请求所标识的资源，不允许使用请求行中所指定的方法。请确保为所请求的资源设置了正确的 MIME 类型。
        //如果问题依然存在，请与服务器的管理员联系。
        const val HTTP_BAD_METHOD_MSG = "不允许此方法"


        const val HTTP_INTERNAL_ERROR_MSG = "服务器内部错误 "
        const val HTTP_BAD_GATEWAY_MSG = "网关错误 "
        const val HTTP_UNAVAILABLE_MSG = "服务器忙\n暂时不可用"
        const val HTTP_GATEWAY_TIMEOUT_MSG = "网关超时"
        const val HTTP_VERSION_MSG = "HTTP版本不受支持"


    }
}