package com.aries.library.fast.basis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.R;
import com.aries.library.fast.i.IBasisView;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.util.FastUtil;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGAKeyboardUtil;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created: AriesHoo on 2017/7/19 15:37
 * E-Mail: AriesHoo@126.com
 * Function: 所有Activity的基类
 * Description:
 * 1、2018-6-15 09:31:42 调整滑动返回类控制
 * 2、2018-6-20 17:15:12 调整主页back键操作逻辑
 */
public abstract class BasisActivity extends RxAppCompatActivity implements IBasisView, BGASwipeBackHelper.Delegate {

    protected Activity mContext;
    protected View mContentView;
    protected Unbinder mUnBinder;
    protected BGASwipeBackHelper mSwipeBackHelper;

    protected boolean mIsViewLoaded = false;
    protected boolean mIsFirstShow = true;
    protected boolean mIsFirstBack = true;
    protected long mDelayBack = 2000;
    protected final String TAG = getClass().getSimpleName();
    protected NavigationViewHelper mNavigationViewHelper;

    @Nullable
    public <T extends View> T findViewByViewId(@IdRes int viewId) {
        return (T) findViewById(viewId);
    }

    @Override
    public boolean isEventBusEnable() {
        return true;
    }

    /**
     * 设置init之前用于调整属性
     *
     * @param navigationHelper
     */
    protected void beforeControlNavigation(NavigationViewHelper navigationHelper) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isEventBusEnable())
            EventBus.getDefault().register(this);
        initSwipeBack();
        super.onCreate(savedInstanceState);
        mContext = this;
        beforeSetContentView();
        mContentView = View.inflate(mContext, getContentLayout(), null);
        setContentView(mContentView);
        mUnBinder = ButterKnife.bind(this);
        mIsViewLoaded = true;
        beforeInitView();
        setControlNavigation();
        initView(savedInstanceState);
    }

    @Override
    protected void onResume() {
        beforeLazyLoad();
        super.onResume();
    }

    @Override
    public void finish() {
        BGAKeyboardUtil.closeKeyboard(this);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        if (isEventBusEnable())
            EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    /**
     * 初始化滑动返回
     */
    private void initSwipeBack() {
        if (!FastUtil.isClassExist("cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper")) {
            LoggerManager.e(TAG, "initSwipeBack:Please compile 'cn.bingoogolapple:bga-swipebacklayout:1.1.8@aar' in app main program");
            return;
        }
        mSwipeBackHelper = new BGASwipeBackHelper(this, this)
                .setShadowResId(R.drawable.bga_sbl_shadow)
                .setIsNavigationBarOverlap(true);
        FastManager.getInstance().getSwipeBackControl().setSwipeBack(this, mSwipeBackHelper);
    }

    @Override
    public void beforeSetContentView() {
    }

    @SuppressLint("ResourceType")
    @Override
    public void beforeInitView() {
        FastManager.getInstance().getActivityFragmentControl().setContentViewBackground(mContentView, this.getClass());
    }

    /**
     * 设置NavigationView控制
     */
    private void setControlNavigation() {
        mNavigationViewHelper = FastManager.getInstance().getNavigationBarControl()
                .createNavigationBarControl(this, mContentView);
        beforeControlNavigation(mNavigationViewHelper);
        mNavigationViewHelper.init();
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


    /**
     * 退出程序
     */
    protected void quitApp() {
        mDelayBack = FastManager.getInstance().getQuitAppControl().quipApp(mIsFirstBack, this);
        if (mDelayBack <= 0) {
            FastManager.getInstance().getQuitAppControl().quipApp(false, this);
            return;
        }
        //编写逻辑
        if (mIsFirstBack) {
            mIsFirstBack = false;
            RxJavaManager.getInstance().setTimer(mDelayBack, new RxJavaManager.TimerListener() {
                @Override
                public void timeEnd() {
                    mIsFirstBack = true;
                }
            });
            return;
        }
        //通知执行操作
        FastManager.getInstance().getQuitAppControl().quipApp(mIsFirstBack, this);
    }

    @Override
    public void onBackPressed() {
        if (mSwipeBackHelper == null) {
            super.onBackPressed();
            return;
        }
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        super.onBackPressed();
    }

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
        if (mSwipeBackHelper != null) {
            mSwipeBackHelper.swipeBackward();
        }
    }
}
