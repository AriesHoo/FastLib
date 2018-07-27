package com.aries.library.fast.retrofit;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.i.IHttpRequestControl;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DefaultObserver;

/**
 * @Author: AriesHoo on 2018/7/23 14:21
 * @E-Mail: AriesHoo@126.com
 * Function: Retrofit快速观察者-观察者基类用于错误全局设置
 * Description:
 * 1、2017-11-16 11:35:12 AriesHoo增加返回错误码全局控制
 * 2、2018-6-20 15:15:45 重构
 * 3、2018-7-9 14:27:03 删除IHttpRequestControl判断避免http错误时无法全局控制BUG
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
        if (FastManager.getInstance().getHttpRequestControl() != null) {
            FastManager.getInstance().getHttpRequestControl().httpRequestError(mHttpRequestControl, e);
        }
        _onError(e);
    }

    @Override
    public void onNext(T entity) {
        _onNext(entity);
    }

    /**
     * 获取成功后数据展示
     *
     * @param entity
     */
    public abstract void _onNext(@NonNull T entity);

    public void _onError(@NonNull Throwable e) {
    }
}
