package com.aries.library.fast.delegate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.R;
import com.aries.library.fast.i.IFastRefreshLoadView;
import com.aries.library.fast.i.IMultiStatusView;
import com.aries.ui.util.FindViewUtil;
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
    private FastManager mManager;

    public FastRefreshLoadDelegate(View rootView, IFastRefreshLoadView<T> iFastRefreshLoadView) {
        this.mIFastRefreshLoadView = iFastRefreshLoadView;
        this.mContext = rootView.getContext().getApplicationContext();
        this.mManager = FastManager.getInstance();
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
                    mManager.getMultiStatusView().createMultiStatusView();
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
                mManager.getDefaultRefreshHeader()
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
                    mManager.getLoadMoreFoot().createDefaultLoadMoreView(mAdapter));
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
        mRefreshLayout = rootView.findViewById(R.id.smartLayout_rootFastLib);
        if (mRefreshLayout == null) {
            mRefreshLayout = FindViewUtil.getTargetView(rootView, SmartRefreshLayout.class);
        }
    }

    /**
     * 获取布局RecyclerView
     *
     * @param rootView
     */
    private void getRecyclerView(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.rv_contentFastLib);
        if (mRecyclerView == null) {
            mRecyclerView = FindViewUtil.getTargetView(rootView, RecyclerView.class);
        }
    }

    /**
     * 获取布局EasyStatusView
     *
     * @param rootView
     */
    private void getStatusView(View rootView) {
        mStatusView = rootView.findViewById(R.id.esv_layoutFastLib);
        if (mStatusView == null) {
            mStatusView = FindViewUtil.getTargetView(rootView, EasyStatusView.class);
        }
    }
}
