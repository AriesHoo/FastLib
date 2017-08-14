package com.aries.library.fast.demo.module.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.base.BaseTitleFragment;
import com.aries.ui.view.title.TitleBarView;
import com.flyco.tablayout.SegmentTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created: AriesHoo on 2017/7/20 11:45
 * Function:
 * Desc:
 */
public class ActivityFragment extends BaseTitleFragment {

    @BindView(R.id.vp_content) ViewPager vpContent;
    private List<Fragment> listFragment = new ArrayList<>();
    private SegmentTabLayout segmentTabLayout;

    public static ActivityFragment newInstance() {
        Bundle args = new Bundle();
        ActivityFragment fragment = new ActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.addCenterAction(new TitleBarView.Action<View>() {
            @Override
            public View getData() {
                View view = View.inflate(mContext, R.layout.layout_activity_tab_segment, null);
                segmentTabLayout = (SegmentTabLayout) view.findViewById(R.id.tabLayout_segment);
                segmentTabLayout.setTabData(getResources().getStringArray(R.array.arrays_tab_management));
                return view;
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return null;
            }
        });

    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_activity;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }
}
