package com.aries.library.fast.i;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.aries.library.fast.entity.FastTabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.List;

/**
 * @Author: AriesHoo on 2018/7/20 17:07
 * @E-Mail: AriesHoo@126.com
 * Function: 包含CommonTabLayout的主页面Activity/Fragment
 * Description:
 */
public interface IFastMainView extends OnTabSelectListener {

    /**
     * 控制主界面Fragment是否可滑动切换
     *
     * @return
     */
    boolean isSwipeEnable();

    /**
     * 用于添加Tab属性(文字-图标)
     *
     * @return
     */
    @Nullable
    List<FastTabEntity> getTabList();

    /**
     * 返回CommonTabLayout 对象用于自定义设置
     *
     * @param tabLayout
     */
    void setTabLayout(CommonTabLayout tabLayout);

    /**
     * 设置ViewPager属性
     *
     * @param mViewPager
     */
    void setViewPager(ViewPager mViewPager);
}
