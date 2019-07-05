package com.aries.library.fast.i;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aries.library.fast.entity.FastTabEntity;
import com.aries.ui.view.tab.CommonTabLayout;
import com.aries.ui.view.tab.listener.OnTabSelectListener;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * @Author: AriesHoo on 2018/7/20 17:07
 * @E-Mail: AriesHoo@126.com
 * Function: 包含CommonTabLayout的主页面Activity/Fragment
 * Description:1、2019-7-5 15:05:42 增加 getContainerViewId以解决多层Fragment嵌套id相同后造成Fragmen加载错误问题
 */
public interface IFastMainView extends OnTabSelectListener {

    /**
     * 控制主界面Fragment是否可滑动切换
     *
     * @return true 可滑动切换(配合ViewPager)
     */
    default boolean isSwipeEnable() {
        return false;
    }
     /**
     * 承载主界面Fragment的ViewGroup id 一般为FrameLayout
     *
     * @return viewId
     */
    default int getContainerViewId(){
        return R.id.fLayout_containerFastMain;
    }

    /**
     * 用于添加Tab属性(文字-图标)
     *
     * @return 主页tab数组
     */
    @Nullable
    List<FastTabEntity> getTabList();

    /**
     * 获取onCreate 携带参数
     * {@link android.app.Activity#onCreate(Bundle)}
     * {@link com.aries.library.fast.module.activity.FastMainActivity#beforeInitView(Bundle)}
     * {@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * {@link com.aries.library.fast.module.fragment.FastMainFragment#beforeInitView(Bundle)}
     *
     * @return
     */
    Bundle getSavedInstanceState();

    /**
     * 返回 CommonTabLayout  对象用于自定义设置
     *
     * @param tabLayout CommonTabLayout 对象用于单独属性调节
     */
    void setTabLayout(CommonTabLayout tabLayout);

    /**
     * 设置ViewPager属性
     *
     * @param mViewPager ViewPager属性控制
     */
    default void setViewPager(ViewPager mViewPager) {

    }

    /**
     * tab首次选中
     *
     * @param position
     */
    @Override
    default void onTabSelect(int position) {

    }

    /**
     * tab选中状态再点击
     *
     * @param position
     */
    @Override
    default void onTabReselect(int position) {

    }
}
