package com.aries.library.fast.delegate;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.aries.library.fast.FastConfig;
import com.aries.library.fast.R;
import com.aries.library.fast.i.IFastTitleView;
import com.aries.library.fast.util.FastUtil;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2018/4/20/020 13:53
 * E-Mail: AriesHoo@126.com
 * Function:带TitleBarView 的Activity及Fragment代理类
 * Description:
 * 1、2018-4-20 13:53:57 简化config设置通过接口暴露实现
 */
public class FastTitleDelegate {
    public TitleBarView mTitleBar;

    public FastTitleDelegate(View rootView, Activity activity, IFastTitleView iTitleBarView, boolean isActivity) {
        mTitleBar = rootView.findViewById(R.id.titleBar_headFastLib);
        if (mTitleBar == null) {
            mTitleBar = FindViewUtil.getTargetView(rootView, TitleBarView.class);
        }
        if (mTitleBar == null) {
            return;
        }
        Drawable mDrawable = FastUtil.getTintDrawable(activity.getResources().getDrawable(R.drawable.fast_ic_back),
                activity.getResources().getColor(R.color.colorTitleText));
        mTitleBar.setLeftTextDrawable(isActivity ? mDrawable:null);
        FastConfig.getInstance(activity).getTitleBarViewControl().createTitleBarViewControl(mTitleBar, isActivity);
        mTitleBar.setOnLeftTextClickListener(iTitleBarView.getLeftClickListener());
        iTitleBarView.beforeSetTitleBar(mTitleBar);
        iTitleBarView.setTitleBar(mTitleBar);
    }
}
