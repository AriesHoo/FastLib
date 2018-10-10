package com.aries.library.fast.retrofit;


import com.aries.library.fast.manager.LoggerManager;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @Author: AriesHoo on 2018/10/10 15:42
 * @E-Mail: AriesHoo@126.com
 * @Function: RxJava 重试机制--retryWhen操作符{@link Observable#retryWhen(Function)}
 * @Description:
 */
public class FastRetryWhen implements Function<Observable<? extends Throwable>, ObservableSource<?>> {
    /**
     * 最大尝试次数--不包含原始请求次数
     */
    private final int mRetryMaxTime;
    /**
     * 尝试时间间隔ms
     */
    private final long mRetryDelay;
    /**
     * 记录已尝试次数
     */
    private int mRetryCount;
    private String TAG = getClass().getSimpleName();

    public FastRetryWhen(int retryMaxTime, long retryDelay) {
        this.mRetryMaxTime = retryMaxTime;
        this.mRetryDelay = retryDelay;
    }

    public FastRetryWhen() {
        this(3, 500);
    }

    /**
     * Applies this function to the given argument.
     *
     * @param observable the function argument
     * @return the function result
     */
    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) {
        return observable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Throwable throwable) {
                //仅仅对连接失败相关错误进行重试
                if (throwable instanceof ConnectException
                        || throwable instanceof UnknownHostException
                        || throwable instanceof SocketTimeoutException
                        || throwable instanceof SocketException
                        || throwable instanceof TimeoutException) {
                    if (++mRetryCount <= mRetryMaxTime) {
                        LoggerManager.e(TAG, "网络请求错误,将在 " + mRetryDelay + " ms后进行重试, 重试次数 " + mRetryCount + ";throwable:" + throwable);
                        return Observable.timer(mRetryDelay, TimeUnit.MILLISECONDS);
                    }
                }
                return Observable.error(throwable);
            }
        });
    }
}
