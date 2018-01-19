package com.aries.library.fast.basis;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.aries.library.fast.FastConfig;
import com.aries.library.fast.entity.FastNavigationConfigEntity;
import com.aries.library.fast.entity.FastQuitConfigEntity;
import com.aries.library.fast.i.IBasisView;
import com.aries.library.fast.i.NavigationBarControl;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.NavigationBarUtil;
import com.aries.library.fast.util.SnackBarUtil;
import com.aries.library.fast.util.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created: AriesHoo on 2017/7/19 15:37
 * E-Mail: AriesHoo@126.com
 * Function: 所有Activity的基类
 * Description:
 */
public abstract class BasisActivity extends RxAppCompatActivity implements IBasisView {

    protected Activity mContext;
    protected View mContentView;
    protected Unbinder mUnBinder;
    protected BGASwipeBackHelper mSwipeBackHelper;

    protected boolean mIsViewLoaded = false;
    protected boolean mIsFirstShow = true;
    protected boolean mIsFirstBack = true;
    private boolean mIsGlobal = false;
    protected long mDelayBack = 2000;
    protected final String TAG = getClass().getSimpleName();
    protected FastQuitConfigEntity mQuitEntity;
    protected ViewGroup mLayoutNavigation;

    protected NavigationBarControl getNavigationBarControl() {
        return FastConfig.getInstance(this).getNavigationBarControl();
    }

