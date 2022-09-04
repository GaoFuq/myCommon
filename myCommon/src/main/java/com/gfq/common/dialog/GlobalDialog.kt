package com.gfq.common.dialog

import android.app.Dialog
import android.content.Context
import com.gfq.common.system.ActivityManager

/**
 *  2022/1/12 11:54
 * @auth gaofuq
 * @description
 * 继承 [GlobalDialog] ,不再显式的传递 context 参数
 * @param style [R.style.xxx]
 */
open class GlobalDialog(
    val mContext: Context = ActivityManager.getAllActivities().last(),
    style: Int = 0,
) : Dialog(mContext, style)