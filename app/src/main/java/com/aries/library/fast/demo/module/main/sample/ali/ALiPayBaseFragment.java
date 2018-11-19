package com.aries.library.fast.demo.module.main.sample.ali;

import android.graphics.Color;
import android.util.TypedValue;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.module.fragment.FastTitleFragment;
import com.aries.ui.view.title.TitleBarView;

/**
 * @Author: AriesHoo on 2018/11/19 14:19
 * @E-Mail: AriesHoo@126.com
 * @Function: 支付宝-BaseFragment
 * @Description:
 */
public abstract class ALiPayBaseFragment extends FastTitleFragment {
    String[] titles;
    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
        super.beforeSetTitleBar(titleBar);
        titles = getResources().getStringArray(R.array.arrays_tab_ali);
        titleBar.setStatusAlpha(75)
                .setStatusBarLightMode(false)
                .setLeftTextColor(Color.WHITE)
                .setRightTextColor(Color.WHITE)
                .setLeftTextSize(TypedValue.COMPLEX_UNIT_DIP, 16)
                .setRightTextSize(TypedValue.COMPLEX_UNIT_DIP, 16)
                .setBackgroundResource(R.color.colorMainAli);
    }
}
