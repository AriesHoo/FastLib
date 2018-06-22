package com.aries.library.fast.i;

import android.app.Activity;

import com.aries.ui.helper.navigation.NavigationViewHelper;


/**
 * Created: AriesHoo on 2018/1/5 0005 下午 2:41
 * E-Mail: AriesHoo@126.com
 * Function:虚拟导航栏控制
 * Description:
 * 1、2018-6-22 14:26:44 跳转方法及参数
 */
public interface NavigationBarControl {


    /**
     * @param activity
     * @param helper
     */
    void setNavigationBar(Activity activity, NavigationViewHelper helper);
}
