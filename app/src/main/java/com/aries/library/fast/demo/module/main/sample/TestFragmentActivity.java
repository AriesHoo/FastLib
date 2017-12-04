package com.aries.library.fast.demo.module.main.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.constant.ApiConstant;
import com.aries.library.fast.demo.module.activity.MovieBaseFragment;
import com.aries.library.fast.module.activity.FastTitleActivity;
import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2017/11/28 14:42
 * E-Mail: AriesHoo@126.com
 * Function: Fragment单独嵌套--校验Fragment懒加载问题
 * Description:
 */
public class TestFragmentActivity extends FastTitleActivity {
    private Fragment mFragment;

    @Override
    public int getContentLayout() {
        return R.layout.activity_test_fragment;
    }

    @Override
    public int getContentBackground() {
        return R.color.colorBackground;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("Activity直接嵌套Fragment懒加载效果")
                .setTitleMainTextSize(TypedValue.COMPLEX_UNIT_DIP,16)
                .setTitleMainTextMarquee(true);
    }

    @Override
    public void loadData() {
        super.loadData();
        mFragment = MovieBaseFragment.newInstance(ApiConstant.API_MOVIE_IN_THEATERS);
        //此处设置先隐藏再显示方能进入Fragment类的显示隐藏回调--懒加载
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fLayout_containerTestFragment, mFragment)
//                .hide(mFragment)
//                .show(mFragment)
                .commit();
    }
}
