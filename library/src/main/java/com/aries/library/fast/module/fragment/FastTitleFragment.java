package com.aries.library.fast.module.fragment;

import android.view.View;

import com.aries.library.fast.FastConfig;
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
    public int getLeftIcon() {
        return 0;
    }

    @Override
    public View.OnClickListener getLeftClickListener() {
        return null;
    }

    @Override
    public boolean isLightStatusBarEnable() {
        return FastConfig.getInstance(mContext).isLightStatusBarEnable();
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        mFastTitleDelegate = new FastTitleDelegate(mContentView, mContext, this);
        mTitleBar = mFastTitleDelegate.titleBar;
    }
}
