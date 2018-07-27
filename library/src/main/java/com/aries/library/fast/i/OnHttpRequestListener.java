package com.aries.library.fast.i;

import java.util.List;

/**
 * @Author: AriesHoo on 2018/7/23 10:39
 * @E-Mail: AriesHoo@126.com
 * Function: http请求成功后处理结果回调{@link HttpRequestControl#httpRequestSuccess(IHttpRequestControl, List, OnHttpRequestListener)}
 * Description:
 */
public interface OnHttpRequestListener {

    /**
     * 无数据回调
     */
    void onEmpty();

    /**
     * 无更多数据回调
     */
    void onNoMore();

    /**
     * 加载数据回调
     */
    void onNext();
}
