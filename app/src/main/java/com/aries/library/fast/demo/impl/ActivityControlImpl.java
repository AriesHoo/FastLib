package com.aries.library.fast.demo.impl;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aries.library.fast.BuildConfig;
import com.aries.library.fast.FastLifecycleCallbacks;
import com.aries.library.fast.basis.BasisActivity;
import com.aries.library.fast.basis.BasisFragment;
import com.aries.library.fast.demo.App;
import com.aries.library.fast.demo.AppData;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.module.SplashActivity;
import com.aries.library.fast.demo.module.main.MainActivity;
import com.aries.library.fast.demo.module.main.sample.SwipeBackActivity;
import com.aries.library.fast.demo.module.main.sample.TestStatusActivity;
import com.aries.library.fast.i.ActivityDispatchEventControl;
import com.aries.library.fast.i.ActivityFragmentControl;
import com.aries.library.fast.i.ActivityKeyEventControl;
import com.aries.library.fast.impl.FastActivityLifecycleCallbacks;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.SnackBarUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.helper.navigation.KeyboardHelper;
import com.aries.ui.helper.navigation.NavigationBarUtil;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.helper.status.StatusViewHelper;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.util.StatusBarUtil;
import com.google.android.gms.ads.AdActivity;
import com.luck.picture.lib.PictureBaseActivity;
import com.luck.picture.lib.PicturePreviewActivity;
import com.parfoismeng.slidebacklib.SlideBack;
import com.parfoismeng.slidebacklib.callback.SlideBackCallBack;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import static com.aries.library.fast.demo.App.isControlNavigation;

/**
 * @Author: AriesHoo on 2018/12/4 18:04
 * @E-Mail: AriesHoo@126.com
 * @Function: Activity/Fragment 生命周期全局处理及BasisActivity 的按键处理
 * @Description:
 */
public class ActivityControlImpl implements ActivityFragmentControl, ActivityKeyEventControl, ActivityDispatchEventControl {
    private static String TAG = "ActivityControlImpl";
    /**
     * Audio管理器，用了控制音量
     */
    private AudioManager mAudioManager = null;
    private int mMaxVolume = 0;
    private int mMinVolume = 0;
    private int mCurrentVolume = 0;

