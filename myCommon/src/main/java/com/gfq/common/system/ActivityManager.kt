package com.gfq.common.system

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.Utils
import com.gfq.common.R
import com.gfq.common.helper.actlifecycle.SimpleActivityLifecycleCallbacks
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import java.util.*
import kotlin.system.exitProcess

/**
 * 持有 [Application] 实例，并对 [FragmentActivity] 进行了管理。
 */
object ActivityManager {
    private const val TAG = "【ActivityManager】"

    /**
     * 全局设置Activity的方向
     * @see ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
     * @see ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
     */
    var globalOrientation: Int? = null
    lateinit var application: Application
    private val activities = Collections.synchronizedList(mutableListOf<FragmentActivity>())
    private val currentResumedActivity = Collections.synchronizedList(mutableListOf<Activity>())


    private val activityLifecycleCallbacks = object : SimpleActivityLifecycleCallbacks(true, TAG) {
        override fun onActivityDestroyed(activity: Activity) {
            super.onActivityDestroyed(activity)
            activities.remove(activity)
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            super.onActivityCreated(activity, savedInstanceState)
            globalOrientation?.let { setScreenOrientationPortrait(activity, it) }
            if (activity is FragmentActivity) {
                activities.add(activity)
            }
        }

        override fun onActivityResumed(activity: Activity) {
            super.onActivityResumed(activity)
            currentResumedActivity.add(activity)
        }

        override fun onActivityStopped(activity: Activity) {
            super.onActivityStopped(activity)
            currentResumedActivity.clear()
        }
    }


    fun init(application: Application) {
        Log.d(TAG, "init")
        ActivityManager.application = application
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        initLogger()
        Utils.init(application)
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

    /**
     * 获取最近的一个未被 Destroy 的 FragmentActivity 。
     * 当从 B 返回 A 时， 在 A 的 onResume 里调用该方法，得到的是 B。
    onActivityResumed: MainActivity
    onActivityDestroyed: SplashActivity
    onActivityPaused: MainActivity //点击跳转 SettingActivity
    onActivityCreated: SettingActivity
    onActivityStarted: SettingActivity
    onActivityResumed: SettingActivity
    onActivityStopped: MainActivity
    onActivityPaused: SettingActivity //返回 MainActivity
    onActivityStarted: MainActivity
    onActivityResumed: MainActivity
    onActivityStopped: SettingActivity
    onActivityDestroyed: SettingActivity // 此时才会移除 SettingActivity
     */
    fun getTopActivity() = activities.lastOrNull()

    /**
     * 获取当前正处于 onResume 状态的 Activity
     */
    fun getTopResumeActivity() = currentResumedActivity.firstOrNull()

    /**
     * 在Activity的onResume中调用，根据全限定类名，判断是否从某Activity返回
     */
    fun isBackFrom(clazz: Class<out Activity>): Boolean {
        val topAct = getTopActivity() ?: return false
        return topAct::class.java.name == clazz.name
    }


    /**
     * 全局设置 屏幕方向
     */
    @SuppressLint("SourceLockedOrientationActivity")
    private fun setScreenOrientationPortrait(activity: Activity, orientation: Int) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            AdaptsHelper.fixOrientationInAndroidO(activity)
        } else {
            activity.requestedOrientation = orientation
        }
    }


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