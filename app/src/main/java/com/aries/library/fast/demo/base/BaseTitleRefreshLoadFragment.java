package com.aries.library.fast.demo.base;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.module.fragment.FastTitleRefreshLoadFragment;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshHeader;

/**
 * Created: AriesHoo on 2017/9/6 21:34
 * Function: 带TitleBarView及下拉刷新加载更多基类Fragment
 * Desc:
 */

public abstract class BaseTitleRefreshLoadFragment<T> extends FastTitleRefreshLoadFragment<T> {

    @Override
    public boolean isLightStatusBarEnable() {
        return true;
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
