package com.aries.library.fast.retrofit;


import com.aries.library.fast.widget.FastLoadDialog;

/**
 * Created: AriesHoo on 2017/8/24 16:09
 * Function: 快速创建支持Loading的Retrofit观察者
 * Desc:
 */
public abstract class FastLoadingObserver<T> extends FastObserver<T> {


    private FastLoadDialog dialog;

    public FastLoadingObserver(FastLoadDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
        super.onComplete();
    }


    protected void showProgressDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgressDialog();
    }
}
