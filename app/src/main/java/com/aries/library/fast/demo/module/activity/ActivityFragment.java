package com.aries.library.fast.demo.module.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.constant.EventConstant;
import com.aries.library.fast.demo.constant.MovieConstant;
import com.aries.library.fast.demo.constant.SPConstant;
import com.aries.library.fast.demo.helper.TitleBarHelper;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.TabLayoutManager;
import com.aries.library.fast.module.fragment.FastTitleFragment;
import com.aries.library.fast.util.SPUtil;
import com.aries.ui.view.title.TitleBarView;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.SlidingTabLayout;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created: AriesHoo on 2017/7/20 11:45
 * Function: 活动页--Retrofit演示
 * Desc:
 */
public class ActivityFragment extends FastTitleFragment {

    @BindView(R.id.vp_content) ViewPager vpContent;
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
        TitleBarHelper.getInstance().setTitleBarView(titleBar, mContext, false);
        isSliding = (boolean) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_TAB_SLIDING, true);
        if (isSliding && viewSliding == null) {
            viewSliding = View.inflate(mContext, R.layout.layout_activity_sliding, null);
            mSlidingTab = (SlidingTabLayout) viewSliding.findViewById(R.id.tabLayout_slidingActivity);
        } else if (!isSliding && viewSegment == null) {
            viewSegment = View.inflate(mContext, R.layout.layout_activity_segment, null);
            mSegmentTab = (SegmentTabLayout) viewSegment.findViewById(R.id.tabLayout_segment);
        }
        LinearLayout center = titleBar.getLinearLayout(Gravity.CENTER);
        if (isSliding) {
            if (center.indexOfChild(viewSliding) == -1) {
                titleBar.addCenterAction(titleBar.new ViewAction(viewSliding));
            }
            viewSliding.setVisibility(View.VISIBLE);
            if (viewSegment != null) {
                viewSegment.setVisibility(View.GONE);
            }
        } else {
            if (center.indexOfChild(viewSegment) == -1) {
                titleBar.addCenterAction(titleBar.new ViewAction(viewSegment));
            }
            viewSegment.setVisibility(View.VISIBLE);
            if (viewSliding != null) {
                viewSliding.setVisibility(View.GONE);
            }
        }
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

    private void setTab() {
        isSliding = (boolean) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_TAB_SLIDING, isSliding);
        vpContent.removeAllViews();
        listFragment.clear();
        listFragment.add(MovieBaseFragment.newInstance(MovieConstant.MOVIE_IN_THEATERS));
        listFragment.add(MovieBaseFragment.newInstance(MovieConstant.MOVIE_COMING_SOON));
        listFragment.add(MovieBaseFragment.newInstance(MovieConstant.MOVIE_TOP));
        if (isSliding) {
            TabLayoutManager.getInstance().setSlidingTabData(this, mSlidingTab, vpContent,
                    getTitles(R.array.arrays_tab_activity), listFragment);
        } else {
            TabLayoutManager.getInstance().setSegmentTabData(this, mSegmentTab, vpContent,
                    getResources().getStringArray(R.array.arrays_tab_activity), listFragment);
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        setTab();
    }

    private List<String> getTitles(int array) {
        return Arrays.asList(getResources().getStringArray(array));
    }

    @Subscriber(mode = ThreadMode.MAIN, tag = EventConstant.EVENT_KEY_REFRESH_ACTIVITY_TAB)
    public void refreshActivityTab(boolean isSliding) {
        mIsFirstShow = true;
        setTitleBar(mTitleBar);
        setTab();
    }
}
