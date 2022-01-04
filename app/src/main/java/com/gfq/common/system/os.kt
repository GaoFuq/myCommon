package com.gfq.common.system

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import java.lang.Exception

/**
 *  2021/12/31 10:19
 * @auth gaofuq
 * @description
 */

//需要获得READ_PHONE_STATE权限，>=6.0，默认返回null
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
fun getAndroidId(context: Context): String {
    try {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
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
