package com.aries.library.fast.delegate;

import android.app.Activity;
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
 * @Author: AriesHoo on 2018/7/13 17:53
 * @E-Mail: AriesHoo@126.com
 * Function: 带TitleBarView 的Activity及Fragment代理类
 * Description:
 * 1、2018-4-20 13:53:57 简化全局属性设置通过接口暴露实现
 * 2、2018-6-22 14:06:50 设置通用基础数据
 * 3、2018-7-23 09:47:16 修改TitleBarView设置主标题逻辑
 * ({@link Activity#getTitle()}获取不和应用名称一致才进行设置-因Manifest未设置Activity的label属性获取的是应用名称)
 * 4、2018-11-19 11:27:42 设置全局Tint颜色资源
 */
public class FastTitleDelegate {
    public TitleBarView mTitleBar;

    public FastTitleDelegate(View rootView, IFastTitleView iTitleBarView, Class<?> cls) {
        mTitleBar = rootView.findViewById(R.id.titleBar_headFastLib);
        if (mTitleBar == null) {
            mTitleBar = FindViewUtil.getTargetView(rootView, TitleBarView.class);
        }
        if (mTitleBar == null) {
            return;
        }
        LoggerManager.i("class:" + cls.getSimpleName());
        //默认的MD风格返回箭头icon如使用该风格可以不用设置
        final Activity activity = FastStackUtil.getInstance().getActivity(cls);
        //设置TitleBarView 所有TextView颜色
        mTitleBar.setLeftTextDrawable(activity != null ? R.drawable.fast_ic_back : 0)
                .setLeftTextDrawableTintResource(R.color.colorTitleText)
                .setOnLeftTextClickListener(activity == null ? null : new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                })
                .setTextColorResource(R.color.colorTitleText)
                .setRightTextDrawableTintResource(R.color.colorTitleText)
                .setActionTintResource(R.color.colorTitleText)
                .setTitleMainText(getTitle(activity));
        TitleBarViewControl titleBarViewControl = FastManager.getInstance().getTitleBarViewControl();
        if (titleBarViewControl != null) {
            titleBarViewControl.createTitleBarViewControl(mTitleBar, cls);
        }
        iTitleBarView.beforeSetTitleBar(mTitleBar);
        iTitleBarView.setTitleBar(mTitleBar);
    }

    /**
     * 获取Activity 标题({@link Activity#getTitle()}获取不和应用名称一致才进行设置-因Manifest未设置Activity的label属性获取的是应用名称)
     *
     * @param activity
     * @return
     */
    private CharSequence getTitle(Activity activity) {
        if (activity != null) {
            CharSequence appName = FastUtil.getAppName(activity);
            CharSequence label = activity.getTitle();
            if (label != null && !label.equals(appName)) {
                return label;
            }
        }
        return "";
    }
}
