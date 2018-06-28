package com.aries.library.fast.i;

import android.app.Activity;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created: AriesHoo on 2018/6/15 9:35
 * E-Mail: AriesHoo@126.com
 * Function: Activity 滑动返回控制接口
 * Description:
 * 1、新增滑动过程回调
 */
public interface SwipeBackControl {

    /**
     * @param activity
     * @param swipeBackHelper BGASwipeBackHelper 控制详见{@link com.aries.library.fast.FastManager}
     */
    void setSwipeBack(Activity activity, BGASwipeBackHelper swipeBackHelper);

    /**
     * 正在滑动返回
     *
     * @param activity
     * @param slideOffset 滑动偏移量 0-1
     */
    void onSwipeBackLayoutSlide(Activity activity, float slideOffset);

    /**
     * 没达到滑动返回的阈值,取消滑动返回动作,回到默认状态
     *
     * @param activity
     */
    void onSwipeBackLayoutCancel(Activity activity);

    /**
     * 滑动返回执行完毕,销毁当前 Activity
     *
     * @param activity
     */
    void onSwipeBackLayoutExecuted(Activity activity);
}
