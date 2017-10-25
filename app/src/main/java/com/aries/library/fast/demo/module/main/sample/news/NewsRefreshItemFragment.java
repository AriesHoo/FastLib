package com.aries.library.fast.demo.module.main.sample.news;

import android.os.Bundle;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.adapter.WidgetAdapter;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.module.fragment.FastRefreshLoadFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * Created: AriesHoo on 2017/8/7 11:38
 * Function: 腾讯新闻-刷新
 * Desc:
 */

public class NewsRefreshItemFragment extends FastRefreshLoadFragment {

    public static NewsRefreshItemFragment newInstance() {
        Bundle args = new Bundle();
        NewsRefreshItemFragment fragment = new NewsRefreshItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fast_layout_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public RefreshHeader getRefreshHeader() {
        return new ClassicsHeader(mContext);
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        return new WidgetAdapter();
    }

    @Override
    public void loadData(int page) {
        mRefreshLayout.autoRefresh();
        RxJavaManager.getInstance().setTimer(1000, new RxJavaManager.TimerListener() {
            @Override
            public void timeEnd() {
                mRefreshLayout.finishRefresh();
            }
        });
    }
}
