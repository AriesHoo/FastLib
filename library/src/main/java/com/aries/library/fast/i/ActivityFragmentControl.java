package com.aries.library.fast.i;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.FragmentManager;
import android.view.View;

/**
 * Created: AriesHoo on 2018/6/15 12:36
 * E-Mail: AriesHoo@126.com
 * Function:Activity/Fragment 属性控制(生命周期/背景色/屏幕控制)
 * Description:
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
