package com.aries.library.fast.module.activity;

import com.aries.library.fast.basis.BasisActivity;
import com.aries.library.fast.delegate.FastTitleDelegate;
import com.aries.library.fast.interfaces.IFastTitleView;
import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2017/7/3 16:04
 * Function: title 基类
 * Desc:
 */

public abstract class FastTitleActivity extends BasisActivity implements IFastTitleView {

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
