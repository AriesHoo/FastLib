package com.aries.library.fast.retrofit;


import android.app.Activity;
import android.support.annotation.Nullable;

import com.aries.library.fast.FastConfig;
import com.aries.library.fast.widget.FastLoadDialog;

/**
 * Created: AriesHoo on 2017/8/24 16:09
 * E-Mail: AriesHoo@126.com
 * Function: 快速创建支持Loading的Retrofit观察者
 * Description:
 * 1、2017-11-16 13:38:16 AriesHoo增加多种构造用于实现父类全局设置网络请求错误码
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

    public FastLoadingObserver(@Nullable Activity activity, Object... args) {
        this(FastConfig.getInstance(activity).getLoadingDialog().createLoadingDialog(activity), args);
    }

    public FastLoadingObserver(FastLoadDialog dialog, Object... args) {
        super(dialog.getDialog() != null ? dialog.getDialog().getContext() : null, args);
        this.mDialog = dialog;
    }

    public FastLoadingObserver(FastLoadDialog dialog) {
        super(dialog.getDialog() != null ? dialog.getDialog().getContext() : null);
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
