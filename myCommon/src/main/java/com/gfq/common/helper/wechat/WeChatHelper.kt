package com.gfq.common.helper.wechat

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import com.gfq.common.helper.CompressImageHelper
import com.gfq.common.helper.wechat.WeChatUtil
import com.gfq.common.net.RequestDelegate
import com.gfq.common.system.ActivityManager
import com.gfq.common.utils.downloadFile
import com.gfq.common.utils.getExtension
import com.gfq.common.utils.ioThread
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelbiz.WXOpenCustomerServiceChat
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.io.File
import java.io.FileInputStream


/**
 * @see WXEntryActivity
 */
internal object WeChatHelper {

    internal enum class State(val text: String) {
        stateLogin("mdwl_live_android_scope_login"),
        stateBind("mdwl_live_android_scope_bind"),
    }

    val wxApi = WXAPIFactory.createWXAPI(ActivityManager.application, "weChatAppID", true)

    fun registerApp(app: Application) {
        app.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                wxApi.registerApp("weChatAppID")
            }
        }, IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP))
    }
    fun wxReadOpenApplet(name:String,path:String){
        val req = WXLaunchMiniProgram.Req()
        req.userName = name
        req.path = path
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE
        wxApi.sendReq(req)
    }

    /**
     * 微信支付
     */
    fun wxPay(date: MoneyPlaceOrderResp, callback: WechatPayCallback){
        WechatPay.registerWechatPayCallback(callback)
        val payReq = PayReq()
        payReq.appId = "weChatAppID"
        payReq.partnerId = date.mchId
        payReq.prepayId = date.prepayId
        payReq.packageValue = "Sign=WXPay"
        payReq.nonceStr = date.nonceStr
        payReq.timeStamp = date.timeStamp
        payReq.sign = date.paySign
        payReq.extData = date.payNo
        wxApi.sendReq(payReq)
    }

    /**
     * @see WXEntryActivity.onResp
     */
    fun authorize(state: State) {
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = state.text
        wxApi.sendReq(req)
    }

    fun onAuthorizeFailed() {
        onAuthFailed?.invoke()
        onAuthFailed = null
    }

    var onBindWeChatSuccess: (() -> Unit)? = null
    var onAuthFailed: (() -> Unit)? = null


    fun bindWeChat(code: String) {
        onBindWeChatSuccess?.invoke()
        onBindWeChatSuccess = null
    }


    fun unbindWeChat(requestDelegate: RequestDelegate, success: () -> Unit) {
    }


    @JvmStatic
    private fun downloadThumb(imgPath: String, success: (ByteArray) -> Unit, failed: () -> Unit) {
        ioThread {
            try {
                val ext = imgPath.getExtension()
                downloadFile(url = imgPath,
                    saveFileName = "temp_share_${System.currentTimeMillis()}.$ext",
                    isInsertMediaStore = false,
                    success = {
                        val compressedPath =CompressImageHelper.compress(path = it.path,
                            fileSaveName = "share_${System.currentTimeMillis()}.$ext")
                        val inps = WeChatUtil.inputStreamToByte(FileInputStream(File(compressedPath)))
                        success(inps)
//                        dismissLoading()
                        it.delete()
                    },
                    failed = failed)
            } catch (e: Exception) {
                e.printStackTrace()
//                failed()
            }
        }
    }


    /**
     * 分享到微信网页
     * thumb 不大于 32 kb
     */
    @JvmStatic
    fun share2WeChatWebPage(pagePath: String, title: String, description: String, imgPath: String) {
        ioThread {
//            showLoading()
            val bytesTemp = WeChatUtil.getHtmlByteArray(imgPath)
            val bytesCompressed = WeChatUtil.bmpToByteArrayForCompress(BitmapFactory.decodeByteArray(bytesTemp,0,bytesTemp.size),true)
//            dismissLoading()
            share2WeChatWebPage(pagePath, title, description, bytesCompressed)
        }
    }


    @JvmStatic
    private fun share2WeChatMini(title: String, pagePath: String, thumbData: ByteArray) {
        val miniProgramObj = WXMiniProgramObject()
        miniProgramObj.webpageUrl = "http://www.qq.com" // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = getMiniType()// 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = "weChatMiniOriginID" // 小程序原始id
        miniProgramObj.path = pagePath //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
        val msg = WXMediaMessage(miniProgramObj)
        msg.title = title // 小程序消息title
        msg.description = "小程序消息Desc" // 小程序消息desc

        // 小程序消息封面图片，小于128k
        msg.thumbData = thumbData
//

//        WeChatUtil.bmpToByteArray(
//                AppCompatResources.getDrawable(ActivityManager.application,
//                    R.mipmap.icon_logo_216)
//                    ?.toBitmap(), true)

        val req = SendMessageToWX.Req()
        req.transaction = "mmjMiniProgram" + System.currentTimeMillis()
        req.message = msg
        req.scene = SendMessageToWX.Req.WXSceneSession // 目前只支持会话
        wxApi.sendReq(req)
    }

    @JvmStatic
    private fun share2WeChatWebPage(
        pagePath: String,
        title: String,
        description: String,
        thumbData: ByteArray,
    ) {
        //初始化一个WXWebpageObject，填写url
        val webpage = WXWebpageObject()
        webpage.webpageUrl = pagePath
        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage(webpage)
        msg.title = title
        msg.description = description
        msg.thumbData = thumbData
        //构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = "mmjWebPage" + System.currentTimeMillis()
        req.message = msg
        req.scene = SendMessageToWX.Req.WXSceneSession
        //调用 api 接口，发送数据到微信
        wxApi.sendReq(req)
    }


    @JvmStatic
    fun jumpCustomerService() {
        // 判断当前版本是否支持拉起客服会话
        if (wxApi.wxAppSupportAPI >= Build.SUPPORT_OPEN_CUSTOMER_SERVICE_CHAT) {
            val req = WXOpenCustomerServiceChat.Req()
            req.corpId = ""                              // 企业ID
            req.url = ""    // 客服URL
            wxApi.sendReq(req);
        }
    }




    @JvmStatic
    private fun jumpWechatMini(path: String) {
        val req = WXLaunchMiniProgram.Req()
        req.userName = "weChatMiniOriginID" // 填小程序原始id
        req.path = path //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = getMiniType()// 可选打开 开发版，体验版和正式版
        wxApi.sendReq(req)
    }

    @JvmStatic
    private fun getMiniType(): Int {
        return if ("BuildConfig.BUILD_TYPE"== "release") {
            0
        } else {
            2
        }
    }
}