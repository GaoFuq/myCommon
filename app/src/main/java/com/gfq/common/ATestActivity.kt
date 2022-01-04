package com.gfq.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 *  2021/12/29 15:37
 * @auth gaofuq
 * @description
 */
internal class ATestActivity:AppCompatActivity() {
    val test by TestSP("key","default")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}