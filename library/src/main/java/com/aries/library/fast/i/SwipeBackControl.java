package com.aries.library.fast.i;

import android.app.Activity;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created: AriesHoo on 2018/6/15 9:35
 * E-Mail: AriesHoo@126.com
 * Function: Activity 滑动返回控制接口
 * Description:
 */
public interface SwipeBackControl {

    /**
     * @param activity
     * @param swipeBackHelper BGASwipeBackHelper 控制详见{@link com.aries.library.fast.FastManager}
     */
    void setSwipeBack(Activity activity, BGASwipeBackHelper swipeBackHelper);
}
