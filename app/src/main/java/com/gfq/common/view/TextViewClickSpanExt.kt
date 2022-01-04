package com.gfq.common

/**
 *  2021/12/30 11:55
 * @auth gaofuq
 * @description
 */
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.view.doOnLayout


/**
 * 一个TextView空间内设置不同颜色的文字，并使得该部分的文字有着单独的点击事件
 *

        val txt = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        binding.textView.setMultiClickSpan(
            txt, 2,
            ClickInfo(0, 5, Color.RED) {
                Log.e("xx", "setMultiClickSpan RED")
            },
            ClickInfo(5, 10, Color.YELLOW) {
                Log.e("xx", "setMultiClickSpan YELLOW")
            },
            ClickInfo(10, txt.length, Color.GREEN) {
                Log.e("xx", "setMultiClickSpan GREEN")
            }
        )

 */
fun TextView.setMultiClickSpan(
    content: String,
    maxLines: Int = 0,
    vararg clickInfo: ClickInfo
) {
    if (clickInfo.isEmpty()) {
        text = content
        return
    }

    text = content
    doOnLayout {
        val realText = if (maxLines != 0 && lineCount > maxLines) {
            val lineEndIndex = layout.getLineEnd(maxLines - 1)// 设置第maxLines行打省略号
            "${text.subSequence(0, lineEndIndex - 1)}..."
        } else {
            content
        }

        val ss = SpannableString(realText)
        clickInfo.forEach {
            if (it.start < realText.length) {
                val start = it.start
                val end = if (it.end > realText.length) realText.length else it.end
                ss.setSpan(
                    ClickTextViewSpan(it.color, it.clickListener),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        text = ss

        movementMethod = LinkMovementMethod.getInstance() // 不设置就没有点击事件
        highlightColor = Color.TRANSPARENT // 设置点击后的颜色为透明
        setOnTouchListener(LinkMovementMethodOverride())// 固定 TextView 行数的时候，点击 ClickableSpan 文本会出现滚动现象
    }

}

data class ClickInfo(
    val start: Int,
    val end: Int,
    @ColorInt val color: Int,
    val clickListener: (() -> Unit)? = null
)

internal class LinkMovementMethodOverride : View.OnTouchListener {
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val widget = v as TextView
        val text = widget.text
        if (text is Spanned) {

            val action = event.action
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                var x = event.x.toInt()
                var y = event.y.toInt()

                x -= widget.totalPaddingLeft
                y -= widget.totalPaddingTop

                x += widget.scrollX
                y += widget.scrollY

                val layout = widget.layout
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x.toFloat())

                val link = text.getSpans(
                    off, off,
                    ClickableSpan::class.java
                )

                if (link.isNotEmpty()) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget)
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        // Selection only works on Spannable text. In our case setSelection doesn't work on spanned text
                        //Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                    }
                    return true
                }
            }

        }

        return false
    }

}

/**
 * 一个TextView空间内设置不同颜色的文字，并使得该部分的文字有着单独的点击事件
 */
internal class ClickTextViewSpan(
    @ColorInt val textColor: Int,
    private val clickListener: (() -> Unit)? = null
) : ClickableSpan() {

    override fun onClick(widget: View) {
        clickListener?.invoke()
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = textColor// 设置可以点击文本部分的颜色
        ds.isUnderlineText = false // 设置该文本部分是否显示超链接形式的下划线
    }
}

