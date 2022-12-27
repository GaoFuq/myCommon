package com.gfq.common.helper.wechat

/**
 * 微信支付回调
 * @author calm
 * @date 2022/11/10 11:24
 */
interface WechatPayCallback {
    fun onPaySuccess(payNum:String)
    fun onPayFailed(payNum:String,eMsg:String?)
    fun onPayCancel(payNum:String)
}