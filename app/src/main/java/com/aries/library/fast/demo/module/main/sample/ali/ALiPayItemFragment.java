package com.aries.library.fast.demo.module.main.sample.ali;

import android.os.Bundle;

import com.aries.library.fast.demo.R;
import com.aries.ui.view.title.TitleBarView;

/**
 * @Author: AriesHoo on 2018/11/19 14:19
 * @E-Mail: AriesHoo@126.com
 * @Function: 支付宝-主页Fragment
 * @Description:
 */
public class ALiPayItemFragment extends ALiPayBaseFragment {

    private int mPosition = 0;

    public static ALiPayItemFragment newInstance(int position) {
        Bundle args = new Bundle();
        ALiPayItemFragment fragment = new ALiPayItemFragment();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
        mPosition = getArguments().getInt("position");
        super.beforeSetTitleBar(titleBar);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setLeftText(titles[mPosition]);
        if (mPosition == 3) {
            titleBar.setRightText(R.string.setting);
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_ali_pay_item;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }
}
