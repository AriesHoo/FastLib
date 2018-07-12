package com.aries.library.fast.retrofit;

/**
 * Created: AriesHoo on 2018/7/11 17:33
 * E-Mail: AriesHoo@126.com
 * Function: 上传进度监听
 * Description:
 */
public interface FastUploadRequestListener {

    void onProgress(float progress, long current, long total);

    void onFail(Throwable e);
}
