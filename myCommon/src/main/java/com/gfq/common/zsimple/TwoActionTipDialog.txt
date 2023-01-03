package com.gfq.common.zsimple

import android.annotation.SuppressLint
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.gfq.common.base.BaseDialogFragment
import com.gfq.common.view.setDebounceClick

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:gravity="center_horizontal"
        android:orientation="vertical"
        android:background="@drawable/bg_tip_dialog"
        tools:background="@color/color999">

        <com.gfq.common.view.TextViewNoFontPadding
            android:id="@+id/tvTip"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/dp48"
            android:layout_marginBottom="@dimen/dp42"
            android:gravity="center_horizontal"
            android:textColor="@color/colorTitleTextColor"
            android:textSize="@dimen/sp14"
            tools:text="余额不足，是否跳转充值页" />

        <View
            android:layout_width="match_parent"
            android:layout_height="@dimen/dp0_5"
            android:background="@color/colorLine"
            tools:background="@color/white" />


        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="@dimen/dp48"
            android:gravity="center_vertical"
            android:orientation="horizontal">

            <TextView
                android:id="@+id/tvActionLeft"
                android:layout_width="0dp"
                android:layout_height="match_parent"
                android:layout_weight="1"
                android:gravity="center"
                android:text="取消"
                android:textColor="@color/colorTitleTextColor"
                android:textSize="@dimen/sp16" />

            <View
                android:layout_width="@dimen/dp0_5"
                android:layout_height="match_parent"
                android:background="@color/colorLine"
                tools:background="@color/white" />

            <TextView
                android:id="@+id/tvActionRight"
                android:layout_width="0dp"
                android:layout_height="match_parent"
                android:layout_weight="1"
                android:gravity="center"
                android:text="确认"
                android:textColor="@color/theme"
                android:textSize="@dimen/sp16" />

        </LinearLayout>
    </LinearLayout>


@SuppressLint("SetTextI18n")
class TwoActionTipDialog :
    BaseDialogFragment<DialogTipTwoActionBinding>(R.layout.dialog_tip_two_action) {
    var doAction: (() -> Unit)? = null

    override fun initView() {
        dialogBinding.tvActionLeft.setDebounceClick { dismiss() }
        dialogBinding.tvActionRight.setDebounceClick {
            dismiss()
            doAction?.invoke()
        }
    }

    override fun setWindowLayoutParams(param: WindowManager.LayoutParams?) {
    }

    fun ensureDeleteAllSearchHistory(f:Fragment) :TwoActionTipDialog {
        doOnStart = {
            dialogBinding.tvTip.text = "是否确认清空全部搜索记录？"
        }
        show(f)
        return this
    }

    fun ensureDeleteAllLiveRecords(f: FragmentActivity) :TwoActionTipDialog {
        doOnStart = {
            dialogBinding.tvTip.text = "是否确认清空全部观看记录？"
        }
        show(f)
        return this
    }

    fun ensureDeleteDynamic(f: FragmentActivity) :TwoActionTipDialog {
        doOnStart = {
            dialogBinding.tvTip.text = "是否确认删除动态？"
        }
        show(f)
        return this
    }

    fun ensureDeleteAllBill(f: FragmentActivity) :TwoActionTipDialog {
        doOnStart = {
            dialogBinding.tvTip.text = "是否确认删除所有账单记录？"
        }
        show(f)
        return this
    }
}

