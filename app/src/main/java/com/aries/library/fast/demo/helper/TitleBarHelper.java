package com.aries.library.fast.demo.helper;

import android.app.Activity;
import android.view.View;

import com.aries.library.fast.demo.R;
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
        titleBar.setBackgroundResource(R.color.colorWhite);
        titleBar.setLeftTextDrawable(backArrow ? R.drawable.fast_ic_back : 0);
        if (mActivity != null) {
            titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.onBackPressed();
                }
            });
        }
    }
}
