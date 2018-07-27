package com.aries.library.fast.i;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.aries.library.fast.widget.FastLoadDialog;

/**
 * @Author: AriesHoo on 2018/7/23 10:39
 * @E-Mail: AriesHoo@126.com
 * Function: 用于全局配置网络请求登录Loading提示框
 * Description:
 */
public interface LoadingDialog {

    /**
     * @param activity
     * @return
     */
    @Nullable
    FastLoadDialog createLoadingDialog(@Nullable Activity activity);
}
