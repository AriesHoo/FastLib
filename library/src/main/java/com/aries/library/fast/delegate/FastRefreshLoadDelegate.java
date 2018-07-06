package com.aries.library.fast.delegate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.R;
import com.aries.library.fast.i.IFastRefreshLoadView;
import com.aries.library.fast.widget.FastLoadMoreView;
import com.aries.ui.util.FindViewUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * Created: AriesHoo on 2018/6/22 14:45
 * E-Mail: AriesHoo@126.com
 * Function:快速实现下拉刷新及上拉加载更多代理类
 * Description:
 * 1、使用StatusLayoutManager重构多状态布局功能
 */
public class FastRefreshLoadDelegate<T> {

    public SmartRefreshLayout mRefreshLayout;
    public RecyclerView mRecyclerView;
    public BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    public StatusLayoutManager mStatusManager;
    private IFastRefreshLoadView<T> mIFastRefreshLoadView;
    private Context mContext;
    private FastManager mManager;
    public View mRootView;

    public FastRefreshLoadDelegate(View rootView, IFastRefreshLoadView<T> iFastRefreshLoadView) {
        this.mRootView = rootView;
        this.mIFastRefreshLoadView = iFastRefreshLoadView;
        this.mContext = rootView.getContext().getApplicationContext();
        this.mManager = FastManager.getInstance();
        if (mIFastRefreshLoadView == null) {
            return;
        }
        getRefreshLayout(rootView);
        getRecyclerView(rootView);
        initRefreshHeader();
        initRecyclerView();
    }

    /**
     * 初始化刷新头配置
     */
    protected void initRefreshHeader() {
        if (mRefreshLayout == null) {
            return;
        }
        mRefreshLayout.setRefreshHeader(mIFastRefreshLoadView.getRefreshHeader() != null
                ? mIFastRefreshLoadView.getRefreshHeader() :
                mManager.getDefaultRefreshHeader() != null ?
                        mManager.getDefaultRefreshHeader().createRefreshHeader(mContext, mRefreshLayout) :
                        new ClassicsHeader(mContext).setSpinnerStyle(SpinnerStyle.Translate));
        mRefreshLayout.setOnRefreshListener(mIFastRefreshLoadView);
        mRefreshLayout.setEnableRefresh(mIFastRefreshLoadView.isRefreshEnable());
        setStatusManager(mRefreshLayout);
    }

    /**
     * 初始化RecyclerView配置
     */
    protected void initRecyclerView() {
        if (mRecyclerView == null) {
            return;
        }
        mAdapter = mIFastRefreshLoadView.getAdapter();
        mRecyclerView.setLayoutManager(mIFastRefreshLoadView.getLayoutManager());
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter != null) {
            setLoadMore(mIFastRefreshLoadView.isLoadMoreEnable());
            //先判断是否Activity/Fragment设置过;再判断是否有全局设置;最后设置默认
            mAdapter.setLoadMoreView(mIFastRefreshLoadView.getLoadMoreView() != null
                    ? mIFastRefreshLoadView.getLoadMoreView() :
                    mManager.getLoadMoreFoot() != null ?
                            mManager.getLoadMoreFoot().createDefaultLoadMoreView(mAdapter) :
                            new FastLoadMoreView(mContext).getBuilder().build());
            if (mIFastRefreshLoadView.isItemClickEnable()) {
                mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        mIFastRefreshLoadView.onItemClicked(adapter, view, position);
                    }

                });
            }
        }
        if (mRefreshLayout != null) {
            return;
        }
        setStatusManager(mRecyclerView);
    }

    public void setLoadMore(boolean enable) {
        mAdapter.setOnLoadMoreListener(enable ? mIFastRefreshLoadView : null, mRecyclerView);
    }

    private void setStatusManager(View contentView) {
        if (contentView == null) {
            return;
        }
        StatusLayoutManager.Builder builder = new StatusLayoutManager.Builder(contentView)
                .setDefaultLayoutsBackgroundColor(android.R.color.transparent)
                .setDefaultEmptyText(R.string.fast_multi_empty)
                .setDefaultEmptyClickViewTextColor(contentView.getResources().getColor(R.color.colorTitleText))
                .setDefaultLoadingText(R.string.fast_multi_loading)
                .setDefaultErrorText(R.string.fast_multi_error)
                .setDefaultErrorClickViewTextColor(contentView.getResources().getColor(R.color.colorTitleText))
                .setOnStatusChildClickListener(mIFastRefreshLoadView.getMultiStatusViewChildClickListener() != null ?
                        mIFastRefreshLoadView.getMultiStatusViewChildClickListener() :
                        new OnStatusChildClickListener() {
                            @Override
                            public void onEmptyChildClick(View view) {
                                mStatusManager.showLoadingLayout();
                                mIFastRefreshLoadView.onRefresh(mRefreshLayout);
                            }

                            @Override
                            public void onErrorChildClick(View view) {
                                mStatusManager.showLoadingLayout();
                                mIFastRefreshLoadView.onRefresh(mRefreshLayout);
                            }

                            @Override
                            public void onCustomerChildClick(View view) {
                                mStatusManager.showLoadingLayout();
                                mIFastRefreshLoadView.onRefresh(mRefreshLayout);
                            }
                        });
        if (mManager != null && mManager.getMultiStatusView() != null) {
            mManager.getMultiStatusView().setMultiStatusView(builder, mIFastRefreshLoadView);
        }
        mIFastRefreshLoadView.setMultiStatusView(builder);
        mStatusManager = builder.build();
        mStatusManager.showLoadingLayout();
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
}
