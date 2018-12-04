package com.aries.library.fast.demo.impl;

import android.app.Activity;

import com.aries.library.fast.demo.App;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.i.SwipeBackControl;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackLayout;

/**
 * @Author: AriesHoo on 2018/12/4 18:00
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public class SwipeBackControlImpl implements SwipeBackControl {

    /**
     * 设置当前Activity是否支持滑动返回(用于控制是否添加一层{@link BGASwipeBackLayout})
     * 返回为true {@link #setSwipeBack(Activity, BGASwipeBackHelper)}才有设置的意义
     *
     * @param activity
     * @return
     */
    @Override
    public boolean isSwipeBackEnable(Activity activity) {
        return true;
    }

    /**
     * 设置Activity 全局滑动属性--包括三方库
     *
     * @param activity
     * @param swipeBackHelper BGASwipeBackHelper 控制详见{@link com.aries.library.fast.FastManager}
     */
    @Override
    public void setSwipeBack(Activity activity, BGASwipeBackHelper swipeBackHelper) {
        //以下为默认设置
        //需设置activity window背景为透明避免滑动过程中漏出背景也可减少背景层级降低过度绘制
        activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        swipeBackHelper.setSwipeBackEnable(true)
                .setShadowResId(R.color.colorSwipeBackBackground)
                //底部导航条是否悬浮在内容上设置过NavigationViewHelper可以不用设置该属性
                .setIsNavigationBarOverlap(App.isControlNavigation());
    }

    @Override
    public void onSwipeBackLayoutSlide(Activity activity, float slideOffset) {

    }

    @Override
    public void onSwipeBackLayoutCancel(Activity activity) {

    }

    @Override
    public void onSwipeBackLayoutExecuted(Activity activity) {

    }
}
