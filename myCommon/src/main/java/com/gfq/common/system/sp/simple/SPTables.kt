package com.gfq.common.system.sp.simple

import com.gfq.common.system.sp.SPData
import com.gfq.common.system.sp.SPDelegate

/**
 * 实现[SPData]的自定义数据类型的key是[SPData.dataKeyName]。
 * 实现[SPData]的自定义数据类型的表名是[SPData.spTableName]。
 */
internal class UserSP : SPDelegate<User>()

/**
 * 基本数据类型的key和声明的变量名称相同。
 * 这里的泛型既可以是基本数据类型也可以是自定义数据类型。
 */
internal class AnySP<T>(def: T?) : SPDelegate<T>(default = def)

/**
 * 指定表名称
 */
internal class StringSP : SPDelegate<String>("stringTable", "")
/**
 * 指定表名称
 */
internal class NoClearSP<T>(def: T?) : SPDelegate<T?>(SPCache.Table.notClearOnExitApp, def)