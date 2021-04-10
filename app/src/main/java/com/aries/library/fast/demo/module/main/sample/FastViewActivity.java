package com.aries.library.fast.demo.module.main.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.i.IFastRefreshView;
import com.aries.library.fast.i.IFastTitleView;
import com.aries.ui.view.title.TitleBarView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.header.ClassicsHeader;

/**
 * @Author: AriesHoo on 2019/3/25 15:22
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public class FastViewActivity extends AppCompatActivity implements IFastTitleView, IFastRefreshView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_view);
    }

    @Override
    public View getContentView() {
        return findViewById(R.id.tv_tipFastView);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        TextView textView = new TextView(titleBar.getContext());
        textView.setText("你好");
        textView.setTextColor(Color.BLUE);
//        titleBar.setTitleMainTextMarquee(true);
//        titleBar.getLinearLayout(Gravity.RIGHT);
        titleBar.addRightAction(titleBar.new ViewAction(textView));
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh();
    }

    @Override
    public void setRefreshLayout(SmartRefreshLayout refreshLayout) {
        //如果以根布局传入可能需根据情况预览TitleBar位置
//        int statusHeight = StatusBarUtil.getStatusBarHeight() + getResources().getDimensionPixelSize(R.dimen.dp_title_height);
//        refreshLayout.setHeaderInsetStart(SizeUtil.px2dp(statusHeight))
//                .setRefreshHeader(new MaterialHeader(this)
//                        .setColorSchemeColors(Color.MAGENTA, Color.BLUE))
//                .autoRefresh();
        refreshLayout.setRefreshHeader(new ClassicsHeader(this))
                .setEnableHeaderTranslationContent(true)
                .setPrimaryColors(Color.TRANSPARENT)
                .autoRefresh();
    }
}
