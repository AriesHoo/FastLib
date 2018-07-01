package com.aries.library.fast.delegate;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.R;
import com.aries.library.fast.i.IFastTitleView;
import com.aries.library.fast.i.TitleBarViewControl;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.FastUtil;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2018/4/20/020 13:53
 * E-Mail: AriesHoo@126.com
 * Function:带TitleBarView 的Activity及Fragment代理类
 * Description:
 * 1、2018-4-20 13:53:57 简化config设置通过接口暴露实现
 * 2、2018-6-22 14:06:50 设置通用基础数据
 */
public class FastTitleDelegate {
    public TitleBarView mTitleBar;

    public FastTitleDelegate(View rootView, IFastTitleView iTitleBarView, Class<?> cls) {
        mTitleBar = rootView.findViewById(R.id.titleBar_headFastLib);
        Context context = rootView.getContext();
        if (mTitleBar == null) {
            mTitleBar = FindViewUtil.getTargetView(rootView, TitleBarView.class);
        }
        if (mTitleBar == null) {
            return;
        }
        LoggerManager.i("class:"+cls.getSimpleName());
        //默认的MD风格返回箭头icon如使用该风格可以不用设置
        Drawable mDrawable = FastUtil.getTintDrawable(context.getResources().getDrawable(R.drawable.fast_ic_back),
                context.getResources().getColor(R.color.colorTitleText));
        final Activity activity = FastStackUtil.getInstance().getActivity(cls);
        //设置TitleBarView 所有TextView颜色
        mTitleBar.setLeftTextDrawable(activity != null ? mDrawable : null)
                .setOnLeftTextClickListener(activity == null ? null : new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                })
                .setTextColor(context.getResources().getColor(R.color.colorTitleText))
                .setTitleMainText(activity != null ? activity.getTitle() : "");
        TitleBarViewControl titleBarViewControl = FastManager.getInstance().getTitleBarViewControl();
        if (titleBarViewControl != null) {
            titleBarViewControl.createTitleBarViewControl(mTitleBar, cls);
        }
        iTitleBarView.beforeSetTitleBar(mTitleBar);
        iTitleBarView.setTitleBar(mTitleBar);
    }
}
