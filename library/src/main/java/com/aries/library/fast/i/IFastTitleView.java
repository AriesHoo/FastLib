package com.aries.library.fast.i;

import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2017/7/25 11:27
 * Function: 包含TitleBarView
 * Desc:
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

    /**
     * 是否浅色状态栏(设置黑色文字--Flyme 4.0 MIUI 6.0及Android 6.0 以上支持)
     *
     * @return
     */
    boolean isLightStatusBarEnable();
}
