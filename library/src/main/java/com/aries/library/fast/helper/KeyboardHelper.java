package com.aries.library.fast.helper;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.aries.library.fast.util.NavigationBarUtil;

/**
 * Created: AriesHoo on 2018/1/29/029 16:12
 * E-Mail: AriesHoo@126.com
 * Function: 软键盘和虚拟导航栏统一设置
 * Description:
 */
public class KeyboardHelper {
    private Activity mActivity;
    private Window mWindow;
    private View mDecorView;
    private View mContentView;
    private boolean mFlag = false;
    private boolean isControlNavigationBar;
    private int mKeyMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED;

    public static KeyboardHelper with(Activity activity) {
        if (activity == null)
            throw new IllegalArgumentException("Activity不能为null");
        return new KeyboardHelper(activity);
    }

    private KeyboardHelper(Activity activity) {
        this(activity, ((ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0));
    }

    private KeyboardHelper(Activity activity, View contentView) {
        this(activity, null, "", contentView);
    }

    private KeyboardHelper(Activity activity, Dialog dialog, String tag) {
        this(activity, dialog, tag, dialog.getWindow().findViewById(android.R.id.content));
    }

    private KeyboardHelper(Activity activity, Dialog dialog, String tag, View contentView) {
        this.mActivity = activity;
        this.mWindow = dialog != null ? dialog.getWindow() : activity.getWindow();
        this.mDecorView = activity.getWindow().getDecorView();
        this.mContentView = contentView != null ? contentView
                : mWindow.getDecorView().findViewById(android.R.id.content);
        if (!mContentView.equals(mDecorView.findViewById(android.R.id.content)))
            this.mFlag = true;
    }

    private KeyboardHelper(Activity activity, Window window) {
        this.mActivity = activity;
        this.mWindow = window;
        this.mDecorView = activity.getWindow().getDecorView();
        ViewGroup frameLayout = (ViewGroup) mWindow.getDecorView().findViewById(android.R.id.content);
        if (frameLayout.getChildAt(0) != null) {
            this.mFlag = true;
        }
        this.mContentView = frameLayout.getChildAt(0) != null ? frameLayout.getChildAt(0) : frameLayout;
    }

    /**
     * 设置是否控制虚拟导航栏
     *
     * @param controlNavigationBar
     * @return
     */
    public KeyboardHelper setControlNavigationBar(boolean controlNavigationBar) {
        isControlNavigationBar = controlNavigationBar;
        return this;
    }

    /**
     * 监听layout变化
     */
    public KeyboardHelper setEnable() {
        setEnable(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return this;
    }

    /**
     * 设置监听
     *
     * @param mode
     */
    public KeyboardHelper setEnable(int mode) {
        mWindow.setSoftInputMode(mode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时,所要调用的回调函数的接口类
            mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        }
        return this;
    }

    /**
     * 取消监听
     */
    public KeyboardHelper setDisable() {
        setDisable(mKeyMode);
        return this;
    }

    /**
     * 取消监听
     *
     * @param mode
     */
    public KeyboardHelper setDisable(int mode) {
        mWindow.setSoftInputMode(mode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
        return this;
    }

    /**
     * 设置View变化监听
     */
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            mDecorView.getWindowVisibleDisplayFrame(r); //获取当前窗口可视区域大小的
            int height = mDecorView.getContext().getResources().getDisplayMetrics().heightPixels; //获取屏幕密度，不包含导航栏
            int diff = height - r.bottom +
                    (isControlNavigationBar ? NavigationBarUtil.getNavigationBarHeight(mWindow.getWindowManager()) : 0);
            if (diff >= 0) {
                mContentView.setPadding(0, mContentView.getPaddingTop(), 0, diff);
            }
        }
    };
}
