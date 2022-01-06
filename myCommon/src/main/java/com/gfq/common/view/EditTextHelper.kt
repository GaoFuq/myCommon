package com.gfq.common.view

import android.text.InputFilter
import android.text.Spanned

/**
 *  2021/12/31 9:28
 * @auth gaofuq
 * @description
 */

/**
 * 给 EditText 设置过滤器
 *
 * @param length 分割位数（每隔 length 个字符，分割一次）
 * @param split 分隔符
 * @param useSpecialSymbol 是否可以使用特殊符号 "空格.;+-*#/" ，默认 false
 * return InputFilter
 */
fun getSplitInputFilter(length: Int, split: String, useSpecialSymbol: Boolean = false) =
    InputFilter { source, start, end, dest, dstart, dend ->
        var result = ""
        val text = dest.toString()
        val x = text.length % (length + 1)
        result = if (x == 0) {
            "$split$source"
        } else {
            source.toString()
        }
        if (useSpecialSymbol) {
            if (source == "" || source == " " || source == "." || source == "+" || source == "-" || source == "*" || source == "/" || source == "," || source == "#") {
                result = ""
            }
        }
        result
    }


/**
 * 给 EditText 设置过滤器
 * 作用：限制小数的输入
 * 使用场景：money输入
 * @param decimalDigits 小数位数
 */
fun getLimitDecimalsInputFilter(decimalDigits: Int) =
    InputFilter { source, start, end, dest, dstart, dend ->
        var result = ""
        // 删除等特殊字符，直接返回
        val sVal = source.toString()
        if ("" == sVal) {
            result = ""
        }
        val dVal = dest.toString()
        if ("." == sVal && "" == dVal) {
            result = "0."
        }
        if ("0" == dVal && "." != sVal) {
            result = ""
        }
        val splitArray = dVal.split("\\.")
        if (splitArray.size > 1) {
            val dotValue = splitArray[1]
            val diff = dotValue.length + 1 - decimalDigits
            if (diff > 0) {
                result = ""
            }
        }
        result
    }
