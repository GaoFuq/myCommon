package com.gfq.common.system.sp.simple

import com.gfq.common.system.sp.SPData

/**
 *  2022/1/19 10:16
 * @auth gaofuq
 * @description
 */
internal data class User(
    var name: String?,
    var age: Int?,
    override val spTableName: String = SPCache.Table.user,
    override val dataKeyName: String = SPCache.Key.userInfo,
) : SPData