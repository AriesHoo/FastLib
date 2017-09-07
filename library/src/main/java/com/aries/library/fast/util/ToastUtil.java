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

    public static Context mContext;
    private static Toast mSystemToast;
    private static boolean mIsShowRunningForeground;//是否前台运行才显示toast

    public static void init(Context context) {
        mContext = context;
        init(context, false);
    }

    /**
     * @param context
     * @param isShowRunningForeground 是否前台运行才显示toast
     */
    public static void init(Context context, boolean isShowRunningForeground) {
        mContext = context;
        mIsShowRunningForeground = isShowRunningForeground;
    }

    public static void show(int content) {
        if (null == mContext) {
            throw new NullPointerException(FastConstant.EXCEPTION_NOT_INIT);
        }
        show(content, mIsShowRunningForeground);
    }

    public static void show(int content, boolean isShowRunningForeground) {
        if (null == mContext) {
            throw new NullPointerException(FastConstant.EXCEPTION_NOT_INIT);
        }
        show(mContext.getString(content), isShowRunningForeground);
    }

    public static void show(String content) {
        show(content, mIsShowRunningForeground);
    }

    public static void show(String content, boolean isShowRunningForeground) {
        if (null == mContext) {
            throw new NullPointerException(FastConstant.EXCEPTION_NOT_INIT);
        } else {
            int duration = content.length() > 10 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
            if (mSystemToast == null) {
                mSystemToast = Toast.makeText(mContext, content, duration);
            }
            mSystemToast.setText(content);
            mSystemToast.setDuration(duration);
            if (!isShowRunningForeground || (isShowRunningForeground && FastUtil.isRunningForeground(mContext)))
                mSystemToast.show();
        }
    }


}
