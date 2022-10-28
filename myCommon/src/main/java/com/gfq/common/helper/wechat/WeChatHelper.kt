/*
package com.gfq.common.helper.wechat

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.gfq.common.helper.CompressImageHelper
import com.gfq.common.net.RequestDelegate
import com.gfq.common.system.ActivityManager
import com.gfq.common.utils.downloadFile
import com.gfq.common.utils.getExtension
import com.gfq.common.utils.ioThread
import com.mmj.android.BuildConfig
import com.mmj.android.Constant
import com.mmj.android.R
import com.mmj.android.bean.CommodityDetailBean
import com.mmj.android.net.apiService
import com.mmj.android.util.*
import com.mmj.android.wxapi.WXEntryActivity
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelbiz.WXOpenCustomerServiceChat
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.io.File
import java.io.FileInputStream
import java.net.URLEncoder


*/
/**
 * @see WXEntryActivity
 *//*

object WeChatHelper {

    enum class State(val text: String) {
        stateLogin("mmj_android_scope_login"),
        stateBind("mmj_android_scope_bind"),
        stateBindQuick("mmj_android_scope_bind_quick")
    }

    @JvmStatic
    val wxApi = WXAPIFactory.createWXAPI(ActivityManager.application, Constant.weChatAppID, true)

    @JvmStatic
    fun registerApp(app: Application) {
        app.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                wxApi.registerApp(Constant.weChatAppID)
            }
        }, IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP))
    }

    */
/**
     * @see WXEntryActivity.onResp
     *//*

    @JvmStatic
    fun authorize(state: State) {
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = state.text
        wxApi.sendReq(req)
    }

    @JvmStatic
    fun onAuthorizeFailed() {
        onAuthFailed?.invoke()
        onAuthFailed = null
    }

    var onBindWeChatSuccess: (() -> Unit)? = null
    var onAuthFailed: (() -> Unit)? = null


    @JvmStatic
    fun bindWeChat(code: String) {
        Global.requestDelegate.request(
            isShowDialogCompleteFailed = false,
            api = { apiService.bindWeChat(getRSAParams(code)) },
            success = {
                UserInfoManager.updateUserInfoCache {
                    onBindWeChatSuccess?.invoke()
                    onBindWeChatSuccess = null
                }
            },
            special = { code: Int?, _: Any?, message: String? ->

            }
        )
    }


    @JvmStatic
    fun unbindWeChat(requestDelegate: RequestDelegate, success: () -> Unit) {
        requestDelegate.request(
            api = { apiService.unbindWeChat() },
            success = {
                UserInfoManager.updateUserInfoCache { success() }
            },
            special = { code: Int?, _: Any?, message: String? ->

            }
        )
    }

    */
/**
     * 代码绘制的分享图片
     *//*

    @JvmStatic
    private fun shareCommodity2Friend(data: CommodityDetailBean?) {
        data ?: return
        val shareView = LayoutInflater.from(ActivityManager.application)
            .inflate(R.layout.share_commodity_wx,
                ActivityManager.getTopActivity()?.window?.decorView as? FrameLayout)
        val shareLayout = shareView.findViewById<View>(R.id.shareLayout)
        val ivCommodityImage = shareView.findViewById<ImageView>(R.id.ivCommodityImage)
        val tvCommodityName = shareView.findViewById<TextView>(R.id.tvCommodityName)
        val tvMarketPrice = shareView.findViewById<TextView>(R.id.tvMarketPrice)
        if (!data.files.isNullOrEmpty()) {
            ivCommodityImage.setCommodityCover(data.files[0])
        }
        tvCommodityName.text = getCommodityName(false, data.goodsName)
        tvMarketPrice.text = "￥${data.price}"
        tvMarketPrice.styleDeleteLine()


        shareView.post {
            val bitmap = shareLayout.drawToBitmap()
//        初始化 WXImageObject 和 WXMediaMessage 对象
            val imgObj = WXImageObject(bitmap)
            val msg = WXMediaMessage()
            msg.mediaObject = imgObj
            msg.title = "真的，我刚刚看完激励视频就免费拿走了这宝贝(包邮哦~)"
            msg.description = "xxxxxxxxxxxx"
            msg.mediaTagName = "tag"
            bitmap.recycle()
            (shareView.parent as? ViewGroup)?.removeView(shareView)
//        msg.messageAction

            //设置缩略图
            //        val thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true)
            //        bm.recycle()
            //        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

            //构造一个Req
            val req = SendMessageToWX.Req()
            req.transaction = "img${System.currentTimeMillis()}"
            req.message = msg
            req.scene = SendMessageToWX.Req.WXSceneSession
            //调用 api 接口，发送数据到微信
            wxApi.sendReq(req)


        }

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
                        Global.dismissLoading()
                        it.delete()
                    },
                    failed = failed)
            } catch (e: Exception) {
                e.printStackTrace()
//                failed()
            }
        }
    }

    */
/**
     * 分享到微信小程序
     * thumb 不大于 128 kb
     *//*

    @JvmStatic
    fun share2WeChatMini(title: String, pagePath: String, imgPath: String) {
        downloadThumb(imgPath, success = {
            share2WeChatMini(title, pagePath, it)
        }, failed = {
            share2WeChatMini(title, pagePath, ShareHelper.defaultShareImageUrl)
        })
    }

    */
/**
     * 分享到微信网页
     * thumb 不大于 32 kb
     *//*

    @JvmStatic
    fun share2WeChatWebPage(pagePath: String, title: String, description: String, imgPath: String) {
        ioThread {
            Global.showLoading()
            val bytesTemp = WeChatUtil.getHtmlByteArray(imgPath)
            val bytesCompressed = WeChatUtil.bmpToByteArrayForCompress(BitmapFactory.decodeByteArray(bytesTemp,0,bytesTemp.size),true)
            Global.dismissLoading()
            share2WeChatWebPage(pagePath, title, description, bytesCompressed)
        }
    }


    @JvmStatic
    private fun share2WeChatMini(title: String, pagePath: String, thumbData: ByteArray) {
        val miniProgramObj = WXMiniProgramObject()
        miniProgramObj.webpageUrl = "http://www.qq.com" // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = getMiniType()// 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = Constant.weChatMiniOriginID // 小程序原始id
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
            req.corpId = "ww9f450bb0b53ee43f"                              // 企业ID
            req.url = "https://work.weixin.qq.com/kfid/kfc05de1bf3d4680ece"    // 客服URL
            wxApi.sendReq(req);
        }
    }

    //福利群小程序
    @JvmStatic
    fun welfareGroup() {
        jumpWechatMini("pages/my/welfare/welfare")
    }


    //抽奖
    @JvmStatic
    fun luckDraw(id: Int?) {
        val path =
            "pages/auth/auth?path=" + URLEncoder.encode("/pages/luck/luck?taskId=${id}", "UTF-8")
        jumpWechatMini(path)
    }


    @JvmStatic
    private fun jumpWechatMini(path: String) {
        val req = WXLaunchMiniProgram.Req()
        req.userName = Constant.weChatMiniOriginID // 填小程序原始id
        req.path = path //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = getMiniType()// 可选打开 开发版，体验版和正式版
        wxApi.sendReq(req)
    }

    @JvmStatic
    private fun getMiniType(): Int {
        return if (BuildConfig.BUILD_TYPE == "release") {
            0
        } else {
            2
        }
    }
}*/
