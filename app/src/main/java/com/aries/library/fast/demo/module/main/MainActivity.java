package com.aries.library.fast.demo.module.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.helper.CheckVersionHelper;
import com.aries.library.fast.demo.module.WebViewActivity;
import com.aries.library.fast.demo.module.activity.ActivityFragment;
import com.aries.library.fast.demo.module.mine.MineFragment;
import com.aries.library.fast.demo.module.web.WebAppFragment;
import com.aries.library.fast.entity.FastTabEntity;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.module.activity.FastMainActivity;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.SizeUtil;
import com.aries.ui.view.tab.CommonTabLayout;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AlertDialog;

import butterknife.BindView;

/**
 * @Author: AriesHoo on 2018/7/23 10:00
 * @E-Mail: AriesHoo@126.com
 * Function: 示例主页面
 * Description:
 */
public class MainActivity extends FastMainActivity {

    @BindView(R.id.tabLayout_commonFastLib)
    CommonTabLayout mTabLayout;
    private ArrayList<FastTabEntity> mTabEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public List<FastTabEntity> getTabList() {
        mTabEntities = new ArrayList<>();
        mTabEntities.add(new FastTabEntity(R.string.home, R.drawable.ic_home_normal, R.drawable.ic_home_selected, HomeFragment.newInstance()));
        mTabEntities.add(new FastTabEntity(R.string.web_app, R.drawable.ic_app_normal, R.drawable.ic_app_selected, WebAppFragment.newInstance()));
        mTabEntities.add(new FastTabEntity(R.string.activity, R.drawable.ic_activity_normal, R.drawable.ic_activity_selected, ActivityFragment.newInstance()));
        mTabEntities.add(new FastTabEntity(R.string.mine, R.drawable.ic_mine_normal, R.drawable.ic_mine_selected, MineFragment.newInstance()));
        return mTabEntities;
    }

    @Override
    public void beforeSetContentView() {
        super.beforeSetContentView();
        String url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            WebViewActivity.start(mContext, url);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String url = intent.getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            WebViewActivity.start(mContext, url);
        }
    }

    @Override
    public void setTabLayout(CommonTabLayout tabLayout) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.ic_github);
        tabLayout.setCenterView(imageView, SizeUtil.dp2px(42), SizeUtil.dp2px(42), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.start(mContext, "https://github.com/AriesHoo/FastLib/blob/master/README.md");
            }
        });
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        int[] intArray = new int[]{1,56,-5,33};
        Arrays.sort(intArray);
        LoggerManager.i("main_array",Arrays.toString(intArray));
    }

    @Override
    public void onTabSelect(int position) {
        LoggerManager.d("OnTabSelectListener:onTabSelect:" + position);
    }

    @Override
    public void onTabReselect(int position) {
        LoggerManager.d("OnTabSelectListener:onTabReselect:" + position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoggerManager.i(TAG, "onDestroy");
    }

    @Override
    public void loadData() {
        new CheckVersionHelper(mContext).checkVersion(false);
    }
}
