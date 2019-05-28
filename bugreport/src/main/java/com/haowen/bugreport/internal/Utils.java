package com.haowen.bugreport.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 工具类
 */
class Utils {

    /**
     * 获取应用信息
     *
     * @param context 上下文
     * @return 应用信息
     */
    static String getAppInfo(Context context) {
        return getAppName(context) + " " +
                getAppVersion(context) + " " +
                "exception report";
    }

    /**
     * 获取应用名
     *
     * @param context 上下文
     * @return 应用名
     */
    private static String getAppName(Context context) {
        String packageName = getPackageName(context);
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName;
    }

    /**
     * 获取应用版本
     *
     * @param context 上下文
     * @return 应用版本
     */
    private static String getAppVersion(Context context) {
        String packageName = getPackageName(context);
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName;
    }

    /**
     * 获取包名
     *
     * @param context 上下文
     * @return 包名
     */
    private static String getPackageName(Context context) {
        return context.getPackageName();
    }
}