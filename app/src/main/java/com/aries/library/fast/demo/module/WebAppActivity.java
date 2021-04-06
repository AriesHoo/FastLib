package com.aries.library.fast.demo.module;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.aries.library.fast.demo.R;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

/**
 * @Author: AriesHoo on 2019/4/24 13:41
 * @E-Mail: AriesHoo@126.com
 * @Function: 加载WebApp的Activity
 * @Description:
 */
public class WebAppActivity extends WebViewActivity {

    private static int mColor;

    public static void start(Context mActivity, String url) {
        start(mActivity, url, Color.WHITE);
    }

    public static void start(Context mActivity, String url, int color) {
        mColor = color;
        start(mActivity, WebAppActivity.class, url,true);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setHeight(0)
                .setStatusBarLightMode(mColor == Color.WHITE)
                .setStatusAlpha(mColor == Color.WHITE && !StatusBarUtil.isSupportStatusBarFontChange() ? 60 : 0)
                .setBgColor(mColor)
                .setVisibility(View.VISIBLE);
    }

    @Override
    public void setRefreshLayout(SmartRefreshLayout refreshLayout) {
        super.setRefreshLayout(refreshLayout);
        refreshLayout.setRefreshHeader(new MaterialHeader(mContext).setColorSchemeColors(Color.MAGENTA, Color.BLUE))
                .setPrimaryColorsId(R.color.colorTextBlack)
                .setEnableHeaderTranslationContent(false);
    }
}
