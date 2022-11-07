package com.gfq.common.system

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.*
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment


/**
 *  2021/12/31 10:19
 * @auth gaofuq
 * @description
 */

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
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
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
    val resources = resources
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}
