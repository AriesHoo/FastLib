package com.aries.library.fast.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.aries.library.fast.FastConstant;
import com.aries.library.fast.R;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.util.NavigationBarUtil;

import java.lang.ref.SoftReference;

/**
 * Created: AriesHoo on 2018/1/30/030 9:10
 * E-Mail: AriesHoo@126.com
 * Function: 虚拟导航栏控制帮助类
 * Description:
 */
public class NavigationViewHelper {

    public final static int TAG_SET_NAVIGATION_CONTROL = 0x10000011;
    private String TAG = getClass().getSimpleName();
    private SoftReference<Activity> mActivity;
    private boolean mControlEnable;
    private boolean mTransEnable;
    private boolean mPlusNavigationViewEnable;
    private boolean mControlBottomEditTextEnable = true;
    @ColorInt
    private int mNavigationViewColor = Color.TRANSPARENT;
    private Drawable mNavigationViewDrawable = new ColorDrawable(mNavigationViewColor);
    private Drawable mNavigationLayoutDrawable = new ColorDrawable(Color.WHITE);
    private View mBottomView;//设置activity最底部View用于增加导航栏的padding

    private View mContentView;//activity xml设置根布局
    private LinearLayout mLinearLayout;
    private LinearLayout mLayoutNavigation;

    private NavigationViewHelper(Activity activity) {
        mActivity = new SoftReference<>(activity);
    }

    public static NavigationViewHelper with(Activity activity) {
        if (activity == null) {
            throw new NullPointerException(FastConstant.EXCEPTION_ACTIVITY_NOT_NULL);
        }
        return new NavigationViewHelper(activity);
    }

    /**
     * 设置是否控制虚拟导航栏
     *
     * @param controlEnable
     * @return
     */
    public NavigationViewHelper setControlEnable(boolean controlEnable) {
        mControlEnable = controlEnable;
        if (!controlEnable) {
            setPlusNavigationViewEnable(true)
                    .setNavigationViewColor(Color.BLACK)
                    .setNavigationLayoutColor(Color.BLACK);
        }
        return this;
    }

    /**
     * 设置是否全透明
     *
     * @param transEnable
     * @return
     */
    public NavigationViewHelper setTransEnable(boolean transEnable) {
        this.mTransEnable = transEnable;
        setNavigationLayoutColor(Color.WHITE);
        return setNavigationViewColor(transEnable ? Color.TRANSPARENT : Color.argb(102, 0, 0, 0));
    }

    /**
     * 是否设置假的导航栏--用于沉浸遮挡
     *
     * @param plusNavigationViewEnable
     * @return
     */
    public NavigationViewHelper setPlusNavigationViewEnable(boolean plusNavigationViewEnable) {
        this.mPlusNavigationViewEnable = plusNavigationViewEnable;
        return this;
    }

    /**
     * 设置是否自动控制底部输入框
     *
     * @param controlBottomEditTextEnable
     * @return
     */
    public NavigationViewHelper setControlBottomEditTextEnable(boolean controlBottomEditTextEnable) {
        mControlBottomEditTextEnable = controlBottomEditTextEnable;
        return this;
    }

    /**
     * 设置 NavigationView背景颜色
     *
     * @param navigationViewColor
     * @return
     */
    public NavigationViewHelper setNavigationViewColor(@ColorInt int navigationViewColor) {
        this.mNavigationViewColor = navigationViewColor;
        return setNavigationViewDrawable(new ColorDrawable(navigationViewColor));
    }

    /**
     * 设置假NavigationView背景资源
     *
     * @param drawable
     * @return
     */
    public NavigationViewHelper setNavigationViewDrawable(Drawable drawable) {
        this.mNavigationViewDrawable = drawable;
        return this;
    }

    /**
     * 设置假NavigationView父ViewGroup背景颜色
     *
     * @param navigationLayoutColor
     * @return
     */
    public NavigationViewHelper setNavigationLayoutColor(@ColorInt int navigationLayoutColor) {
        return setNavigationLayoutDrawable(new ColorDrawable(navigationLayoutColor));
    }

    /**
     * 设置假NavigationView父ViewGroup背景资源
     *
     * @param navigationLayoutDrawable
     * @return
     */
    public NavigationViewHelper setNavigationLayoutDrawable(Drawable navigationLayoutDrawable) {
        this.mNavigationLayoutDrawable = navigationLayoutDrawable;
        return this;
    }

    /**
     * 设置最底部--虚拟状态栏上边的View
     *
     * @param bottomView
     * @return
     */
    public NavigationViewHelper setBottomView(View bottomView) {
        mBottomView = bottomView;
        return this;
    }

