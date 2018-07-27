package com.aries.library.fast.module.activity;

import android.support.v4.view.ViewPager;

import com.aries.library.fast.R;
import com.aries.library.fast.basis.BasisActivity;
import com.aries.library.fast.delegate.FastMainTabDelegate;
import com.aries.library.fast.i.IFastMainView;

/**
 * @Author: AriesHoo on 2018/7/23 10:00
 * @E-Mail: AriesHoo@126.com
 * Function: 快速创建主页Activity布局
 * Description:
 */
public abstract class FastMainActivity extends BasisActivity implements IFastMainView {

    protected FastMainTabDelegate mFastMainTabDelegate;

    @Override
    public void setViewPager(ViewPager mViewPager) {

    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public int getContentLayout() {
        return isSwipeEnable() ? R.layout.fast_activity_main_view_pager : R.layout.fast_activity_main;
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        mFastMainTabDelegate = new FastMainTabDelegate(mContentView, this, this);
    }


    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onBackPressed() {
        quitApp();
    }
}
