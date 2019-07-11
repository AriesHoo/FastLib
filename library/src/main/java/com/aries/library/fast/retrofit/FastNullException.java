package com.aries.library.fast.retrofit;

/**
 * @Author: AriesHoo on 2019/7/11 21:55
 * @E-Mail: AriesHoo@126.com
 * @Function:  特定空对象Exception 用于解决接口某些情况下数据null无法回调{@link FastObserver#_onNext(Object)}的情况
 * @Description:
 */
public class FastNullException extends Exception {

    public FastNullException(String message) {
        super(message);
    }

    public FastNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public FastNullException(Throwable cause) {
        super(cause);
    }
}
