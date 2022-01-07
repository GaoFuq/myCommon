package com.gfq.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gfq.common.system.SPManager

/**
 *  2021/12/29 15:37
 * @auth gaofuq
 * @description
 */
internal class TestSP<T>(key: String, default: T) : SPManager.SPDelegate<T>("testSP", key, default)

internal class ATestActivity:AppCompatActivity() {
    var test by TestSP("key","default")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        test = ""
        SPManager.clear("")
        SPManager.clearAll()
    }
}