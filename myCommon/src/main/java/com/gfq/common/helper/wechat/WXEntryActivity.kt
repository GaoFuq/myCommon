/*
package com.gfq.common.helper.wechat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.mmj.android.Constant
import com.mmj.android.helper.LoginHelper
import com.mmj.android.helper.WeChatHelper
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.lang.ref.WeakReference

class WXEntryActivity : Activity(), IWXAPIEventHandler {
    private var api: IWXAPI? = null
    private var handler: MyHandler? = null

    private class MyHandler(wxEntryActivity: WXEntryActivity) : Handler(Looper.getMainLooper()) {
        private val wxEntryActivityWkRef = WeakReference(wxEntryActivity)
        override fun handleMessage(msg: Message) {
            val tag = msg.what
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, Constant.weChatAppID, false)
        handler = MyHandler(this)
        api?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api?.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {
        Log.e(TAG, "onReq: req = $req")
        when (req.type) {
            ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX -> goToGetMsg()
            ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX -> goToShowMsg(req as ShowMessageFromWX.Req)
            else -> {
            }
        }
        finish()
    }

    override fun onResp(resp: BaseResp) {
        Log.e(TAG, "onResp: resp.errCode = " + resp.errCode)
        Log.e(TAG, "onResp: resp.errStr = " + resp.errStr)
        Log.e(TAG, "onResp: resp.getType = " + resp.type)
        when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> {
            }
            BaseResp.ErrCode.ERR_UNSUPPORT -> {
            }
            else -> {
            }
        }
        when (resp.type) {
            ConstantsAPI.COMMAND_SUBSCRIBE_MESSAGE -> {
            }
            ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM -> {
            }
            ConstantsAPI.COMMAND_OPEN_BUSINESS_VIEW -> {
            }
            ConstantsAPI.COMMAND_OPEN_BUSINESS_WEBVIEW -> {
            }
            ConstantsAPI.COMMAND_SENDAUTH -> {
                val authResp = resp as SendAuth.Resp
                val code = authResp.code
                when (authResp.state) {
                    WeChatHelper.State.stateLogin.text -> LoginHelper.bindPhoneIfNeed(code)
                    WeChatHelper.State.stateBind.text -> WeChatHelper.bindWeChat(code)
                    WeChatHelper.State.stateBindQuick.text -> LoginHelper.weChatBindQuickIfNeed(code)
                }
            }
        }

        if(resp.errCode != BaseResp.ErrCode.ERR_OK){
            WeChatHelper.onAuthorizeFailed()
        }
        finish()
    }


    private fun goToGetMsg() {}
    private fun goToShowMsg(showReq: ShowMessageFromWX.Req) {}

    companion object {
        private const val TAG = "WXEntryActivity"
    }
}*/
