package com.gfq.common.utils

/**
 *  2021/12/30 18:00
 * @auth gaofuq
 * @description
 */


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.getSystemService
import java.util.regex.Pattern

/**
 * 文本复制到剪切板
 */
fun CharSequence?.copy2Clipboard(context: Context?) {
    // 得到剪贴板管理器
    context?.let {
        it.getSystemService<ClipboardManager>()
            ?.setPrimaryClip(ClipData.newPlainText(null, this ?: ""))
    }

}

/**
 * ￥ 99.01
 *
 * @param color1  ￥ color
 * @param sizeSp1 ￥ textSize
 * @param color2  99 color
 * @param sizeSp2 99 textSize
 * @param color3  01 color
 * @param sizeSp3 01 textSize
 */
@SuppressLint("DefaultLocale")
fun getMoneyTextSmallSpace(
    spanUtil: SpanUtils?,
    color1: Int,
    sizeSp1: Float,
    color2: Int,
    sizeSp2: Float,
    color3: Int,
    sizeSp3: Float,
    money: Double
): SpanUtils {
    val spanUtils: SpanUtils
    if (spanUtil == null) {
        spanUtils = SpanUtils()
    }
    val moneyStr = String.format("%.2f", money)
    val size1: Int = SizeUtils.sp2px(sizeSp1)
    val size2: Int = SizeUtils.sp2px(sizeSp2)
    val size3: Int = SizeUtils.sp2px(sizeSp3)
    val index = moneyStr.indexOf(".")
    spanUtils.append("¥").setFontSize(size1).setForegroundColor(color1)
        .appendSpace(SizeUtils.dp2px(3))
        .append(moneyStr.substring(0, index)).setFontSize(size2).setForegroundColor(color2)
        .append(moneyStr.substring(index)).setFontSize(size3).setForegroundColor(color3)
    return spanUtils
}


/**
 * 字符串是否包含中文汉字
 */
fun String?.containsChinese(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val regex = "[\u4e00-\u9fa5]"
    return Pattern.compile(regex).matcher(this).find()
}

/**
 * 字符串是否包含中文汉字或者中文标点
 */
fun String?.containsChineseAndPunctuation(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val regex = "[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]"
    return Pattern.compile(regex).matcher(this).find()
}