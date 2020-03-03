package com.aries.library.fast.i;

import android.app.Activity;
import android.view.View;

import com.aries.ui.helper.status.StatusViewHelper;

/**
 * @Author: AriesHoo on 2019/7/19 14:44
 * @E-Mail: AriesHoo@126.com
 * @Function: Activity 全局状态栏控制
 * @Description: 1、2019-7-19 14:35:52 从{@link ActivityFragmentControl}抽离用于Activity做定制化
 */
public interface IStatusBar {
    /**
     * Activity 全局状态栏控制可设置部分页面属性
     *
     * @param activity 目标Activity
     * @param helper   StatusViewHelper
     * @param topView  顶部Activity
     * @return true 表示调用 helper 的init方法进行设置
     */
    boolean setStatusBar(Activity activity, StatusViewHelper helper, View topView);

}
