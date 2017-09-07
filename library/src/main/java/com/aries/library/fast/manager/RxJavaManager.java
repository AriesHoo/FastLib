package com.aries.library.fast.manager;

import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.retrofit.FastTransformer;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;


/**
 * Created: AriesHoo on 2017/7/24 9:10
 * Function: RxJava使用管理类
 * Desc:
 */

public class RxJavaManager {

    public interface TimerListener {
        void timeEnd();
    }

    private static volatile RxJavaManager instance;

    private RxJavaManager() {
    }

    public static RxJavaManager getInstance() {
        if (instance == null) {
            synchronized (RxJavaManager.class) {
                if (instance == null) {
                    instance = new RxJavaManager();
                }
            }
        }
        return instance;
    }

    public <T> Observable<T> getDelayObservable(T value, long delay, TimeUnit unit) {
        return Observable.just(value)
                .delay(delay, unit)
                .compose(FastTransformer.<T>switchSchedulers());
    }

    public <T> Observable<T> getDelayObservable(T value, long delay) {
        return getDelayObservable(value, delay, TimeUnit.SECONDS);
    }

    public void setTimer(long delayTime, final TimerListener listener) {
        getDelayObservable("", delayTime, TimeUnit.MILLISECONDS).subscribe(new FastObserver<String>() {
            @Override
            public void _onNext(String entity) {

            }

            @Override
            public void _onError(int errorRes, int errorCode, Throwable e) {

            }

            @Override
            public void onComplete() {
                if (listener != null) {
                    listener.timeEnd();
                }
            }
        });
    }


}
