package com.aries.library.fast.module.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.aries.library.fast.R;
import com.aries.library.fast.basis.BasisActivity;
import com.aries.library.fast.entity.FastTabEntity;
import com.aries.library.fast.i.IFastMainView;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.manager.TabLayoutManager;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.ToastUtil;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created: AriesHoo on 2017/7/19 16:52
 * Function:快速创建主页布局
 * Desc:
 */
public abstract class FastMainActivity extends BasisActivity implements IFastMainView, OnTabSelectListener {

    public CommonTabLayout mTabLayout;
    public ViewPager mViewPager;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private boolean mIsPager;
    protected boolean isFirstBack = true;
    private List<Integer> mPosition;
    private LinearLayout mTabLinearLayout;

    protected void setViewPager(ViewPager mViewPager) {
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public int getContentLayout() {
        return mIsPager ? R.layout.fast_activity_main_view_pager : R.layout.fast_activity_main;
    }

    @Override
    public void beforeSetContentView() {
        mIsPager = isSwipeEnable();
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        mTabLayout = (CommonTabLayout) findViewById(R.id.tabLayout_common);
        List<FastTabEntity> tabEntities = getTabList();
        if (tabEntities.size() == 0) {
            return;
        }
        if (TextUtils.isEmpty(tabEntities.get(0).mTitle)) {
            mTabLayout.setTextsize(0);
            mTabLayout.setIconHeight(28);
            mTabLayout.setIconWidth(28);
        }
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < tabEntities.size(); i++) {
            FastTabEntity entity = tabEntities.get(i);
            fragments.add(entity.mFragment);
            mTabEntities.add(entity);
            if (TextUtils.isEmpty(entity.mTitle)) {
                if (mPosition == null) {
                    mPosition = new ArrayList<>();
                }
                mPosition.add(i);
            }
        }
        if (mIsPager) {
            initViewPager(fragments);
        } else {
            mTabLayout.setTabData(mTabEntities, this, R.id.fLayout_container, fragments);
            mTabLayout.setOnTabSelectListener(this);
        }
        setTabLayout(mTabLayout);
        setViewPager(mViewPager);
    }

    private void initViewPager(final List<Fragment> fragments) {
        mViewPager = (ViewPager) findViewById(R.id.vp_content);
        TabLayoutManager.getInstance().setCommonTabData(this, mTabLayout, mViewPager, mTabEntities, fragments, this);
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onBackPressed() {
        quitApp();
    }

    protected void quitApp() {
        if (isFirstBack) {
            ToastUtil.show(R.string.fast_quit_app);
            isFirstBack = false;
            RxJavaManager.getInstance().setTimer(2000, new RxJavaManager.TimerListener() {
                @Override
                public void timeEnd() {
                    isFirstBack = true;
                }
            });
        } else if (!isFirstBack) {
            FastStackUtil.getInstance().exit();
        }
    }
}
