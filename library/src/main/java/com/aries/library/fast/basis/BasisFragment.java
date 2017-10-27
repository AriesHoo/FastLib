package com.aries.library.fast.basis;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aries.library.fast.FastConfig;
import com.aries.library.fast.i.IBasisView;
import com.aries.library.fast.manager.RxJavaManager;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created: AriesHoo on 2017/7/19 16:11
 * Function:所有Fragment的基类
 * Desc:
 */
public abstract class BasisFragment extends RxFragment implements IBasisView {

    protected Activity mContext;
    protected View mContentView;
    protected boolean mIsFirstShow;
    protected boolean mIsViewLoaded;
    protected Unbinder mUnBinder;
    protected final String TAG = getClass().getSimpleName();

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
        mUnBinder = ButterKnife.bind(this, mContentView);
        mIsViewLoaded = true;
        EventBus.getDefault().register(this);
        beforeInitView();
        initView(savedInstanceState);
        return mContentView;
    }

    @Nullable
    public <T extends View> T findViewById(@IdRes int viewId) {
        return (T) mContentView.findViewById(viewId);
    }

    @Override
    public int getContentBackground() {
        return FastConfig.getInstance(mContext).getContentViewBackgroundResource();
    }

    @Override
    public void beforeSetContentView() {

    }

    @Override
    public void beforeInitView() {
        mContentView.setBackgroundResource(getContentBackground());
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
        EventBus.getDefault().unregister(this);
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
