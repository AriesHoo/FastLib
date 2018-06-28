package com.aries.library.fast.demo.base;

import android.app.Activity;

import com.aries.ui.util.ResourceUtil;

import org.simple.eventbus.EventBus;

import butterknife.Unbinder;

/**
 * Created: AriesHoo on 2018/5/3/003 11:11
 * E-Mail: AriesHoo@126.com
 * Function:
 * Description:
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
