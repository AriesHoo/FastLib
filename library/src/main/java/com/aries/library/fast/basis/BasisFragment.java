package com.aries.library.fast.basis;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.i.IBasisView;
import com.aries.library.fast.i.IFastRefreshLoadView;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.RxJavaManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author: AriesHoo on 2018/7/13 17:49
 * @E-Mail: AriesHoo@126.com
 * Function: 所有Fragment的基类实现懒加载
 * Description:
 * 1、新增控制是否为FragmentActivity的唯一Fragment 方法以优化懒加载方式
 * 2、增加解决StatusLayoutManager与SmartRefreshLayout冲突解决方案
 * 3、2018-7-6 17:12:16 删除IBasisFragment 控制是否单Fragment 通过另一种方式实现
 */
public abstract class BasisFragment extends RxFragment implements IBasisView {

    protected Activity mContext;
    protected View mContentView;
    protected boolean mIsFirstShow;
    protected boolean mIsViewLoaded;
    protected Unbinder mUnBinder;
    protected final String TAG = getClass().getSimpleName();
    protected boolean mIsVisibleChanged = false;

    @Override
    public boolean isEventBusEnable() {
        return true;
    }

    /**
     * 检查Fragment或FragmentActivity承载的Fragment是否只有一个
     *
     * @return
     */
    protected boolean isSingleFragment() {
        int size = 0;
        FragmentManager manager = getFragmentManager();
        if (manager != null && manager.getFragments() != null) {
            size = manager.getFragments().size();
        }
        LoggerManager.i(TAG, TAG + ";FragmentManager承载Fragment数量:" + size);
        return size <= 1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
        mIsFirstShow = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        beforeSetContentView();
        mContentView = inflater.inflate(getContentLayout(), container, false);
        //解决StatusLayoutManager与SmartRefreshLayout冲突
        if (this instanceof IFastRefreshLoadView && mContentView.getClass() == SmartRefreshLayout.class) {
            FrameLayout frameLayout = new FrameLayout(container.getContext());
            if (mContentView.getLayoutParams() != null) {
                frameLayout.setLayoutParams(mContentView.getLayoutParams());
            }
            frameLayout.addView(mContentView);
            mContentView = frameLayout;
        }
        mUnBinder = ButterKnife.bind(this, mContentView);
        mIsViewLoaded = true;
        if (isEventBusEnable()) {
            EventBus.getDefault().register(this);
        }
        beforeInitView();
        initView(savedInstanceState);

        if (isSingleFragment() && !mIsVisibleChanged) {
            if (getUserVisibleHint() || isVisible() || !isHidden()) {
                onVisibleChanged(true);
            }
        }
        LoggerManager.i(TAG, TAG + ";mIsVisibleChanged:" + mIsVisibleChanged
                + ";getUserVisibleHint:" + getUserVisibleHint()
                + ";isHidden:" + isHidden() + ";isVisible:" + isVisible());
        return mContentView;
    }

    @Nullable
    @Override
    public <T extends View> T findView(int viewId) {
        return (T) mContentView.findViewById(viewId);
    }

    @Override
    public void beforeSetContentView() {

    }

    @Override
    public void beforeInitView() {
        if (FastManager.getInstance().getActivityFragmentControl() != null) {
            FastManager.getInstance().getActivityFragmentControl().setContentViewBackground(mContentView, this.getClass());
        }
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        if (isEventBusEnable()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    /**
     * 不在viewpager中Fragment懒加载
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        onVisibleChanged(!hidden);
    }

    /**
     * 在viewpager中的Fragment懒加载
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        onVisibleChanged(isVisibleToUser);
    }

    /**
     * 用户可见变化回调
     *
     * @param isVisibleToUser
     */
    protected void onVisibleChanged(final boolean isVisibleToUser) {
        LoggerManager.i(TAG, "isVisibleToUser:" + isVisibleToUser);
        mIsVisibleChanged = true;
        if (isVisibleToUser) {
            if (!mIsViewLoaded) {//避免因视图未加载子类刷新UI抛出异常
                RxJavaManager.getInstance().setTimer(10, new RxJavaManager.TimerListener() {
                    @Override
                    public void timeEnd() {
                        onVisibleChanged(isVisibleToUser);
                    }
                });
            } else {
                lazyLoad();
            }
        }
    }

    private void lazyLoad() {
        if (mIsFirstShow && mIsViewLoaded) {
            mIsFirstShow = false;
            loadData();
        }
    }
}
