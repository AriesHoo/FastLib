package com.aries.library.fast.i;

import android.view.View;

/**
 * Created: AriesHoo on 2017/11/8 9:50
 * E-Mail: AriesHoo@126.com
 * Function: 多状态布局控制--加载中/空视图/错误视图/无网络视图
 * Description:
 */
public interface IMultiStatusView {

    View getLoadingView();

    View getEmptyView();

    View getErrorView();

    View getNoNetView();
}
