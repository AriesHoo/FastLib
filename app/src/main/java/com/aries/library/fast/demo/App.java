package com.aries.library.fast.demo;

import android.content.Context;

import com.aries.library.fast.FastApplication;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.util.ToastUtil;


/**
 * Created: AriesHoo on 2017/7/20 14:11
 * Function:
 * Desc:
 */
public class App extends FastApplication {
    private static Context mContext;
    private String TAG = "FastLib";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        LoggerManager.init(TAG, BuildConfig.DEBUG);
        ToastUtil.init(mContext,true);
    }
}
