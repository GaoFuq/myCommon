package com.gfq.common.system.sp.simple



/**
 *  2022/1/19 10:12
 * @auth gaofuq
 * @description
 *
 * @see [Test.test]
 * @see [User]
 * @see [UserSP]
 * @see [AnySP]
 * @see [NoClearSP]
 */
internal object SPCache {
    class Table {
        companion object {
            const val user = "userTable"
            const val notClearOnExitApp = "notClearOnExitAppTable"
        }
    }
    class Key {
        companion object {
            const val userInfo = "userInfo"
        }
    }

    var userInfo by UserSP()
    var userInfo2 by AnySP(User("name",1))
    var name by AnySP("")
    var age by AnySP(18)
    var isLike by AnySP(false)
    var address by StringSP()
    var isFirstInApp by NoClearSP(false)
}

