package com.gfq.common.system

import android.app.Activity
import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.gfq.common.BuildConfig
import com.gfq.common.R
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import java.util.*
import kotlin.system.exitProcess

/**
 * 持有 [Application] 实例，并对 [FragmentActivity] 进行了管理。
 */
object ActivityManager {
    private const val TAG = "【ActivityManager】"
    lateinit var application: Application
    private val activities = Collections.synchronizedList(mutableListOf<FragmentActivity>())


    private val activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity) {
            Log.d(TAG, "onActivityPaused: ${activity.javaClass.simpleName}")
        }

        override fun onActivityResumed(activity: Activity) {
            Log.d(TAG, "onActivityResumed: ${activity.javaClass.simpleName}")
        }

        override fun onActivityStarted(activity: Activity) {
            Log.d(TAG, "onActivityStarted: ${activity.javaClass.simpleName}")
        }

        override fun onActivityDestroyed(activity: Activity) {
            Log.d(TAG, "onActivityDestroyed: ${activity.javaClass.simpleName}")
            activities.remove(activity)
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityStopped(activity: Activity) {
            Log.d(TAG, "onActivityStopped: ${activity.javaClass.simpleName}")
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Log.d(TAG, "onActivityCreated: ${activity.javaClass.simpleName}")
            if (activity is FragmentActivity) {
                activities.add(activity)
            }
        }

    }

    fun init(application: Application) {
        Log.d(TAG, "init")
        ActivityManager.application = application
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        initLogger()
    }

    private fun initLogger() {
        val logAdapter = MyAndroidLogAdapter(
            PrettyFormatStrategy.newBuilder()
                .showThreadInfo(application.resources.getBoolean(R.bool.showThreadInfo))
                .methodOffset(application.resources.getInteger(R.integer.methodOffset))
                .methodCount(application.resources.getInteger(R.integer.methodCount))
                .tag(application.resources.getString(R.string.loggerTag))
                .build()
        )
        logAdapter.isLoggable(application.resources.getBoolean(R.bool.openLogger))
        Logger.addLogAdapter(logAdapter)
    }


    /**
     * 关闭所有Activity
     */
    fun finishAllActivities() {
        activities.forEach { it.finish() }
    }

    /**
     * 关闭所有 FragmentActivity，除了指定的 FragmentActivity。
     */
    fun <T : FragmentActivity> finishAllActivitiesExclude(vararg classes: Class<out T>) {
        activities.filter { !classes.contains(it::class.java) }.forEach { it.finish() }
    }

    /**
     * 关闭指定的所有 FragmentActivity
     */
    fun <T : FragmentActivity> finishAllActivitiesInclude(vararg classes: Class<out T>) {
        activities.filter { classes.contains(it::class.java) }.forEach { it.finish() }
    }

    /**
     * 获取指定 FragmentActivity 实例
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Activity> getActivity(clazz: Class<T>): T? {
        return activities.firstOrNull {
            it.javaClass == clazz
        } as? T
    }

    fun getAllActivities(): List<FragmentActivity> = activities

    fun getTopActivity() = activities.lastOrNull()

    /**
     * 退出整个app
     */
    fun exitApp() {
        Log.d(TAG, "exitApp")
        // 关闭所有界面，避免后一句代码执行后，如果栈中还有界面，会重启进程并启动下一个界面，导致不能彻底退出应用。
        finishAllActivities()
        exitProcess(0)
    }

    /**
     * 这里参考https://www.cnblogs.com/zhujiabin/p/6874508.html
     */
    fun isDebug(): Boolean {
        return application.applicationInfo != null && (application.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }
}