package com.aries.library.fast.demo.module.main;

import android.os.Bundle;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.module.activity.ActivityFragment;
import com.aries.library.fast.demo.module.mine.MineFragment;
import com.aries.library.fast.entity.FastTabEntity;
import com.aries.library.fast.module.activity.FastMainActivity;
import com.aries.ui.helper.navigation.NavigationViewHelper;
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
    public int getContentBackground() {
        return 0;
    }

    @Override
    protected void beforeControlNavigation(NavigationViewHelper navigationHelper) {
        super.beforeControlNavigation(navigationHelper);
        navigationHelper.setBottomView(mTabLayout);
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
//        ToastUtil.show("测试", ToastUtil.newBuilder()
//                .setTextSize(SizeUtil.dp2px(24))
//                .setTextDrawableGravity(Gravity.TOP)
//                .setTextGravity(Gravity.CENTER)
//                .setPaddingLeft(60)
//                .setPaddingRight(60)
//                .setRadius(12)
//                .setTextDrawable(getResources().getDrawable(R.drawable.ic_launcher))
//                .setGravity(Gravity.CENTER)
//                .setGravityYOffset(0)
//                .setBackgroundColor(Color.MAGENTA));
    }

}
