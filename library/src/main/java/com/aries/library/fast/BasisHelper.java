package com.aries.library.fast;

import android.app.Activity;

import com.aries.library.fast.manager.LoggerManager;

import butterknife.Unbinder;

/**
 * @Author: AriesHoo on 2019/8/7 14:22
 * @E-Mail: AriesHoo@126.com
 * @Function: 绑定Activity Helper
 * @Description:
 */
public class BasisHelper {
    protected Activity mContext;
    protected Unbinder mUnBinder;
    protected String mTag = getClass().getSimpleName();

    public BasisHelper(Activity context) {
        mContext = context;
        FastDelegateManager.getInstance().putBasisHelper(context, this);
    }

    /**
     * Activity 关闭onDestroy调用
     */
    public void onDestroy() {
        LoggerManager.i(mTag, "onDestroy");
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
    }
}
