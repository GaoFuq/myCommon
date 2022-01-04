package com.gfq.common.base
/*


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

*/
/**
 *  2021/4/19 11:09
 * @auth gaofuq
 * @description
 *//*

open class BaseVM : ViewModel() {


    private var isShowLayer = true
    private var noNeedLoadingTip = false

    val loadingDialog by lazy { LoadingDialog() }
    val loadingLayer by lazy { LoadingLayer() }


    fun <T> request(
        apiRequest: suspend CoroutineScope.() -> BaseResp<T>,
        success: (T?) -> Unit = {},
        failed: (Int) -> Unit = {},
        error: (Throwable?) -> Unit = {},
        special: (T?) -> Unit = {},//特殊情况
        isShowLayer: Boolean = true,//默认不显示dialogLoading，显示layerLoading
        noNeedLoadingTip: Boolean = false,//都不需要显示
    ) {
        this.isShowLayer = isShowLayer
        this.noNeedLoadingTip = noNeedLoadingTip

        if (!isNetworkConnected()) {
            toast("请检查网络")
        }

        showLoading()

        viewModelScope.launch {
            flow { emit(apiRequest()) }    //网络请求
                .flowOn(Dispatchers.IO)  //指定网络请求的线程
                .catch { e: Throwable? ->//异常捕获处理
                    handleException(e, error)
                } //数据请求返回处理  emit(block()) 返回的数据
                .collect {
                    handleResponse(it, success, special, failed)
                }
        }
    }


    private fun <T> handleResponse(
        it: BaseResp<T>,
        success: (T?) -> Unit,
        special: (T?) -> Unit = {},
        failed: (Int) -> Unit = {},
    ) {
        when (it.code) {     //网络响应解析
            ResponseCode.SUCCESS.code -> {
                success(it.data)  //数据加载完成交由业务层处理
                if (it.msg.isNotEmpty() && it.msg != "获取成功") {
                    dismissComplete(it.msg)
                } else {
                    dismiss()
                }
            }
            ResponseCode.TOKEN_FAILED.code -> {
                toast("登录失效，请重新登录")
                RongIM.getInstance().logout()
                CacheManager.getInstance().signOut()
                val intent = Intent(App.application, LoginAct3::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("clearOtherAct", true)
                ContextCompat.startActivity(App.application, intent, null)
            }
            //有未上传战绩的赛事(只有王者1v1有这个情况)
            ResponseCode.NO_UPLOAD_GRADE.code -> {
                special(it.data)
            }
            ResponseCode.ACCOUNT_BANNED.code -> {
                //被封号提示框
                special(it.data)
                dismiss()
            }
            else -> {
                dismissError(it.msg)
                failed(it.code)
            }
        }
    }

    private fun handleException(e: Throwable?, error: (Throwable?) -> Unit) {
        if (e is HttpException) {
            if (e.code() == 500) {
                if (BuildConfig.BUILD_TYPE == "release") {
                    loadingLayer.dismissError("请稍后再试")
                } else {
                    loadingLayer.dismissError("服务器错误")
                }
            } else {
                loadingLayer.dismissError("网络异常")
            }
        } else if (e is UnknownHostException) {
            loadingLayer.dismissError("网络连接失败")
        } else if (e is ConnectTimeoutException || e is SocketTimeoutException) {
            loadingLayer.dismissError("请求超时")
        } else if (e is IOException) {
            loadingLayer.dismissError("网络异常")
        } else if (e is JsonParseException || e is JSONException) {
            loadingLayer.dismissError("数据解析错误")
        } else {
            loadingLayer.dismissError("请稍后再试")
        }
        CrashReport.postCatchedException(e)
        error(e)
        if (showDebug) {
            deBugAdapter.addAll(listOf(DebugBean(e?.message.toString(),
                DebugContentType.ERROR_CONTENT)))
        }


    }

    private fun showLoading() {
        if (!noNeedLoadingTip) {
            if (isShowLayer) {
                loadingLayer.showLoading()
            } else {
                loadingDialog.showLoading()
            }
        }
    }

    private fun dismissComplete(msg: String) {
        if (!noNeedLoadingTip) {
            if (isShowLayer) {
                loadingLayer.dismissComplete(msg)
            } else {
                loadingDialog.dismissComplete(msg)
            }
        }
    }

    private fun dismiss() {
        if (!noNeedLoadingTip) {
            if (isShowLayer) {
                loadingLayer.dismiss()
            } else {
                loadingDialog.dismiss()
            }
        }
    }

    private fun dismissError(msg: String) {
        if (!noNeedLoadingTip) {
            if (isShowLayer) {
                loadingLayer.dismissError(msg)
            } else {
                loadingDialog.dismissError(msg)
            }
        }
    }

    fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager =
            App.application
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        return result
    }
}*/
