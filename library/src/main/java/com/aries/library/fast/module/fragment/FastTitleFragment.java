package com.aries.library.fast.module.fragment;

import android.os.Bundle;

import com.aries.library.fast.FastLifecycleCallbacks;
import com.aries.library.fast.basis.BasisFragment;
import com.aries.library.fast.i.IFastTitleView;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.view.title.TitleBarView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * @Author: AriesHoo on 2018/7/23 10:34
 * @E-Mail: AriesHoo@126.com
 * Function: 设置有TitleBar的Fragment
 * Description:
 * 1、2019-3-25 17:03:43 推荐使用{@link IFastTitleView}通过接口方式由FastLib自动处理{@link FastLifecycleCallbacks#onFragmentStarted(FragmentManager, Fragment)}
 */
public abstract class FastTitleFragment extends BasisFragment implements IFastTitleView {

    protected TitleBarView mTitleBar;

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mTitleBar = FindViewUtil.getTargetView(mContentView, TitleBarView.class);
    }
}
