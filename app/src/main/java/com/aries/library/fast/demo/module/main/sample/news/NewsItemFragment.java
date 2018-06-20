package com.aries.library.fast.demo.module.main.sample.news;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.manager.TabLayoutManager;
import com.aries.library.fast.module.fragment.FastTitleFragment;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;


/**
 * Created: AriesHoo on 2017/8/5 23:06
 * Function: 腾讯新闻-Fragment
 * Desc:
 */
public class NewsItemFragment extends FastTitleFragment {

    @BindView(R.id.vp_contentFastLib) ViewPager vpContent;
    private String[] titles;
    private int[] slidingTab = new int[]{R.array.arrays_news_news_sliding, 0, R.array.arrays_news_live_sliding, 0};
    private int mPosition = 0;
    private SlidingTabLayout mSlidingTab;
    private List<Fragment> listFragments;

    public static NewsItemFragment newInstance(int position) {
        Bundle args = new Bundle();
        NewsItemFragment fragment = new NewsItemFragment();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titles = getResources().getStringArray(R.array.arrays_tab_news);
        mPosition = getArguments().getInt("position");
        titleBar.setTitleMainTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        if (mPosition == 3) {
            titleBar.setTitleMainTextColor(Color.WHITE)
                    .setBackgroundResource(R.color.colorMainAli);
        }
        if (mPosition == 0 || mPosition == 2) {
            View view = View.inflate(mContext, R.layout.layout_news_sliding, null);
            mSlidingTab = view.findViewById(R.id.tabLayout_slidingNews);
            titleBar.addCenterAction(titleBar.new ViewAction(view));
        } else {
            titleBar.setTitleMainText(titles[mPosition]);
        }
        titleBar.setLeftTextDrawable(mPosition == 0 || mPosition == 1 ? R.drawable.ic_news_search : 0)
                .setLeftText(mPosition == 1 ? getString(R.string.find) : "")
                .setRightTextDrawable(mPosition == 3 ? R.drawable.ic_news_setting_normal : mPosition == 0 ? R.drawable.ic_news_channel_plus : 0);
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_news_item;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    private void setListFragment(List<String> titles) {
        listFragments = new ArrayList<>();
        if (titles != null && titles.size() > 0) {
            for (String str : titles) {
                listFragments.add(NewsRefreshItemFragment.newInstance());
            }
        }
    }

    private List<String> getTitles(int array) {
        return Arrays.asList(getResources().getStringArray(array));
    }

    @Override
    public void loadData() {
        super.loadData();
        if (mSlidingTab != null) {
            List<String> titles = getTitles(slidingTab[mPosition]);
            setListFragment(titles);
            TabLayoutManager.getInstance().setSlidingTabData(this, mSlidingTab, vpContent, titles, listFragments);
            //SlidingTabLayout--需这样切换一下不然选中变粗没有效果不知是SlidingTabLayout BUG还是设置问题
            mSlidingTab.setCurrentTab(1);
            mSlidingTab.setCurrentTab(0);
        }
    }

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        super.onVisibleChanged(isVisibleToUser);
        if (mContext != null && mPosition == 3) {
            if (!isVisibleToUser) {
                StatusBarUtil.setStatusBarLightMode(mContext);
            } else {
                StatusBarUtil.setStatusBarDarkMode(mContext);
            }
        }
    }
}
