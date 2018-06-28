package com.aries.library.fast.i;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.helper.status.StatusViewHelper;

/**
 * Created: AriesHoo on 2018/6/15 12:36
 * E-Mail: AriesHoo@126.com
 * Function:Activity/Fragment 属性控制(生命周期/背景色/屏幕控制)
 * Description:
 * 1、将原Activity 虚拟导航栏功能迁移新增全局控制Activity StatusBarView功能
 */
public interface ActivityFragmentControl {

    /**
     * 设置背景色
     *
     * @param contentView
     * @param cls
     */
    void setContentViewBackground(View contentView, Class<?> cls);

    /**
     * 强制设置横竖屏
     *
     * @param activity
     */
    void setRequestedOrientation(Activity activity);

    /**
     * @param activity
     * @param helper
     * @param topView
     * @return true 表示调用 helper 的init方法进行设置
     */
    boolean setStatusBar(Activity activity, StatusViewHelper helper,View topView);

    /**
     * @param activity
     * @param helper
     * @param bottomView
     * @return true 表示调用 helper 的init方法进行设置
     */
    boolean setNavigationBar(Activity activity, NavigationViewHelper helper,View bottomView);

    /**
     * activity全局生命周期回调
     *
     * @return
     */
    Application.ActivityLifecycleCallbacks getActivityLifecycleCallbacks();

    /**
     * Fragment全局生命周期回调
     *
     * @return
     */
    FragmentManager.FragmentLifecycleCallbacks getFragmentLifecycleCallbacks();
}
