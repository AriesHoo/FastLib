package com.aries.library.fast.i;

import com.aries.library.fast.retrofit.FastNullException;
import com.aries.library.fast.retrofit.FastObserver;

/**
 * @Author: AriesHoo on 2019/7/12 10:25
 * @E-Mail: AriesHoo@126.com
 * @Function: {@link FastObserver}错误信息拦截并做其它操作处理配合{@link FastNullException}以处理解决接口某些情况下无法回调成功问题
 * @Description:
 */
public interface FastObserverControl {

    /**
     * @param o {@link FastObserver} 对象用于后续事件逻辑
     * @param e 原始错误
     * @return true 拦截操作不进行原始{@link FastObserver#onError(Throwable)}后续逻辑
     * false 不拦截继续后续逻辑
     */
    boolean onError(FastObserver o, Throwable e);
}
