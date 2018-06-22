package com.aries.library.fast.i;

import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2017/7/25 11:27
 * Function:
 * Desc:
 */

/**
 * Created: AriesHoo on 2018/4/20/020 10:14
 * E-Mail: AriesHoo@126.com
 * Function:包含TitleBarView
 * Description:
 * 1、2018-4-20 10:15:01 去掉isLightStatusBarEnable通过{@link TitleBarView#setStatusBarLightMode(boolean)}
 * 去掉getLeftIcon控制通过{@link TitleBarView#setLeftTextDrawable(int)}设置
 * 2、2018-6-22 14:05:50 去掉返回键设置属性
 */
public interface IFastTitleView {
    /**
     * 子类回调setTitleBar之前执行用于app设置全局Base控制统一TitleBarView
     *
     * @param titleBar
     */
    void beforeSetTitleBar(TitleBarView titleBar);

    /**
     * 用于子类设置TitleBarView 其它属性
     *
     * @param titleBar
     */
    void setTitleBar(TitleBarView titleBar);

}
