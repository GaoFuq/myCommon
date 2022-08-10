package com.gfq.common.utils;

import android.app.Application;
import android.content.Context;
import android.net.TrafficStats;

import com.gfq.common.system.ActivityManager;

/**
 * implementation 'com.github.wanliyang1990:wlmedia:2.0.0'
 */
public class NetSpeedUtil {
    private static long lastTotalBytes = 0L;
    private static long lastTime = 0L;

    public NetSpeedUtil() {
    }

    public static long getNetSpeed() {
        return getNetSpeed(ActivityManager.application);
    }

    public static boolean reset() {
       return reset(ActivityManager.application);
    }

    private static long getNetSpeed(Context context) {
        if (TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) == -1L) {
            return 0L;
        } else {
            long totalBytes = TrafficStats.getTotalRxBytes();
            long nowTime = System.currentTimeMillis();
            if (nowTime - lastTime == 0L) {
                return 0L;
            } else {
                long netSpeed = (totalBytes - lastTotalBytes) * 1000L / (nowTime - lastTime);
                lastTotalBytes = totalBytes;
                lastTime = nowTime;
                return netSpeed;
            }
        }
    }

    private static boolean reset(Context context) {
        if (TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) == -1L) {
            return false;
        } else {
            lastTime = System.currentTimeMillis();
            lastTotalBytes = TrafficStats.getTotalRxBytes();
            return true;
        }
    }
}
