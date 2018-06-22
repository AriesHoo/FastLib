package com.aries.library.fast.module.fragment;

import com.aries.library.fast.basis.BasisFragment;
import com.aries.library.fast.delegate.FastTitleDelegate;
import com.aries.library.fast.i.IFastTitleView;
import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2017/7/21 10:10
 * Function: 设置有TitleBar的Fragment
 * Desc:
 */

public abstract class FastTitleFragment extends BasisFragment implements IFastTitleView {

    protected FastTitleDelegate mFastTitleDelegate;
    protected TitleBarView mTitleBar;

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        mFastTitleDelegate = new FastTitleDelegate(mContentView, this,this.getClass());
        mTitleBar = mFastTitleDelegate.mTitleBar;
    }
}
