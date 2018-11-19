package com.aries.library.fast.demo.util;

import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

/**
 * @Author: AriesHoo on 2018/11/19 14:30
 * @E-Mail: AriesHoo@126.com
 * @Function: PopupWindow工具类
 * @Description:
 */
public class PopupWindowUtil {

    public static void showAsDropDown(PopupWindow mWindow, View anchor) {
        showAsDropDown(mWindow, anchor, 0, 0);
    }

    /**
     * 展示popupWindow--解决Android 7.0版本兼容性问题
     *
     * @param mWindow PopupWindow
     * @param anchor  the view on which to pin the popup window
     * @param x       A horizontal offset from the anchor in pixels
     * @param y       A vertical offset from the anchor in pixels
     */
    public static void showAsDropDown(PopupWindow mWindow, View anchor, int x, int y) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0] + x, location[1] + anchor.getHeight() + y);
        } else {
            mWindow.showAsDropDown(anchor, x, y);
        }
    }
}
