package com.aries.library.fast.demo.module.main.sample;

import android.graphics.Color;
import android.os.Bundle;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.module.activity.FastTitleActivity;
import com.aries.ui.view.title.TitleBarView;
/**
 * @Author: AriesHoo on 2018/9/19 10:37
 * @E-Mail: AriesHoo@126.com
 * Function: QQ默认主题Title背景渐变
 * Description:
 */
public class QQTitleActivity extends FastTitleActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_qq_title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setLeftTextDrawable(R.drawable.ic_back_white)
                .setStatusBarLightMode(false)
                .setTitleMainTextColor(Color.WHITE)
                .setBgResource(R.drawable.shape_qq_bg);
    }
}
