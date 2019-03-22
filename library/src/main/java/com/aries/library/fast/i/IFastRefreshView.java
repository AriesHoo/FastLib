package com.aries.library.fast.i;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * @Author: AriesHoo on 2019/3/22 14:03
 * @E-Mail: AriesHoo@126.com
 * @Function: 快速刷新
 * @Description:
 */
public interface IFastRefreshView extends OnRefreshListener {

    /**
     * 获取下拉刷新头布局
     *
     * @return 下拉刷新头
     */
    default RefreshHeader getRefreshHeader() {
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
     * @param refreshLayout
     */
    default void setRefreshLayout(SmartRefreshLayout refreshLayout) {

    }
}
