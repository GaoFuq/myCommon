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
import com.gfq.common.system.dp
import java.util.regex.Pattern


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