package com.gfq.common.system.sp

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.alibaba.fastjson.JSON
import com.gfq.common.system.ActivityManager
import java.io.File

/**
 * 管理包里面所有的[SharedPreferences]数据，类比数据库中的表 。
 * @see [SPDelegate]
 *
 * 注意：
 * 1.属性委托[SPDelegate]声明的变量，每次调用获取到的数据都是sp表中最新的值。
 * 2.实现[SPData]的类调用[SPData.save]方法，会直接把sp中对应的值覆盖掉,所以1所述成立。
 *
 * API
 *  @see [SP.get]
 *  @see [SP.getAllSPFormNames]
 *  @see [SP.getAllSPFormData]
 *  @see [SP.getSPFormDataByName]
 *  @see [SP.delete]
 *  @see [SP.deleteAll]
 *  @see [SP.clear]
 *  @see [SP.clearAll]
 *
 */
object SP {


    /**
     * sp表单数量
     */
    internal const val spFormCount = "spFormCount"

    /**
     * sp表单索引
     */
    internal const val formIndex_ = "form_index_"

    /**
     * 储存所有sp表单的sp表单的名称后缀
     */
    internal const val nameSuffix = ".sp_all_info666"

    /**
     * 用于存储自定义的对象
     */
    internal const val dataClassName = "sp_g_f_q_dataClassName"
    internal const val dataKeyName = "sp_g_f_q_dataKeyName"
    internal const val dataValue = "sp_g_f_q_dataValue"

    /**
     * 获取储存了包里所有sp表单信息的sp
     *
     * 储存包里面所有的sp表单 数量 名称 和 名称对应的编号
     * key: [spFormCount]       value: sp表单数量
     * key: [formIndex_]+index  value: sp表单名称
     */
    internal fun getSaveAllSp() = get(ActivityManager.application.packageName + nameSuffix)

    /**
     * 通过sp名称获取[SharedPreferences]实例
     */
    fun get(name: String) = ActivityManager.application.getSharedPreferences(name, 0)!!

    @RequiresApi(Build.VERSION_CODES.N)
    fun deleteSP(name: String) = ActivityManager.application.deleteSharedPreferences(name)

    /**
     * 保存/更新
     */
    fun save(spFormName: String, dataKeyName: String, any: Any?) {
        get(spFormName).put(dataKeyName, any)
    }

    /**
     * 获取所有SP表单名称的集合
     */
    fun getAllSPFormNames(): List<String> {
        val spCount = getSaveAllSp().getInt(spFormCount, 0)
        val list = mutableListOf<String>()
        for (index in 0 until spCount) {
            val existName = getSaveAllSp().getString("${formIndex_}$index", "")
            existName?.let { list.add(it) }
        }
        return list
    }

    /**
     * 获取SP表单的名字获取数据Map
     * @return Map<String, Any?>
     * key：sp表单名称
     * value：该sp表单的所有数据json字符串
     */
    fun getSPFormDataByName(spFormName: String): Map<String, Any?> {
        val map = mutableMapOf<String, Map<String, Any?>>()
        val all = get(spFormName).all.toMutableMap()
        all.forEach { (key, value) ->
            if (value is String) {
                if (value.contains(dataClassName)
                    && value.contains(dataKeyName)
                    && value.contains(dataValue)
                ) {
                    val spDataWrapper = JSON.parseObject(value, SPDataWrapper::class.java)
                    all[key] = spDataWrapper.sp_g_f_q_dataValue
                }
            }
        }
        map[spFormName] = all
        return map
    }

    /**
     * 获取所有SP表单的所有数据Map
     * @return Map<String, Any?>
     * key：sp表单名称
     * value：所有sp表单的所有数据的json字符串
     */
    fun getAllSPFormData(): Map<String, Any?> {
        val list = getAllSPFormNames()
        val map = mutableMapOf<String, Map<String, Any?>>()
        list.forEach { name ->
            val all = get(name).all.toMutableMap()
            all.forEach { (key, value) ->
                if (value is String) {
                    if (value.contains(dataClassName)
                        && value.contains(dataKeyName)
                        && value.contains(dataValue)
                    ) {
                        val spDataWrapper = JSON.parseObject(value, SPDataWrapper::class.java)
                        all[key] = spDataWrapper.sp_g_f_q_dataValue
                    }
                }

            }
            map[name] = all
        }
        return map
    }

    /**
     * 清空指定名称的 sp 表单里面的数据
     */
    fun clear(spFormName: String) {
        require(spFormName.isNotEmpty()) { KEY_IS_EMPTY_EXCEPTION }
        get(spFormName).edit().clear().apply()
    }


    /**
     * 清空所有 sp 表单里面的数据
     */
    fun clearAll() {
        getAllSPFormNames().forEach { spFormName ->
            get(spFormName).edit().clear().apply()
        }
    }

    /**
     * 删除指定名称的 sp 表单
     */
    fun delete(spFormName: String) {
        require(spFormName.isNotEmpty()) { KEY_IS_EMPTY_EXCEPTION }
        val spFile =
            File("data/data/${ActivityManager.application.packageName}/shared_prefs", "$spFormName.xml")
        if (spFile.exists()) {
            val deleteSuccess = spFile.delete()
            if (deleteSuccess) {
                val list = getAllSPFormNames().filter { it != spFormName }
                val count = getSaveAllSp().getInt(spFormCount, 0)
                if (count > 0) {
                    if (list.size == count - 1) {
                        val editor = getSaveAllSp().edit()
                        editor.clear()
                        editor.putInt(spFormCount, count - 1)
                        for (index in 0 until count - 1) {
                            editor.putString("${formIndex_}${index}", list[index])
                        }
                        editor.apply()
                    }
                }
            }
        }
    }

    /**
     * 删除所有 sp 表单
     */
    fun deleteAll() {
        getAllSPFormNames().forEach { spFormName ->
            delete(spFormName)
        }
    }

}