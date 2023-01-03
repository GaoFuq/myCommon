package com.gfq.common.view.simple

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import com.gfq.common.base.BaseBindingView
import com.gfq.common.view.setDebounceClick

  <declare-styleable name="ConfirmButton">
        <attr format="string" name="cb_confirmText"/>
        <attr format="color" name="cb_confirmTextColor"/>
        <attr format="integer" name="cb_confirmTextSize"/>
        <attr format="boolean" name="cb_confirmTextBold"/>
        <attr format="flags" name="cb_style">
            <flag name="enable" value="1"/>
            <flag name="notEnable" value="2"/>
        </attr>
        <attr format="boolean" name="cb_withContainer"/>
        <attr format="reference|color" name="cb_confirmTextBg"/>
    </declare-styleable>


  <FrameLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="@dimen/bottomConfirmButtonHeight"
        >

        <TextView
            android:id="@+id/tvConfirm"
            android:layout_width="match_parent"
            android:layout_height="@dimen/confirmButtonHeight"
            android:layout_marginStart="@dimen/dp16"
            android:layout_marginTop="@dimen/dp8"
            android:layout_marginEnd="@dimen/dp16"
            android:background="@drawable/bg_round_theme"
            android:gravity="center"
            android:textColor="@color/white"
            android:textSize="@dimen/sp16"
            tools:text="Confirm" />
    </FrameLayout>

class ConfirmButton (context: Context, attrs: AttributeSet?) :
    BaseBindingView<ConfirmButtonViewBinding>(context, attrs) {

    override fun layoutResId(): Int = R.layout.confirm_button_view

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ConfirmButton)
        val defConfirmColor = context.getColor(R.color.confirmButtonDefTextColor)
        val confirmText = a.getString(R.styleable.ConfirmButton_cb_confirmText)
        val confirmTextColor = a.getColor(R.styleable.ConfirmButton_cb_confirmTextColor, defConfirmColor)
        val confirmTextSize = a.getInt(R.styleable.ConfirmButton_cb_confirmTextSize, 16)
        val confirmTextBold = a.getBoolean(R.styleable.ConfirmButton_cb_confirmTextBold, false)
        val style = a.getInt(R.styleable.ConfirmButton_cb_style, 0)
        val withContainer = a.getBoolean(R.styleable.ConfirmButton_cb_withContainer, true)
        val confirmTextBg = a.getResourceId(R.styleable.ConfirmButton_cb_confirmTextBg, R.drawable.bg_round_theme)




        vBinding.tvConfirm.setTextColor(confirmTextColor)
        vBinding.tvConfirm.text = confirmText
        vBinding.tvConfirm.textSize = confirmTextSize.toFloat()

        if (confirmTextBold) {
            vBinding.tvConfirm.typeface = Typeface.DEFAULT_BOLD
        } else {
            vBinding.tvConfirm.typeface = Typeface.DEFAULT
        }
        vBinding.tvConfirm.setBackgroundResource(confirmTextBg)



        if(!withContainer){
            vBinding.container.updateLayoutParams<MarginLayoutParams> { height = -2 }
            vBinding.tvConfirm.updateLayoutParams<MarginLayoutParams> {
                topMargin = 0
                bottomMargin = 0
                leftMargin = 0
                rightMargin = 0
            }
        }


        if(style == 1){
            setEnableStyle()
        }else if(style == 2){
            setNotEnableStyle()
        }

        a.recycle()
    }

    fun setConfirmClick(block: (TextView) -> Unit) {
        vBinding.tvConfirm.setDebounceClick {
            block(vBinding.tvConfirm)
        }
    }

    fun setNotEnableStyle(){
        vBinding.tvConfirm.setBackgroundResource(R.drawable.bg_round_not_enable)
        vBinding.tvConfirm.setTextColor(context.getColor(R.color.colorNotEnableText))
    }

    fun setEnableStyle(){
        vBinding.tvConfirm.setBackgroundResource(R.drawable.bg_round_theme)
        vBinding.tvConfirm.setTextColor(context.getColor(R.color.white))
    }

    fun setConfirmText(text:String?){
        vBinding.tvConfirm.text = text
    }
}