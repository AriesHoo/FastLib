package com.aries.library.fast.demo;

import android.content.Context;

import com.aries.library.fast.FastApplication;

/**
 * Created: AriesHoo on 2017/6/29 17:00
 * Function:
 * Desc:
 */
public class App extends FastApplication {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

}
