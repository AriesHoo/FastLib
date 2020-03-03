package com.aries.library.fast.module.activity;

import android.app.Activity;
import android.os.Bundle;

import com.aries.library.fast.FastLifecycleCallbacks;
import com.aries.library.fast.basis.BasisActivity;
import com.aries.library.fast.i.IFastTitleView;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.view.title.TitleBarView;

/**
 * @Author: AriesHoo on 2018/7/23 10:35
 * @E-Mail: AriesHoo@126.com
 * Function: 包含TitleBarView的Activity
 * Description:
 * 1、2019-3-25 17:03:43 推荐使用{@link IFastTitleView}通过接口方式由FastLib自动处理{@link FastLifecycleCallbacks#onActivityStarted(Activity)}
 */
public abstract class FastTitleActivity extends BasisActivity implements IFastTitleView {

    protected TitleBarView mTitleBar;

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mTitleBar = FindViewUtil.getTargetView(mContentView, TitleBarView.class);
    }

    @Override
    protected void onDestroy() {
        mTitleBar = null;
        super.onDestroy();
    }
}
