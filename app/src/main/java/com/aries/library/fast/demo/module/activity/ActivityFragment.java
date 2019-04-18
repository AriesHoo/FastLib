package com.aries.library.fast.demo.module.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.constant.ApiConstant;
import com.aries.library.fast.demo.constant.EventConstant;
import com.aries.library.fast.demo.constant.SPConstant;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.TabLayoutManager;
import com.aries.library.fast.module.fragment.FastTitleFragment;
import com.aries.library.fast.util.SPUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.tab.SegmentTabLayout;
import com.aries.ui.view.tab.SlidingTabLayout;
import com.aries.ui.view.title.TitleBarView;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * @Author: AriesHoo on 2018/7/13 17:38
 * @E-Mail: AriesHoo@126.com
 * @Function: 活动页--Retrofit演示
 * @Description:
 */
public class ActivityFragment extends FastTitleFragment {

    @BindView(R.id.vp_contentFastLib) ViewPager vpContent;
    private List<Fragment> listFragment = new ArrayList<>();
    private SegmentTabLayout mSegmentTab;
    private SlidingTabLayout mSlidingTab;
    private View viewSliding;
    private View viewSegment;

    private boolean isSliding = true;

    public static ActivityFragment newInstance() {
        Bundle args = new Bundle();
        ActivityFragment fragment = new ActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void beforeSetContentView() {
        super.beforeSetContentView();
        LoggerManager.d(TAG, "refreshActivityTab:" + isSliding);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        isSliding = (boolean) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_TAB_SLIDING, true);
        if (isSliding && viewSliding == null) {
            viewSliding = View.inflate(mContext, R.layout.layout_activity_sliding, null);
            mSlidingTab = viewSliding.findViewById(R.id.tabLayout_slidingActivity);
        } else if (!isSliding && viewSegment == null) {
            viewSegment = View.inflate(mContext, R.layout.layout_activity_segment, null);
            mSegmentTab = viewSegment.findViewById(R.id.tabLayout_segment);
        }
        LinearLayout center = titleBar.getLinearLayout(Gravity.CENTER);
        if (isSliding) {
            if (center.indexOfChild(viewSliding) == -1) {
                titleBar.addCenterAction(titleBar.new ViewAction(viewSliding),
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            viewSliding.setVisibility(View.VISIBLE);
            if (viewSegment != null) {
                viewSegment.setVisibility(View.GONE);
            }
        } else {
            if (center.indexOfChild(viewSegment) == -1) {
                titleBar.addCenterAction(titleBar.new ViewAction(viewSegment),
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            viewSegment.setVisibility(View.VISIBLE);
            if (viewSliding != null) {
                viewSliding.setVisibility(View.GONE);
            }
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
//        setTab();
    }

    @Override
    public void loadData() {
        super.loadData();
//        setTab();
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            LoggerManager.d(TAG, "竖屏");
//        } else {
//            LoggerManager.d(TAG, "横屏");
//        }
//        setTab();
//    }

    private void setTab() {
        isSliding = (boolean) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_TAB_SLIDING, isSliding);
        vpContent.removeAllViews();
        listFragment.clear();

        listFragment.add(MovieBaseFragment.newInstance(ApiConstant.API_MOVIE_IN_THEATERS));
        listFragment.add(MovieBaseFragment.newInstance(ApiConstant.API_MOVIE_COMING_SOON));
        listFragment.add(MovieBaseFragment.newInstance(ApiConstant.API_MOVIE_TOP));
        if (isSliding) {
            TabLayoutManager.getInstance().setSlidingTabData(this, mSlidingTab, vpContent,
                    getTitles(R.array.arrays_tab_activity), listFragment);
        } else {
            TabLayoutManager.getInstance().setSegmentTabData(this, mSegmentTab, vpContent,
                    getResources().getStringArray(R.array.arrays_tab_activity), listFragment);
        }
        //SlidingTabLayout--需这样切换一下不然选中变粗没有效果不知是SlidingTabLayout BUG还是设置问题
//        mSlidingTab.setCurrentTab(1);
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

    @Subscriber(mode = ThreadMode.MAIN, tag = EventConstant.EVENT_KEY_REFRESH_ACTIVITY_TAB)
    public void refreshActivityTab(boolean isSliding) {
        mIsFirstShow = true;
        setTitleBar(mTitleBar);
        setTab();
    }

}
