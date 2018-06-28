package com.aries.library.fast.i;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * Created: AriesHoo on 2018/6/15 16:19
 * E-Mail: AriesHoo@126.com
 * Function:
 * Description:
 */
public interface IHttpRequestControl {

    SmartRefreshLayout getRefreshLayout();

    BaseQuickAdapter getRecyclerAdapter();

    StatusLayoutManager getStatusLayoutManager();

    int getCurrentPage();

    int getPageSize();

}
