package com.aries.library.fast.demo.base;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.helper.RefreshHeaderHelper;
import com.aries.library.fast.module.fragment.FastRefreshLoadFragment;
import com.scwang.smartrefresh.layout.api.RefreshHeader;

/**
 * Created: AriesHoo on 2017/9/6 21:34
 * Function: 带TitleBarView及下拉刷新加载更多基类Fragment
 * Desc:
 */

public abstract class BaseRefreshLoadFragment<T> extends FastRefreshLoadFragment<T> {

    @Override
    public RefreshHeader getRefreshHeader() {
        return RefreshHeaderHelper.getInstance().getRefreshHeader(mContext);
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
