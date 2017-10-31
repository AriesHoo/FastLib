package com.aries.library.fast.manager;

import android.text.TextUtils;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created: AriesHoo on 2017/7/26 21:11
 * Function: logger日志管理类
 * Desc:
 */
public abstract class LoggerManager {

    private static String TAG;
    private static boolean DEBUG = true;

    public static void init(String Tag) {
        init(Tag, true);
    }

    public static void init(String tag, final boolean isDebug) {
        LoggerManager.TAG = tag;
        setDebug(isDebug);
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .methodCount(3)
                .tag(TAG) // 全局tag
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return isDebug;
            }
        });
    }

    public static void setDebug(boolean isDebug) {
        LoggerManager.DEBUG = isDebug;
    }

    public static void d(Object msg) {
        d(null, msg);
    }

    public static void d(String tag, Object msg) {
        if (isInit() && DEBUG) {
            Logger.t(tag).d(msg);
        }
    }

    public static void e(String msg) {
        e(null, msg);
    }

    public static void e(String tag, String msg) {
        if (isInit() && DEBUG) {
            Logger.t(tag).e(msg);
        }
    }

    public static void w(String msg) {
        w(null, msg);
    }

    public static void w(String tag, String msg) {
        if (isInit() && DEBUG) {
            Logger.t(tag).w(msg);
        }
    }

    public static void i(String msg) {
        i(null, msg);
    }

    public static void i(String tag, String msg) {
        if (isInit() && DEBUG) {
            Logger.t(tag).i(msg);
        }
    }

    public static void v(String message) {
        v(null, message);
    }

    public static void v(String tag, String message) {
        if (isInit() && DEBUG) {
            Logger.t(tag).v(message);
        }
    }

    public static void json(String json) {
        json(null, json);
    }

    public static void json(String tag, String json) {
        if (isInit() && DEBUG) {
            Logger.t(tag).json(json);
        }
    }

    public static void xml(String xml) {
        xml(null, xml);
    }

    public static void xml(String tag, String xml) {
        if (isInit() && DEBUG) {
            Logger.t(tag).xml(xml);
        }
    }

    private static boolean isInit() {
        if (TextUtils.isEmpty(TAG)) {
            init("LoggerManager");
        }
        return true;
    }
}
