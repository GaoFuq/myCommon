package com.gfq.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.gfq.common.R

/**
 * https://www.cnblogs.com/summer-xx/p/14245264.html
 * https://www.jianshu.com/p/43eccb55ad45
 */
class EditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle,
) : AppCompatEditText(context, attrs, defStyleAttr) {

    var isAlwaysSelectionEnd: Boolean? = null
        set(value) {
            field = value
            value?.let { boo ->
                doAfterTextChanged {
                    it?.let {
                        if (boo) {
                            setSelection(it.length)
                        }
                    }
                }
            }
        }

    /**
     * 当软键盘点击删除
     */
    var onDeleteClick: (() -> Unit)? = null

    var onCopy: (() -> Unit)? =null
    var onPaste: (() -> Unit)? =null
    var onCut: (() -> Unit)? =null

    override fun onTextContextMenuItem(id: Int): Boolean {
        when (id) {
            android.R.id.paste -> {
                onPaste?.invoke()
            }
            android.R.id.copy -> {
                onCopy?.invoke()
            }
            android.R.id.cut -> {
                onCut?.invoke()
            }
        }
        return super.onTextContextMenuItem(id)
    }


    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection? {
        // 返回自己的实现
        return BackspaceInputConnection(super.onCreateInputConnection(outAttrs), true)
    }

    inner class BackspaceInputConnection(target: InputConnection?, mutable: Boolean) :
        InputConnectionWrapper(target, mutable) {

        //当有按键输入时，该方法会被回调。比如点击退格键时，搜狗输入法应该就是通过调用该方法，
        //发送keyEvent的，但谷歌输入法却不会调用该方法，而是调用下面的deleteSurroundingText()方法。
        override fun sendKeyEvent(event: KeyEvent): Boolean {
            if (event.action == KeyEvent.ACTION_DOWN
                && event.keyCode == KeyEvent.KEYCODE_DEL
            ) {
                onDeleteClick?.let {
                    text?.let { ed ->
                        if (ed.isNotEmpty()) {
                            text = ed.delete(ed.length - 1, ed.length)
                            setSelection(ed.length)
                            it.invoke()
                            return true
                        }
                    }
                }
            }
            return super.sendKeyEvent(event)
        }

        /**
         * 当软键盘删除文本之前，会调用这个方法通知输入框，我们可以重写这个方法并判断是否要拦截这个删除事件。
         * 在谷歌输入法上，点击退格键的时候不会调用[.sendKeyEvent]，
         * 而是直接回调这个方法，所以也要在这个方法上做拦截；
         */
        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
            return if (beforeLength == 1 && afterLength == 0) {
                sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        &&
                        sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
            } else super.deleteSurroundingText(beforeLength, afterLength)
        }


        //当输入法输入了字符，包括表情，字母、文字、数字和符号等内容，会回调该方法
        override fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean {
            return super.commitText(text, newCursorPosition)
        }


        //结束组合文本输入的时候，回调该方法
        override fun finishComposingText(): Boolean {
            return super.finishComposingText()
        }
    }
}