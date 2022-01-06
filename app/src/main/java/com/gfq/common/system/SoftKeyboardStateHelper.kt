package com.gfq.common.system

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.*


fun FragmentActivity.doOnSoftInputStateChange(
    activityRootView: View,
    isSoftKeyboardOpened: Boolean = false,
    onClose: () -> Unit = {},
    onOpen: (diffPx: Int) -> Unit = {},
) {
    SoftKeyboardStateHelper(
        activity = this,
        activityRootView = activityRootView,
        isSoftKeyboardOpened = isSoftKeyboardOpened,
        onClose = onClose,
        onOpen = onOpen
    )
}

class SoftKeyboardStateHelper(
    private val activity: FragmentActivity,
    private val activityRootView: View,
    var isSoftKeyboardOpened: Boolean = false,
    val onClose: () -> Unit = {},
    val onOpen: (diffPx: Int) -> Unit = {},
) : ViewTreeObserver.OnGlobalLayoutListener, LifecycleObserver {

    /**
     * Default value is zero (0)
     * @return last saved keyboard height in px
     */

    override fun onGlobalLayout() {
        val r = Rect()
        val screenH = activity.resources.displayMetrics.heightPixels
        //r will be populated with the coordinates of your view that area still visible.
        activityRootView.getWindowVisibleDisplayFrame(r)
        val heightDiff = activityRootView.rootView.height - (r.bottom - r.top)
        if (!isSoftKeyboardOpened && heightDiff > screenH / 3) { // if more than 100 pixels, its probably a keyboard...
            isSoftKeyboardOpened = true
            onOpen(heightDiff)
        } else if (isSoftKeyboardOpened && heightDiff < screenH / 3) {
            isSoftKeyboardOpened = false
            onClose()
        }
    }


    init {
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener(this)
        activity.lifecycle.addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onActFinish() {
        Log.d("SoftKeyboardStateHelper", "removeOnGlobalLayoutListener")
        activityRootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }
}