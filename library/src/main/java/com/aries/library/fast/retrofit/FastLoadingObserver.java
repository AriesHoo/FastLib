package com.aries.library.fast.retrofit;


import android.app.Activity;
import android.support.annotation.Nullable;

import com.aries.library.fast.FastConfig;
import com.aries.library.fast.widget.FastLoadDialog;

/**
 * Created: AriesHoo on 2017/8/24 16:09
 * Function: 快速创建支持Loading的Retrofit观察者
 * Desc:
 */
public abstract class FastLoadingObserver<T> extends FastObserver<T> {


    private FastLoadDialog mDialog;

    /**
     * 用于全局配置
     *
     * @param activity
     */
    public FastLoadingObserver(@Nullable Activity activity) {
        this(FastConfig.getInstance(activity).getLoadingDialog().createLoadingDialog(activity));
    }

    public FastLoadingObserver(FastLoadDialog dialog) {
        this.mDialog = dialog;
    }

    @Override
    public void onNext(T entity) {
        dismissProgressDialog();
        super.onNext(entity);
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        super.onError(e);
    }

    protected void showProgressDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgressDialog();
    }
}
