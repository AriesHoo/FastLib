package com.aries.library.fast.demo.base;

import android.app.Activity;

import com.aries.ui.util.ResourceUtil;

import org.simple.eventbus.EventBus;

import butterknife.Unbinder;

/**
 * @Author: AriesHoo on 2018/11/19 14:13
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public class BaseHelper {
    protected Activity mContext;
    protected Unbinder mUnBinder;
    protected ResourceUtil mResourceUtil;

    public BaseHelper(Activity context) {
        mContext = context;
        mResourceUtil = new ResourceUtil(mContext);
    }

    /**
     * Activity 关闭onDestroy调用
     */
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
    }

}
