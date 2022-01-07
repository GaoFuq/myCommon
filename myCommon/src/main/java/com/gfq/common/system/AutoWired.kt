package com.gfq.common.system
import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment

/**
 * 基于反射与注解实现页面跳转参数注入。配合[com.like.common.util.injectForIntentExtras]使用。
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class AutoWired

/**
 * 在[Activity]中，通过反射从[android.content.Intent]中获取参数值，并赋值给被[AutoWired]注解的字段。
 * 例子：
 * @AutoWired
 * private var param4: Int? = null
 * @AutoWired
 * private var param5: List<P>? = null
 * override fun onCreate(savedInstanceState: Bundle?) {
 *     super.onCreate(savedInstanceState)
 *     injectForIntentExtras()
 * }
 *
 * 注意：
 * 1、[android.content.Intent]中传递的参数的key必须和字段名一致。
 * 如果不一致，则会警告：@AutoWired field com.like.common.sample.autowired.AutoWiredActivity.param3 not found
 * 2、数据类型必须一致。
 * 如果不一致，则会抛异常：[java.lang.IllegalArgumentException]
 */
@Throws(java.lang.IllegalArgumentException::class)
fun Activity.injectForIntentExtras() {
    val extras = intent.extras ?: return
    val declaredFields = try {
        javaClass.declaredFields
    } catch (e: Exception) {
        null
    }
    if (declaredFields.isNullOrEmpty()) {
        return
    }


    for (field in declaredFields) {
        if (field.isAnnotationPresent(AutoWired::class.java)) {
            val key = field.name
            if (extras.containsKey(key)) {
                field.isAccessible = true
                field.set(this, extras[key])
                continue
            }
            Log.w("AutoWired","@AutoWired field ${javaClass.name}.$key not found")
        }
    }
}


@Throws(java.lang.IllegalArgumentException::class)
fun Fragment.injectForArguments() {
    val extras = arguments ?: return
    val declaredFields = try {
        javaClass.declaredFields
    } catch (e: Exception) {
        null
    }
    if (declaredFields.isNullOrEmpty()) {
        return
    }


    for (field in declaredFields) {
        if (field.isAnnotationPresent(AutoWired::class.java)) {
            val key = field.name
            if (extras.containsKey(key)) {
                field.isAccessible = true
                field.set(this, extras[key])
                continue
            }
            Log.w("AutoWired","@AutoWired field ${javaClass.name}.$key not found")
        }
    }
}