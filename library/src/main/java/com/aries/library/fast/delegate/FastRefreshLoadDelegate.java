package com.aries.library.fast.delegate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.R;
import com.aries.library.fast.i.IFastRefreshLoadView;
import com.aries.ui.util.FindViewUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * Created: AriesHoo on 2018/6/22 14:45
 * E-Mail: AriesHoo@126.com
 * Function:快速实现下拉刷新及上拉加载更多代理类
 * Description:
 */
public class FastRefreshLoadDelegate<T> {

    public SmartRefreshLayout mRefreshLayout;
    public RecyclerView mRecyclerView;
    public BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    public StatusLayoutManager mStatusManager;
    private IFastRefreshLoadView<T> mIFastRefreshLoadView;
    private Context mContext;
    private FastManager mManager;

    public FastRefreshLoadDelegate(View rootView, IFastRefreshLoadView<T> iFastRefreshLoadView) {
        this.mIFastRefreshLoadView = iFastRefreshLoadView;
        this.mContext = rootView.getContext().getApplicationContext();
        this.mManager = FastManager.getInstance();
        getRefreshLayout(rootView);
        getRecyclerView(rootView);
        initRefreshHeader();
        initRecyclerView();
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
        mStatusManager = new StatusLayoutManager.Builder(mRefreshLayout).build();
        mStatusManager.showLoadingLayout();
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
        if (mRefreshLayout != null) {
            return;
        }
        mStatusManager = new StatusLayoutManager.Builder(mRefreshLayout).build();
//        mStatusManager.showLoadingLayout();
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
}
