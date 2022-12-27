package com.gfq.common.helper.wechat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.gfq.common.system.loge
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * 微信支付回调
 * @author Calm
 * @date 2022/11/10 10:59
 */
internal class WXPayEntryActivity : Activity(), IWXAPIEventHandler {
    private var api: IWXAPI? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, "weChatAppID", false)
        api?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api?.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {

    }

    override fun onResp(resp: BaseResp) {
        if(resp is PayResp){
            loge("onResp ${resp.type} ${resp.extData} ${resp.prepayId} ${resp.errCode} ${resp.errStr}")
            if(resp.type == ConstantsAPI.COMMAND_PAY_BY_WX){
                when(resp.errCode){
                    //成功
                    BaseResp.ErrCode.ERR_OK -> {
                        WechatPay.onSuccess(resp.extData)
                    }
                    //失败
                    BaseResp.ErrCode.ERR_COMM->{
                        WechatPay.onFailed(resp.extData,resp.errStr)
                    }
                    //取消
                    BaseResp.ErrCode.ERR_USER_CANCEL -> {
                        WechatPay.onCancel(resp.extData)
                    }
                }
                finish()
            }
        }
    }
}