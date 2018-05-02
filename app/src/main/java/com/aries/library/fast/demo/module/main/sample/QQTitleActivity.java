package com.aries.library.fast.demo.module.main.sample;

import android.graphics.Color;
import android.os.Bundle;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.module.activity.FastTitleActivity;
import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2017/8/25 9:18
 * Function: QQ默认主题Title背景渐变
 * Desc:
 */
public class QQTitleActivity extends FastTitleActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_qq_title;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setLeftTextDrawable(R.drawable.ic_back_white)
                .setStatusBarLightMode(false)
                .setTitleMainText("QQ默认主题TitleBar背景渐变")
                .setTitleMainTextColor(Color.WHITE)
                .setBgResource(R.drawable.shape_qq_bg);
    }
}
