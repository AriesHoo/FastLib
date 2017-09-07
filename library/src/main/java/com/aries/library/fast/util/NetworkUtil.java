package com.aries.library.fast.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * Created: AriesHoo on 2017/8/24 13:14
 * Function: 网络相关工具类
 * Desc:
 */

public class NetworkUtil {


    /**
     * 打开网络设置界面
     */
    public static void openWirelessSettings(Context context) {
        if (context != null)
            context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 获取活动网络信息
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return NetworkInfo
     */
    private static NetworkInfo getActiveNetworkInfo(Context content) {
        if (content == null) {
            return null;
        }
        return ((ConnectivityManager) content.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    /**
     * 判断网络是否连接
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isConnected(Context content) {
        NetworkInfo info = getActiveNetworkInfo(content);
        return info != null && info.isConnected();
    }
}
