package com.aries.library.fast.demo.module.main.sample;

import android.graphics.Color;
import android.os.Bundle;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.i.IFastRefreshView;
import com.aries.library.fast.i.IFastTitleView;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainTextMarquee(true);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh();
    }

    @Override
    public void setRefreshLayout(SmartRefreshLayout refreshLayout) {
        int statusHeight = StatusBarUtil.getStatusBarHeight() + getResources().getDimensionPixelSize(R.dimen.dp_title_height);
        refreshLayout.setHeaderInsetStartPx(statusHeight)
                .setRefreshHeader(new MaterialHeader(this)
                        .setColorSchemeColors(Color.MAGENTA, Color.BLUE));
        refreshLayout.autoRefresh();
    }
}
