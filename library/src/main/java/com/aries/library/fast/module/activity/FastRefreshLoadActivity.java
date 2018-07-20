package com.aries.library.fast.module.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.library.fast.delegate.FastRefreshLoadDelegate;
import com.aries.library.fast.i.IFastRefreshLoadView;
import com.aries.library.fast.i.IHttpRequestControl;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @Author: AriesHoo on 2018/7/20 16:54
 * @E-Mail: AriesHoo@126.com
 * Function:下拉刷新及上拉加载更多
 * Description:
 * 1、2018-7-9 09:50:59 修正Adapter 错误造成无法展示列表数据BUG
 * 2、2018-7-20 16:54:47 设置StatusLayoutManager 目标View
 */
public abstract class FastRefreshLoadActivity<T>
        extends FastTitleActivity implements IFastRefreshLoadView<T> {
    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected StatusLayoutManager mStatusManager;
    private BaseQuickAdapter mQuickAdapter;
    protected int DEFAULT_PAGE = 0;
    protected int DEFAULT_PAGE_SIZE = 10;

    protected FastRefreshLoadDelegate<T> mFastRefreshLoadDelegate;
    private Class<?> mClass;

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        mClass = getClass();
        mFastRefreshLoadDelegate = new FastRefreshLoadDelegate<>(mContentView, this);
        mRecyclerView = mFastRefreshLoadDelegate.mRecyclerView;
        mRefreshLayout = mFastRefreshLoadDelegate.mRefreshLayout;
        mStatusManager = mFastRefreshLoadDelegate.mStatusManager;
        mQuickAdapter = mFastRefreshLoadDelegate.mAdapter;
    }

    @Override
    public RefreshHeader getRefreshHeader() {
        return null;
    }

    @Override
    public LoadMoreView getLoadMoreView() {
        return null;
    }

    @Override
    public View getMultiStatusContentView() {
        return null;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    @Override
    public void setMultiStatusView(StatusLayoutManager.Builder statusView) {

    }

    @Override
    public OnStatusChildClickListener getMultiStatusViewChildClickListener() {
        return null;
    }

    @Override
    public IHttpRequestControl getIHttpRequestControl() {
        IHttpRequestControl requestControl = new IHttpRequestControl() {
            @Override
            public SmartRefreshLayout getRefreshLayout() {
                return mRefreshLayout;
            }

            @Override
            public BaseQuickAdapter getRecyclerAdapter() {
                return mQuickAdapter;
            }

            @Override
            public StatusLayoutManager getStatusLayoutManager() {
                return mStatusManager;
            }

            @Override
            public int getCurrentPage() {
                return DEFAULT_PAGE;
            }

            @Override
            public int getPageSize() {
                return DEFAULT_PAGE_SIZE;
            }

            @Override
            public Class<?> getRequestClass() {
                return mClass;
            }
        };
        return requestControl;
    }

    @Override
    public void onItemClicked(BaseQuickAdapter<T, BaseViewHolder> adapter, View view, int position) {

    }

    @Override
    public boolean isItemClickEnable() {
        return true;
    }

    @Override
    public boolean isRefreshEnable() {
        return true;
    }

    @Override
    public boolean isLoadMoreEnable() {
        return true;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        DEFAULT_PAGE = 0;
        mFastRefreshLoadDelegate.setLoadMore(isLoadMoreEnable());
        loadData(DEFAULT_PAGE);
    }

    @Override
    public void onLoadMoreRequested() {
        loadData(++DEFAULT_PAGE);
    }

    @Override
    public void loadData() {
        loadData(DEFAULT_PAGE);
    }
}
