package com.gfq.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gfq.common.system.ActivityManager
import com.gfq.common.system.SharedPreferencesDelegate

/**
 *  2021/12/29 15:37
 * @auth gaofuq
 * @description
 */
class TestSP<T>(key: String, default: T) : SharedPreferencesDelegate<T>(ActivityManager.application, "testSP", key, default)

internal class ATestActivity:AppCompatActivity() {
    val test by TestSP("key","default")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}