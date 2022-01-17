package com.gfq.common.system.sp

/**
 *  2022/1/17 13:32
 * @auth gaofuq
 * @description
 * 自定义的数据类需要实现 SPData
 *
 * 示例：
 *
 data class User(var name: String) : SPData {
    override val spFormName: String
        get() = "userInfo"
    override val dataKeyName: String
        get() = "user张三"
}

 */
interface SPData {
    val spFormName: String
    val dataKeyName: String
    fun save() {
        SP.save(spFormName = spFormName, dataKeyName = dataKeyName, this)
    }
}