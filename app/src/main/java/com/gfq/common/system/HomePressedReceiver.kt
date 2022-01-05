package com.gfq.common.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 *  2022/1/5 16:27
 * @auth gaofuq
 * @description

有导航按键：
    短按：点击home键触发；
    长按：点击菜单键触发；（触发后显示最近任务列表）

手势导航：
    短按：不触发；
    长按：上滑app触发；（触发后回到桌面或者显示最近任务列表）

    override fun onResume() {
        super.onResume()
        registerHomePressedReceiver(
            onHomePressed = {
                Log.d("xx", "onHomePressed")
            },
            onHomeLongPressed = {
                Log.d("xx", "onHomeLongPressed")
            }
        )
    }

    override fun onPause() {
        super.onPause()
        unRegisterHomePressedReceiver()
    }

 */

class HomePressedReceiver(
    var onHomePressed: (() -> Unit)?=null,
    var onHomeLongPressed: (() -> Unit)?=null
) : BroadcastReceiver() {
    private val SYSTEM_DIALOG_REASON_KEY = "reason"
    private val SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions"
    private val SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps"
    private val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"
    private val TAG = "HomePressedReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
            val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
            if (reason != null) {
                Log.d(TAG, "action:$action,reason:$reason")
                if (reason == SYSTEM_DIALOG_REASON_HOME_KEY) {
                    Log.d(TAG, "短按home键")
                    onHomePressed?.invoke()
                } else if (reason == SYSTEM_DIALOG_REASON_RECENT_APPS) {
                    Log.d(TAG, "长按home键")
                    onHomeLongPressed?.invoke()
                }
            }
        }
    }
}