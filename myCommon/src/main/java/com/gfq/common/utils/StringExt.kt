package com.gfq.common.utils

/**
 *  2021/12/30 18:00
 * @auth gaofuq
 * @description
 */


import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import androidx.annotation.StringRes
import androidx.core.text.isDigitsOnly
import com.gfq.common.system.ActivityManager
import java.util.regex.Pattern
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun String?.isEmail(): Boolean {
    this?:return false
    val regExp = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$"
    val p = Pattern.compile(regExp)
    val m = p.matcher(this)
    return m.matches()
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


/**
 * 字符是否汉字
 */
fun Char?.isChineseCharacter(): Boolean {
    if (this == null) return false
    val regex = "[\u4e00-\u9fa5]"
    return Pattern.compile(regex).matcher(this.toString()).find()
}

/**
 * 字符是否汉字 或 数字 或 字母
 */
fun Char?.isChineseCharacterOrDigitOrLetter(): Boolean {
    if (this == null) return false
    val regex = "[^0-9a-zA-Z\u4e00-\u9fa5]"
    return Pattern.compile(regex).matcher(this.toString()).find()
}

/**
 * 字符串是否包含了数字和字母
 */
fun CharSequence?.containsLetterOrDigit(): Boolean {
    if (this == null) return false
    return this.toString().toCharArray().all { it.isLetter() || it.isDigit() }
}

/**
 * 字符串是否是中文（是否全部是中文字符）
 */
fun CharSequence?.isChineseCharacter(): Boolean {
    if (this == null) return false
    return this.toString().toCharArray().all { it.isChineseCharacter() }
}

/**
 * 字符串是否是emoji表情
 */
fun CharSequence?.isEmoji(): Boolean {
    if (this == null) return false
    val regex =
        "[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\ud83e\\udc00-\\ud83e\\udfff]|[\\ud83f\\udc00-\\ud83f\\udfff]|[\\u2600-\\u27ff]|[\\u200D]|[\\u0020]|[\\u2642]|[\\u2640]|[\\uFE0F]|[\\u2300-\\u23FF]|[\\udb40\\udc00-\\udb40\\uddff]"
    return Pattern.compile(regex).matcher(this.toString()).find()
}

/**
 * 字符串是否全部是数字
 */
fun CharSequence?.isLetter(): Boolean {
    if (this == null) return false
    return this.toString().toCharArray().all { it.isLetter() }
}

/**
 * 字符串是否由数字和字母组成
 */
fun CharSequence?.isLetterOrDigit(): Boolean {
    if (this == null) return false
    return this.toString().toCharArray().all { it.isLetter() || it.isDigit() }
}

/**
 * 字符串是否包含中文字符
 */
fun CharSequence?.containsChineseCharacter(): Boolean {
    if (this == null) return false
    return this.toString().toCharArray().any { it.isChineseCharacter() }
}

/**
 * 字符串是否包含数字
 */
fun CharSequence?.containsDigit(): Boolean {
    if (this == null) return false
    return this.toString().toCharArray().any { it.isDigit() }
}

/**
 * 字符串是否包含空格
 */
fun CharSequence?.containsWhiteSpace(): Boolean {
    if (this == null) return false
    return this.toString().toCharArray().any { it.isWhitespace() }
}

abstract class BaseInputFilter : InputFilter {
    private val TAG = this.javaClass.simpleName
    private val sb = StringBuilder()
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        Log.e(TAG, "source = $source")
        sb.clear()
        if (source == null) return null
        val arr = source.toString().toCharArray()
        arr.forEach {
            if (keepCondition(it)) {
                sb.append(it)
            }
        }
        return sb
    }

    abstract fun keepCondition(char: Char): Boolean
}

/**
 * 去除空格
 */
class InputFilterExcludeWhiteSpace : BaseInputFilter() {
    override fun keepCondition(char: Char) = !char.isWhitespace()
}

/**
 * 去除数字
 */
class InputFilterExcludeDigit : BaseInputFilter() {
    override fun keepCondition(char: Char) = !char.isDigit()
}

/**
 * 只要数字
 */
class InputFilterOnlyDigit : BaseInputFilter() {
    override fun keepCondition(char: Char) = char.isDigit()
}

