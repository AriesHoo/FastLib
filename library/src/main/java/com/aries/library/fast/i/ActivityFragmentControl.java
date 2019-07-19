package com.aries.library.fast.i;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;

/**
 * @Author: AriesHoo on 2018/7/22/022 18:19
 * @E-Mail: AriesHoo@126.com
 * Function:Activity/Fragment 属性控制(生命周期/背景色/屏幕控制)
 * Description:
 * 1、将原Activity 虚拟导航栏功能迁移新增全局控制Activity StatusBarView功能
 * 2、2019-7-19 14:48:38 将{@link IStatusBar} 状态栏控制及{@link INavigationBar}抽离方便Activity进行特殊化定制
 * 3、2019-7-19 14:48:43 将{@link #setRequestedOrientation(Activity)}标记废弃
 * 通过{@link #getActivityLifecycleCallbacks()}{@link Application.ActivityLifecycleCallbacks#onActivityCreated(Activity, Bundle)}进行操作
 */
public interface ActivityFragmentControl extends INavigationBar, IStatusBar {

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
     * @param activity 目标Activity
     *                 {@link Application.ActivityLifecycleCallbacks#onActivityCreated(Activity, Bundle)}
     */
    @Deprecated
    default void setRequestedOrientation(Activity activity) {
    }

    /**
     * `
     * Activity 全局生命周期回调
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
