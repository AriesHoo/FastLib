package com.aries.library.fast.i;

import android.view.View;

/**
 * @Author: AriesHoo on 2018/7/20 17:08
 * @E-Mail: AriesHoo@126.com
 * Function: StatusLayoutManager 属性控制
 * Description:
 */
public interface IMultiStatusView {
    /**
     * 设置StatusLayoutManager 的目标View
     *
     * @return
     */
    View getMultiStatusContentView();

    /**
     * 获取空布局里点击View回调
     *
     * @return
     */
    View.OnClickListener getEmptyClickListener();

    /**
     * 获取错误布局里点击View回调
     *
     * @return
     */
    View.OnClickListener getErrorClickListener();

    /**
     * 获取自定义布局里点击View回调
     *
     * @return
     */
    View.OnClickListener getCustomerClickListener();
}
