package com.aries.library.fast.i;

import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * @Author: AriesHoo on 2019/3/22 14:03
 * @E-Mail: AriesHoo@126.com
 * @Function: 快速刷新{@link FastRefreshDelegate}
 * @Description:
 */
public interface IFastRefreshView extends OnRefreshListener {

    /**
     * 获取下拉刷新头布局 建议通过{@link #setRefreshLayout(SmartRefreshLayout)} {@link SmartRefreshLayout#setRefreshHeader(RefreshHeader)}修改
     *
     * @return 下拉刷新头
     */
    @Deprecated
    default RefreshHeader getRefreshHeader() {
        return null;
    }

    /**
     * 需要下拉刷新的布局 --可以是Activity根布局、Fragment 不要传根布局(除非根布局为SmartRefreshLayout)
     *
     * @return
     */
    default View getContentView() {
        return null;
    }

    /**
     * 是否支持下拉刷新功能
     *
     * @return
     */
    default boolean isRefreshEnable() {
        return true;
    }

    /**
     * 回调设置的SmartRefreshLayout
     *
     * @param refreshLayout
     */
    default void setRefreshLayout(SmartRefreshLayout refreshLayout) {

    }


}
