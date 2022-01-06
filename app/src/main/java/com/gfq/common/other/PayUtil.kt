package com.gfq.common.other

import android.app.Activity
import com.orhanobut.logger.Logger
import com.xgr.alipay.alipay.AliPay
import com.xgr.alipay.alipay.AlipayInfoImpli
import com.xgr.easypay.EasyPay
import com.xgr.easypay.callback.IPayCallback
import com.xgr.wechatpay.wxpay.WXPay
import com.xgr.wechatpay.wxpay.WXPayInfoImpli


/**
 *  2021/4/28 10:23
 * @auth gaofuq
 * @description
 */
fun wxpay(
    act: Activity,
    timestamp: String,
    sign: String,
    prepayId: String,
    partnerid: String,
    appid: String,
    nonceStr: String,
    packageValue: String,
    success: () -> Unit = {},
    failed: () -> Unit = {},
    cancel: () -> Unit = {}
) {
    //实例化微信支付策略
    val wxPay = WXPay.getInstance()
    //构造微信订单实体。一般都是由服务端直接返回。
    val wxPayInfoImpli = WXPayInfoImpli()
    wxPayInfoImpli.timestamp = timestamp
    wxPayInfoImpli.sign = sign
    wxPayInfoImpli.prepayId = prepayId
    wxPayInfoImpli.partnerid = partnerid
    wxPayInfoImpli.appid = appid
    wxPayInfoImpli.nonceStr = nonceStr
    wxPayInfoImpli.packageValue = packageValue
    //策略场景类调起支付方法开始支付，以及接收回调。
    EasyPay.pay(wxPay, act, wxPayInfoImpli, object : IPayCallback {
        override fun success() {
            Logger.d("wxPay 支付成功")
            success.invoke()
        }

        override fun failed(code: Int, msg: String?) {
            Logger.e("wxPay 支付失败 code = $code msg = $msg")
            msg?.let {
                Logger.e(it)
            }
            failed.invoke()
        }

        override fun cancel() {
            Logger.d("wxPay 支付取消")
            cancel.invoke()
        }
    })
}


fun alipay(
    act: Activity, orderInfo: String,
    success: () -> Unit = {},
    failed: () -> Unit = {},
    cancel: () -> Unit = {}
) {
    //实例化支付宝支付策略
    val aliPay = AliPay()
    //构造支付宝订单实体。一般都是由服务端直接返回。
    val alipayInfoImpli = AlipayInfoImpli()
    alipayInfoImpli.orderInfo = orderInfo
    //策略场景类调起支付方法开始支付，以及接收回调。
    EasyPay.pay(aliPay, act, alipayInfoImpli, object : IPayCallback {
        override fun success() {
            Logger.d("alipay 支付成功")
            success.invoke()
        }

        override fun failed(code: Int, msg: String?) {
            Logger.d("alipay 支付失败")
            msg?.let {
                Logger.e(it)
            }
            failed.invoke()
        }

        override fun cancel() {
            Logger.d("alipay 支付取消")
            cancel.invoke()
        }
    })
}