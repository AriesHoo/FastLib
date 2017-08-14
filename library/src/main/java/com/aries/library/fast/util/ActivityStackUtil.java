package com.aries.library.fast.util;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

/**
 * Created: AriesHoo on 2017/7/19 15:45
 * Function: Activity堆栈管理工具类
 * Desc:
 */
public class ActivityStackUtil {
    private final String TAG = this.getClass().getSimpleName();
    private static Stack<Activity> activityStack;
    private static volatile ActivityStackUtil instance;

    private ActivityStackUtil() {
    }

    public static ActivityStackUtil getInstance() {
        if (instance == null) {
            synchronized (ActivityStackUtil.class) {
                if (instance == null) {
                    instance = new ActivityStackUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 获取最后一个入栈Activity理论上是应用当前停留Activity
     * (前提是所有Activity均在onCreate调用 push onDestroy调用pop)
     *
     * @return
     */
    public Activity getCurrent() {
        if (activityStack != null && activityStack.size() != 0) {
            Activity activity = activityStack.lastElement();
            Log.i(this.TAG, "get current activity:" + activity.getClass().getSimpleName());
            return activity;
        } else {
            return null;
        }
    }

    /**
     * 如栈
     *
     * @param activity
     */
    public void push(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack();
        }
        Log.i(this.TAG, "push stack activity:" + activity.getClass().getSimpleName());
        activityStack.add(activity);
    }

    /**
     * 出栈
     *
     * @param activity
     */
    public void pop(Activity activity) {
        if (activity != null) {
            activity.finish();
            Log.i(this.TAG, "remove current activity:" + activity.getClass().getSimpleName());
            activityStack.remove(activity);
        }

    }

    /**
     * 将栈里的Activity全部清空
     */
    public void popAll() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = this.getCurrent();
                if (activity == null) {
                    break;
                }
                this.pop(activity);
            }
        }

    }

    /**
     * 将堆栈里退回某个Activity为止
     *
     * @param cls
     */
    public void popAllExceptCurrent(Class cls) {
        while (true) {
            Activity activity = this.getCurrent();
            if (activity == null || activity.getClass().equals(cls)) {
                return;
            }

            this.pop(activity);
        }
    }

    /**
     * 将栈里除某个Activity全部清空
     *
     * @param cls
     */
    public void popAllExcept(Class cls) {
        if (activityStack == null || activityStack.size() == 0) {
            return;
        }
        for (Activity activity : activityStack) {
            if (activity != null && !activity.getClass().equals(cls)) {
                pop(activity);
            }
        }
    }

    /**
     * 获取前一个Activity
     *
     * @return
     */
    public Activity getPrevious() {
        if (activityStack != null && activityStack.size() >= 2) {
            Activity activity = activityStack.get(activityStack.size() - 2);
            Log.i(this.TAG, "get Previous Activity:" + activity.getClass().getSimpleName());
            return activity;
        } else {
            return null;
        }
    }
}
