package com.aries.library.fast.retrofit;


import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: AriesHoo on 2018/7/23 14:24
 * @E-Mail: AriesHoo@126.com
 * Function: 控制操作线程的辅助类
 * Description:
 */
public class FastTransformer {

    /**
     * 线程切换
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> switchSchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return switchSchedulers(upstream);
            }
        };
    }

    /**
     * 线程切换
     *
     * @param observable
     * @param <T>
     * @return
     */
    public static <T> Observable<T> switchSchedulers(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
