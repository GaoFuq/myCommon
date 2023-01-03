package com.gfq.common.zsimple

import android.content.Context
import android.graphics.Typeface
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doAfterTextChanged
import com.gfq.common.base.BaseBindingView
import com.gfq.common.utils.dpF

/**
 *  2022/12/6 13:55
 * @author gaofuq
 * @description
 */

    <declare-styleable name="TitleEditView">
        <attr format="string" name="te_title"/>
        <attr format="string" name="te_digits"/>
        <attr format="flags" name="te_inputType">
            <flag name="text" value="0x00000001" />
            <flag name="number" value="0x00000002" />
            <flag name="textPassword" value="0x00000081" />
        </attr>
        <attr format="string" name="te_content"/>
        <attr format="reference" name="te_space"/>
        <attr format="reference" name="te_titleIcon"/>
        <attr format="boolean" name="te_arrowVisible"/>
        <attr format="color" name="te_titleTextColor"/>
        <attr format="boolean" name="te_titleTextBold"/>
        <attr format="integer" name="te_titleTextSize"/>
        <attr format="color" name="te_contentTextColor"/>
        <attr format="integer" name="te_contentTextSize"/>
        <attr format="string" name="te_contentHintText"/>
        <attr format="color" name="te_contentHintTextColor"/>
    </declare-styleable>

     <LinearLayout
         android:layout_width="match_parent"
         android:layout_height="@dimen/dp52"
         android:paddingStart="@dimen/dp16"
         android:paddingEnd="@dimen/dp12"
         android:gravity="center_vertical">

         <TextView
             android:id="@+id/tvTitle"
             android:layout_width="wrap_content"
             android:layout_height="wrap_content"
             tools:drawableStart="@mipmap/icon_mobile_phone_authentication"
             android:drawablePadding="@dimen/dp8"
             android:gravity="center_vertical"
             tools:text="手机认证"
             android:textColor="@color/color666"
             android:textSize="@dimen/sp14" />

         <androidx.legacy.widget.Space
             android:id="@+id/space"
             android:layout_width="@dimen/dp4"
             android:layout_height="0dp"/>

         <com.gfq.common.view.EditText
             android:id="@+id/editText"
             android:background="@android:color/transparent"
             android:gravity="center_vertical|start"
             android:layout_width="0dp"
             android:layout_weight="1"
             android:layout_height="match_parent"
             android:drawableEnd="@mipmap/btn_entrance"
             tools:text="hint"
             android:textColorHint="@color/colorHint"
             android:lines="1"
             android:singleLine="true"
             android:ellipsize="end"
             android:drawablePadding="@dimen/dp4"
             android:textColor="@color/color666"
             android:textSize="@dimen/sp14" />

     </LinearLayout>

class TitleEditView(context: Context, attrs: AttributeSet?) :
    BaseBindingView<TitleEditViewBinding>(context, attrs) {
    override fun layoutResId(): Int = R.layout.title_edit_view


    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TitleEditView)
        val defTitleColor = context.getColor(R.color.colorTitleTextColor)
        val defContentColor = context.getColor(R.color.color666)
        val defContentColorHint = context.getColor(R.color.colorHint)
        val title = a.getString(R.styleable.TitleEditView_te_title)
        val titleTextColor = a.getColor(R.styleable.TitleEditView_te_titleTextColor,defTitleColor)
        val titleTextBold = a.getBoolean(R.styleable.TitleEditView_te_titleTextBold,false)
        val titleTextSize = a.getInt(R.styleable.TitleEditView_te_titleTextSize,14)
        val content = a.getString(R.styleable.TitleEditView_te_content)
        val contentTextColor = a.getColor(R.styleable.TitleEditView_te_contentTextColor,defContentColor)
        val contentTextSize = a.getInt(R.styleable.TitleEditView_te_contentTextSize,14)
        val contentHintText = a.getString(R.styleable.TitleEditView_te_contentHintText)
        val contentHintTextColor = a.getColor(R.styleable.TitleEditView_te_contentHintTextColor,defContentColorHint)
        val arrowVisible = a.getBoolean(R.styleable.TitleEditView_te_arrowVisible,true)
        val titleIcon = a.getResourceId(R.styleable.TitleEditView_te_titleIcon,0)
        val space = a.getDimension(R.styleable.TitleEditView_te_space,dpF(R.dimen.dp4))
        val inputType = a.getInt(R.styleable.TitleEditView_te_inputType,EditorInfo.TYPE_CLASS_TEXT)
        val digits = a.getString(R.styleable.TitleEditView_te_digits)
        vBinding.tvTitle.text = title
        vBinding.tvTitle.setTextColor(titleTextColor)
        vBinding.tvTitle.setDrawableLeft(titleIcon)
        vBinding.editText.setText(content)
        vBinding.editText.setTextColor(contentTextColor)
        vBinding.editText.setTextSize(contentTextSize.toFloat())
        vBinding.tvTitle.setTextSize(titleTextSize.toFloat())
        vBinding.editText.hint = contentHintText
        vBinding.editText.setHintTextColor(contentHintTextColor)
        val arrowRes = if(arrowVisible){
            R.mipmap.btn_entrance
        }else{
            0
        }
        vBinding.editText.setDrawableRight(arrowRes)
        vBinding.editText.inputType = inputType

        vBinding.space.updateLayoutParams<MarginLayoutParams> { width = space.toInt() }
        if(titleTextBold){
            vBinding.tvTitle.setTypeface(Typeface.DEFAULT_BOLD)
        }
        if(digits!=null){
            vBinding.editText.setKeyListener(DigitsKeyListener.getInstance(digits))
        }
        a.recycle()
    }

    fun hideRightArrow(){
        vBinding.editText.setDrawableNUll()
    }

    fun setContent(text:String?){
        vBinding.editText.setText(text)
    }

    fun getContent():String?{
        return  vBinding.editText.text?.toString()
    }
    fun setTitle(text:String?){
        vBinding.tvTitle.text = text
    }

    fun doAfterContentChanged(block:(String?)->Unit){
        vBinding.editText.doAfterTextChanged {
            block(it?.toString())
        }
    }
}