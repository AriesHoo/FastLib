package com.aries.library.fast.demo.module.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.base.BaseTitleActivity;
import com.aries.library.fast.util.AppUtil;
import com.aries.ui.view.radius.RadiusTextView;
import com.aries.ui.view.title.TitleBarView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created: AriesHoo on 2017/7/31 11:27
 * Function: 具有滑动返回Activity
 * Desc:
 */
public class SwipeBackActivity extends BaseTitleActivity {

    @BindView(R.id.rtv_swipe) RadiusTextView rtvSwipe;

    private String title;

    public static void start(Activity mActivity, String title) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        AppUtil.startActivity(mActivity, SwipeBackActivity.class, bundle, false);
    }

    @Override
    public boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    public boolean isLightStatusBarEnable() {
        return false;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        title = getIntent().getStringExtra("title");
        titleBar.setBackgroundColor(Color.parseColor("#38393E"));
        titleBar.setStatusAlpha(102);
        titleBar.setTitleMainText(title);
        titleBar.setLeftTextDrawable(R.drawable.ic_back_white);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_swipe_back;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @OnClick({R.id.rtv_swipe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rtv_swipe:
                SwipeBackActivity.start(mContext, title);
                break;
        }
    }
}
