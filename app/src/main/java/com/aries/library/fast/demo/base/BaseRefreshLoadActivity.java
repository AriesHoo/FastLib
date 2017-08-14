package com.aries.library.fast.demo.base;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.helper.TitleBarHelper;
import com.aries.library.fast.module.activity.FastRefreshLoadActivity;
import com.aries.ui.view.title.TitleBarView;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;

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
    public void beforeSetTitleBar(TitleBarView titleBar) {
        super.beforeSetTitleBar(titleBar);
        mContentView.setBackgroundResource(R.color.colorBackground);
        TitleBarHelper.getInstance().setTitleBarView(titleBar, mContext);
    }

    @Override
    public RefreshHeader getRefreshHeader() {
        return new BezierRadarHeader(mContext);
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
