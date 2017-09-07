package com.aries.library.fast.basis;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.aries.library.fast.R;
import com.aries.library.fast.i.IBasisView;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.ToastUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created: AriesHoo on 2017/7/19 15:37
 * Function: 所有Activity的基类
 * Desc:
 */
public abstract class BasisActivity extends RxAppCompatActivity implements IBasisView {

    protected Activity mContext;
    protected View mContentView;
    protected Unbinder mUnBinder;
    protected BGASwipeBackHelper mSwipeBackHelper;

    protected boolean isFirstBack = true;
    protected boolean isViewLoaded = false;
    protected boolean mIsFirstShow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        mContext = this;
        FastStackUtil.getInstance().push(this);
        initSwipeBack();
        beforeSetContentView();
        mContentView = View.inflate(mContext, getContentLayout(), null);
        setContentView(mContentView);
        mUnBinder = ButterKnife.bind(this);
        isViewLoaded = true;
        beforeInitView();
        initView(savedInstanceState);
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
        if (mUnBinder != null)
            mUnBinder.unbind();
        FastStackUtil.getInstance().pop(this);
    }

    /**
     * 是否开启滑动返回
     */
    protected boolean isSwipeBackEnable() {
        return false;
    }

    /**
     * 初始化滑动返回
     */
    private void initSwipeBack() {
        if (isSwipeBackEnable())
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

    @Nullable
    public <T extends View> T findViewByViewId(@IdRes int viewId) {
        return (T) findViewById(viewId);
    }

    @Override
    public void beforeSetContentView() {

    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void loadData() {

    }

    private void beforeLazyLoad() {
        if (!isViewLoaded) {//确保视图加载及视图绑定完成避免刷新UI抛出异常
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
        if (isFirstBack) {
            ToastUtil.show(R.string.fast_quit_app);
            isFirstBack = false;
            RxJavaManager.getInstance().setTimer(2000, new RxJavaManager.TimerListener() {
                @Override
                public void timeEnd() {
                    isFirstBack = true;
                }
            });
        } else if (!isFirstBack) {
            FastStackUtil.getInstance().exit();
        }
    }

}
