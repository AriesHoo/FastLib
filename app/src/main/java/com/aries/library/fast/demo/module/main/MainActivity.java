package com.aries.library.fast.demo.module.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.entity.TabEntity;
import com.aries.library.fast.module.activity.FastMainActivity;
import com.flyco.tablayout.CommonTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FastMainActivity {

    @Override
    public boolean isSwipeEnable() {
        return true;
    }

    @Override
    public List<TabEntity> getTabList() {
        ArrayList<TabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity(getString(R.string.home), R.drawable.ic_home_normal, R.drawable.ic_home_selected, HomeFragment.newInstance()));
//        tabEntities.add(new TabEntity(getString(R.string.activity), R.drawable.ic_activity_normal, R.drawable.ic_activity_selected, ActivityFragment.newInstance()));
        tabEntities.add(new TabEntity(getString(R.string.mine), R.drawable.ic_mine_normal, R.drawable.ic_mine_selected, MineFragment.newInstance()));
        return tabEntities;
    }

    @Override
    public void setTabLayout(CommonTabLayout tabLayout) {
    }

    @Override
    protected void setViewPager(ViewPager mViewPager) {
        super.setViewPager(mViewPager);
        if (isSwipeEnable()) {
            //mViewPager.setCurrentItem(2);
            mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }
}
