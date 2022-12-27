package com.gfq.common.zsimple

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.gfq.common.base.BaseBindingView

/**
 *  2022/12/6 13:55
 * @author gaofuq
 * @description
 */

    <declare-styleable name="TitleContentView">
        <attr format="string" name="tc_title"/>
        <attr format="string" name="tc_content"/>
        <attr format="reference" name="tc_titleIcon"/>
        <attr format="boolean" name="tc_arrowVisible"/>
        <attr format="color" name="tc_titleTextColor"/>
        <attr format="boolean" name="tc_titleTextBold"/>
        <attr format="integer" name="tc_titleTextSize"/>
        <attr format="color" name="tc_contentTextColor"/>
        <attr format="integer" name="tc_contentTextSize"/>
        <attr format="string" name="tc_contentHintText"/>
        <attr format="color" name="tc_contentHintTextColor"/>
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
             android:drawableStart="@mipmap/icon_mobile_phone_authentication"
             android:drawablePadding="@dimen/dp8"
             android:gravity="center_vertical"
             android:text="手机认证"
             android:textColor="@color/colorTitleTextColor"
             android:textSize="@dimen/sp14" />

         <TextView
             android:id="@+id/tvContent"
             android:gravity="center_vertical|end"
             android:layout_width="0dp"
             android:layout_weight="1"
             android:layout_height="wrap_content"
             android:drawableEnd="@mipmap/btn_entrance"
             android:text="未认证"
             android:lines="1"
             android:singleLine="true"
             android:ellipsize="end"
             android:layout_marginStart="@dimen/dp10"
             android:drawablePadding="@dimen/dp4"
             android:textColor="@color/color666"
             android:textSize="@dimen/sp14" />

     </LinearLayout>

class TitleContentView(context: Context, attrs: AttributeSet?) :
    BaseBindingView<TitleContentViewBinding>(context, attrs) {
    override fun layoutResId(): Int = R.layout.title_content_view


    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TitleContentView)
        val defTitleColor = context.getColor(R.color.colorTitleTextColor)
        val defContentColor = context.getColor(R.color.color666)
        val title = a.getString(R.styleable.TitleContentView_tc_title)
        val titleTextColor = a.getColor(R.styleable.TitleContentView_tc_titleTextColor,defTitleColor)
        val titleTextBold = a.getBoolean(R.styleable.TitleContentView_tc_titleTextBold,false)
        val titleTextSize = a.getInt(R.styleable.TitleContentView_tc_titleTextSize,14)
        val content = a.getString(R.styleable.TitleContentView_tc_content)
        val contentTextColor = a.getColor(R.styleable.TitleContentView_tc_contentTextColor,defContentColor)
        val contentTextSize = a.getInt(R.styleable.TitleContentView_tc_contentTextSize,14)
        val contentHintText = a.getString(R.styleable.TitleContentView_tc_contentHintText)
        val contentHintTextColor = a.getColor(R.styleable.TitleContentView_tc_contentHintTextColor,defContentColor)
        val arrowVisible = a.getBoolean(R.styleable.TitleContentView_tc_arrowVisible,true)
        val titleIcon = a.getResourceId(R.styleable.TitleContentView_tc_titleIcon,0)
        vBinding.tvTitle.text = title
        vBinding.tvTitle.setTextColor(titleTextColor)
        vBinding.tvTitle.setDrawableLeft(titleIcon)
        vBinding.tvContent.text = content
        vBinding.tvContent.setTextColor(contentTextColor)
        vBinding.tvContent.setTextSize(contentTextSize.toFloat())
        vBinding.tvTitle.setTextSize(titleTextSize.toFloat())
        vBinding.tvContent.hint = contentHintText
        vBinding.tvContent.setHintTextColor(contentHintTextColor)
        val arrowRes = if(arrowVisible){
            R.mipmap.btn_entrance
        }else{
            0
        }
        vBinding.tvContent.setDrawableRight(arrowRes)

        if(titleTextBold){
            vBinding.tvTitle.setTypeface(Typeface.DEFAULT_BOLD)
        }
        a.recycle()
    }

    fun hideRightArrow(){
        vBinding.tvContent.setDrawableNUll()
    }

    fun setContent(text:String?){
        vBinding.tvContent.text = text
    }

    fun setTitle(text:String?){
        vBinding.tvTitle.text = text
    }
}