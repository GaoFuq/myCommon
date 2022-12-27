package com.gfq.common.helper.wechat
/**
 *
 * @author calm
 * @date 2022/11/10 11:28
 */
object WechatPay {
    private var wechatPayCallback:WechatPayCallback? = null
    fun registerWechatPayCallback(callback: WechatPayCallback){
        wechatPayCallback = callback
    }
    fun onSuccess(payNum:String){
        wechatPayCallback?.onPaySuccess(payNum)
        wechatPayCallback = null
    }
    fun onFailed(payNum:String,eMsg:String?){
        wechatPayCallback?.onPayFailed(payNum,eMsg)
        wechatPayCallback = null
    }
    fun onCancel(payNum:String){
        wechatPayCallback?.onPayCancel(payNum)
        wechatPayCallback = null
    }
}