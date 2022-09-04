package com.gfq.common.system.sp

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.alibaba.fastjson.JSON
import com.gfq.common.system.ActivityManager
import java.io.File
import com.gfq.common.utils.FileUtil

/**
 *
 * 管理包里面所有的[SharedPreferences]数据，类比数据库中的表 。
 * @see [SPTableDelegate]
 *
 * 注意：
 * 1.属性委托[SPTableDelegate]声明的变量，每次调用获取到的数据都是sp表中最新的值。
 * 2.实现[SPData]的数据类，调用[SPData.save]方法，会直接把sp中对应的值覆盖掉,所以1所述成立。
 * 3.基本数据类型的key和声明的变量名称相同。
 * 4.实现[SPData]的自定义数据类型的key是[SPData.dataKeyName]
 * 4.实现[SPData]的自定义数据类型的表名是[SPData.spTableName]
 *
 * API
 *  @see [SP.get]
 *  @see [SP.getAllSPTableNames]
 *  @see [SP.getAllSPTableData]
 *  @see [SP.getSPTableDataByName]
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
    internal const val spTableCount = "spTableCount"

    /**
     * sp表单索引
     */
    internal const val tableIndex_ = "table_index_"

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
     * key: [spTableCount]       value: sp表单数量
     * key: [tableIndex_]+index  value: sp表单名称
     */
    internal fun getSaveAllSp() = get(ActivityManager.application.packageName + nameSuffix)

    /**
     * 通过名称获取[SharedPreferences]实例。
     */
    @JvmStatic
    fun get(spTableName: String) = ActivityManager.application.getSharedPreferences(spTableName, 0)!!

    @RequiresApi(Build.VERSION_CODES.N)
    @JvmStatic
    fun deleteSP(spTableName: String) = ActivityManager.application.deleteSharedPreferences(spTableName)

    /**
     * 如果[dataKeyName]存在，则返回对应类型的数据，如果转换数据类型失败，则返回 default。
     * 如果[dataKeyName]不存在，则返回 default。
     */
    @JvmStatic
    fun <T> getOrDefault(spTableName:String,dataKeyName: String, default: T): T {
        return get(spTableName).getOrDefault(dataKeyName,default)
    }

    /**
     * 保存/更新
     */
    @JvmStatic
    fun save(spTableName: String, dataKeyName: String, any: Any?) {
        get(spTableName).put(dataKeyName, any)
    }

    /**
     * 获取所有SP表单名称的集合
     */
    @JvmStatic
    fun getAllSPTableNames(): List<String> {
        val spTableCount = getSaveAllSp().getInt(spTableCount, 0)
        val list = mutableListOf<String>()
        for (index in 0 until spTableCount) {
            val existName = getSaveAllSp().getString("${tableIndex_}$index", "")
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
    @JvmStatic
    fun getSPTableDataByName(spTableName: String): Map<String, Any?> {
        val map = mutableMapOf<String, Map<String, Any?>>()
        val all = get(spTableName).all.toMutableMap()
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
        map[spTableName] = all
        return map
    }

    /**
     * 获取所有SP表单的所有数据Map
     * @return Map<String, Any?>
     * key：sp表单名称
     * value：所有sp表单的所有数据的json字符串
     */
    @JvmStatic
    fun getAllSPTableData(): Map<String, Any?> {
        val list = getAllSPTableNames()
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
     * 获取指定SP文件的大小
     */
    @JvmStatic
    fun getCacheSize(spTableName: String):String{
        require(spTableName.isNotEmpty()) { KEY_IS_EMPTY_EXCEPTION }
        val spFile = File("data/data/${ActivityManager.application.packageName}/shared_prefs", "$spTableName.xml")
        return FileUtil.getFormatSize(FileUtil.getFolderSize(spFile).toDouble())
    }

    /**
     * 获取全部SP文件的大小
     */
    @JvmStatic
    fun getAllSPCacheSize():String{
        var totalSize = 0L
        getAllSPTableNames().forEach {
            val spFile = File("data/data/${ActivityManager.application.packageName}/shared_prefs", "$it.xml")
            totalSize+= FileUtil.getFolderSize(spFile)
        }
        return FileUtil.getFormatSize(totalSize.toDouble())
    }

    /**
     * 清空指定名称的 sp 表单里面的数据
     */
    @JvmStatic
    fun clear(spTableName: String) {
        require(spTableName.isNotEmpty()) { KEY_IS_EMPTY_EXCEPTION }
        get(spTableName).edit().clear().apply()
    }


    /**
     * 清空所有 sp 表单里面的数据
     */
    @JvmStatic
    fun clearAll() {
        getAllSPTableNames().forEach { spTableName ->
            get(spTableName).edit().clear().apply()
        }
    }

    /**
     * 清空所有 sp 表单里面的数据
     */
    @SuppressLint("ApplySharedPref")
    @JvmStatic
    fun clearAllImmediate() {
        getAllSPTableNames().forEach { spTableName ->
            get(spTableName).edit().clear().commit()
        }
    }

    /**
     * 删除指定名称的 sp 表单
     */
    @JvmStatic
    fun delete(spTableName: String) {
        require(spTableName.isNotEmpty()) { KEY_IS_EMPTY_EXCEPTION }
        val spFile =
            File("data/data/${ActivityManager.application.packageName}/shared_prefs", "$spTableName.xml")
        if (spFile.exists()) {
            val deleteSuccess = spFile.delete()
            if (deleteSuccess) {
                val list = getAllSPTableNames().filter { it != spTableName }
                val count = getSaveAllSp().getInt(spTableCount, 0)
                if (count > 0) {
                    if (list.size == count - 1) {
                        val editor = getSaveAllSp().edit()
                        editor.clear()
                        editor.putInt(spTableCount, count - 1)
                        for (index in 0 until count - 1) {
                            editor.putString("${tableIndex_}${index}", list[index])
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
    @JvmStatic
    fun deleteAll() {
        getAllSPTableNames().forEach { spTableName ->
            delete(spTableName)
        }
    }

}