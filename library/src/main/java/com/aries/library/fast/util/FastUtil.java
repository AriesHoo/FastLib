package com.aries.library.fast.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;
import java.util.Random;


/**
 * Created: AriesHoo on 2017-03-14 08:54
 * Function: app使用工具类
 * Desc:
 */
public class FastUtil {

    /**
     * 获取 一定范围随机数
     *
     * @param max 最大值
     * @param min 最小值
     * @return
     */
    public static int getRandom(int max, int min) {
        Random random = new Random();// 定义随机类
        int result = random.nextInt(max) % (max - min + 1) + min;
        return result;
    }

    /**
     * 获取一定长度随机数
     *
     * @param length
     * @return
     */
    public static int getRandom(int length) {
        return getRandom(length, 1);
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (null != packageManager) {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                if (null != packageInfo) {
                    return packageInfo.versionName;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (null != packageManager) {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                if (null != packageInfo) {
                    return packageInfo.versionCode;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean isRunningForeground(Context context) {
        return isRunningForeground(context, null);
    }

    /**
     * 检查某个应用是否前台运行
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isRunningForeground(Context context, String packageName) {
        if (context == null) {
            return false;
        }
        if (TextUtils.isEmpty(packageName)) {
            packageName = context.getPackageName();
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void jumpMarket(Context mContext) {
        jumpMarket(mContext, null);
    }

    /**
     * 跳转应用市场详情
     *
     * @param mContext
     * @param packageName
     */
    public static void jumpMarket(Context mContext, String packageName) {
        if (mContext == null) {
            return;
        }
        if (packageName == null || packageName.isEmpty()) {
            packageName = mContext.getPackageName();
        }
        String mAddress = "market://details?id=" + packageName;
        try {
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            mContext.startActivity(marketIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param mContext
     * @param activity 跳转Activity
     * @param bundle
     * @param isSingle
     */
    public static void startActivity(Activity mContext, Class<? extends Activity> activity, Bundle bundle, boolean isSingle) {
        if (mContext == null) {
            return;
        }
        Intent intent = new Intent(mContext, activity);
        intent.setFlags(isSingle ? Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP : Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }

    public static void startActivity(Activity mContext, Class<? extends Activity> activity, Bundle bundle) {
        startActivity(mContext, activity, bundle, true);
    }

    public static void startActivity(Activity mContext, Class<? extends Activity> activity) {
        startActivity(mContext, activity, null);
    }

    public static void startActivity(Activity mContext, Class<? extends Activity> activity, boolean isSingle) {
        startActivity(mContext, activity, null, isSingle);
    }

    /**
     * @param mActivity
     * @param url
     */

    public static void startShareText(Activity mActivity, String url) {
        startShareText(mActivity, url, null);
    }

    public static void startShareText(Activity mActivity, String url, CharSequence title) {
        if (mActivity == null) {
            return;
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        mActivity.startActivity(Intent.createChooser(shareIntent, title));
    }
}
