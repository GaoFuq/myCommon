package com.gfq.common.system

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Rect
import android.os.*
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.luck.picture.lib.tools.ScreenUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import java.lang.Exception

/**
 *  2021/12/31 10:19
 * @auth gaofuq
 * @description
 *
 * 在 Application onCreate 中调用，初始化 Logger
Logger.addLogAdapter(
AndroidLogAdapter(
PrettyFormatStrategy.newBuilder()
.showThreadInfo(false)
.build()
)
)

 */

fun dpF(n: Number?): Float {
    if (n == null) return 0f
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        n.toFloat(),
        Resources.getSystem().displayMetrics
    )
}

fun dp(n: Number?): Int = dpF(n).toInt()


//需要获得READ_PHONE_STATE权限，>=6.0，默认返回null
@RequiresPermission("android.permission.READ_PRIVILEGED_PHONE_STATE")
fun getIMEI(context: Context): String? {
    try {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return tm.deviceId
        }
    } catch (ex: Exception) {
        return ""
    }
    return ""
}

/**
 * 获得设备的AndroidId
 *
 * @param context 上下文
 * @return 设备的AndroidId
 */
@SuppressLint("HardwareIds")
fun Context.getAndroidId(): String {
    try {
        val id = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        if (id == null || "9774d56d682e549c" == id) {
            return ""
        } else {
            return id
        }

    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return ""
}

/**
 * 获得设备序列号（如：WTK7N16923005607）, 个别设备无法获取
 *
 * @return 设备序列号
 */
@SuppressLint("HardwareIds")
fun getSERIAL(): String? {
    try {
        return Build.SERIAL
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return ""
}

fun Context.showSoftInput() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}


fun View.showSoftInput() {
    showSoftInput(context, this, 0)
}

private fun showSoftInput(context: Context, view: View, flags: Int) {
    val imm =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
    view.isFocusable = true
    view.isFocusableInTouchMode = true
    view.requestFocus()
    imm.showSoftInput(view, flags, object : ResultReceiver(Handler()) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN
                || resultCode == InputMethodManager.RESULT_HIDDEN
            ) {
                imm.toggleSoftInput(0, 0)
            }
        }
    })
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun Activity.hideSoftInput() {
    hideSoftInput(this.window)
}

fun Fragment.hideSoftInput() {
    this.activity?.hideSoftInput()
}

fun Fragment.showSoftInput() {
    this.activity?.showSoftInput()
}

fun hideSoftInput(window: Window) {
    var view = window.currentFocus
    if (view == null) {
        val decorView = window.decorView
        val focusView = decorView.findViewWithTag<View>("keyboardTagView")
        if (focusView == null) {
            view = EditText(window.context)
            view.setTag("keyboardTagView")
            (decorView as ViewGroup).addView(view, 0, 0)
        } else {
            view = focusView
        }
        view.requestFocus()
    }
    view.hideSoftInput()
}

fun View.hideSoftInput() {
    val imm =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}


fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return if (result == 0) dp(25) else result
}

