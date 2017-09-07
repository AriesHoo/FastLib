package com.aries.library.fast.delegate;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aries.library.fast.i.IFastRefreshLoadView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.marno.easystatelibrary.EasyStatusView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * Created: AriesHoo on 2017/7/26 11:15
 * Function: 快速实现下拉刷新及上拉加载更多代理类
 * Desc:
 */
public class FastRefreshLoadDelegate<T> {

    public SmartRefreshLayout mRefreshLayout;
    public RecyclerView mRecyclerView;
    public BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    public EasyStatusView mStatusView;
    private IFastRefreshLoadView<T> mIFastRefreshLoadView;

    public FastRefreshLoadDelegate(View rootView, IFastRefreshLoadView<T> iFastRefreshLoadView) {
        this.mIFastRefreshLoadView = iFastRefreshLoadView;
        getRefreshLayout(rootView);
        getRecyclerView(rootView);
        getStatusView(rootView);
        if (mRefreshLayout == null || mRecyclerView == null || mIFastRefreshLoadView == null) {
            return;
        }
        if (mStatusView != null) {
            mStatusView.loading();
            mStatusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentStatus = mStatusView.getCurrentStatus();
                    if (currentStatus != 2 && currentStatus != 1) {//非loading且非content状态
                        mStatusView.loading();
                        mIFastRefreshLoadView.onRefresh(mRefreshLayout);
                    }
                }
            });
        }
        initRefreshHeader();
        initRecyclerView();

    }

    protected void initRefreshHeader() {
        if (mIFastRefreshLoadView.isRefreshEnable()) {
            mRefreshLayout.setRefreshHeader(mIFastRefreshLoadView.getRefreshHeader());
            mRefreshLayout.setOnRefreshListener(mIFastRefreshLoadView);
            mRefreshLayout.setEnableRefresh(true);
        } else
            mRefreshLayout.setEnableRefresh(false);
    }

    protected void initRecyclerView() {
        mAdapter = mIFastRefreshLoadView.getAdapter();
        mRecyclerView.setLayoutManager(mIFastRefreshLoadView.getLayoutManager());
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRecyclerView.setAdapter(mAdapter);
        setLoadMore(mIFastRefreshLoadView.isLoadMoreEnable());
        if (mIFastRefreshLoadView.isItemClickEnable() && mAdapter != null) {
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    mIFastRefreshLoadView.onItemClicked(adapter, view, position);
                }

            });
        }
    }

    public void setLoadMore(boolean enable) {
        mAdapter.setOnLoadMoreListener(enable ? mIFastRefreshLoadView : null, mRecyclerView);
    }

    /**
     * 获取布局里的刷新Layout
     *
     * @param rootView
     * @return
     */
    private void getRefreshLayout(View rootView) {
        if (rootView instanceof SmartRefreshLayout && mRefreshLayout == null) {
            mRefreshLayout = (SmartRefreshLayout) rootView;
        } else if (rootView instanceof ViewGroup) {
            ViewGroup contentView = (ViewGroup) rootView;
            int childCount = contentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = contentView.getChildAt(i);
                getRefreshLayout(childView);
            }
        }
    }

    /**
     * 获取布局RecyclerView
     *
     * @param rootView
     */
    private void getRecyclerView(View rootView) {
        if (rootView instanceof RecyclerView && mRecyclerView == null) {
            mRecyclerView = (RecyclerView) rootView;
        } else if (rootView instanceof ViewGroup) {
            ViewGroup contentView = (ViewGroup) rootView;
            int childCount = contentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = contentView.getChildAt(i);
                getRecyclerView(childView);
            }
        }
    }

    /**
     * 获取布局EasyStatusView
     *
     * @param rootView
     */
    private void getStatusView(View rootView) {
        if (rootView instanceof EasyStatusView && mStatusView == null) {
            mStatusView = (EasyStatusView) rootView;
        } else if (rootView instanceof ViewGroup) {
            ViewGroup contentView = (ViewGroup) rootView;
            int childCount = contentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = contentView.getChildAt(i);
                getStatusView(childView);
            }
        }
    }
}
