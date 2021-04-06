package com.aries.library.fast.demo.module.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.constant.ApiConstant;
import com.aries.library.fast.demo.constant.SPConstant;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.TabLayoutManager;
import com.aries.library.fast.module.fragment.FastTitleFragment;
import com.aries.library.fast.util.SPUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.tab.SegmentTabLayout;
import com.aries.ui.view.tab.SlidingTabLayout;
import com.aries.ui.view.title.TitleBarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * @Author: AriesHoo on 2018/7/13 17:38
 * @E-Mail: AriesHoo@126.com
 * @Function: 活动页--Retrofit演示
 * @Description:
 */
public class ActivityFragment extends FastTitleFragment {

    @BindView(R.id.vp_contentFastLib)
    ViewPager mVpContent;
    private List<Fragment> listFragment = new ArrayList<>();
    private SlidingTabLayout mSlidingTab;
    private View mViewSliding;

    public static ActivityFragment newInstance() {
        Bundle args = new Bundle();
        ActivityFragment fragment = new ActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void beforeSetContentView() {
        super.beforeSetContentView();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        mViewSliding = View.inflate(mContext, R.layout.layout_activity_sliding, null);
        mSlidingTab = mViewSliding.findViewById(R.id.tabLayout_slidingActivity);
        LinearLayout center = titleBar.getLinearLayout(Gravity.CENTER);
        if (center.indexOfChild(mViewSliding) == -1) {
            titleBar.addCenterAction(titleBar.new ViewAction(mViewSliding),
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        setTab();
    }

    @Override
    public int getContentLayout() {
        return R.layout.fast_layout_title_view_pager;
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void initView(Bundle savedInstanceState) {
    }

    private void setTab() {
        mVpContent.removeAllViews();
        listFragment.clear();

        listFragment.add(MovieBaseFragment.newInstance(ApiConstant.API_MOVIE_IN_THEATERS));
        listFragment.add(MovieBaseFragment.newInstance(ApiConstant.API_MOVIE_COMING_SOON));
        listFragment.add(MovieBaseFragment.newInstance(ApiConstant.API_MOVIE_TOP));
        TabLayoutManager.getInstance().setSlidingTabData(this, mSlidingTab, mVpContent,
                getTitles(R.array.arrays_tab_activity), listFragment);
        mSlidingTab.setCurrentTab(0);
    }

    private List<String> getTitles(int array) {
        return Arrays.asList(getResources().getStringArray(array));
    }

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        super.onVisibleChanged(isVisibleToUser);
        if (isVisibleToUser) {
            StatusBarUtil.setStatusBarLightMode(mContext);
        }
    }
}
