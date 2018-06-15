package com.aries.library.fast.i;

/**
 * Created: AriesHoo on 2018/6/15 16:15
 * E-Mail: AriesHoo@126.com
 * Function:http请求成功后处理结果回调
 * Description:
 */
public interface OnHttpRequestListener {

    void onEmpty();

    void onNoMore();

    void onNext();
}
