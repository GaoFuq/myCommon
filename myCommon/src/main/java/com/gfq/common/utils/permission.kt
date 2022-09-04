package com.gfq.common.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.gfq.common.system.toSystemSetting
import com.gfq.common.toast.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Fragment.hasPermission(permission: String): Boolean {
    if (this.context == null) return false
    return this.requireContext().hasPermission(permission)
}

//不可以 lazy 实例化 launcher
fun FragmentActivity.registerPermissionLauncher(callback: ActivityResultCallback<Boolean>): ActivityResultLauncher<String> {
    return registerForActivityResult(ActivityResultContracts.RequestPermission(), callback)
}

fun Fragment.registerPermissionLauncher(callback: ActivityResultCallback<Boolean>): ActivityResultLauncher<String> {
    return registerForActivityResult(ActivityResultContracts.RequestPermission(), callback)
}

fun FragmentActivity.registerPermissionLauncher(
    callback: () -> Unit,
    not: () -> Unit = {
        toast("请手动打开权限")
        lifecycleScope.launch {
            delay(1000)
            toSystemSetting(this@registerPermissionLauncher)
        }
    }
): ActivityResultLauncher<String> {
    return registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            callback()
        } else {
            not()
        }
    }
}

fun Fragment.registerPermissionLauncher(
    callback: () -> Unit,
    not: () -> Unit = {
        toast("请手动打开权限")
        lifecycleScope.launch {
            delay(1000)
            toSystemSetting(context)
        }
    }
): ActivityResultLauncher<String> {
    return registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            callback()
        } else {
            not()
        }
    }
}
