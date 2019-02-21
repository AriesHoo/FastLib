package com.aries.library.fast.i;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author: AriesHoo on 2018/11/19 12:02
 * @E-Mail: AriesHoo@126.com
 * @Function: {@link IFastRefreshLoadView}列表布局全局控制RecyclerView
 * @Description:
 */
public interface FastRecyclerViewControl {

    /**
     * 全局设置
     *
     * @param recyclerView
     * @param cls
     */
    void setRecyclerView(RecyclerView recyclerView, Class<?> cls);
}
