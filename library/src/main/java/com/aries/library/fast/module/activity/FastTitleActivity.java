package com.aries.library.fast.module.activity;

import android.os.Bundle;

import com.aries.library.fast.basis.BasisActivity;
import com.aries.library.fast.delegate.FastTitleDelegate;
import com.aries.library.fast.i.IFastTitleView;
import com.aries.ui.view.title.TitleBarView;

/**
 * @Author: AriesHoo on 2018/7/23 10:35
 * @E-Mail: AriesHoo@126.com
 * Function: 包含TitleBarView的Activity
 * Description:
 */
public abstract class FastTitleActivity extends BasisActivity implements IFastTitleView {

    protected FastTitleDelegate mFastTitleDelegate;
    protected TitleBarView mTitleBar;

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mFastTitleDelegate = new FastTitleDelegate(mContentView, this, this.getClass());
        mTitleBar = mFastTitleDelegate.mTitleBar;
    }
}
