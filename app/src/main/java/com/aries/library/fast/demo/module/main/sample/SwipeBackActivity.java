package com.aries.library.fast.demo.module.main.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.module.activity.FastTitleActivity;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.ui.view.radius.RadiusTextView;
import com.aries.ui.view.title.TitleBarView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created: AriesHoo on 2017/7/31 11:27
 * Function: 具有滑动返回Activity
 * Desc:
 */
public class SwipeBackActivity extends FastTitleActivity {

    @BindView(R.id.rtv_swipe) RadiusTextView rtvSwipe;

    private String title;

    @Override
    public int getContentBackground() {
        return R.color.colorBackground;
    }

    public static void start(Activity mActivity, String title) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        FastUtil.startActivity(mActivity, SwipeBackActivity.class, bundle, false);
    }

    @Override
    public boolean isLightStatusBarEnable() {
        return false;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        title = getIntent().getStringExtra("title");
        titleBar.setStatusAlpha(102)
                .setCenterGravityLeft(true)
                .setCenterGravityLeftPadding(SizeUtil.dp2px(24))
                .setTitleMainText(title)
                .setTitleMainTextColor(Color.WHITE)
                .setLeftTextDrawable(R.drawable.ic_back_white)
                .setBackgroundColor(Color.parseColor("#38393E"));
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
