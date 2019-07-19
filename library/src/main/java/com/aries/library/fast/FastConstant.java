package com.aries.library.fast;

/**
 * @Author: AriesHoo on 2018/7/23 14:39
 * @E-Mail: AriesHoo@126.com
 * Function: 全局常量
 * Description:
 */
public interface FastConstant {

    String EXCEPTION_NOT_INIT = "You've to call static method init() first in Application";
    String EXCEPTION_NOT_INIT_FAST_MANAGER = "You've to call static method init(Application) first in Application";
    String EXCEPTION_EMPTY_URL = "You've configured an invalid url";
    /**
     * 是否设置状态栏标记
     */
    String IS_SET_STATUS_VIEW_HELPER = "IS_SET_STATUS_VIEW_HELPER";
    /**
     * 是否设置导航栏标记
     */
    String IS_SET_NAVIGATION_VIEW_HELPER = "IS_SET_NAVIGATION_VIEW_HELPER";
    /**
     * 是否设置更布局背景标记
     */
    String IS_SET_CONTENT_VIEW_BACKGROUND = "IS_SET_CONTENT_VIEW_BACKGROUND";
    /**
     * 是否设置下拉刷新标记
     */
    String IS_SET_REFRESH_VIEW = "IS_SET_REFRESH_VIEW";
    /**
     * 是否设置TitleBarView标记
     */
    String IS_SET_TITLE_BAR_VIEW = "IS_SET_TITLE_BAR_VIEW";
}
