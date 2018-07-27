package com.aries.library.fast.manager;

import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.retrofit.FastTransformer;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * @Author: AriesHoo on 2018/7/23 11:02
 * @E-Mail: AriesHoo@126.com
 * Function: RxJava使用管理类
 * Description:
 * 1、2018-7-23 11:10:35 去掉设置定时器返回值并标记为废弃使用另一个{@link #setTimer(long)}
 */
public class RxJavaManager {

    public interface TimerListener {
        /**
         * 倒计时结束回调
         */
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

    /**
     * 创建Observable
     *
     * @param value
     * @param delay
     * @param unit
     * @param <T>
     * @return
     */
    public <T> Observable<T> getDelayObservable(T value, long delay, TimeUnit unit) {
        return Observable.just(value)
                .delay(delay, unit)
                .compose(FastTransformer.<T>switchSchedulers());
    }

    /**
     * 创建 时延单位秒的Observable
     *
     * @param value
     * @param delay
     * @param <T>
     * @return
     */
    public <T> Observable<T> getDelayObservable(T value, long delay) {
        return getDelayObservable(value, delay, TimeUnit.SECONDS);
    }

    /**
     * 设置毫秒定时器去掉返回值
     *
     * @param delayTime
     * @param listener
     */
    @Deprecated
    public void setTimer(long delayTime, final TimerListener listener) {
        setTimer(delayTime)
                .subscribe(new FastObserver<String>() {
                    @Override
                    public void _onNext(String entity) {

                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (listener != null) {
                            listener.timeEnd();
                        }
                    }
                });
    }

    /**
     * 设置时延为毫秒的定时器
     *
     * @param delayTime
     * @return
     */
    public Observable<String> setTimer(long delayTime) {
        return getDelayObservable("", delayTime, TimeUnit.MILLISECONDS);
    }

}
