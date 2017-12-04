package com.aries.library.fast.demo.module.main.sample.ali;

import android.os.Bundle;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.entity.FastTabEntity;
import com.aries.library.fast.module.activity.FastMainActivity;
import com.flyco.tablayout.CommonTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: AriesHoo on 2017/8/5 20:48
 * Function: 快速实现支付宝主页
 * Desc:
 */
public class ALiPayMainActivity extends FastMainActivity {

    String[] titles;

    @Override
    protected boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    public List<FastTabEntity> getTabList() {
        ArrayList<FastTabEntity> list = new ArrayList<>();
        list.add(new FastTabEntity(titles[0], R.drawable.ic_tab_main_ali, R.drawable.ic_tab_main_ali_selected, ALiPayItemFragment.newInstance(0)));
        list.add(new FastTabEntity(titles[1], R.drawable.ic_tab_kou_bei_ali, R.drawable.ic_tab_kou_bei_ali_selected, ALiPayItemFragment.newInstance(1)));
        list.add(new FastTabEntity(titles[2], R.drawable.ic_tab_friends_ali, R.drawable.ic_tab_friends_ali_selected, ALiPayItemFragment.newInstance(2)));
        list.add(new FastTabEntity(titles[3], R.drawable.ic_tab_mine_ali, R.drawable.ic_tab_mine_ali_selected, ALiPayItemFragment.newInstance(3)));
        return list;
    }

    @Override
    public void setTabLayout(CommonTabLayout tabLayout) {
        tabLayout.setTextsize(10);
        tabLayout.setIconMargin(2);
        tabLayout.setIconWidth(22);
        tabLayout.setIconHeight(22);
        tabLayout.setTextSelectColor(getResources().getColor(R.color.colorMainAli));
    }

    @Override
    public void beforeInitView() {
        titles = getResources().getStringArray(R.array.arrays_tab_ali);
        super.beforeInitView();
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
