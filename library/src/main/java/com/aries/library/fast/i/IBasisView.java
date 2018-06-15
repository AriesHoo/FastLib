package com.aries.library.fast.i;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;

/**
 * Created: AriesHoo on 2017/7/26 13:20
 * Function: Basis Activity/Fragment
 * Desc:
 */
public interface IBasisView {

    /**
     * 是否注册EventBus
     *
     * @return
     */
    boolean isEventBusEnable();

    /**
     * Activity或Fragment 布局xml
     *
     * @return
     */
    @LayoutRes
    int getContentLayout();

    /**
     * 设置根布局背景
     *
     * @return
     */
    @DrawableRes
    int getContentBackground();

    /**
     * 初始化
     *
     * @param savedInstanceState
     */
    void initView(Bundle savedInstanceState);

    /**
     * 执行加载布局文件之前操作方法前调用
     */
    void beforeSetContentView();

    /**
     * 在初始化控件前进行一些操作
     */
    void beforeInitView();

    /**
     * 需要加载数据时重写此方法
     */
    void loadData();
}
