package com.aries.library.fast.i;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * @Author: AriesHoo on 2018/7/16 9:16
 * @E-Mail: AriesHoo@126.com
 * Function: 网络请求相关(可全局控制网络成功及失败展示逻辑-需继承{@link com.aries.library.fast.retrofit.FastObserver}或自己实现类似逻辑)
 * Description:
 */
public interface HttpRequestControl {

    /**
     * 网络成功回调
     *
     * @param httpRequestControl
     * @param list
     * @param listener
     * @return
     */
    void httpRequestSuccess(IHttpRequestControl httpRequestControl, List<? extends Object> list, OnHttpRequestListener listener);

    /**
     * 网络成功后执行
     *
     * @param httpRequestControl
     * @param e
     */
    void httpRequestError(IHttpRequestControl httpRequestControl, @NonNull Throwable e);
}
