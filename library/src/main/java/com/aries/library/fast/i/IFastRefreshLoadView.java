package com.aries.library.fast.i;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @Author: AriesHoo on 2018/7/20 16:52
 * @E-Mail: AriesHoo@126.com
 * Function: 下拉及上拉刷新
 * Description:
 * 1、2018-7-20 17:11:22 多状态集成关系
 * 2、2018-7-20 17:39:55 去掉点击事件getMultiStatusViewChildClickListener
 */
public interface IFastRefreshLoadView<T> extends OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, IMultiStatusView {
    /**
     * 使用BaseRecyclerViewAdapterHelper作为上拉加载的实现方式
     * 如果使用ListView或GridView等需要自己去实现上拉加载更多的逻辑
     *
     * @return BaseRecyclerViewAdapterHelper的实现类
     */
    BaseQuickAdapter<T, BaseViewHolder> getAdapter();

    /**
     * 获取RecyclerView的布局管理器对象，根据自己业务实际情况返回
     *
     * @return RecyclerView的布局管理器对象
     */
    RecyclerView.LayoutManager getLayoutManager();

    /**
     * 获取下拉刷新头布局
     *
     * @return 下拉刷新头
     */
    RefreshHeader getRefreshHeader();

    /**
     * 获取加载更多布局
     *
     * @return
     */
    LoadMoreView getLoadMoreView();

    /**
     * 触发下拉或上拉刷新操作
     *
     * @param page
     */
    void loadData(int page);

    /**
     * 是否支持加载更多功能
     *
     * @return
     */
    boolean isLoadMoreEnable();

    /**
     * 是否支持下拉刷新功能
     *
     * @return
     */
    boolean isRefreshEnable();

    /**
     * item是否有点击事件
     *
     * @return
     */
    boolean isItemClickEnable();

    /**
     * item点击回调
     *
     * @param adapter
     * @param view
     * @param position
     */
    void onItemClicked(BaseQuickAdapter<T, BaseViewHolder> adapter, View view, int position);

    /**
     * 设置StatusLayoutManager属性
     *
     * @param statusView
     */
    void setMultiStatusView(StatusLayoutManager.Builder statusView);

    /**
     * 设置全局监听接口
     *
     * @return
     */
    IHttpRequestControl getIHttpRequestControl();
}
