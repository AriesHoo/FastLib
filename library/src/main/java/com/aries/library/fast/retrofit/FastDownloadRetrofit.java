package com.aries.library.fast.retrofit;

import com.aries.library.fast.util.SSLUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * @Author: AriesHoo on 2018/7/23 13:46
 * @E-Mail: AriesHoo@126.com
 * Function: 快速下载功能-单独retrofit使用
 * Description:
 */
class FastDownloadRetrofit {

    public Retrofit mRetrofit;
    private static volatile FastDownloadRetrofit sInstance;

    private FastDownloadRetrofit() {
        //获取当前OkHttpClient
        OkHttpClient okHttpClient = FastRetrofit.getOkHttpClient();
        //创建新的OkHttpClient
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        for (Interceptor in : okHttpClient.interceptors()) {
            //貌似日志打印的拦截器会影响下载进度监控
            if (!in.toString().contains("HttpLoggingInterceptor")) {
                okHttpBuilder.addInterceptor(in);
            }
        }
        SSLUtil.SSLParams params = FastRetrofit.getInstance().getCertificates();
        if (params != null) {
            okHttpBuilder.sslSocketFactory(params.sslSocketFactory, params.trustManager);
        } else {
            okHttpBuilder.sslSocketFactory(okHttpClient.sslSocketFactory());
        }
        //将原属性配置至新OkHttpClient
        OkHttpClient client = okHttpBuilder
                .retryOnConnectionFailure(okHttpClient.retryOnConnectionFailure())
                .connectTimeout(okHttpClient.connectTimeoutMillis(), TimeUnit.MILLISECONDS)
                .readTimeout(okHttpClient.readTimeoutMillis(), TimeUnit.MILLISECONDS)
                .writeTimeout(okHttpClient.writeTimeoutMillis(), TimeUnit.MILLISECONDS)
                .build();
        mRetrofit = FastRetrofit.getRetrofit().newBuilder()
                .client(client)
                .build();
    }

    public static FastDownloadRetrofit getInstance() {
        if (sInstance == null) {
            synchronized (FastDownloadRetrofit.class) {
                if (sInstance == null) {
                    sInstance = new FastDownloadRetrofit();
                }
            }
        }
        return sInstance;
    }

    /**
     * 注意线程转换
     *
     * @param fileUrl 下载路径(可以为全路径)
     * @param header  额外头信息
     * @return 返回数据流
     */
    public Observable<ResponseBody> downloadFile(String fileUrl, Map<String, Object> header) {
        return mRetrofit
                .create(FastRetrofitService.class)
                .downloadFile(fileUrl, header == null ? new HashMap<String, Object>(0) : header)
                .observeOn(Schedulers.computation())
                .subscribeOn(Schedulers.io());
    }
}
