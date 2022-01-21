package com.gfq.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gfq.common.system.sp.SPDelegate

/**
 *  2021/12/29 15:37
 * @auth gaofuq
 * @description
 */
internal class TestSP<T>( default: T) : SPDelegate<T>("testSP", default)

internal class ATestActivity:AppCompatActivity() {
    var test by TestSP("default")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        test = ""
    }
}