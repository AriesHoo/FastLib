package com.aries.library.fast.i;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.aries.library.fast.entity.FastTabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.List;

/**
 * Created: AriesHoo on 2017/7/19 16:40
 * Function: 包含CommonTabLayout的主页面Activity/Fragment
 * Desc:
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
     */
    @Nullable
    List<FastTabEntity> getTabList();

    /**
     * 返回CommonTabLayout 对象用于自定义设置
     *
     * @param tabLayout
     */
    void setTabLayout(CommonTabLayout tabLayout);

    void setViewPager(ViewPager mViewPager);
}
