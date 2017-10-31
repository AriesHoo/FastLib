package com.aries.library.fast.demo.module.main;

import android.os.Bundle;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.module.activity.ActivityFragment;
import com.aries.library.fast.demo.module.mine.MineFragment;
import com.aries.library.fast.entity.FastTabEntity;
import com.aries.library.fast.module.activity.FastMainActivity;
import com.flyco.tablayout.CommonTabLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created: AriesHoo on 2017/9/7 10:59
 * Function: 示例主页面
 * Desc:
 */
public class MainActivity extends FastMainActivity {

    @Override
    public boolean isSwipeEnable() {
        return true;
    }

    @Override
    public List<FastTabEntity> getTabList() {
        ArrayList<FastTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new FastTabEntity(getString(R.string.home), R.drawable.ic_home_normal, R.drawable.ic_home_selected, HomeFragment.newInstance()));
        tabEntities.add(new FastTabEntity(getString(R.string.activity), R.drawable.ic_activity_normal, R.drawable.ic_activity_selected, ActivityFragment.newInstance()));
        tabEntities.add(new FastTabEntity(getString(R.string.mine), R.drawable.ic_mine_normal, R.drawable.ic_mine_selected, MineFragment.newInstance()));
        return tabEntities;
    }

    @Override
    public void setTabLayout(CommonTabLayout tabLayout) {
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

}
