package com.aries.library.fast.delegate;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.aries.library.fast.R;
import com.aries.library.fast.interfaces.IFastTitleView;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2017/7/26 10:39
 * Function: 带TitleBarView 的Activity及Fragment代理类
 * Desc:
 */
public class FastTitleDelegate {
    public TitleBarView titleBar;
    public int type = 0;//type >0 表示支持状态栏黑白字切换

    public FastTitleDelegate(View rootView, Activity mContext, IFastTitleView iTitleBarView) {
        if (iTitleBarView.isLightStatusBarEnable()) {
            type = StatusBarUtil.setStatusBarLightMode(mContext);
        }
        getTitleBarView(rootView);
        if (titleBar == null) {
            return;
        }
        if (iTitleBarView.isLightStatusBarEnable()) {
            int colorText = mContext.getResources().getColor(R.color.colorTitleText);
            titleBar.setTitleMainTextColor(colorText);
            titleBar.setTitleSubTextColor(colorText);
            titleBar.setLeftTextColor(colorText);
            titleBar.setRightTextColor(colorText);
        }
        if (iTitleBarView.isLightStatusBarEnable() && type <= 0) {
            titleBar.setStatusAlpha(102);
        }
        iTitleBarView.beforeSetTitleBar(titleBar);
        iTitleBarView.setTitleBar(titleBar);
    }

    /**
     * 获取布局里的TitleBarView
     *
     * @param rootView
     * @return
     */
    private void getTitleBarView(View rootView) {
        if (rootView instanceof TitleBarView && titleBar == null) {
            titleBar = (TitleBarView) rootView;
        } else if (rootView instanceof ViewGroup) {
            ViewGroup contentView = (ViewGroup) rootView;
            int childCount = contentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = contentView.getChildAt(i);
                getTitleBarView(childView);
            }
        }
    }
}
