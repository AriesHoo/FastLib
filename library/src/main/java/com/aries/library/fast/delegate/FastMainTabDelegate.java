package com.aries.library.fast.delegate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.aries.library.fast.R;
import com.aries.library.fast.entity.FastTabEntity;
import com.aries.library.fast.i.IFastMainView;
import com.aries.library.fast.manager.TabLayoutManager;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.view.tab.CommonTabLayout;
import com.aries.ui.view.tab.listener.CustomTabEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: AriesHoo on 2018/7/13 17:52
 * @E-Mail: AriesHoo@126.com
 * Function: 主页tab代理类
 * Description:
 * 1、2018-7-20 17:15:08 修正获取子控件方法
 * 2、2018-11-19 11:29:20 修正废弃 方法
 * 3、2018-11-29 16:08:12 新增Activity被系统回收相关处理
 */
public class FastMainTabDelegate {

    public CommonTabLayout mTabLayout;
    public ViewPager mViewPager;
    public List<FastTabEntity> mListFastTab = new ArrayList<>();
    private IFastMainView mIFastMainView;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private Context mContext;
    private Object mObject;
    private Bundle mSavedInstanceState;
    private FragmentManager mFragmentManager;
    public static final String SAVED_INSTANCE_STATE_CURRENT_TAB = "saved_instance_state_current_tab";
    /**
     * 存放历史主页Tab数量
     */
    public static final String SAVED_INSTANCE_STATE_FRAGMENT_NUMBER = "saved_instance_state_fragment_number";
    /**
     * 获取历史主页Tab数量Key --
     */
    public static final String SAVED_INSTANCE_STATE_KEY_FRAGMENT = "saved_instance_state_key_fragment";
    public static final String SAVED_INSTANCE_STATE_KEY_TITLE = "saved_instance_state_key_title";
    public static final String SAVED_INSTANCE_STATE_KEY_SELECTED_ICON = "saved_instance_state_key_selected_icon";
    public static final String SAVED_INSTANCE_STATE_KEY_UN_SELECTED_ICON = "saved_instance_state_key_un_selected_icon";

    public FastMainTabDelegate(View rootView, FragmentActivity activity, IFastMainView iFastMainView) {
        if (iFastMainView == null || rootView == null || activity == null) {
            return;
        }
        this.mContext = activity;
        this.mObject = activity;
        this.mIFastMainView = iFastMainView;
        mFragmentManager = activity.getSupportFragmentManager();
        mSavedInstanceState = iFastMainView.getSavedInstanceState();
        getTabLayout(rootView);
        getViewPager(rootView);
        initTabLayout();
    }

    public FastMainTabDelegate(View rootView, Fragment activity, IFastMainView iFastMainView) {
        if (iFastMainView == null || rootView == null || activity == null) {
            return;
        }
        this.mContext = activity.getContext();
        this.mObject = activity;
        this.mIFastMainView = iFastMainView;
        mFragmentManager = activity.getChildFragmentManager();
        mSavedInstanceState = iFastMainView.getSavedInstanceState();
        getTabLayout(rootView);
        getViewPager(rootView);
        initTabLayout();
    }