    protected View getNavigationBarControlView() {
        return mContentView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        LoggerManager.i(TAG, "getRequestedOrientation:" + (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
        //先判断xml没有设置屏幕模式避免将开发者本身想设置的覆盖掉
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            setRequestedOrientation(getOrientation());
        }
        super.onCreate(savedInstanceState);
        mContext = this;
        addNavigationBar();
        FastStackUtil.getInstance().push(this);
        initSwipeBack();
        beforeSetContentView();
        mContentView = View.inflate(mContext, getContentLayout(), null);
        setContentView(mContentView);
        mUnBinder = ButterKnife.bind(this);
        mIsViewLoaded = true;
        setNavigationBar();
        beforeInitView();
        initView(savedInstanceState);
    }

    /**
     * 添加一层假NavigationView用于占位
     */
    private void addNavigationBar() {
        if (!isSupportNavigationBarControl()) {
            return;
        }
        FastNavigationConfigEntity entity = getNavigationBarControl().createNavigationBarControl(this);
        if (entity == null || !entity.isControlEnable() || !entity.isAddNavigationViewEnable()) {
            return;
        }
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        ViewGroup root = (ViewGroup) getWindow().getDecorView();
        LoggerManager.i(TAG, "widow-DecorView的第一个子View为:" + root.getChildAt(0).getClass().getSimpleName());
        if (root.getChildAt(0) instanceof LinearLayout) {
            final LinearLayout linearLayout = (LinearLayout) root.getChildAt(0);
            LoggerManager.i(TAG, "widow-DecorView的第一个子View总有子View数量为:" + linearLayout.getChildCount());
            if (linearLayout.getChildCount() >= 2) {//其实也只有2个子View
                View viewChild = linearLayout.getChildAt(1);
                //设置LinearLayout第二个View占用屏幕高度权重为1
                // 预留假的NavigationView位置并保证Navigation始终在最底部--被虚拟导航栏遮住
                viewChild.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));
                //创建假的NavigationView
                View navigationView = new View(mContext);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        NavigationBarUtil.getNavigationBarHeight(getWindowManager()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    navigationView.setBackground(entity.getDrawable());
                } else {
                    navigationView.setBackgroundDrawable(entity.getDrawable());
                }
                //创建假的NavigationView包裹ViewGroup用于设置背景与mContentView一致
                mLayoutNavigation = new LinearLayout(mContext);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mLayoutNavigation.setBackground(entity.getBackgroundDrawable());
                }else {
                    mLayoutNavigation.setBackgroundDrawable(entity.getBackgroundDrawable());
                }
                mLayoutNavigation.addView(navigationView, params);

                linearLayout.addView(mLayoutNavigation,
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    /**
     * 控制虚拟导航栏
     */
    private void setNavigationBar() {
        if (!isSupportNavigationBarControl()) {
            return;
        }
        FastNavigationConfigEntity entity = getNavigationBarControl().createNavigationBarControl(this);
        if (entity == null || !entity.isControlEnable() || entity.isAddNavigationViewEnable()) {
            return;
        }
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && entity.isTransEnable()) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setNavigationBarColor(entity.getColor());
        }
        final View controlView = getNavigationBarControlView();
        if (controlView != null && NavigationBarUtil.hasSoftKeys(getWindowManager())) {
            controlView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (mIsGlobal) {
                        return;
                    }
                    mIsGlobal = true;
                    ViewGroup.LayoutParams params = controlView.getLayoutParams();
                    if (params != null && params.height >= 0) {//默认
                        params.height = params.height + NavigationBarUtil.getNavigationBarHeight(getWindowManager());
                    }
                    controlView.setPadding(
                            controlView.getPaddingLeft(),
                            controlView.getPaddingTop(),
                            controlView.getPaddingRight(),
                            controlView.getPaddingBottom() +
                                    NavigationBarUtil.getNavigationBarHeight(getWindowManager()));
                }
            });
        }
    }

    @Override
    protected void onResume() {
        beforeLazyLoad();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        FastStackUtil.getInstance().pop(this, false);
    }

    /**
     * 是否开启滑动返回
     */
    protected boolean isSwipeBackEnable() {
        return FastConfig.getInstance(this).isSwipeBackEnable();
    }

    @Override
    public int getContentBackground() {
        return FastConfig.getInstance(this).getContentViewBackgroundResource();
    }

    /**
     * 设置屏幕方向
     * 默认自动 ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
     * 竖屏 ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
     * 横屏 ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
     * {@link ActivityInfo#screenOrientation ActivityInfo.screenOrientation}
     *
     * @return
     */
    public int getOrientation() {
        return FastConfig.getInstance(this).getRequestedOrientation();
    }

    /**
     * 初始化滑动返回
     */
    private void initSwipeBack() {
        if (!FastUtil.isClassExist("cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper")) {
            LoggerManager.e(TAG, "initSwipeBack:Please compile 'cn.bingoogolapple:bga-swipebacklayout:1.1.1@aar' in app main program");
            return;
        }
        if (isSwipeBackEnable()) {
            mSwipeBackHelper = new BGASwipeBackHelper(this, new BGASwipeBackHelper.Delegate() {
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
                    //滑动返回执行完毕，销毁当前 Activity
                    mSwipeBackHelper.swipeBackward();
                }
            });
        }
    }

    @Nullable
    public <T extends View> T findViewByViewId(@IdRes int viewId) {
        return (T) findViewById(viewId);
    }

    @Override
    public void beforeSetContentView() {
        mQuitEntity = FastConfig.getInstance(this).getQuitConfig();
        mDelayBack = mQuitEntity.getQuitDelay();
    }

    @Override
    public void beforeInitView() {
        if (getContentBackground() > 0) {
            mContentView.setBackgroundResource(getContentBackground());
        }
    }

    @Override
    public void loadData() {

    }

    private void beforeLazyLoad() {
        if (!mIsViewLoaded) {//确保视图加载及视图绑定完成避免刷新UI抛出异常
            RxJavaManager.getInstance().setTimer(10, new RxJavaManager.TimerListener() {
                @Override
                public void timeEnd() {
                    beforeLazyLoad();
                }
            });
        } else {
            lazyLoad();
        }
    }

    private void lazyLoad() {
        if (mIsFirstShow) {
            mIsFirstShow = false;
            loadData();
        }
    }

    protected void quitApp() {
        quitApp(mQuitEntity.isSnackBarEnable(), mQuitEntity.isBackToTaskEnable());
    }

    /**
     * 退出程序
     *
     * @param isSnackBar
     */
    protected void quitApp(boolean isSnackBar, boolean isBackToTask) {
        if (isBackToTask && !mQuitEntity.isBackToTaskDelayEnable()) {//设置退回桌面且不等待
            moveTaskToBack(true);
            return;
        }
        if (mIsFirstBack) {
            if (isSnackBar) {
                SnackBarUtil.with(mContentView)
                        .setBgColor(mQuitEntity.getSnackBarBackgroundColor())
                        .setMessageColor(mQuitEntity.getSnackBarMessageColor())
                        .setMessage(mQuitEntity.getQuitMessage())
                        .show();
            } else {
                ToastUtil.show(mQuitEntity.getQuitMessage());
            }
            mIsFirstBack = false;
            RxJavaManager.getInstance().setTimer(mDelayBack, new RxJavaManager.TimerListener() {
                @Override
                public void timeEnd() {
                    mIsFirstBack = true;
                }
            }).compose(bindUntilEvent(ActivityEvent.DESTROY));
        } else {
            if (isBackToTask) {
                moveTaskToBack(true);
            } else {
                FastStackUtil.getInstance().exit();
            }
        }
    }

    /**
     * 是否支持虚拟导航栏控制--有虚拟导航栏且配置操作参数
     *
     * @return
     */
    protected boolean isSupportNavigationBarControl() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                NavigationBarUtil.hasSoftKeys(getWindowManager()) &&
                getNavigationBarControl() != null;
    }
}
