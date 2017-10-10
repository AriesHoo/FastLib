package com.aries.library.fast.demo.helper;

import android.app.Activity;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2017/7/27 9:13
 * Function: 带TitleBarView 的属性统一设置
 * Desc:
 */

public class TitleBarHelper {
    private static volatile TitleBarHelper instance;

    private TitleBarHelper() {
    }

    public static TitleBarHelper getInstance() {
        if (instance == null) {
            synchronized (TitleBarHelper.class) {
                if (instance == null) {
                    instance = new TitleBarHelper();
                }
            }
        }
        return instance;
    }

    public void setTitleBarView(TitleBarView titleBar, Activity mActivity) {
        setTitleBarView(titleBar, mActivity, true);
    }

    public void setTitleBarView(TitleBarView titleBar, Activity mActivity, boolean backArrow) {
        if (titleBar == null) {
            return;
        }
        titleBar.setLeftTextDrawable(backArrow ? R.drawable.fast_ic_back : 0)
                .setOnLeftTextClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mActivity != null)
                            mActivity.onBackPressed();
                    }
                })
                .setBackgroundResource(R.color.colorWhite);
        int elevation = mActivity.getResources().
                getDimensionPixelSize(R.dimen.dp_elevation);
        ViewCompat.setElevation(titleBar, elevation);
        LoggerManager.d("elevation:" + elevation);
    }
}
