package com.aries.library.fast.i;

import android.app.Activity;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * @author: AriesHoo on 2018/7/23 10:47
 * @E-Mail: AriesHoo@126.com
 * Function: Activity 滑动返回控制接口
 * Description:
 * 1、新增滑动过程回调
 * 2、2018-10-8 12:30:41 新增是否滑动返回支持
 */
public interface SwipeBackControl {

    /**
     * 设置当前Activity是否支持滑动返回(用于控制是否添加一层{@link cn.bingoogolapple.swipebacklayout.BGASwipeBackLayout})
     * 返回为true {@link #setSwipeBack(Activity, BGASwipeBackHelper)}才有设置的意义
     *
     * @param activity 当前Activity
     * @return
     */
    boolean isSwipeBackEnable(Activity activity);

    /**
     * 设置滑动返回控制属性
     *
     * @param activity        当前Activity
     * @param swipeBackHelper BGASwipeBackHelper 控制详见{@link com.aries.library.fast.FastManager}
     */
    void setSwipeBack(Activity activity, BGASwipeBackHelper swipeBackHelper);

    /**
     * 正在滑动返回
     *
     * @param activity    滑动的Activity
     * @param slideOffset 滑动偏移量 0-1
     */
    void onSwipeBackLayoutSlide(Activity activity, float slideOffset);

    /**
     * 没达到滑动返回的阈值,取消滑动返回动作,回到默认状态
     *
     * @param activity 当前Activity
     */
    void onSwipeBackLayoutCancel(Activity activity);

    /**
     * 滑动返回执行完毕,销毁当前 Activity
     *
     * @param activity 当前activity
     */
    void onSwipeBackLayoutExecuted(Activity activity);

}
