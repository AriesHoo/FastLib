package com.aries.library.fast.delegate;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.aries.library.fast.R;
import com.aries.library.fast.entity.FastTabEntity;
import com.aries.library.fast.i.IFastMainView;
import com.aries.library.fast.manager.TabLayoutManager;
import com.aries.library.fast.util.SizeUtil;
import com.aries.ui.util.FindViewUtil;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: AriesHoo on 2018/7/13 17:52
 * @E-Mail: AriesHoo@126.com
 * Function: 主页tab代理类
 * Description:
 * 1、2018-7-20 17:15:08 修正获取子控件方法
 * 2、2018-11-19 11:29:20 修正废弃 方法
 */
public class FastMainTabDelegate {

    public CommonTabLayout mTabLayout;
    public ViewPager mViewPager;
    private IFastMainView mIFastMainView;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private Context mContext;
    private Object mObject;

    public FastMainTabDelegate(View rootView, FragmentActivity activity, IFastMainView iFastMainView) {
        this.mContext = activity;
        this.mObject = activity;
        this.mIFastMainView = iFastMainView;
        if (iFastMainView == null) {
            return;
        }
        getTabLayout(rootView);
        getViewPager(rootView);
        initTabLayout();
    }

    public FastMainTabDelegate(View rootView, Fragment activity, IFastMainView iFastMainView) {
        this.mContext = activity.getContext();
        this.mObject = activity;
        this.mIFastMainView = iFastMainView;
        if (iFastMainView == null) {
            return;
        }
        getTabLayout(rootView);
        getViewPager(rootView);
        initTabLayout();
    }

    private void initTabLayout() {
        if (mTabLayout == null) {
            return;
        }
        List<FastTabEntity> tabEntities = mIFastMainView.getTabList();
        if (tabEntities.size() == 0) {
            return;
        }
        mTabLayout.setBackgroundResource(R.color.colorTabBackground);
        mTabLayout.setTextSelectColor(ContextCompat.getColor(mContext,R.color.colorTabTextSelect));
        mTabLayout.setTextUnselectColor(ContextCompat.getColor(mContext,R.color.colorTabTextUnSelect));
        mTabLayout.setUnderlineColor(ContextCompat.getColor(mContext,R.color.colorTabUnderline));
        mTabLayout.setTextsize(SizeUtil.px2dp(mContext.getResources().getDimension(R.dimen.dp_tab_text_size)));
        mTabLayout.setUnderlineGravity(Gravity.TOP);
        mTabLayout.setUnderlineHeight(SizeUtil.px2dp(mContext.getResources().getDimension(R.dimen.dp_tab_underline)));
        mTabLayout.setIconMargin(SizeUtil.px2dp(mContext.getResources().getDimension(R.dimen.dp_tab_margin)));
        mTabLayout.setIconWidth(SizeUtil.px2dp(mContext.getResources().getDimension(R.dimen.dp_tab_icon)));
        mTabLayout.setIconHeight(SizeUtil.px2dp(mContext.getResources().getDimension(R.dimen.dp_tab_icon)));
        //设置指示器高度为0
        mTabLayout.setIndicatorHeight(0);
        ViewGroup.LayoutParams params = mTabLayout.getLayoutParams();
        if (params != null) {
            params.height = mContext.getResources().getDimensionPixelSize(R.dimen.dp_tab_height);
        }
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < tabEntities.size(); i++) {
            FastTabEntity entity = tabEntities.get(i);
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
}
