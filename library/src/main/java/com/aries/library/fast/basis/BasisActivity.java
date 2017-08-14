package com.aries.library.fast.basis;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.aries.library.fast.R;
import com.aries.library.fast.interfaces.IBasisView;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.util.ActivityStackUtil;
import com.aries.library.fast.util.ToastUtil;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created: AriesHoo on 2017/7/19 15:37
 * Function: 所有Activity的基类
 * Desc:
 */
public abstract class BasisActivity extends RxActivity implements IBasisView {

    protected Activity mContext;
    protected View mContentView;
    protected Unbinder mUnBinder;
    protected BGASwipeBackHelper mSwipeBackHelper;

    protected boolean isFirstBack = true;
    protected boolean isViewLoaded = false;
    protected boolean mIsFirstShow = true;

    /**
     * 是否开启滑动返回
     */
    protected boolean isSwipeBackEnable() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        mContext = this;
        ActivityStackUtil.getInstance().push(this);
        initSwipeBack();
        beforeSetContentView();
        mContentView = View.inflate(mContext, getContentLayout(), null);
        setContentView(mContentView);
        mUnBinder = ButterKnife.bind(this);
        isViewLoaded = true;
        beforeInitView();
        initView(savedInstanceState);
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

    @Override
    protected void onResume() {
        if (!isViewLoaded) {
            RxJavaManager.getInstance().setTimer(500, new RxJavaManager.TimerListener() {
                @Override
                public void timeEnd() {
                    isViewLoaded = true;
                    goLoad();
                }
            });
        } else {
            goLoad();
        }
        super.onResume();
    }

    private void goLoad() {
        if (mIsFirstShow && isViewLoaded) {
            mIsFirstShow = false;
            loadData();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        ActivityStackUtil.getInstance().pop(this);
        mUnBinder.unbind();
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
            ActivityStackUtil.getInstance().popAll();
            finish();
            System.exit(0);
        }
    }

}
