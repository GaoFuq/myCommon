package com.gfq.common.view.simple

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.isGone
import com.gfq.common.base.BaseBindingView
import com.gfq.common.view.setDebounceClick

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintVertical_bias="0.35"
            android:gravity="center_horizontal"
            app:layout_constraintEnd_toEndOf="parent">

            <ImageView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:src="@mipmap/icon_dummy_status"/>

            <TextView
                android:id="@+id/tvEmptyTip"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                tools:text="空状态描述"
                android:textSize="@dimen/sp14"
                android:layout_marginTop="@dimen/dp13"
                android:textColor="@color/colorBDBDBD"/>


            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:tag="@string/stateViewRefreshTag"
                android:gravity="center"
                android:layout_marginBottom="@dimen/dp50"
                android:paddingStart="@dimen/dp25"
                android:paddingEnd="@dimen/dp25"
                android:layout_marginTop="@dimen/dp16"
                android:background="@drawable/bg_round_theme"
                android:orientation="horizontal">

                <ImageView
                    android:id="@+id/ivActionIcon"
                    android:layout_marginEnd="@dimen/dp4"
                    tools:src="@mipmap/icon_search"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"/>

                <TextView
                    android:id="@+id/tvAction"
                    android:layout_width="wrap_content"
                    android:layout_height="@dimen/dp45"
                    android:gravity="center"
                    tools:text="按钮文本"
                    android:textColor="@color/white"
                    android:textSize="@dimen/sp16"
                    />
            </LinearLayout>



        </LinearLayout>


    </androidx.constraintlayout.widget.ConstraintLayout>

class EmptyView @JvmOverloads constructor(context: Context, attrs: AttributeSet?=null) :
    BaseBindingView<StateEmptyViewBinding>(context, attrs) {

    override fun layoutResId(): Int = R.layout.state_empty_view

    fun initView(emptyTip:String?,actionIcon:Int,actionText:String?,action: (() -> Unit)? = null){
        vBinding.run {
            tvEmptyTip.text = emptyTip
            ivActionIcon.setImageResource(actionIcon)
            tvAction.isGone = actionText == null
            tvAction.text = actionText
            tvAction.setDebounceClick {
                action?.invoke()
            }
        }
    }
}