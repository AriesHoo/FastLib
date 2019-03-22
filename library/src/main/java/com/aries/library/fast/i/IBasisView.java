package com.aries.library.fast.i;

import android.os.Bundle;

import androidx.annotation.LayoutRes;

/**
 * @Author: AriesHoo on 2018/7/16 16:31
 * @E-Mail: AriesHoo@126.com
 * Function: Basis Activity及Fragment通用属性
 * Description:
 * 1、2018-7-23 10:37:39 删除findView 因高版本系统jar已实现相应功能
 */
public interface IBasisView {

    /**
     * 是否注册EventBus
     *
     * @return
     */
    default boolean isEventBusEnable() {
        return true;
    }

    /**
     * Activity或Fragment 布局xml
     *
     * @return
     */
    @LayoutRes
    int getContentLayout();

    /**
     * 初始化
     *
     * @param savedInstanceState
     */
    void initView(Bundle savedInstanceState);

    /**
     * 执行加载布局文件之前操作方法前调用
     */
    default void beforeSetContentView() {

    }

    /**
     * 在初始化控件前进行一些操作
     *
     * @param savedInstanceState
     */
    default void beforeInitView(Bundle savedInstanceState) {

    }

    /**
     * 需要加载数据时重写此方法
     */
    default void loadData() {

    }
}
