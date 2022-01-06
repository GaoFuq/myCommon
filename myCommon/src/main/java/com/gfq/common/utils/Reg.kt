package com.gfq.common.utils

import java.util.regex.Pattern

/**
 *  2021/12/31 10:14
 * @auth gaofuq
 * @description
 */

/**
 * 检验邮箱格式
 *
 * @param str
 * @return
 */
fun isEmail(str: String?): Boolean {
    val regExp = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$"
    val p = Pattern.compile(regExp)
    val m = p.matcher(str)
    return m.matches()
}
