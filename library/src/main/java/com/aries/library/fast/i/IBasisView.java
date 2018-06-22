package com.aries.library.fast.i;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created: AriesHoo on 2018/6/21 13:33
 * E-Mail: AriesHoo@126.com
 * Function:Basis Activity及Fragment通用属性
 * Description:
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

    /**
     * @param viewId
     * @param <T>
     * @return
     */
    @Nullable
    <T extends View> T findView(@IdRes int viewId);
}
