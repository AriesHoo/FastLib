package com.aries.library.fast.demo.base;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.helper.TitleBarHelper;
import com.aries.library.fast.module.activity.FastTitleActivity;
import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2017/7/27 9:04
 * Function: 包含TitleBarView 的Activity
 * Desc:
 */
public abstract class BaseTitleActivity extends FastTitleActivity {
    @Override
    protected boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    public boolean isLightStatusBarEnable() {
        return true;
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
    }

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
        super.beforeSetTitleBar(titleBar);
        mContentView.setBackgroundResource(R.color.colorBackground);
        TitleBarHelper.getInstance().setTitleBarView(titleBar, mContext);
    }

}
