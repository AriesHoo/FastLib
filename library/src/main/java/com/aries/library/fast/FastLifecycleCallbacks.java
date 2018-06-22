package com.aries.library.fast;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.aries.library.fast.i.ActivityFragmentControl;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.FastUtil;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.util.RomUtil;
import com.flyco.tablayout.CommonTabLayout;

import cn.bingoogolapple.swipebacklayout.BGAKeyboardUtil;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created: AriesHoo on 2018/6/21 12:22
 * E-Mail: AriesHoo@126.com
 * Function: Activity/Fragment生命周期
 * Description:
 */
public class FastLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private String TAG = getClass().getSimpleName();
    private ActivityFragmentControl mActivityFragmentControl;
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;
    private FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycleCallbacks;

    public FastLifecycleCallbacks() {
        mActivityFragmentControl = FastManager.getInstance().getActivityFragmentControl();
        mActivityLifecycleCallbacks = mActivityFragmentControl.getActivityLifecycleCallbacks();
        mFragmentLifecycleCallbacks = mActivityFragmentControl.getFragmentLifecycleCallbacks();
    }

    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        LoggerManager.i(TAG, "onActivityCreated:" + activity.getClass().getSimpleName());
        //统一Activity堆栈管理
        FastStackUtil.getInstance().push(activity);
        mActivityFragmentControl.setRequestedOrientation(activity);
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityCreated(activity, savedInstanceState);
        }
        //统一Fragment生命周期处理
        if (activity instanceof FragmentActivity && mFragmentLifecycleCallbacks != null) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks, true);
        }
        //设置滑动返回
        if (activity instanceof BGASwipeBackHelper.Delegate) {
            return;//已设置滑动返回
        }
        LoggerManager.i(TAG, "设置Activity滑动返回");
        //需设置activity window背景为透明避免滑动过程中漏出背景也可减少背景层级降低过度绘制
        activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        BGASwipeBackHelper swipeBackHelper = new BGASwipeBackHelper(activity, new BGASwipeBackHelper.Delegate() {
            @Override
            public boolean isSupportSwipeBack() {
                return true;
            }

            @Override
            public void onSwipeBackLayoutSlide(float slideOffset) {

            }

            @Override
            public void onSwipeBackLayoutCancel() {

            }

            @Override
            public void onSwipeBackLayoutExecuted() {
                //设置退出动画-确保效果准确
                if (activity == null || activity.isFinishing()) return;
                BGAKeyboardUtil.closeKeyboard(activity);
                activity.finish();
                activity.overridePendingTransition(R.anim.bga_sbl_activity_swipeback_enter, R.anim.bga_sbl_activity_swipeback_exit);
            }
        })
                //设置滑动背景
                .setShadowResId(R.drawable.bga_sbl_shadow)
                //底部导航条是否悬浮在内容上设置过NavigationViewHelper可以不用设置该属性
                .setIsNavigationBarOverlap(true)
                .setIsShadowAlphaGradient(true);
        //用于全局控制
        FastManager.getInstance().getSwipeBackControl().setSwipeBack(activity, swipeBackHelper);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        LoggerManager.i(TAG, "onActivityStarted:" + activity.getClass().getSimpleName());
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityStarted(activity);
        }
        Object object = activity.getWindow().getDecorView().getTag(R.id.navigation_view_helper);
        if (object != null) {
            return;
        }
        //设置标记避免多次设置导致多出导航栏高度
        activity.getWindow().getDecorView().setTag(R.id.navigation_view_helper, true);
        View contentView = FastUtil.getRootView(activity);
        CommonTabLayout tabLayout = FindViewUtil.getTargetView(contentView, CommonTabLayout.class);
        //设置虚拟导航栏控制
        NavigationViewHelper navigationViewHelper = NavigationViewHelper.with(activity)
                .setLogEnable(BuildConfig.DEBUG)
                //是否控制虚拟导航栏true 后续属性有效--第一优先级
                .setControlEnable(true)
                //是否全透明导航栏优先级第二--同步设置setNavigationViewColor故注意调用顺序
                //华为的半透明和全透明类似
                .setTransEnable(isTrans())
                //是否增加假的NavigationView用于沉浸至虚拟导航栏遮住
                .setPlusNavigationViewEnable(isTrans())
                //设置是否控制底部输入框--默认属性
                .setControlBottomEditTextEnable(true)
                //设置最下边View用于增加paddingBottom--建议activity 根布局
                .setBottomView(tabLayout == null ? contentView : tabLayout)
                //影响setPlusNavigationViewEnable(true)单个条件
                //或者(setPlusNavigationViewEnable(false)&&setControlEnable(true))--两个前置条件
                //半透明默认设置102
                .setNavigationViewColor(Color.argb(isTrans() ? 0 : 102, 0, 0, 0))
                //setPlusNavigationViewEnable(true)才有效注意与setNavigationViewColor调用顺序
//                .setNavigationViewDrawable(mContext.getResources().getDrawable(R.drawable.img_bg_login))
                //setPlusNavigationViewEnable(true)有效
                .setNavigationLayoutColor(Color.WHITE);
        FastManager.getInstance().getNavigationBarControl().setNavigationBar(activity, navigationViewHelper);
        navigationViewHelper.init();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        LoggerManager.i(TAG, "onActivityResumed:" + activity.getClass().getSimpleName());
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityResumed(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        LoggerManager.i(TAG, "onActivityPaused:" + activity.getClass().getSimpleName() + ";isFinishing:" + activity.isFinishing());
        //Activity销毁前的时机需要关闭软键盘-在onActivityStopped及onActivityDestroyed生命周期内已无法关闭
        if (activity.isFinishing()) {
            BGAKeyboardUtil.closeKeyboard(activity);
        }
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityPaused(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        LoggerManager.i(TAG, "onActivityStopped:" + activity.getClass().getSimpleName() + ";isFinishing:" + activity.isFinishing());
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityStopped(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LoggerManager.i(TAG, "onActivitySaveInstanceState:" + activity.getClass().getSimpleName());
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivitySaveInstanceState(activity, outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LoggerManager.i(TAG, "onActivityDestroyed:" + activity.getClass().getSimpleName() + ";isFinishing:" + activity.isFinishing());
        FastStackUtil.getInstance().pop(activity);
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityDestroyed(activity);
        }
        //统一注销Fragment生命周期处理
        if (activity instanceof FragmentActivity && mFragmentLifecycleCallbacks != null) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .unregisterFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks);
        }
    }


    /**
     * 是否全透明-华为4.1以上可根据导航栏位置颜色设置导航图标颜色
     *
     * @return
     */
    protected boolean isTrans() {
        return RomUtil.isEMUI() && (RomUtil.getEMUIVersion().compareTo("EmotionUI_4.1") > 0);
    }
}
