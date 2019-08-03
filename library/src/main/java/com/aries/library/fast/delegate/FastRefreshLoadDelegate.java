package com.aries.library.fast.delegate;

import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.R;
import com.aries.library.fast.i.IFastRefreshLoadView;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.widget.FastLoadMoreView;
import com.aries.ui.util.FindViewUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @Author: AriesHoo on 2018/7/13 17:52
 * @E-Mail: AriesHoo@126.com
 * Function: 快速实现下拉刷新及上拉加载更多代理类
 * Description:
 * 1、使用StatusLayoutManager重构多状态布局功能
 * 2、2018-7-20 17:00:16 新增StatusLayoutManager 设置目标View优先级
 * 3、2018-7-20 17:44:30 新增StatusLayoutManager 点击事件处理
 */
public class FastRefreshLoadDelegate<T> {

    public SmartRefreshLayout mRefreshLayout;
    public RecyclerView mRecyclerView;
    public BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    public StatusLayoutManager mStatusManager;
    private IFastRefreshLoadView<T> mIFastRefreshLoadView;
    private FastRefreshDelegate mRefreshDelegate;
    private Context mContext;
    private FastManager mManager;
    public View mRootView;
    private Class<?> mTargetClass;

    public FastRefreshLoadDelegate(View rootView, IFastRefreshLoadView<T> iFastRefreshLoadView, Class<?> cls) {
        this.mRootView = rootView;
        this.mIFastRefreshLoadView = iFastRefreshLoadView;
        this.mTargetClass = cls;
        this.mContext = rootView.getContext().getApplicationContext();
        this.mManager = FastManager.getInstance();
        if (mIFastRefreshLoadView == null) {
            return;
        }
        mRefreshDelegate = new FastRefreshDelegate(rootView, iFastRefreshLoadView);
        mRefreshLayout = mRefreshDelegate.mRefreshLayout;
        getRecyclerView(rootView);
        initRecyclerView();
        setStatusManager();
    }

    /**
     * 初始化RecyclerView配置
     */
    protected void initRecyclerView() {
        if (mRecyclerView == null) {
            return;
        }
        if (FastManager.getInstance().getFastRecyclerViewControl() != null) {
            FastManager.getInstance().getFastRecyclerViewControl().setRecyclerView(mRecyclerView, mTargetClass);
        }
        mAdapter = mIFastRefreshLoadView.getAdapter();
        mRecyclerView.setLayoutManager(mIFastRefreshLoadView.getLayoutManager() == null ? new LinearLayoutManager(mContext) : mIFastRefreshLoadView.getLayoutManager());
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
    }

    public void setLoadMore(boolean enable) {
        if (mAdapter != null) {
            mAdapter.setOnLoadMoreListener(enable ? mIFastRefreshLoadView : null, mRecyclerView);
        }
    }

    private void setStatusManager() {
        //优先使用当前配置
        View contentView = mIFastRefreshLoadView.getMultiStatusContentView();
        if (contentView == null) {
            contentView = mRefreshLayout;
        }
        if (contentView == null) {
            contentView = mRecyclerView;
        }
        if (contentView == null) {
            contentView = mRootView;
        }
        if (contentView == null) {
            return;
        }
        StatusLayoutManager.Builder builder = new StatusLayoutManager.Builder(contentView)
                .setDefaultLayoutsBackgroundColor(android.R.color.transparent)
                .setDefaultEmptyText(R.string.fast_multi_empty)
                .setDefaultEmptyClickViewTextColor(ContextCompat.getColor(mContext, R.color.colorTitleText))
                .setDefaultLoadingText(R.string.fast_multi_loading)
                .setDefaultErrorText(R.string.fast_multi_error)
                .setDefaultErrorClickViewTextColor(ContextCompat.getColor(mContext, R.color.colorTitleText))
                .setOnStatusChildClickListener(new OnStatusChildClickListener() {
                    @Override
                    public void onEmptyChildClick(View view) {
                        if (mIFastRefreshLoadView.getEmptyClickListener() != null) {
                            mIFastRefreshLoadView.getEmptyClickListener().onClick(view);
                            return;
                        }
                        mStatusManager.showLoadingLayout();
                        mIFastRefreshLoadView.onRefresh(mRefreshLayout);
                    }

                    @Override
                    public void onErrorChildClick(View view) {
                        if (mIFastRefreshLoadView.getErrorClickListener() != null) {
                            mIFastRefreshLoadView.getErrorClickListener().onClick(view);
                            return;
                        }
                        mStatusManager.showLoadingLayout();
                        mIFastRefreshLoadView.onRefresh(mRefreshLayout);
                    }

                    @Override
                    public void onCustomerChildClick(View view) {
                        if (mIFastRefreshLoadView.getCustomerClickListener() != null) {
                            mIFastRefreshLoadView.getCustomerClickListener().onClick(view);
                            return;
                        }
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
     * 与Activity 及Fragment onDestroy 及时解绑释放避免内存泄露
     */
    public void onDestroy() {
        if (mRefreshDelegate != null) {
            mRefreshDelegate.onDestroy();
            mRefreshDelegate = null;
        }
        mRefreshLayout = null;
        mRecyclerView = null;
        mAdapter = null;
        mStatusManager = null;
        mIFastRefreshLoadView = null;
        mContext = null;
        mManager = null;
        mRootView = null;
        mTargetClass = null;
        LoggerManager.i("FastRefreshLoadDelegate", "onDestroy");
    }
}
