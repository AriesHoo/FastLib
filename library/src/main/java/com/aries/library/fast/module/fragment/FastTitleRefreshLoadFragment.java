package com.aries.library.fast.module.fragment;

import android.os.Bundle;

import com.aries.library.fast.i.IFastTitleView;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.view.title.TitleBarView;

/**
 * @Author: AriesHoo on 2018/7/23 10:34
 * @E-Mail: AriesHoo@126.com
 * Function: 设置有TitleBar及下拉刷新Fragment
 * Description:
 */
public abstract class FastTitleRefreshLoadFragment<T> extends FastRefreshLoadFragment<T> implements IFastTitleView {

    protected TitleBarView mTitleBar;

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mTitleBar = FindViewUtil.getTargetView(mContentView, TitleBarView.class);
    }
}
