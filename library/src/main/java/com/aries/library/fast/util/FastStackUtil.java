package com.aries.library.fast.util;

import android.app.Activity;

import com.aries.library.fast.manager.LoggerManager;

import java.util.Stack;

/**
 * Created: AriesHoo on 2017/7/19 15:45
 * Function: Activity堆栈管理工具类
 * Desc:
 */
public class FastStackUtil {
    private final String TAG = this.getClass().getSimpleName();
    private static Stack<Activity> activityStack;
    private static volatile FastStackUtil instance;

    private FastStackUtil() {
    }

    public static FastStackUtil getInstance() {
        if (instance == null) {
            synchronized (FastStackUtil.class) {
                if (instance == null) {
                    instance = new FastStackUtil();
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
            LoggerManager.i(this.TAG, "get current activity:" + activity.getClass().getSimpleName());
            return activity;
        } else {
            return null;
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
            LoggerManager.i(this.TAG, "get Previous Activity:" + activity.getClass().getSimpleName());
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
        LoggerManager.i(this.TAG, "push stack activity:" + activity.getClass().getSimpleName());
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
            LoggerManager.i(this.TAG, "remove current activity:" + activity.getClass().getSimpleName());
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
     * 应用程序退出
     */
    public void exit() {
        try {
            popAll();
            //退出JVM(java虚拟机),释放所占内存资源,0表示正常退出(非0的都为异常退出)
            System.exit(0);
            //从操作系统中结束掉当前程序的进程
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
