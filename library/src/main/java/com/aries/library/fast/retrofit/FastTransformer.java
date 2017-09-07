package com.aries.library.fast.retrofit;


import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created: AriesHoo on 2017/8/23 16:33
 * Function:控制操作线程的辅助类
 * Desc:
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
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
