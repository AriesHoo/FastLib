package com.aries.library.fast.basis;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aries.library.fast.interfaces.IBasisView;
import com.aries.library.fast.manager.RxJavaManager;

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
    protected boolean isViewLoaded = false;
    protected Unbinder mUnBinder;

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
        EventBus.getDefault().register(this);
        beforeInitView();
        initView(savedInstanceState);
        isViewLoaded = true;
        return mContentView;
    }

    @Nullable
    public <T extends View> T findViewById(@IdRes int viewId) {
        return (T) mContentView.findViewById(viewId);
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
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
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
        if (!hidden) lazyLoad();
    }

    /**
     * 在viewpager中的Fragment懒加载
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isViewLoaded) {
                RxJavaManager.getInstance().setTimer(500, new RxJavaManager.TimerListener() {
                    @Override
                    public void timeEnd() {
                        isViewLoaded = true;
                        lazyLoad();
                    }
                });
            } else {
                lazyLoad();
            }
        }
    }

    private void lazyLoad() {
        if (mIsFirstShow && isViewLoaded) {
            mIsFirstShow = false;
            loadData();
        }
    }
}
