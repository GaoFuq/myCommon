package com.gfq.common.system

/**
 *  2021/12/29 15:33
 * @auth gaofuq
 * @description
 */

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * SharedPreferences属性委托
 * 支持基本数据类型：String、Boolean、Int、Long、Float
 *
 * 示例：var xxx by SharedPreferencesDelegate()
 *
 * @property context
 * @property name  sharedPreferences对于的文件名字
 * @property key                        存储的key
 * @property default                    获取失败时，返回的默认值
 *
 * 便捷使用：
 *   class TestSP<T>(key: String, default: T) : SharedPreferencesDelegate<T>(ApplicationHolder.application, "testSP", key, default)
 *   val test by TestSP("key","default")
 */


object SPManager {

    private val spFormMap = mutableMapOf<String, SP>()

    private fun addSharedPreferences(name: String, sp: SP) {
        spFormMap[name] = sp
    }


    open class SPDelegate<T>(
        private val name: String,
        private val key: String,
        private val default: T? = null
    ) : ReadWriteProperty<Any?, T?> {

        private val sp = SP(ActivityManager.application, name)

        init {
            addSharedPreferences(name, sp)
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return sp.get(key, default)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            sp.put(key, value)
        }

    }


    fun clear(name: String) {
        spFormMap[name]?.clear()
        spFormMap.remove(name)
    }

    fun clearAll() {
        spFormMap.keys.forEach {
            clear(name = it)
        }
    }


    internal class SP(
        context: Context,
        sharedPreferencesFileName: String = context.packageName
    ) {
        val prefs: SharedPreferences = context.applicationContext.getSharedPreferences(
            sharedPreferencesFileName,
            Context.MODE_PRIVATE
        )
        private val NOT_INIT_EXCEPTION = "you must init SPUtils by init() first"
        private val KEY_IS_EMPTY_EXCEPTION = "key is empty"

        /**
         * 如果[key]存在，则返回对应类型的数据，如果转换数据类型失败，则返回[default]。
         * 如果[key]不存在，则返回[default]；
         */
        @Suppress("UNCHECKED_CAST")
        @Throws(IllegalArgumentException::class)
        fun <T> get(key: String, default: T): T {
            require(key.isNotEmpty()) { KEY_IS_EMPTY_EXCEPTION }
            getAll()?.forEach {
                if (it.key == key) {
                    return try {
                        it.value as T
                    } catch (e: Exception) {
                        default
                    }
                }
            }
            return default
        }

        /**
         * 如果[key]已经存在，则会覆盖数据
         * @param value     如果为 null，则会移除对应的数据。
         */
        @Throws(IllegalArgumentException::class)
        fun put(key: String, value: Any?) {
            require(key.isNotEmpty()) { KEY_IS_EMPTY_EXCEPTION }
            if (value == null) {
                remove(key)
            } else {
                try {
                    with(prefs.edit()) {
                        when (value) {
                            is String -> putString(key, value)
                            is Boolean -> putBoolean(key, value)
                            is Int -> putInt(key, value)
                            is Long -> putLong(key, value)
                            is Float -> putFloat(key, value)
                            else -> null
                        }?.apply()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        /**
         * 移除某个key对应的那一条数据
         * @param key
         */
        @Throws(IllegalArgumentException::class)
        fun remove(key: String) {
            require(key.isNotEmpty()) { KEY_IS_EMPTY_EXCEPTION }
            try {
                prefs.edit().remove(key).apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * 清除所有数据
         */
        @Throws(IllegalArgumentException::class)
        fun clear() {
            try {
                prefs.edit().clear().apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        /**
         * 查询某个key是否已经存在
         * @param key
         * @return
         */
        @Throws(IllegalArgumentException::class)
        fun contains(key: String): Boolean {
            require(key.isNotEmpty()) { KEY_IS_EMPTY_EXCEPTION }
            return try {
                prefs.contains(key)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        /**
         * 返回所有的键值对数据
         * @return
         */
        @Throws(IllegalArgumentException::class)
        fun getAll(): Map<String, Any?>? {
            return try {
                prefs.all
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    }


}


