package com.aries.library.fast.demo.retrofit.repository;

import android.accounts.NetworkErrorException;

import com.aries.library.fast.demo.base.BaseEntity;
import com.aries.library.fast.retrofit.FastTransformer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created: AriesHoo on 2017/6/23 17:23
 * Function: retrofit使用基类封装.
 * Desc:
 */
public abstract class BaseRepository {

    /**
     * @param observable 用于解析 统一返回实体统一做相应的错误码--如登录失效
     * @param <T>
     * @return
     */
    protected <T> Observable<T> transform(Observable<BaseEntity<T>> observable) {
        return FastTransformer.switchSchedulers(observable)
                .flatMap(new Function<BaseEntity<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull BaseEntity<T> result) throws Exception {
                        if (result == null) {
                            return Observable.error(new NetworkErrorException());
                        } else {
                            return Observable.just(result.data);
                        }
                    }
                });
    }
}
