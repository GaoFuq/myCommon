package com.gfq.common.helper.numberchange

import android.text.InputFilter
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.gfq.common.toast.toast
import com.gfq.common.utils.InputFilterFirstNot0
import com.gfq.common.utils.InputFilterMaxValue
import com.gfq.common.utils.InputFilterOnlyDigit

/**
 *  2022/10/28 14:47
 * @auth gaofuq
 * @description
 */
class NumberChangeHelper(
    private val editText: EditText,
    private val defNumber: Int = 0,
    private val  minNumber :Int = 1,
    private val  maxNumber: Int = 9999,
    maxLength: Int = 4,
) : INumberChange {

    val number: Int
        get() {
            return if (editText.text.isNullOrEmpty()) {
                defNumber
            } else {
                editText.text.toString().toInt()
            }
        }

    //当手动输入的数字大于给定的最大值
    var onNumberGreaterThanMax: ((Int) -> Unit)? =null

    var updateIncreaseButton :(Int)->Unit = {}
    var updateDecreaseButton :(Int)->Unit = {}
    var onNumberChanged : ((Int) -> Unit)? = null
    var onEditTextClick : (() -> Unit)? = null


    init {
        editText.filters = arrayOf(InputFilterOnlyDigit(),
            InputFilter.LengthFilter(maxLength+1), InputFilterFirstNot0(),
            InputFilterMaxValue(maxNumber,onNumberGreaterThanMax))

        editText.doAfterTextChanged {
            updateIncreaseButton(number)
            updateDecreaseButton(number)
            if (editText.hasFocus()) {
                onNumberChanged?.invoke(number)
            }
        }

        editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && editText.text?.toString()?.length == 0) {
                editText.setText(minNumber.toString())
                editText.setSelection(minNumber.toString().length)
            }
            if (hasFocus) {
                onEditTextClick?.invoke()
            }
        }

    }




    /**
     * 加
     */
    override fun onIncrease() {
        var num = number + 1
        if (num > maxNumber) {
            num = maxNumber
           onNumberGreaterThanMax?.invoke(num)
        }
        editText.setText("$num")
        editText.setSelection(num.toString().length)
        editText.requestFocus()
    }

    /**
     * 减
     */
    override fun onDecrease() {
        if (number <= minNumber) return
        var num = number - 1
        if (num == minNumber-1) {
            num = minNumber
        }
        editText.setText("$num")
        editText.setSelection(num.toString().length)
        editText.requestFocus()
    }

}