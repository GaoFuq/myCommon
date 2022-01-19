package com.gfq.common.system.sp.simple

/**
 *  2022/1/19 11:38
 * @auth gaofuq
 * @description
 */
internal class Test {
    internal fun test() {
        SPCache.name = null
        SPCache.name = ""
        SPCache.name = "name"
        SPCache.age = 20
        SPCache.address = null
        SPCache.address = "address"
        SPCache.isFirstInApp = true
    }
}