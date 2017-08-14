package com.aries.library.fast.demo.module.main;

import android.os.Bundle;
import android.view.View;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.adapter.WidgetAdapter;
import com.aries.library.fast.demo.base.BaseRefreshLoadActivity;
import com.aries.library.fast.demo.entity.WidgetEntity;
import com.aries.library.fast.demo.retrofit.DefaultSubscriber;
import com.aries.library.fast.enums.RxLifeCycle;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created: AriesHoo on 2017/8/7 16:45
 * Function: 项目使用其他第三方库列表
 * Desc:
 */
public class ThirdLibraryActivity extends BaseRefreshLoadActivity<WidgetEntity> {

    private BaseQuickAdapter mAdapter;

    @Override
    public boolean isRefreshEnable() {
        return false;
    }

    @Override
    public boolean isLoadMoreEnable() {
        return false;
    }

    @Override
    protected boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText(R.string.third_part);
    }

    @Override
    public int getContentLayout() {
        return R.layout.fast_layout_title_multi_status_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public BaseQuickAdapter<WidgetEntity, BaseViewHolder> getAdapter() {
        mAdapter = new WidgetAdapter();
        return mAdapter;
    }

    @Override
    public void loadData(int page) {
        List<WidgetEntity> list = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.arrays_library_list_title);
        String[] contents = getResources().getStringArray(R.array.arrays_library_list_content);
        String[] urls = getResources().getStringArray(R.array.arrays_library_list_url);
        for (int i = 0; i < titles.length; i++) {
            list.add(new WidgetEntity(titles[i], contents[i], urls[i]));
        }
        RxJavaManager.getInstance().getDelayObservable(list, 200, TimeUnit.MILLISECONDS)
                .compose(bindLifeCycle(RxLifeCycle.DESTROY))
                .subscribe(new DefaultSubscriber<List<WidgetEntity>>() {
                    @Override
                    public void _onNext(List<WidgetEntity> entity) {
                        mEasyStatusView.content();
                        if (mRefreshLayout.isRefreshing()) {
                            mAdapter.setNewData(entity);
                        } else {
                            mAdapter.addData(entity);
                        }
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadmore();
                        mAdapter.loadMoreComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mEasyStatusView.error();
                        mRefreshLayout.finishRefresh(false);
                        mAdapter.loadMoreComplete();
                    }
                });
    }

    @Override
    public void onItemClicked(BaseQuickAdapter<WidgetEntity, BaseViewHolder> adapter, View view, int position) {
        super.onItemClicked(adapter, view, position);
        WidgetEntity entity = adapter.getItem(position);
        WebViewActivity.start(mContext, entity.url);
    }
}
