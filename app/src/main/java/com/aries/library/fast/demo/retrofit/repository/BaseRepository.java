package com.aries.library.fast.demo.retrofit.repository;

import android.accounts.NetworkErrorException;

import com.aries.library.fast.demo.base.BaseEntity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created: AriesHoo on 2017/6/23 17:23
 * Function: retrofit使用基类封装
 * Desc:
 */
public abstract class BaseRepository {

    protected <T> Observable<T> transform(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    protected <T> Observable<T> transformData(Observable<BaseEntity<T>> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseEntity<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseEntity<T> result) {
                        if (result == null) {
                            return Observable.error(new NetworkErrorException());
                        } else {
                            return Observable.just(result.data);
                        }
                    }
                });
    }
}
