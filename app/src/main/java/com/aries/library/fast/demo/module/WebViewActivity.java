package com.aries.library.fast.demo.module;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.aries.library.fast.module.activity.FastWebActivity;
import com.aries.ui.view.title.TitleBarView;
import com.just.library.AgentWeb;


/**
 * Created: AriesHoo on 2017/8/8 10:31
 * Function:
 * Desc:
 */
public class WebViewActivity extends FastWebActivity {

    private static boolean mIsShowTitle = true;

    public static void start(Activity mActivity, String url) {
        start(mActivity, url, true);
    }

    public static void start(Activity mActivity, String url, boolean isShowTitle) {
        mIsShowTitle = isShowTitle;
        start(mActivity, WebViewActivity.class, url);
    }

    @Override
    protected boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    public boolean isLightStatusBarEnable() {
        return mIsShowTitle;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        if (!mIsShowTitle) {
            titleBar.setVisibility(View.GONE);
        }
//        titleBar.getLinearLayout(Gravity.LEFT).removeViewAt(1);
//        titleBar.setRightVisible(false);
    }

    @Override
    protected void setAgentWeb(AgentWeb mAgentWeb, AgentWeb.CommonAgentBuilder mAgentBuilder) {
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }
}
