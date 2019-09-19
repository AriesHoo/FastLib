package com.aries.library.fast.demo.impl;

import android.app.Activity;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.module.main.sample.SwipeBackActivity;
import com.aries.library.fast.i.SwipeBackControl;
import com.aries.library.fast.util.FastStackUtil;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackLayout;

/**
 * @Author: AriesHoo on 2018/12/4 18:00
 * @E-Mail: AriesHoo@126.com
 * @Function: 滑动返回处理-鉴于现在全面屏比较多不推荐使用该库;推荐使用使用类全面屏手势滑动返回库
 * 可参考{@link ActivityControlImpl#getActivityLifecycleCallbacks()}
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
        Activity previous = FastStackUtil.getInstance().getPrevious();
        return previous != null && previous instanceof SwipeBackActivity;
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
                .setShadowResId(R.drawable.bga_sbl_shadow);
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
