package com.gfq.common.helper

import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.gfq.common.view.styleBold
import com.gfq.common.view.styleNotBold
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 *  2022/5/9 11:44
 * @auth gaofuq
 * @description
 *
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">

<!--    TabLayout 自定义 indicator -->
<!--    边距：item -> left top bottom right-->
<!--    宽高：item -> shape -> size -> width height-->
<!--    要设置渐变色，需要在xml中设置 TabLayout app:tabIndicatorColor="@android:color/transparent"-->
<item android:gravity="center"
android:bottom="3dp"
>
<shape>

<size
android:width="@dimen/dp20"
android:height="@dimen/dp4" />

<corners android:radius="2dp" />

<gradient android:startColor="#FF00B7EC" android:endColor="#FF2DDAC2"/>

</shape>
</item>

</layer-list>

 */
class PagerAndTabLayoutHelper(
    lifecycleOwner: LifecycleOwner,
    private val pager: ViewPager2,
    tabLayout: TabLayout,
    adapter: FragmentStateAdapter,
    tabTitles: List<String>,
    tabTextSize: Float = 14f,
    tabTextColor: Int = Color.parseColor("#333333"),
    tabSelectedTextSize: Float = 18f,
    tabSelectedTextColor: Int = Color.parseColor("#999999"),
    isTabSelectedBold: Boolean = true,
    private val callback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            //可以来设置选中时tab的大小
            val tabCount: Int = tabLayout.tabCount
            for (i in 0 until tabCount) {
                val tab = tabLayout.getTabAt(i)
                val tabView = tab?.customView as? TextView
                if (tab?.position == position) {
                    tabView?.textSize = tabSelectedTextSize
                    if (isTabSelectedBold) {
                        tabView?.styleBold()
                    }
                } else {
                    tabView?.textSize = tabTextSize
                    tabView?.styleNotBold()
                }
            }
        }
    }
) : LifecycleObserver {


    private val mediator = TabLayoutMediator(tabLayout, pager) { tab, position ->
        //这里可以自定义TabView
        val tabView = TextView(tabLayout.context).apply {
            gravity = Gravity.CENTER
            layoutParams = ViewGroup.MarginLayoutParams(-2, -2)
        }

        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(R.attr.state_selected)
        states[1] = intArrayOf()


        val colors = intArrayOf(tabSelectedTextColor, tabTextColor)
        val colorStateList = ColorStateList(states, colors)
        tabView.text = tabTitles[position]
        tabView.textSize = tabTextSize
        tabView.setTextColor(colorStateList)

        tab.customView = tabView
    }


    init {
        lifecycleOwner.lifecycle.addObserver(this)
        pager.adapter = adapter
        pager.registerOnPageChangeCallback(callback)
        mediator.attach()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        pager.unregisterOnPageChangeCallback(callback)
        mediator.detach()
    }

}