    /**
     * 开始设置NavigationView相应效果
     */
    public void init() {
        Activity activity = mActivity.get();
        if (activity == null || activity.isFinishing()) {
            throw new NullPointerException(FastConstant.EXCEPTION_ACTIVITY_NOT_EXIST);
        }
        setControlEnable(mControlEnable);
        final Window window = activity.getWindow();
        mContentView = ((ViewGroup) window.getDecorView().findViewById(android.R.id.content)).getChildAt(0);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && ((!mPlusNavigationViewEnable && mTransEnable) || mPlusNavigationViewEnable)) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        //控制底部输入框
        if (mControlBottomEditTextEnable) {
            setBottomView(!mPlusNavigationViewEnable && mControlEnable ? null : mBottomView);
            KeyboardHelper.with(activity)
                    .setControlNavigationBar(!mPlusNavigationViewEnable && mControlEnable)
                    .setEnable();
        }
        addNavigationBar(window);
        if (mLayoutNavigation != null) {
            mLayoutNavigation.setVisibility(mPlusNavigationViewEnable ? View.VISIBLE : View.GONE);
        }
        if (mBottomView != null) {
            if (!mPlusNavigationViewEnable)
                mBottomView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ViewGroup.LayoutParams params = mBottomView.getLayoutParams();
                        if (params != null && params.height >= 0) {//默认
                            params.height = params.height + NavigationBarUtil.getNavigationBarHeight(window.getWindowManager());
                        }
                        Object isSet = mBottomView.getTag(TAG_SET_NAVIGATION_CONTROL);
                        if (isSet == null) {
                            mBottomView.setPadding(
                                    mBottomView.getPaddingLeft(),
                                    mBottomView.getPaddingTop(),
                                    mBottomView.getPaddingRight(),
                                    mBottomView.getPaddingBottom() +
                                            NavigationBarUtil.getNavigationBarHeight(window.getWindowManager()));
                        }
                        mBottomView.setTag(TAG_SET_NAVIGATION_CONTROL, true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mBottomView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            mBottomView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });
        }
    }

    private void addNavigationBar(Window window) {
        if (!isSupportNavigationBarControl(window)) {
            return;
        }
        if (mLinearLayout == null) {
            getLinearLayout(window.getDecorView());
        }
        if (mLinearLayout != null && mPlusNavigationViewEnable) {
            LoggerManager.i(TAG, "id:" + mLinearLayout.getId());
            final LinearLayout linearLayout = mLinearLayout;
            Context mContext = window.getContext();
            if (linearLayout.getChildCount() >= 2) {//其实也只有2个子View
                View viewChild = linearLayout.getChildAt(1);
                //设置LinearLayout第二个View占用屏幕高度权重为1
                // 预留假的NavigationView位置并保证Navigation始终在最底部--被虚拟导航栏遮住
                viewChild.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));

                //创建假的NavigationView包裹ViewGroup用于设置背景与mContentView一致
                mLayoutNavigation = (LinearLayout) linearLayout.findViewById(R.id.fast_fake_navigation_layout);
                View viewNavigation;
                if (mLayoutNavigation == null) {
                    mLayoutNavigation = new LinearLayout(mContext);
                    mLayoutNavigation.setId(R.id.fast_fake_navigation_layout);
                    //创建假的NavigationView
                    viewNavigation = new View(mContext);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            NavigationBarUtil.getNavigationBarHeight(window.getWindowManager()));
                    viewNavigation.setId(R.id.fast_fake_navigation_view);
                    mLayoutNavigation.addView(viewNavigation, params);
                    linearLayout.addView(mLayoutNavigation,
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                } else {
                    viewNavigation = mLayoutNavigation.findViewById(R.id.fast_fake_navigation_view);
                }
                if (mLayoutNavigation != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mLayoutNavigation.setBackground(mNavigationLayoutDrawable);
                        viewNavigation.setBackground(mNavigationViewDrawable);
                    } else {
                        mLayoutNavigation.setBackgroundDrawable(mNavigationLayoutDrawable);
                        viewNavigation.setBackgroundDrawable(mNavigationViewDrawable);
                    }
                }
            }
        }
    }

    protected boolean isSupportNavigationBarControl(Window window) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                NavigationBarUtil.hasSoftKeys(window.getWindowManager());
    }

    /**
     * 获取window的DecorView的第一个LinearLayout用于设置假的NavigationView
     *
     * @param rootView
     */
    private void getLinearLayout(View rootView) {
        if (rootView instanceof LinearLayout && mLinearLayout == null) {
            mLinearLayout = (LinearLayout) rootView;
            LoggerManager.i(TAG, "SimpleName:" + rootView.getClass().getSimpleName() + ";children:" + mLinearLayout.getChildCount());
        } else if (rootView instanceof ViewGroup) {
            ViewGroup contentView = (ViewGroup) rootView;
            LoggerManager.i(TAG, "SimpleName:" + rootView.getClass().getSimpleName() + ";children:" + contentView.getChildCount());
            int childCount = contentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = contentView.getChildAt(i);
                getLinearLayout(childView);
            }
        } else {
            LoggerManager.i(TAG, "SimpleName:" + rootView.getClass().getSimpleName());
        }
    }

}
