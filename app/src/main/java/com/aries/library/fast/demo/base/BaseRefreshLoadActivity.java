package com.aries.library.fast.demo.base;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.helper.TitleBarHelper;
import com.aries.library.fast.module.activity.FastRefreshLoadActivity;
import com.aries.ui.view.title.TitleBarView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshHeader;

/**
 * Created: AriesHoo on 2017/7/27 14:05
 * Function:
 * Desc:
 */
public abstract class BaseRefreshLoadActivity<T> extends FastRefreshLoadActivity<T> {

    @Override
    public boolean isLightStatusBarEnable() {
        return true;
    }

    @Override
    protected boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
        super.beforeSetTitleBar(titleBar);
        mContentView.setBackgroundResource(R.color.colorBackground);
        TitleBarHelper.getInstance().setTitleBarView(titleBar, mContext);
    }

    @Override
    public RefreshHeader getRefreshHeader() {
        MaterialHeader materialHeader = new MaterialHeader(mContext);
        materialHeader.setColorSchemeColors(R.color.colorTitleText);
        return materialHeader;
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnableHeaderTranslationContent(false);
            mRefreshLayout.setPrimaryColorsId(R.color.colorTextBlackLight);
        }
    }
}
