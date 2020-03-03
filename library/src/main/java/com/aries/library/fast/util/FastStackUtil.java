package com.aries.library.fast.util;

import android.app.Activity;
import android.os.Bundle;

import com.aries.library.fast.manager.LoggerManager;

import java.util.Stack;

/**
 * @Author: AriesHoo on 2018/7/23 14:32
 * @E-Mail: AriesHoo@126.com
 * Function:Activity堆栈管理工具类
 * Description:
 * 1、2018-6-21 09:49:11 新增根据class获取Activity方法
 * 2、2018-7-30 10:00:45 修改方法返回值
 * 3、2018-11-29 11:44:16 新增{@link #pop(Activity, boolean)} 增加是否调用finish()方法参数,避免因Activity状态变换(如横竖屏切换)造成onActivityDestroyed时候切换回来状态无法保存
 * 即:{@link Activity#onCreate(Bundle)} onCreate(Bundle savedInstanceState)  savedInstanceState对象为空-因为pop的时候已将其finish
 * 4、2019-1-7 17:51:08 新增{@link #exit(boolean)} 是否杀死进程控制
 */
public class FastStackUtil {
    private final String TAG = this.getClass().getSimpleName();
    private static Stack<Activity> mActivityStack;
    private static volatile FastStackUtil sInstance;

    private FastStackUtil() {
    }

    public static FastStackUtil getInstance() {
        if (sInstance == null) {
            synchronized (FastStackUtil.class) {
                if (sInstance == null) {
                    sInstance = new FastStackUtil();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取Stack
     *
     * @return
     */
    public Stack<Activity> getStack() {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        return mActivityStack;
    }

    /**
     * 获取最后一个入栈Activity理论上是应用当前停留Activity
     * (前提是所有Activity均在onCreate调用 push onDestroy调用pop)
     *
     * @return
     */
    public Activity getCurrent() {
        if (mActivityStack != null && mActivityStack.size() != 0) {
            Activity activity = mActivityStack.lastElement();
            LoggerManager.i(TAG, "get current activity:" + activity.getClass().getSimpleName());
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
        if (mActivityStack != null && mActivityStack.size() >= 2) {
            Activity activity = mActivityStack.get(mActivityStack.size() - 2);
            LoggerManager.i(TAG, "get Previous Activity:" + activity.getClass().getSimpleName());
            return activity;
        } else {
            return null;
        }
    }

    /**
     * 根据Class 获取Activity
     *
     * @param cls
     * @return
     */
    public Activity getActivity(Class cls) {
        if (mActivityStack == null || mActivityStack.size() == 0 || cls == null) {
            return null;
        }
        for (Activity activity : mActivityStack) {
            if (activity != null && activity.getClass() == cls) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 入栈
     *
     * @param activity
     */
    public FastStackUtil push(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack();
        }
        mActivityStack.add(activity);
        LoggerManager.i(TAG, "push stack activity:" + activity.getClass().getSimpleName());
        return sInstance;
    }

    public FastStackUtil pop(Activity activity) {
        return pop(activity, true);
    }

    /**
     * 出栈
     *
     * @param activity Activity对象
     * @param isFinish 是否关闭Activity 调用{@link Activity#finish()}--在生命周期onActivityDestroyed的时候建议传false不然Activity状态无法保存
     * @return
     */
    public FastStackUtil pop(Activity activity, boolean isFinish) {
        if (activity != null) {
            LoggerManager.i(TAG, "remove current activity:" + activity.getClass().getSimpleName() + ";isFinishing" + activity.isFinishing());
            if (mActivityStack != null && mActivityStack.contains(activity)) {
                mActivityStack.remove(activity);
                LoggerManager.i(TAG, "remove current activity:" + activity.getClass().getSimpleName() + ";size:" + mActivityStack.size());
            }
            if (isFinish) {
                activity.finish();
            }
        }
        return sInstance;
    }

    /**
     * 将栈里的Activity全部清空
     */
    public FastStackUtil popAll() {
        if (mActivityStack != null) {
            while (mActivityStack.size() > 0) {
                Activity activity = this.getCurrent();
                if (activity == null) {
                    break;
                }
                pop(activity);
            }
        }
        return sInstance;
    }

    /**
     * 将堆栈里退回某个Activity为止
     *
     * @param cls
     */
    public FastStackUtil popAllExceptCurrent(Class cls) {
        while (true) {
            Activity activity = this.getCurrent();
            if (activity == null || activity.getClass().equals(cls)) {
                return sInstance;
            }
            pop(activity);
        }
    }

    /**
     * 只留下栈顶一个Activity
     */
    public FastStackUtil popAllExceptCurrent() {
        while (true) {
            Activity activity = this.getPrevious();
            if (activity == null) {
                return sInstance;
            }
            pop(activity);
        }
    }

    public FastStackUtil exit() {
        return exit(true);
    }

    /**
     * 应用程序退出
     *
     * @param kill 是否杀掉进程
     * @return
     */
    public FastStackUtil exit(boolean kill) {
        try {
            popAll();
            if (kill) {
                //退出JVM(java虚拟机),释放所占内存资源,0表示正常退出(非0的都为异常退出)
                System.exit(0);
                //从操作系统中结束掉当前程序的进程
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        } catch (Exception e) {
            System.exit(-1);
            LoggerManager.e(TAG, "exit():" + e.getMessage());
        }
        return sInstance;
    }
}
