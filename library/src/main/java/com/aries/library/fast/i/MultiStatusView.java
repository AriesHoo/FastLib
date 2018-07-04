package com.aries.library.fast.i;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * Created: AriesHoo on 2017/11/10 13:58
 * E-Mail: AriesHoo@126.com
 * Function: 用于全局设置多状态布局
 * Description:
 * 1、修改设置多状态布局方式
 */
public interface MultiStatusView {

    /**
     * @param statusView
     * @param iFastRefreshLoadView
     */
    void setMultiStatusView(StatusLayoutManager.Builder statusView, IFastRefreshLoadView iFastRefreshLoadView);
}
