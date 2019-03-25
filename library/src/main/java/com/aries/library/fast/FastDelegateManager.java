package com.aries.library.fast;

import android.app.Activity;

import com.aries.library.fast.delegate.FastRefreshDelegate;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * @Author: AriesHoo on 2019/3/25 10:48
 * @E-Mail: AriesHoo@126.com
 * @Function: 保存Delegate对象以便统一销毁
 * @Description:
 */
class FastDelegateManager {

    private static volatile FastDelegateManager sInstance;

    private FastDelegateManager() {
    }

    public static FastDelegateManager getInstance() {
        if (sInstance == null) {
            synchronized (FastDelegateManager.class) {
                if (sInstance == null) {
                    sInstance = new FastDelegateManager();
                }
            }
        }
        return sInstance;
    }


    private Map<String, FastRefreshDelegate> mFastRefreshDelegateMap = new HashMap<>();


    public FastRefreshDelegate getFastRefreshDelegate(String cls) {
        FastRefreshDelegate delegate = null;
        if (cls != null && mFastRefreshDelegateMap.containsKey(cls)) {
            delegate = mFastRefreshDelegateMap.get(cls);
        }
        return delegate;
    }

    public void putFastRefreshDelegate(String cls, FastRefreshDelegate delegate) {
        if (cls != null && !mFastRefreshDelegateMap.containsKey(cls)) {
            mFastRefreshDelegateMap.put(cls, delegate);
        }
    }

    /**
     *  {@link FastLifecycleCallbacks#onFragmentViewDestroyed(FragmentManager, Fragment)}
     *  {@link FastLifecycleCallbacks#onActivityDestroyed(Activity)}
     * @param cls
     */
    public void removeFastRefreshDelegate(String cls) {
        FastRefreshDelegate delegate = getFastRefreshDelegate(cls);
        if (delegate != null) {
            delegate.onDestroy();
            mFastRefreshDelegateMap.remove(cls);
        }
    }
}