/**
 * 只要字母
 */
class InputFilterOnlyLetter : BaseInputFilter() {
    override fun keepCondition(char: Char) = char.isLetter()
}


/**
 * 去除中文字符
 */
class InputFilterExcludeChineseCharacter : BaseInputFilter() {
    override fun keepCondition(char: Char) = !char.isChineseCharacter()
}

/**
 * 只要中文字符
 */
class InputFilterOnlyChineseCharacter : BaseInputFilter() {
    override fun keepCondition(char: Char) = char.isChineseCharacter()
}


/**
 * 去除特殊字符（包括emoji,空格等）
 * 只留下数字，字母，汉字
 */
class InputFilterExcludeSpecialCharacters : BaseInputFilter() {
    override fun keepCondition(char: Char) = !char.isChineseCharacterOrDigitOrLetter()
}



/**
 * 去除emoji表情
 */
class InputFilterExcludeEmoji : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        if (source == null) return null
        if (source.isEmoji()) {
            return ""
        } else {
            return null
        }

    }
}


/**
 * 不包括指定字符（去除指定字符）
 */
class InputFilterExclude(private val arr: Array<Char>)  : BaseInputFilter() {
    override fun keepCondition(char: Char) = arr.all { char != it }
}

/**
 * 只要指定字符
 */
class InputFilterOnly(private val arr: Array<Char>) : BaseInputFilter() {
    override fun keepCondition(char: Char) = arr.any { char == it }
}

fun getString(@StringRes id:Int):String= ActivityManager.application.getString(id)

/**
 * 手动输入数字时，第一个数字不能是0
 */
class InputFilterFirstNot0  : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int,
    ): CharSequence {
        if (source == "0" && dstart == 0) {
            return ""
        }
        return source.toString()
    }
}

/**
 *  2022/9/20 12:07
 * @auth gaofuq
 * @description
 *
 * 需要配合 InputFilterFirstNot0 使用 ， 避免 首位数字是 0。
 * 设置的 maxLength 要等于 maxValue.length + 1
 */
class InputFilterMaxValue(
    private val maxValue: Int,
    private val onNumberGreaterThanMax: ((Int) -> Unit)? =null,
) :
    InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int,
    ): CharSequence {
        if (!source.isNullOrEmpty() && !dest.isNullOrEmpty()) {
            if(source.isDigitsOnly() && dest.isDigitsOnly()) {
                val destLongNext = (dest.toString() + source).toInt()
                Log.e("TAG", "filter: destLongNext = $destLongNext")

                if (destLongNext > maxValue) {
                    onNumberGreaterThanMax?.invoke(destLongNext)
                    return ""
                }
            }
        }
        return source.toString()
    }
}

/**
 * 给 EditText 设置过滤器
 * 作用：限制小数的输入
 * 使用场景：money输入
 * @param decimalDigits 小数位数
 */
class InputFilterLimitDecimal(private val decimalDigits: Int):InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int,
    ): CharSequence {
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
        return result
    }

}


/**
 * 给 EditText 设置过滤器
 * 场景：银行卡号的输入，每输入4位，自动添加空格。
 * 需求：点击输入框，打开数字键盘，可以输入数字，空格。
 * 解决：
 * 1.EditText 设置 inputType = "number" ，可以使系统打开数字键盘，但是会限制输入只能是0~9。
 * 2.EditText 设置 digits = "1234567890 " 。
 * @description 在输入时插入特定字符
 * @param insertStr 要插入的字符串
 * @param insertSpace 插入的字符间隔数
 */
class InputFilterInset(private val insertStr: String = " ", private val insertSpace: Int = 4) :
    InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        if (!source.isNullOrEmpty() && (dest?.length ?: 0) % (insertSpace + 1) == 0) {
            return "$insertStr$source"
        }
        return source
    }
}


val String?.md5: String
    get() {
        if (this.isNullOrEmpty()) {
            return ""
        }
        return try {
            val messageDigest = MessageDigest.getInstance("MD5")
            val digestBytes = messageDigest.digest(toByteArray())
            val sb = StringBuilder()
            for (b in digestBytes) {
                sb.append(String.format("%02x", b))
            }
            sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }