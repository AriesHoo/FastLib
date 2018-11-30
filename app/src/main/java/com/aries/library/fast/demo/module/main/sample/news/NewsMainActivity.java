package com.aries.library.fast.demo.module.main.sample.news;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.entity.FastTabEntity;
import com.aries.library.fast.module.activity.FastMainActivity;
import com.aries.library.fast.util.SizeUtil;
import com.flyco.tablayout.CommonTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: AriesHoo on 2018/7/23 10:01
 * @E-Mail: AriesHoo@126.com
 * Function: 腾讯新闻
 * Description:
 */
public class NewsMainActivity extends FastMainActivity {
    String[] titles;

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public List<FastTabEntity> getTabList() {
        titles = getResources().getStringArray(R.array.arrays_tab_news);
        ArrayList<FastTabEntity> list = new ArrayList<>();
        list.add(new FastTabEntity(titles[0], R.drawable.ic_tab_news_main_normal, R.drawable.ic_tab_news_main_selected, NewsItemFragment.newInstance(0)));
        list.add(new FastTabEntity(titles[1], R.drawable.ic_tab_news_recommend_normal, R.drawable.ic_tab_news_recommend_selected, NewsItemFragment.newInstance(1)));
        list.add(new FastTabEntity(titles[2], R.drawable.ic_tab_news_live_normal, R.drawable.ic_tab_news_live_selected, NewsItemFragment.newInstance(2)));
        list.add(new FastTabEntity(titles[3], R.drawable.ic_tab_news_mine_normal, R.drawable.ic_tab_news_mine_selected, NewsItemFragment.newInstance(3)));
        return list;
    }

    @Override
    public void setTabLayout(CommonTabLayout tabLayout) {
        tabLayout.setTextSize(10f)
                .setIconMargin(SizeUtil.dp2px(2))
                .setIconWidth(SizeUtil.dp2px(22))
                .setIconHeight(SizeUtil.dp2px(22))
                .setTextSelectColor(ContextCompat.getColor(mContext, R.color.colorMainNews));
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mContentView.setBackgroundResource(R.color.colorWhite);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
