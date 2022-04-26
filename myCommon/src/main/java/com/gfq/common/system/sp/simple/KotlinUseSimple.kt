package com.gfq.common.system.sp.simple

import com.gfq.common.system.loge
import com.gfq.common.system.sp.SPData
import com.gfq.common.system.sp.SPTableDelegate

/**
 *  2022/4/26 10:16
 * @auth gaofuq
 * @description
 */

internal class KotlinUseSimple {

    //1.定义表名 和 要存的数据对应的key
    internal object UserTable {
        const val SP_TABLE_NAME = "UserTable"
        const val DATA_KEY_NAME = "selfUserInfo"
    }

    internal object AppConfigTable {
        const val SP_TABLE_NAME = "AppConfigTable"
        const val DATA_KEY_NAME = "appConfig"
    }

    //2.定义数据类
    internal data class UserBeanSimple(
        var name: String? = null, var age: Int? = null,
        override val spTableName: String = UserTable.SP_TABLE_NAME,
        override val dataKeyName: String = UserTable.DATA_KEY_NAME,
    ) : SPData

    //3.定义代理类，必须给默认值
    internal class UserSPTable : SPTableDelegate<UserBeanSimple>(default = UserBeanSimple())


    //4.自定义数据类型存取数据
    internal fun saveAndGetData() {
        //4.1 使用代理类
        val userSPTable by UserSPTable()
        //4.1 使用代理类 存
        userSPTable.run {
            name = "name"
            age = 18
            save()
        }
        //4.1 使用代理类 取
        val name = userSPTable.name
        val age = userSPTable.age

        //4.2 网络获取的数据存入
        val bean = getUserFromNet()
        bean?.save()

    }

    //5.基本数据类型存取数据
    internal fun saveAndGetData2() {
        //直接使用 SPTableDelegate ，spTableName = SPTableDelegate.defaultSPTableName
        //5.1 定义代理
        var isLogin by SPTableDelegate(false)
        //5.2 取
        if (isLogin) {
            //do something
        }
        //5.3 存
        isLogin = true


        var userId by SPTableDelegate(0)
        //取
        loge(userId.toString())
        //存
        userId = 111

        var password by SPTableDelegate("111")
        //存
        password = "123456"
        //取
        loge(password)

    }

    internal fun xx(){
        //也可以专门指定一张表来存相关数据
        //数据类型设置为 T ，可以存任意类型。
        //数据类型设置为指定类型 ，就只能存取指定的类型。
        class MySpTable<T>(default: T) : SPTableDelegate<T>(default, "MySpTableName")
        class BooleanSpTable(default: Boolean = false) : SPTableDelegate<Boolean>(default, "BooleanSpTable")
        //使用
        var id by MySpTable(0)
        var name by MySpTable("")
        var time by MySpTable(0L)
        var isBig by MySpTable(false)

        var isBoy by BooleanSpTable(false)
        var isGirl by BooleanSpTable(false)
        var isHappy by BooleanSpTable(false)
    }

    private fun getUserFromNet(): UserBeanSimple? {
        return null
    }
}