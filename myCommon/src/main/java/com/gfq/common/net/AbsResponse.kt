package com.gfq.common.net
import com.gfq.common.net.simple.AbsResponseSimple

/**
 * 数据包装基类
 * 例子 @see [AbsResponseSimple]
 */
abstract class AbsResponse<T>{

    /**
     * 对应请求的结果 状态码
     */
    abstract fun responseCode():Int?

    /**
     * 对应请求的结果 消息
     */
    abstract fun responseMessage():String?


    /**
     * 对应请求的结果 成功返回的数据
     */
    abstract fun responseData():T?

    abstract fun isSuccess():Boolean /* return code == 200 */

    /**
     * 特殊情况特殊处理
     */
    open fun isSpecial():Boolean = false /* return code == 1 || code == 2 */


}