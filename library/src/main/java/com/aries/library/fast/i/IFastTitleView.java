package com.aries.library.fast.i;

import com.aries.ui.view.title.TitleBarView;

/**
 * @Author: AriesHoo on 2018/7/23 9:53
 * @E-Mail: AriesHoo@126.com
 * Function:包含TitleBarView接口
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
     * 一般用于最终实现子类设置TitleBarView 其它属性
     *
     * @param titleBar
     */
    void setTitleBar(TitleBarView titleBar);

}
