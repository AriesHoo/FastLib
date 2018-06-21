package com.aries.library.fast.i;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created: AriesHoo on 2018/6/15 16:04
 * E-Mail: AriesHoo@126.com
 * Function:网络请求相关(可全局控制网络成功及失败展示逻辑)
 * Description:
 */
public interface HttpRequestControl {

    /**
     * @param httpRequestControl
     * @param list
     * @param listener
     * @return
     */
    void httpRequestSuccess(IHttpRequestControl httpRequestControl, List<? extends Object> list, OnHttpRequestListener listener);

    /**
     * @param httpRequestControl
     * @param e
     * @return 将处理抛出全局前是否预处理 --true预处理;false不处理
     */
    boolean httpRequestError(IHttpRequestControl httpRequestControl, @NonNull Throwable e);
}
