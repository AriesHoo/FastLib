package com.aries.library.fast.i;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

/**
 * @Author: AriesHoo on 2018/7/23 10:39
 * @E-Mail: AriesHoo@126.com
 * Function: 设置Adapter全局加载更多脚布局
 * Description:
 */
public interface LoadMoreFoot {

    /**
     * 设置BaseQuickAdapter的加载更多视图
     *
     * @param adapter
     * @return
     */
    @Nullable
    LoadMoreView createDefaultLoadMoreView(BaseQuickAdapter adapter);
}
