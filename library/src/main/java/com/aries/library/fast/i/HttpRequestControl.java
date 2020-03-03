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
     * @param httpRequestControl 调用页面相关参数
     * @param list               数据列表
     * @param listener           设置完成回调--用于特殊需求页面做后续操作
     */
    void httpRequestSuccess(IHttpRequestControl httpRequestControl, List<?> list, OnHttpRequestListener listener);

    /**
     * 网络成功回调
     *
     * @param httpRequestControl 调用页面相关参数
     * @param list               数据列表
     */
    default void httpRequestSuccess(IHttpRequestControl httpRequestControl, List<?> list) {
        httpRequestSuccess(httpRequestControl, list, null);
    }

    /**
     * 网络成功后执行
     *
     * @param httpRequestControl 调用页面相关参数
     * @param e                  错误
     */
    void httpRequestError(IHttpRequestControl httpRequestControl, @NonNull Throwable e);
}
