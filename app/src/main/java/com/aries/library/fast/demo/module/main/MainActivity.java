package com.aries.library.fast.demo.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.helper.CheckVersionHelper;
import com.aries.library.fast.demo.module.activity.ActivityFragment;
import com.aries.library.fast.demo.module.mine.MineFragment;
import com.aries.library.fast.entity.FastTabEntity;
import com.aries.library.fast.module.activity.FastMainActivity;
import com.flyco.tablayout.CommonTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created: AriesHoo on 2017/9/7 10:59
 * Function: 示例主页面
 * Desc:
 */
public class MainActivity extends FastMainActivity {

    @BindView(R.id.tabLayout_commonFastLib) CommonTabLayout mTabLayout;

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public List<FastTabEntity> getTabList() {
        ArrayList<FastTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new FastTabEntity(getString(R.string.home), R.drawable.ic_home_normal, R.drawable.ic_home_selected, HomeFragment.newInstance()));
        tabEntities.add(new FastTabEntity(getString(R.string.activity), R.drawable.ic_activity_normal, R.drawable.ic_activity_selected, ActivityFragment.newInstance()));
        tabEntities.add(new FastTabEntity(getString(R.string.mine), R.drawable.ic_mine_normal, R.drawable.ic_mine_selected, MineFragment.newInstance()));
        tabEntities.add(new FastTabEntity(getString(R.string.activity), R.drawable.ic_activity_normal, R.drawable.ic_activity_selected, ActivityFragment.newInstance()));
        return tabEntities;
    }

    @Override
    public void setTabLayout(CommonTabLayout tabLayout) {
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        CheckVersionHelper.with(this)
                .checkVersion(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> list = getSupportFragmentManager().getFragments();
        if (list == null && list.size() == 0) {
            return;
        }
        for (Fragment f : list) {
            f.onActivityResult(requestCode, resultCode, data);
        }
    }
}
