package com.gfq.common.utils

import android.graphics.Color
import android.widget.TextView

/**
 *  2022/3/21 16:14
 * @auth gaofuq
 * @description
 */


//您点击“同意”，即表示您已阅读并同意更新后的  《服务协议》      及    《隐私政策》           ,巴拉巴拉。。。。
//start                                        protocolFirst    mid    protocolSecond         end
//启动页，用户协议，隐私协议文本模板
fun TextView.setPrivacyText(
    startText: String,
    startTextColor: String,
    protocolFirst: String,
    protocolFirstColor: String,
    midText: String,
    midTextColor:String,
    protocolSecond:String,
    protocolSecondColor:String,
    endText:String,
    endTextColor:String,
    protocolFirstClick:()->Unit,
    protocolSecondClick:()->Unit,
) = SpanUtils.with(this).append(startText)
    .setForegroundColor(Color.parseColor(startTextColor))

    .append(protocolFirst)
    .setClickSpan(Color.parseColor(protocolFirstColor), false) {
        protocolFirstClick()
    }

    .append(midText)
    .setForegroundColor(Color.parseColor(midTextColor))

    .append(protocolSecond)
    .setClickSpan(Color.parseColor(protocolSecondColor), false) {
        protocolSecondClick()
    }
    .append(endText)
    .setForegroundColor(Color.parseColor(endTextColor))
    .create()