    private void initTabLayout() {
        if (mTabLayout == null) {
            return;
        }
        mListFastTab = new ArrayList<>();
        getSaveState();
        //本地缓存及接口都没有获取到
        if (mListFastTab == null || mListFastTab.size() == 0) {
            return;
        }
        mTabLayout.setBackgroundResource(R.color.colorTabBackground);
        mTabLayout.getDelegate()
                .setTextSelectColor(ContextCompat.getColor(mContext, R.color.colorTabTextSelect))
                .setTextUnSelectColor(ContextCompat.getColor(mContext, R.color.colorTabTextUnSelect))
                .setUnderlineColor(ContextCompat.getColor(mContext, R.color.colorTabUnderline))
                .setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelSize(R.dimen.dp_tab_text_size))
                .setUnderlineGravity(Gravity.TOP)
                .setUnderlineHeight(mContext.getResources().getDimension(R.dimen.dp_tab_underline))
                .setIconMargin(mContext.getResources().getDimensionPixelSize(R.dimen.dp_tab_margin))
                .setIconWidth(mContext.getResources().getDimensionPixelSize(R.dimen.dp_tab_icon))
                .setIconHeight(mContext.getResources().getDimensionPixelSize(R.dimen.dp_tab_icon))
                //设置指示器高度为0
                .setIndicatorHeight(0);
        ViewGroup.LayoutParams params = mTabLayout.getLayoutParams();
        if (params != null) {
            params.height = mContext.getResources().getDimensionPixelSize(R.dimen.dp_tab_height);
        }
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < mListFastTab.size(); i++) {
            FastTabEntity entity = mListFastTab.get(i);
            fragments.add(entity.mFragment);
            mTabEntities.add(entity);
        }
        if (mIFastMainView.isSwipeEnable()) {
            initViewPager(fragments);
        } else {
            if (mObject instanceof FragmentActivity) {
                mTabLayout.setTabData(mTabEntities, (FragmentActivity) mObject, R.id.fLayout_containerFastMain, fragments);
                mTabLayout.setOnTabSelectListener(mIFastMainView);
            } else if (mObject instanceof Fragment) {
                mTabLayout.setTabData(mTabEntities, ((Fragment) mObject).getActivity(), R.id.fLayout_containerFastMain, fragments);
                mTabLayout.setOnTabSelectListener(mIFastMainView);
            }

        }
        mIFastMainView.setTabLayout(mTabLayout);
        mIFastMainView.setViewPager(mViewPager);
    }

    private void initViewPager(final List<Fragment> fragments) {
        if (mViewPager != null) {
            if (mObject instanceof FragmentActivity) {
                TabLayoutManager.getInstance().setCommonTabData((FragmentActivity) mObject, mTabLayout, mViewPager, mTabEntities, fragments, mIFastMainView);
            } else if (mObject instanceof Fragment) {
                TabLayoutManager.getInstance().setCommonTabData((Fragment) mObject, mTabLayout, mViewPager, mTabEntities, fragments, mIFastMainView);
            }
        }
    }

    /**
     * 获取布局里的CommonTabLayout
     *
     * @param rootView
     * @return
     */
    private void getTabLayout(View rootView) {
        mTabLayout = rootView.findViewById(R.id.tabLayout_commonFastLib);
        if (mTabLayout == null) {
            mTabLayout = FindViewUtil.getTargetView(rootView, CommonTabLayout.class);
        }
    }

    /**
     * 获取ViewPager
     *
     * @param rootView
     */
    private void getViewPager(View rootView) {
        mViewPager = rootView.findViewById(R.id.vp_contentFastLib);
        if (mViewPager == null) {
            mViewPager = FindViewUtil.getTargetView(rootView, ViewPager.class);
        }
    }

    /**
     * 保存Fragment数据
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        if (mListFastTab != null && mTabLayout != null && mFragmentManager != null) {
            outState.putInt(SAVED_INSTANCE_STATE_FRAGMENT_NUMBER, mListFastTab.size());
            outState.putInt(SAVED_INSTANCE_STATE_CURRENT_TAB, mTabLayout.getCurrentTab());
            List<FastTabEntity> listFastTab = mListFastTab;
            for (int i = 0; i < listFastTab.size(); i++) {
                FastTabEntity item = listFastTab.get(i);
                if (item.mFragment != null) {
                    outState.putInt(SAVED_INSTANCE_STATE_KEY_UN_SELECTED_ICON + i, item.mUnSelectedIcon);
                    outState.putInt(SAVED_INSTANCE_STATE_KEY_SELECTED_ICON + i, item.mSelectedIcon);
                    outState.putString(SAVED_INSTANCE_STATE_KEY_TITLE + i, item.mTitle);
                    mFragmentManager.putFragment(outState, SAVED_INSTANCE_STATE_KEY_FRAGMENT + i, item.mFragment);
                }
            }
        }
    }

    /**
     * 获取本地存储信息
     */
    private void getSaveState() {
        //从本地缓存获取
        if (mSavedInstanceState != null) {
            //先获取数量
            int size = mSavedInstanceState.getInt(SAVED_INSTANCE_STATE_FRAGMENT_NUMBER);
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    Fragment fragment = mFragmentManager.getFragment(mSavedInstanceState, SAVED_INSTANCE_STATE_KEY_FRAGMENT + i);
                    String title = mSavedInstanceState.getString(SAVED_INSTANCE_STATE_KEY_TITLE + i);
                    int selectedIcon = mSavedInstanceState.getInt(SAVED_INSTANCE_STATE_KEY_SELECTED_ICON + i);
                    int unSelectedIcon = mSavedInstanceState.getInt(SAVED_INSTANCE_STATE_KEY_UN_SELECTED_ICON + i);
                    mListFastTab.add(new FastTabEntity(title, unSelectedIcon, selectedIcon, fragment));
                }
            }
        }
        //没有获取到
        if (mListFastTab == null || mListFastTab.size() == 0) {
            mListFastTab = mIFastMainView.getTabList();
        }
    }
}
