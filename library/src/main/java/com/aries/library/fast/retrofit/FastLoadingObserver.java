package com.aries.library.fast.retrofit;


import android.app.Activity;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.i.IHttpRequestControl;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.widget.FastLoadDialog;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * @Author: AriesHoo on 2018/7/23 14:08
 * @E-Mail: AriesHoo@126.com
 * Function: 快速创建支持Loading的Retrofit观察者
 * Description:
 * 1、2017-11-16 13:38:16 AriesHoo增加多种构造用于实现父类全局设置网络请求错误码
 */
public abstract class FastLoadingObserver<T> extends FastObserver<T> {

    /**
     * Dialog
     */
    private FastLoadDialog mDialog;

    /**
     * 用于全局配置
     *
     * @param activity
     */
    public FastLoadingObserver(@Nullable Activity activity, IHttpRequestControl httpRequestControl, @StringRes int resId) {
        this(FastManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(resId), httpRequestControl);
    }

    public FastLoadingObserver(IHttpRequestControl httpRequestControl, @StringRes int resId) {
        this(FastStackUtil.getInstance().getCurrent(), httpRequestControl, resId);
    }

    public FastLoadingObserver(@Nullable Activity activity, IHttpRequestControl httpRequestControl, CharSequence msg) {
        this(FastManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(msg), httpRequestControl);
    }

    public FastLoadingObserver(IHttpRequestControl httpRequestControl, CharSequence msg) {
        this(FastStackUtil.getInstance().getCurrent(), httpRequestControl, msg);
    }

    public FastLoadingObserver(@Nullable Activity activity, @StringRes int resId) {
        this(FastManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(resId));
    }

    public FastLoadingObserver(@StringRes int resId) {
        this(FastStackUtil.getInstance().getCurrent(), resId);
    }

    public FastLoadingObserver(@Nullable Activity activity, CharSequence msg) {
        this(FastManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(msg));
    }

    public FastLoadingObserver(CharSequence msg) {
        this(FastStackUtil.getInstance().getCurrent(), msg);
    }

    public FastLoadingObserver(@Nullable Activity activity, IHttpRequestControl httpRequestControl) {
        this(FastManager.getInstance().getLoadingDialog().createLoadingDialog(activity), httpRequestControl);
    }

    public FastLoadingObserver(IHttpRequestControl httpRequestControl) {
        this(FastStackUtil.getInstance().getCurrent(), httpRequestControl);
    }

    public FastLoadingObserver(@Nullable Activity activity) {
        this(FastManager.getInstance().getLoadingDialog().createLoadingDialog(activity));
    }

    public FastLoadingObserver() {
        this(FastStackUtil.getInstance().getCurrent());
    }

    public FastLoadingObserver(FastLoadDialog dialog) {
        this(dialog, null);
    }

    public FastLoadingObserver(FastLoadDialog dialog, IHttpRequestControl httpRequestControl) {
        super(httpRequestControl);
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
