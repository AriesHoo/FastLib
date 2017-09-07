package com.aries.library.fast.module.fragment;

import com.aries.library.fast.delegate.FastTitleDelegate;
import com.aries.library.fast.i.IFastTitleView;
import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2017/7/21 10:10
 * Function: 设置有TitleBar及下拉刷新Fragment
 * Desc:
 */

public abstract class FastTitleRefreshLoadFragment<T> extends FastRefreshLoadFragment<T> implements IFastTitleView {

    protected FastTitleDelegate mFastTitleDelegate;
    protected TitleBarView mTitleBar;

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {

    }

    @Override
    public boolean isLightStatusBarEnable() {
        return true;
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        mFastTitleDelegate = new FastTitleDelegate(mContentView, mContext, this);
        mTitleBar = mFastTitleDelegate.titleBar;
    }
}
