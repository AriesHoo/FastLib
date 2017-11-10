package com.aries.library.fast.delegate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aries.library.fast.FastConfig;
import com.aries.library.fast.i.IFastRefreshLoadView;
import com.aries.library.fast.i.IMultiStatusView;
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
    private Context mContext;
    private FastConfig mConfig;

    public FastRefreshLoadDelegate(View rootView, IFastRefreshLoadView<T> iFastRefreshLoadView) {
        this.mIFastRefreshLoadView = iFastRefreshLoadView;
        this.mContext = rootView.getContext().getApplicationContext();
        this.mConfig = FastConfig.getInstance(mContext);
        getRefreshLayout(rootView);
        getRecyclerView(rootView);
        getStatusView(rootView);
        initRefreshHeader();
        initRecyclerView();
        initStatusView();
    }

    /**
     * 初始化多状态布局相关配置
     */
    private void initStatusView() {
        if (mStatusView != null) {
            IMultiStatusView iMultiStatusView = mIFastRefreshLoadView.getMultiStatusView() != null ? mIFastRefreshLoadView.getMultiStatusView() :
                    mConfig.getDefaultMultiStatusView().createMultiStatusView(mStatusView);
            mStatusView.setLoadingView(iMultiStatusView.getLoadingView());
            mStatusView.setEmptyView(iMultiStatusView.getEmptyView());
            mStatusView.setErrorView(iMultiStatusView.getErrorView());
            mStatusView.setNoNetworkView(iMultiStatusView.getNoNetView());
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
    }

    /**
     * 初始化刷新头配置
     */
    protected void initRefreshHeader() {
        if (mRefreshLayout == null || mIFastRefreshLoadView == null) {
            return;
        }
        mRefreshLayout.setRefreshHeader(mIFastRefreshLoadView.getRefreshHeader() != null
                ? mIFastRefreshLoadView.getRefreshHeader() :
                mConfig.getDefaultRefreshHeader()
                        .createRefreshHeader(mContext, mRefreshLayout));
        mRefreshLayout.setOnRefreshListener(mIFastRefreshLoadView);
        mRefreshLayout.setEnableRefresh(mIFastRefreshLoadView.isRefreshEnable());
    }

    /**
     * 初始化RecyclerView配置
     */
    protected void initRecyclerView() {
        if (mRecyclerView == null || mIFastRefreshLoadView == null) {
            return;
        }
        mAdapter = mIFastRefreshLoadView.getAdapter();
        mRecyclerView.setLayoutManager(mIFastRefreshLoadView.getLayoutManager());
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter != null) {
            setLoadMore(mIFastRefreshLoadView.isLoadMoreEnable());
            mAdapter.setLoadMoreView(mIFastRefreshLoadView.getLoadMoreView() != null
                    ? mIFastRefreshLoadView.getLoadMoreView() :
                    mConfig.getDefaultLoadMoreView().createDefaultLoadMoreView(mAdapter));
            if (mIFastRefreshLoadView.isItemClickEnable()) {
                mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        mIFastRefreshLoadView.onItemClicked(adapter, view, position);
                    }

                });
            }
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
