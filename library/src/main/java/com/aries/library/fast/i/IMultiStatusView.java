package com.aries.library.fast.i;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created: AriesHoo on 2017/11/8 9:50
 * E-Mail: AriesHoo@126.com
 * Function: 多状态布局控制--加载中/空视图/错误视图/无网络视图
 * Description:
 */
public interface IMultiStatusView {

    /**
     * 加载中View
     *
     * @return
     */
    @NonNull
    View getLoadingView();

    /**
     * 空视图View
     *
     * @return
     */
    @NonNull
    View getEmptyView();

    /**
     * 错误提示View
     *
     * @return
     */
    @NonNull
    View getErrorView();

    /**
     * 无网络提示View
     *
     * @return
     */
    @NonNull
    View getNoNetView();
}