    private void volume(int value, boolean plus) {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) App.getContext().getSystemService(Context.AUDIO_SERVICE);
            // 获取最大音乐音量
            mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            // 获取最小音乐音量
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mMinVolume = mAudioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC);
            }
        }
        // 获取当前音乐音量
        mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (plus) {
            if (mCurrentVolume >= mMaxVolume) {
                ToastUtil.show("当前音量已最大");
                return;
            }
            mCurrentVolume += value;
        } else {
            if (mCurrentVolume <= mMinVolume) {
                ToastUtil.show("当前音量已最小");
                return;
            }
            mCurrentVolume -= value;
        }
        if (mCurrentVolume < mMinVolume) {
            mCurrentVolume = mMinVolume;
        }
        if (mCurrentVolume > mMaxVolume) {
            mCurrentVolume = mMaxVolume;
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolume, AudioManager.FLAG_PLAY_SOUND);
        // 获取当前音乐音量
        mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        LoggerManager.i(TAG, "max:" + mMaxVolume + ";min:" + mMinVolume + ";current:" + mCurrentVolume);
        SnackBarUtil.with(FastStackUtil.getInstance().getCurrent().getWindow().getDecorView())
                .setBgColor(Color.WHITE)
                .setMessageColor(Color.BLACK)
                .setMessage("当前音量:" + mCurrentVolume)
                .showSuccess();
    }

    @Override
    public boolean onKeyDown(Activity activity, int keyCode, KeyEvent event) {
        //演示拦截系统音量键控制--类似抖音
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                volume(1, false);
                LoggerManager.i(TAG, "volumeDown-activity:" + activity.getClass().getSimpleName());
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                volume(1, true);
                LoggerManager.i(TAG, "volumeUp-activity:" + activity.getClass().getSimpleName());
                return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(Activity activity, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyLongPress(Activity activity, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyShortcut(Activity activity, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyMultiple(Activity activity, int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    /**
     * 设置Fragment/Activity根布局背景
     *
     * @param contentView
     * @param cls
     */
    @Override
    public void setContentViewBackground(View contentView, Class<?> cls) {
        //避免背景色重复
        if (!Fragment.class.isAssignableFrom(cls)
                && contentView.getBackground() == null) {
            contentView.setBackgroundResource(R.color.colorBackground);
        } else {
            if (BasisActivity.class.isAssignableFrom(cls) || BasisFragment.class.isAssignableFrom(cls)) {
                return;
            }
            Activity activity = FastStackUtil.getInstance().getCurrent();
            if (activity.getClass().getSimpleName().equals("UniversalActivity")) {
                contentView.setBackgroundColor(Color.WHITE);
            }
            LoggerManager.i("setContentViewBackground_activity:" + activity.getClass().getSimpleName() + ";cls:" + cls.getSimpleName());
        }
    }

    /**
     * 设置非FastLib且未实现Activity 状态栏功能的三方Activity 状态栏沉浸
     *
     * @param activity
     * @param helper
     * @return
     */
    @Override
    public boolean setStatusBar(Activity activity, StatusViewHelper helper, View topView) {
        LoggerManager.e("setStatusBar:" + Thread.currentThread());
        boolean isSupportStatusBarFont = StatusBarUtil.isSupportStatusBarFontChange();
        helper.setTransEnable(isSupportStatusBarFont || isLeak(activity))
                .setPlusStatusViewEnable(!isLeak(activity))
                .setStatusBarLightMode(isSupportStatusBarFont)
                .setStatusViewColor(Color.argb(isSupportStatusBarFont ? 0 : 102, 0, 0, 0))
                .setStatusLayoutColor(Color.WHITE);
        setStatusBarActivity(activity);
        return !(activity instanceof AdActivity);
    }

    private boolean isLeak(Activity activity) {
        return activity.getClass().getSimpleName().equals("DisplayLeakActivity");
    }

    /**
     * {@link FastLifecycleCallbacks#onActivityStarted(Activity)}
     *
     * @param activity
     * @param helper
     */
    @Override
    public boolean setNavigationBar(Activity activity, NavigationViewHelper helper, View bottomView) {

        Activity previous = FastStackUtil.getInstance().getPrevious();
        boolean enable = previous != null && previous instanceof SwipeBackActivity;
        //其它默认属性请参考FastLifecycleCallbacks
        helper.setLogEnable(BuildConfig.DEBUG)
                .setPlusNavigationViewEnable(true)
                //此处为配合BGASwipeBackHelper滑动返回效果-如不使用BGASwipeBackHelper推荐使用上面的方法
                .setPlusNavigationViewEnable(true, enable, enable)
                .setNavigationBarLightMode(NavigationBarUtil.isSupportNavigationBarFontChange() && isPlusView(activity))
                .setOnKeyboardVisibilityChangedListener(getOnKeyboardVisibilityChangedListener(activity))
                .setBottomView(PicturePreviewActivity.class.isAssignableFrom(activity.getClass()) ?
                        FindViewUtil.getTargetView(bottomView, R.id.select_bar_layout) : bottomView)
                .setNavigationViewColor(isLeak(activity) ? Color.BLACK : Color.argb(NavigationBarUtil.isSupportNavigationBarFontChange() && isPlusView(activity) ? 0 : 102, 0, 0, 0))
                .setNavigationLayoutColor(ContextCompat.getColor(activity, !isPlusView(activity) ? R.color.transparent : R.color.colorTabBackground));
        if (!isControlNavigation() && !(activity instanceof MainActivity)) {
            KeyboardHelper.with(activity)
                    .setEnable()
                    .setOnKeyboardVisibilityChangedListener(getOnKeyboardVisibilityChangedListener(activity));
        }
        return isControlNavigation() && !(activity instanceof AdActivity);
    }

    private KeyboardHelper.OnKeyboardVisibilityChangedListener getOnKeyboardVisibilityChangedListener(Activity activity) {
        if (activity instanceof KeyboardHelper.OnKeyboardVisibilityChangedListener) {
            return (KeyboardHelper.OnKeyboardVisibilityChangedListener) activity;
        }
        return mOnKeyboardVisibilityChangedListener;
    }

    /**
     * 是否增加假导航栏占位
     *
     * @param activity
     * @return
     */
    protected boolean isPlusView(Activity activity) {
        return !(activity instanceof SplashActivity)
                && !(activity instanceof TestStatusActivity)
                && !isLeak(activity);
    }

    private KeyboardHelper.OnKeyboardVisibilityChangedListener mOnKeyboardVisibilityChangedListener = (activity, isOpen, heightDiff, navigationHeight) -> {
        View mContent = FastUtil.getRootView(activity);
        LoggerManager.i("onKeyboardVisibilityChanged", "activity:" + activity + ";isOpen:" + isOpen + ";heightDiff:" + heightDiff + ";navigationHeight:" + navigationHeight);
        return false;
    };

    /**
     * 设置屏幕方向--注意targetSDK设置27以上不能设置windowIsTranslucent=true属性不然应用直接崩溃-强烈建议手机应用锁定竖屏
     * 错误为 Only fullscreen activities can request orientation
     * 默认自动 ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
     * 竖屏 ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
     * 横屏 ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
     * {@link ActivityInfo#screenOrientation ActivityInfo.screenOrientation}
     *
     * @param activity
     */
    public void setActivityOrientation(Activity activity) {
        //全局控制屏幕横竖屏
        //先判断xml没有设置屏幕模式避免将开发者本身想设置的覆盖掉
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            try {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } catch (Exception e) {
                e.printStackTrace();
                LoggerManager.e(TAG, "setRequestedOrientation:" + e.getMessage());
            }
        }
    }

    /**
     * Activity 生命周期监听--可用于三方统计页面数据
     * 示例仅为参考如无需添加自己代码可回调null
     *
     * @return
     */
    @Override
    public Application.ActivityLifecycleCallbacks getActivityLifecycleCallbacks() {
        return new FastActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                App.setSaturation(activity);
                //阻止系统截屏功能
                //activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                setActivityOrientation(activity);
                Activity previous = FastStackUtil.getInstance().getPrevious();
                if (previous != null && previous instanceof SwipeBackActivity) {
                    return;
                }
                if (activity instanceof AdActivity) {
                    return;
                }
                //设置类全面屏手势滑动返回
                SlideBack.with(activity)
                        .haveScroll(true)
                        .edgeMode(SlideBack.EDGE_BOTH)
                        .callBack(new SlideBackCallBack() {
                            @Override
                            public void onSlideBack() {
                                activity.onBackPressed();
                            }
                        })
                        .register();
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (activity instanceof FragmentActivity) {
                    FragmentManager manager = ((FragmentActivity) activity).getSupportFragmentManager();
                    List<Fragment> list = manager.getFragments();
                    //有Fragment的FragmentActivity不需调用以下方法避免统计不准
                    if (list == null || list.size() == 0) {
                        MobclickAgent.onPageStart(activity.getClass().getSimpleName());
                    }
                } else {
                    MobclickAgent.onPageStart(activity.getClass().getSimpleName());
                }
                //统计时长
                MobclickAgent.onResume(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                //普通Activity直接onPageEnd
                if (activity instanceof FragmentActivity) {
                    FragmentManager manager = ((FragmentActivity) activity).getSupportFragmentManager();
                    List<Fragment> list = manager.getFragments();
                    //有Fragment的FragmentActivity不需调用以下方法避免统计不准
                    if (list == null || list.size() == 0) {
                        MobclickAgent.onPageEnd(activity.getClass().getSimpleName());
                    }
                } else {
                    MobclickAgent.onPageEnd(activity.getClass().getSimpleName());
                }
                //统计时长
                MobclickAgent.onPause(activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                //统一于滑动返回动画
                if (activity.isFinishing()) {
                    activity.overridePendingTransition(0, R.anim.fast_activity_swipeback_exit);
                }
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                Activity previous = FastStackUtil.getInstance().getPrevious();
                if (previous != null && previous instanceof SwipeBackActivity) {
                    return;
                }
                SlideBack.unregister(activity);
            }
        };
    }

    /**
     * Fragment 生命周期回调
     *
     * @return
     */
    @Override
    public FragmentManager.FragmentLifecycleCallbacks getFragmentLifecycleCallbacks() {
        return new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentResumed(FragmentManager fm, Fragment f) {
                super.onFragmentResumed(fm, f);
                LoggerManager.i(TAG, "onFragmentResumed:统计Fragment:" + f.getClass().getSimpleName());
                MobclickAgent.onPageStart(f.getClass().getSimpleName());
            }

            @Override
            public void onFragmentPaused(FragmentManager fm, Fragment f) {
                super.onFragmentPaused(fm, f);
                LoggerManager.i(TAG, "onFragmentPaused:统计Fragment:" + f.getClass().getSimpleName());
                MobclickAgent.onPageEnd(f.getClass().getSimpleName());
            }

            @Override
            public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentDestroyed(fm, f);
                LoggerManager.i(TAG, "onFragmentDestroyed:" + f.getClass().getSimpleName());
            }

            @Override
            public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentViewDestroyed(fm, f);
                LoggerManager.i(TAG, "onFragmentViewDestroyed:" + f.getClass().getSimpleName());
            }
        };
    }


    /**
     * 根据程序使用的三方库进行改造:本示例使用的三方库实现了自己的沉浸式状态栏及导航栏但和Demo的滑动返回不搭配故做相应调整
     *
     * @param activity
     */
    private void setStatusBarActivity(Activity activity) {
        if (PictureBaseActivity.class.isAssignableFrom(activity.getClass())) {
            View contentView = FastUtil.getRootView(activity);
            //该属性会影响适配滑动返回效果
            contentView.setFitsSystemWindows(false);
            ImageView imageView = contentView != null ? contentView.findViewById(R.id.picture_left_back) : null;
            if (imageView != null) {
                RelativeLayout layout = contentView.findViewById(R.id.rl_picture_title);
                if (layout != null) {
                    ViewCompat.setElevation(layout, activity.getResources().getDimension(R.dimen.dp_elevation));
                }
                //调整返回箭头大小
                imageView.setPadding(SizeUtil.dp2px(15), SizeUtil.dp2px(4), SizeUtil.dp2px(4), SizeUtil.dp2px(4));
            }
        }
    }

    /**
     * @param activity
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(Activity activity, MotionEvent event) {
        //根据事件派发全局控制点击非EditText 关闭软键盘
        if (activity != null) {
            KeyboardHelper.handleAutoCloseKeyboard(true, activity.getCurrentFocus(), event, activity);
        }
        return false;
    }

    @Override
    public boolean dispatchGenericMotionEvent(Activity activity, MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(Activity activity, KeyEvent event) {
        return false;
    }

    @Override
    public boolean dispatchKeyShortcutEvent(Activity activity, KeyEvent event) {
        return false;
    }

    @Override
    public boolean dispatchTrackballEvent(Activity activity, MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(Activity activity, AccessibilityEvent event) {
        return false;
    }
}
