package com.aries.library.fast.demo.module.sample.news;

import android.os.Bundle;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.entity.TabEntity;
import com.aries.library.fast.module.activity.FastMainActivity;
import com.flyco.tablayout.CommonTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: AriesHoo on 2017/8/6 23:27
 * Function: 腾讯新闻
 * Desc:
 */

public class NewsMainActivity extends FastMainActivity {
    String[] titles;

    @Override
    protected boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    public List<TabEntity> getTabList() {
        titles = getResources().getStringArray(R.array.arrays_tab_news);
        ArrayList<TabEntity> list = new ArrayList<>();
        list.add(new TabEntity(titles[0], R.drawable.ic_tab_news_main_normal, R.drawable.ic_tab_news_main_selected, NewsItemFragment.newInstance(0)));
        list.add(new TabEntity(titles[1], R.drawable.ic_tab_news_recommend_normal, R.drawable.ic_tab_news_recommend_selected, NewsItemFragment.newInstance(1)));
        list.add(new TabEntity(titles[2], R.drawable.ic_tab_news_live_normal, R.drawable.ic_tab_news_live_selected, NewsItemFragment.newInstance(2)));
        list.add(new TabEntity(titles[3], R.drawable.ic_tab_news_mine_normal, R.drawable.ic_tab_news_mine_selected, NewsItemFragment.newInstance(3)));
        return list;
    }

    @Override
    public void setTabLayout(CommonTabLayout tabLayout) {
        tabLayout.setTextSelectColor(getResources().getColor(R.color.colorMainNews));
        tabLayout.setIconHeight(20);
        tabLayout.setIconWidth(20);
        tabLayout.setTextsize(10);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
