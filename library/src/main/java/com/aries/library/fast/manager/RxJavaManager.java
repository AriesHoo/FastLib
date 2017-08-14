package com.aries.library.fast.manager;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public <T> Observable<T> getDelayObservable(T value, long delay) {
        return getDelayObservable(value, delay, TimeUnit.SECONDS);
    }

    public void setTimer(long delayTime, final TimerListener listener) {
        getDelayObservable("", delayTime, TimeUnit.MILLISECONDS).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                if (listener != null) {
                    listener.timeEnd();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        });
    }


}
