package com.aries.library.fast;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aries.library.fast.delegate.FastRefreshDelegate;
import com.aries.library.fast.delegate.FastTitleDelegate;
import com.aries.library.fast.manager.LoggerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

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


    /**
     * 装载FastRefreshDelegate Map对象
     */
    private WeakHashMap<Class, FastRefreshDelegate> mFastRefreshDelegateMap = new WeakHashMap<>();
    private WeakHashMap<Class, FastTitleDelegate> mFastTitleDelegateMap = new WeakHashMap<>();
    private WeakHashMap<Activity, List<BasisHelper>> mBasisHelperMap = new WeakHashMap<>();

    public FastRefreshDelegate getFastRefreshDelegate(Class cls) {
        FastRefreshDelegate delegate = null;
        if (cls != null && mFastRefreshDelegateMap.containsKey(cls)) {
            delegate = mFastRefreshDelegateMap.get(cls);
        }
        return delegate;
    }

    public void putFastRefreshDelegate(Class cls, FastRefreshDelegate delegate) {
        if (cls != null && !mFastRefreshDelegateMap.containsKey(cls)) {
            mFastRefreshDelegateMap.put(cls, delegate);
        }
    }

    /**
     * {@link FastLifecycleCallbacks#onFragmentViewDestroyed(FragmentManager, Fragment)}
     * {@link FastLifecycleCallbacks#onActivityDestroyed(Activity)}
     *
     * @param cls
     */
    public void removeFastRefreshDelegate(Class cls) {
        FastRefreshDelegate delegate = getFastRefreshDelegate(cls);
        LoggerManager.i("removeFastRefreshDelegate_class:" + cls + ";delegate:" + delegate);
        if (delegate != null) {
            delegate.onDestroy();
            mFastRefreshDelegateMap.remove(cls);
        }
    }

    public FastTitleDelegate getFastTitleDelegate(Class cls) {
        FastTitleDelegate delegate = null;
        if (cls != null && mFastTitleDelegateMap.containsKey(cls)) {
            delegate = mFastTitleDelegateMap.get(cls);
        }
        return delegate;
    }

    public void putFastTitleDelegate(Class cls, FastTitleDelegate delegate) {
        if (cls != null && !mFastTitleDelegateMap.containsKey(cls)) {
            mFastTitleDelegateMap.put(cls, delegate);
        }
    }

    /**
     * {@link FastLifecycleCallbacks#onFragmentViewDestroyed(FragmentManager, Fragment)}
     * {@link FastLifecycleCallbacks#onActivityDestroyed(Activity)}
     *
     * @param cls
     */
    public void removeFastTitleDelegate(Class cls) {
        FastTitleDelegate delegate = getFastTitleDelegate(cls);
        if (delegate != null) {
            delegate.onDestroy();
            mFastTitleDelegateMap.remove(cls);
        }
    }


    public void putBasisHelper(Activity activity, BasisHelper helper) {
        if (activity == null) {
            return;
        }
        if (mBasisHelperMap.containsKey(activity)) {
            mBasisHelperMap.get(activity).add(helper);
        } else {
            List<BasisHelper> list = new ArrayList<>();
            list.add(helper);
            mBasisHelperMap.put(activity, list);
        }
    }

    /**
     * {@link FastLifecycleCallbacks#onActivityDestroyed(Activity)}
     *
     * @param activity
     */
    public void removeBasisHelper(Activity activity) {
        if (mBasisHelperMap.containsKey(activity)) {
            List<BasisHelper> list = mBasisHelperMap.get(activity);
            if (list != null) {
                LoggerManager.i("list:"+list.size());
                for (BasisHelper item : list) {
                    item.onDestroy();
                }
                list.clear();
                mBasisHelperMap.remove(activity);
            }
        }
    }
}
