package com.aries.library.fast.retrofit;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.i.IHttpRequestControl;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DefaultObserver;

/**
 * Created: AriesHoo on 2017/8/24 9:56
 * E-Mail: AriesHoo@126.com
 * Function: Retrofit快速观察者
 * Description:
 * 1、2017-11-16 11:35:12 AriesHoo增加返回错误码全局控制
 * 2、2018-6-20 15:15:45 重构
 */
public abstract class FastObserver<T> extends DefaultObserver<T> {

    private IHttpRequestControl mHttpRequestControl;

    public FastObserver() {
        this(null);
    }

    public FastObserver(IHttpRequestControl httpRequestControl) {
        this.mHttpRequestControl = httpRequestControl;
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        if (FastManager.getInstance().getHttpRequestControl() != null && mHttpRequestControl != null) {
            FastManager.getInstance().getHttpRequestControl().httpRequestError(mHttpRequestControl, e);
        }
        _onError(e);
    }

    @Override
    public void onNext(T entity) {
        _onNext(entity);
    }

    public abstract void _onNext(@NonNull T entity);

    public void _onError(@NonNull Throwable e) {
    }
}
