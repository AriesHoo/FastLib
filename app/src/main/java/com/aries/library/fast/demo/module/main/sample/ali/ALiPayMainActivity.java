package com.aries.library.fast.demo.module.main.sample.ali;

import android.os.Bundle;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.entity.FastTabEntity;
import com.aries.library.fast.module.activity.FastMainActivity;
import com.aries.ui.view.tab.CommonTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Author: AriesHoo on 2018/7/23 10:00
 * @E-Mail: AriesHoo@126.com
 * Function: 快速实现支付宝主页
 * Description:
 */
public class ALiPayMainActivity extends FastMainActivity {

    @BindView(R.id.tabLayout_commonFastLib) CommonTabLayout mTabLayout;
    String[] titles;

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
//        tabLayout.setTextSize(10f)
//                .setIconMargin(SizeUtil.dp2px(2))
//                .setIconWidth(SizeUtil.dp2px(22))
//                .setIconHeight(SizeUtil.dp2px(22))
//                .setTextSelectColor(ContextCompat.getColor(mContext, R.color.colorMainAli));
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        titles = getResources().getStringArray(R.array.arrays_tab_ali);
        super.beforeInitView(savedInstanceState);
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
