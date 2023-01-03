package com.gfq.common.zsimple

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.gfq.common.base.BaseBindingView
import com.gfq.common.base.BaseFragment
import com.gfq.common.system.loge
import com.gfq.common.utils.dp
import com.gfq.common.view.setDebounceClick
import com.mdwl.live.R
import com.mdwl.live.databinding.TitleBarBinding
import com.mdwl.live.vdialog.TwoActionTipDialog

/**
 *  2022/8/23 9:57
 * @auth gaofuq
 * @description
 */

     <RelativeLayout
         android:layout_width="match_parent"
         android:layout_height="@dimen/titleBarHeight">

         <FrameLayout
             android:id="@+id/back"
             android:layout_width="@dimen/titleBarHeight"
             android:layout_height="@dimen/titleBarHeight">

             <ImageView
                 android:id="@+id/ivBack"
                 android:layout_width="wrap_content"
                 android:layout_height="wrap_content"
                 android:layout_gravity="center"
                 android:src="@mipmap/btn_return"
                 />

         </FrameLayout>

         <TextView
             android:id="@+id/tvTitle"
             android:layout_width="wrap_content"
             android:layout_height="wrap_content"
             tools:text="TITLE"
             android:textColor="@color/colorTitleBar_TitleTextColor"
             android:textSize="@dimen/sp16"
             android:layout_centerInParent="true"
             android:textStyle="bold"
             />

         <FrameLayout
             android:id="@+id/rightContainer"
             android:layout_width="wrap_content"
             android:layout_height="wrap_content"
             android:layout_centerVertical="true"
             android:layout_alignParentEnd="true"/>

     </RelativeLayout>

    <declare-styleable name="TitleBarLayout">
        <attr format="string" name="title"/>
        <attr format="color" name="titleTextColor"/>
        <attr format="color" name="backTint"/>
        <attr format="color" name="backgroundColor"/>
        <attr format="string" name="rightActionText"/>
        <attr format="color" name="rightActionTextColor"/>
        <attr format="reference" name="rightActionImage"/>
        <attr format="dimension" name="rightActionImageMarginEnd"/>
    </declare-styleable>

class TitleBarLayout(context: Context, attrs: AttributeSet?) :
    BaseBindingView<TitleBarBinding>(context, attrs) {
    override fun layoutResId(): Int = R.layout.title_bar

    var rightTextView: TextView? = null
    var rightImageView: ImageView? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TitleBarLayout)
        val defTitleColor = context.getColor(R.color.colorTitleBar_TitleTextColor)
        val defBackgroundColor = context.getColor(R.color.colorTitleBar_BackgroundColor)
        val title = a.getString(R.styleable.TitleBarLayout_title)
        val titleTextColor = a.getColor(R.styleable.TitleBarLayout_titleTextColor, defTitleColor)
        val backTint = a.getColor(R.styleable.TitleBarLayout_backTint, defTitleColor)
        val backgroundColor =
            a.getColor(R.styleable.TitleBarLayout_backgroundColor, defBackgroundColor)
        vBinding.tvTitle.text = title ?: "TITLE"
        vBinding.tvTitle.setTextColor(titleTextColor)
        vBinding.ivBack.imageTintList = ColorStateList.valueOf(backTint)
        vBinding.root.setBackgroundColor(backgroundColor)

        val rightActionText = a.getString(R.styleable.TitleBarLayout_rightActionText)
        val rightActionTextColor = a.getColor(R.styleable.TitleBarLayout_rightActionTextColor,context.getColor(R.color.theme))
        val rightActionImage = a.getResourceId(R.styleable.TitleBarLayout_rightActionImage, 0)
        val rightActionImageMarginEnd = a.getDimension(R.styleable.TitleBarLayout_rightActionImageMarginEnd, 0f)


        if (rightActionText != null) {
            initDefRightTextView(rightActionText)
            rightTextView?.let { addView2Right(it) }
            rightTextView?.setTextColor(rightActionTextColor)
        } else if (rightActionImage != 0) {
            initDefRightImageView(rightActionImage,rightActionImageMarginEnd.toInt())
            rightImageView?.let { addView2Right(it) }
        }
        a.recycle()

        vBinding.back.setOnClickListener {
            if (context is Activity) {
                context.finish()
            }
        }
    }

    private fun initDefRightTextView(rightActionText: String) {
        rightTextView = TextView(context).apply {
            text = rightActionText
            setTextColor(context.getColor(R.color.theme))
            textSize = 16f
            gravity = Gravity.CENTER
            layoutParams = FrameLayout.LayoutParams(-2, -1)
            setPadding(dp(R.dimen.dp16), 0, dp(R.dimen.dp16), 0)
        }
    }

    private fun initDefRightImageView(rightActionImage: Int, rightActionImageMarginEnd: Int) {
        rightImageView = ImageView(context).apply {
            setImageResource(rightActionImage)
            layoutParams = FrameLayout.LayoutParams(-2, -1).apply {
                marginEnd = rightActionImageMarginEnd
            }
        }
    }

    fun addView2Right(view: View) {
        vBinding.rightContainer.addView(view)
    }


    fun addSelectTopicConfirmView(action: () -> Unit) {
        addView2Right(
            TextView(context).apply {
                text = "完成"
                setTextColor(context.getColor(R.color.theme))
                textSize = 16f
                gravity = Gravity.CENTER
                layoutParams = FrameLayout.LayoutParams(-2, -1)
                setPadding(dp(R.dimen.dp16), 0, dp(R.dimen.dp16), 0)
                setDebounceClick {
                    action()
                }
            }
        )
    }

    fun addClearDataView(act: FragmentActivity, action: () -> Unit) {
        addView2Right(
            TextView(context).apply {
                text = "清空"
                setTextColor(context.getColor(R.color.colorTitleTextColor))
                textSize = 16f
                gravity = Gravity.CENTER
                layoutParams = FrameLayout.LayoutParams(-2, -1)
                setPadding(dp(R.dimen.dp16), 0, dp(R.dimen.dp16), 0)
                setDebounceClick {
                    TwoActionTipDialog().ensureDeleteAllLiveRecords(act).doAction = action
                }
            }
        )
    }


    fun addMoreActionView(action: (anchor: View) -> Unit) {
        addView2Right(
            ImageView(context).apply {
                setImageResource(R.mipmap.btn_topic_details_more)
                layoutParams = FrameLayout.LayoutParams(-2, -1).apply {
                    marginEnd = dp(R.dimen.dp4)
                }
                setDebounceClick {
                    action(this)
                }
            }
        )
    }

    fun addMeAttentionEdit(action: () -> Unit) {
        addView2Right(
            TextView(context).apply {
                text = "编辑"
                setTextColor(context.getColor(R.color.theme))
                textSize = 16f
                gravity = Gravity.CENTER
                layoutParams = FrameLayout.LayoutParams(-2, -1)
                setPadding(dp(R.dimen.dp16), 0, dp(R.dimen.dp16), 0)
                setDebounceClick {
                    action()
                }
            }
        )
    }
}