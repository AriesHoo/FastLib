package com.aries.library.fast.util;

import android.content.Context;
import android.widget.Toast;

import com.aries.library.fast.FastConstant;

/**
 * Created: AriesHoo on 2017/7/24 11:20
 * Function: Toast 工具
 * Desc:
 */
public class ToastUtil {

    public static Context sContext;
    private static Toast sSystemToast;
    private static boolean sIsShowRunningForeground;//是否前台运行才显示toast

    public static void init(Context context) {
        sContext = context;
        init(context, false);
    }

    /**
     * @param context
     * @param isShowRunningForeground 是否前台运行才显示toast
     */
    public static void init(Context context, boolean isShowRunningForeground) {
        if (context != null) {
            sContext = context.getApplicationContext();
        }
        sIsShowRunningForeground = isShowRunningForeground;
    }

    public static Toast show(int content) {
        if (null == sContext) {
            throw new NullPointerException(FastConstant.EXCEPTION_NOT_INIT);
        }
        return show(content, sIsShowRunningForeground);
    }

    public static Toast show(int content, boolean isShowRunningForeground) {
        if (null == sContext) {
            throw new NullPointerException(FastConstant.EXCEPTION_NOT_INIT);
        }
        return show(sContext.getString(content), isShowRunningForeground);
    }

    public static Toast show(CharSequence content) {
        return show(content, sIsShowRunningForeground);
    }

    public static Toast show(CharSequence content, boolean isShowRunningForeground) {
        if (null == sContext) {
            throw new NullPointerException(FastConstant.EXCEPTION_NOT_INIT);
        } else {
            int duration = content.length() > 10 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
            if (sSystemToast == null) {
                sSystemToast = Toast.makeText(sContext, content, duration);
            }
            sSystemToast.setText(content);
            sSystemToast.setDuration(duration);
            if (!isShowRunningForeground || (isShowRunningForeground && FastUtil.isRunningForeground(sContext)))
                sSystemToast.show();
        }
        return sSystemToast;
    }


}
