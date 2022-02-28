package com.gfq.common.net

import android.app.Dialog
import com.gfq.common.system.ActivityManager

/**
 *  2022/1/12 11:54
 * @auth gaofuq
 * @description
 * 继承 [GlobalDialog] ,不再显式的传递 context 参数
 */
open class GlobalDialog :Dialog(ActivityManager.getAllActivities().last())