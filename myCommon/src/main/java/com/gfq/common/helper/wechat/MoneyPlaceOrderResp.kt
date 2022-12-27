package com.gfq.common.helper.wechat

internal data class MoneyPlaceOrderResp(
    val nonceStr:String,
    val mchId:String,
    val payNo:String,
    val paySign:String,
    val prepayId:String,
    val signType:String,
    val timeStamp:String)