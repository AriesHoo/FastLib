package com.aries.library.fast;

/**
 * @Author: AriesHoo on 2019/3/25 16:29
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public interface FastKey {
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
    /**
     * 设置FastDelegateManager 的key
     */
     String KEY_FAST_DELEGATE = "KEY_FAST_DELEGATE";
}
