package com.gfq.common.system

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.TypedArray
import android.os.Build
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController

/**
 *  2022/10/10 14:35
 * @auth gaofuq
 * @description android 版本适配
 */
object AdaptsHelper {
    private const val TAG = "AdaptsHelper"
    //隐藏手势小横条
    //适配 一加 等底部显示手势小横条的情况
    //手势小横条已经是隐藏的情况不受影响
    @JvmStatic
    fun hideGestureSmallHorizontalBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window?.insetsController?.let {
                it.hide(WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }


    @JvmStatic
    fun showGestureSmallHorizontalBar(activity: Activity) {
        //适配 一加 等底部 手势小横条
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window?.insetsController?.show(WindowInsets.Type.navigationBars())
        }
    }


    /**
     * 用于适配26 android O (8.0)
     * 解决问题：Only fullscreen activities can request orientation
     * https://blog.csdn.net/u013334392/article/details/100043460
     */
    @JvmStatic
    fun fixOrientationInAndroidO(activity: Activity) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            if(isTranslucentOrFloating(activity)){
                val result = fixOrientation(activity)
                Log.d(TAG,"fixOrientationInAndroidO result = $result")
            }
        }
    }

    /**
     * 获取当前Activity Theme是不是透明的
     */
    @JvmStatic
    private fun isTranslucentOrFloating(activity: Activity): Boolean {
        var isTranslucentOrFloating = false
        try {
            val styleableRes = Class.forName("com.android.internal.R\$styleable").getField("Window")
                .get(null) as IntArray
            val ta: TypedArray = activity.obtainStyledAttributes(styleableRes);
            val m = ActivityInfo::class.java.getMethod("isTranslucentOrFloating",
                TypedArray::class.java)
            m.setAccessible(true)
            isTranslucentOrFloating = m.invoke(null, ta) as Boolean
            m.setAccessible(false);
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isTranslucentOrFloating;
    }


    @JvmStatic
    private fun fixOrientation(activity: Activity): Boolean {
        try {
            val field = Activity::class.java.getDeclaredField("mActivityInfo");
            field.setAccessible(true)
            val o = field.get(activity) as ActivityInfo
            o.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            field.setAccessible(false)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}