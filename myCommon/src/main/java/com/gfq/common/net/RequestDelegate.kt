package com.gfq.common.net

import android.net.ParseException
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.gfq.common.R
import com.gfq.common.net.interfacee.IRequestStateShower
import com.gfq.common.net.interfacee.IStateView
import com.gfq.common.system.ActivityManager
import com.gfq.common.utils.getString
import com.gfq.common.utils.mainThread
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
class RequestDelegate @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    /**
     * 请求状态 loading
     */
    var stateShower: IRequestStateShower? = null,
    /**
     * 请求结果界面状态显示
     */
    var stateView: IStateView? = null,
) {

    private val  TAG = "RequestDelegate"
    private var scope = lifecycleOwner?.lifecycleScope ?: GlobalScope


    /**
     * 发起请求 到 请求异常，stateShower 要显示的时间，默认 1500
     */
    var errorShowTime: Int =
        ActivityManager.application.resources.getInteger(R.integer.RequestDelegate_errorShowTime)

    /**
     * 发起请求 到 请求成功 ，stateShower 要显示的时间，默认 0
     */
    var successShowTime: Int =
        ActivityManager.application.resources.getInteger(R.integer.RequestDelegate_successShowTime)

    /**
     * 发起请求 到 请求成功 ，stateShower 要显示的时间，默认 1500
     */
    var failedShowTime: Int =
        ActivityManager.application.resources.getInteger(R.integer.RequestDelegate_failedShowTime)


    /**
     * 无参的构造方法，会使用 GlobalScope 发起请求。
     * 有必要的情况下，需要手动取消 job 。
     */
    var job: Job? = null

    /**
     * 在 DialogFragment 中使用这个方法请求接口。
     * 因为 DialogFragment 在复用(dismiss后再show)时，
     * RequestDelegate 没有重新创建，由构造方法传入的 scope 会失活，既是 scope.isActive == false。
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
        request(
            api,
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
            handleResponseBySelf
        )
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

        if (!scope.isActive) {
            Log.e("", "request: scope is not active")
            return
        }

        clickView?.isEnabled = false

        if (isShowDialogLoading) {
            stateShower?.showLoading(loadingText)
        }

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
                    scope.launch(Dispatchers.Main) {
                        clickView?.isEnabled = true
                        val apiException = handleException(e)
                        error?.invoke(apiException)
                        showStateViewIfNeed<T, Resp>(e, null)
                        stateShower?.let {
                            if (isShowDialogError) {
                                it.showError(apiException.message)
                                delay(errorShowTime.toLong())
                                it.dismissRequestStateShower()
                            }
                        }
                    }
                } //数据请求返回处理  emit(block()) 返回的数据
                .collect { resp ->
                    //当 scope 为 GlobalScope 时，需要手动切换线程
                    scope.launch(Dispatchers.Main) {
                        clickView?.isEnabled = true
                        handleResponse(resp, success, special, failed, handleResponseBySelf)
                        showStateViewIfNeed<T, Resp>(null, resp)
                        stateShower?.let {
                            if (resp?.isSuccess() == true) {
                                if (isShowDialogCompleteSuccess) {//回调请求完成-成功，默认不显示
                                    it.showComplete(resp)
                                    delay(successShowTime.toLong())
                                }
                            } else {
                                if (isShowDialogCompleteFailed) {//回调请求完成-失败，默认显示错误文本
                                    it.showCompleteFailed(resp)
                                    delay(failedShowTime.toLong())
                                }
                            }
                            it.dismissRequestStateShower()
                        }
                    }
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
     * 单独接口的 special 在全局 handleSpecial 之后执行
     * handleResponseBySelf 最后执行
     */
    fun <T, Resp : AbsResponse<T>> handleResponse(
        response: Resp?,
        success: ((data: T?) -> Unit)?,
        special: ((code: Int?, data: T?, message: String?) -> Unit)?,
        failed: ((code: Int?, message: String?) -> Unit)?,
        handleResponseBySelf: ((Resp?) -> Unit)? = null,//更特殊的情况，自己处理
    ) {
        when {
            response?.isSpecial() == true -> {
                response.handleSpecial(
                    response.responseCode(),
                    response.responseData(),
                    response.responseMessage()
                )
                special?.invoke(
                    response.responseCode(),
                    response.responseData(),
                    response.responseMessage()
                )
            }
            response?.isSuccess() == true -> {
                success?.invoke(response.responseData())
            }
            else -> {
                failed?.invoke(response?.responseCode(), response?.responseMessage())
            }
        }
        handleResponseBySelf?.invoke(response)
    }


    private fun handleException(e: Throwable?): ApiException {

        if (e == null) return ApiException(message = getString(R.string.requestDelegateERROR_str))
        Log.e(TAG, "handleException " + e.message.toString())
        var code: Int? = null
        var message = e.message

        when (e) {
            is ConnectException -> {
                message = getString(R.string.requestDelegateIO_ERROR_str)
            }
            is HttpException -> {
                code = e.code()
                when (e.code()) {
                    HttpURLConnection.HTTP_BAD_REQUEST -> {
                        message = getString(R.string.requestDelegateHTTP_BAD_REQUEST_MSG)
                    }
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        message = getString(R.string.requestDelegateHTTP_UNAUTHORIZED_MSG)
                    }
                    HttpURLConnection.HTTP_FORBIDDEN -> {
                        message = getString(R.string.requestDelegateHTTP_FORBIDDEN_MSG)
                    }
                    HttpURLConnection.HTTP_NOT_FOUND -> {
                        message = getString(R.string.requestDelegateHTTP_NOT_FOUND_MSG)
                    }
                    HttpURLConnection.HTTP_BAD_METHOD -> {
                        message = getString(R.string.requestDelegateHTTP_BAD_METHOD_MSG)
                    }
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                        message = getString(R.string.requestDelegateHTTP_INTERNAL_ERROR_MSG)
                    }
                    HttpURLConnection.HTTP_BAD_GATEWAY -> {
                        message = getString(R.string.requestDelegateHTTP_BAD_GATEWAY_MSG)
                    }
                    HttpURLConnection.HTTP_UNAVAILABLE -> {
                        message = getString(R.string.requestDelegateHTTP_UNAVAILABLE_MSG)
                    }
                    HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> {
                        message = getString(R.string.requestDelegateHTTP_GATEWAY_TIMEOUT_MSG)
                    }
                    HttpURLConnection.HTTP_VERSION -> {
                        message = getString(R.string.requestDelegateHTTP_VERSION_MSG)
                    }
                    in 400..500 -> {
                        message = getString(R.string.requestDelegateHTTP_BAD_REQUEST_MSG)
                    }
                    in 500..600 -> {
                        message = getString(R.string.requestDelegateHTTP_INTERNAL_ERROR_MSG)
                    }
                }
            }

            is UnknownHostException -> {
                message = getString(R.string.requestDelegateIO_ERROR_UNKNOWN_HOST_str)
            }
            is SocketTimeoutException -> {
                message = getString(R.string.requestDelegateIO_ERROR_TIMEOUT_str)
            }


            is JSONException,
            is ParseException,
            is JsonParseException,
            -> {
                message = getString(R.string.requestDelegateDATA_PARSE_ERROR_str)
            }
            else -> {
                message = getString(R.string.requestDelegateERROR_str)
            }

        }


        return ApiException(code, message)
    }


    fun cancel() {
        job?.cancel()
    }

}