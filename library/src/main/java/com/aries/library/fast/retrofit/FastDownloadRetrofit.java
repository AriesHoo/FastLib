package com.aries.library.fast.retrofit;

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
 * Created: AriesHoo on 2018/7/11 10:24
 * E-Mail: AriesHoo@126.com
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
            if (!in.toString().contains("HttpLoggingInterceptor"))
                okHttpBuilder.addInterceptor(in);
        }
        //将原属性配置至新OkHttpClient
        OkHttpClient client = okHttpBuilder
                .retryOnConnectionFailure(okHttpClient.retryOnConnectionFailure())
                .connectTimeout(okHttpClient.connectTimeoutMillis(), TimeUnit.MILLISECONDS)
                .readTimeout(okHttpClient.readTimeoutMillis(), TimeUnit.MILLISECONDS)
                .writeTimeout(okHttpClient.writeTimeoutMillis(), TimeUnit.MILLISECONDS)
                .sslSocketFactory(okHttpClient.sslSocketFactory())
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
     * @param fileUrl
     * @param header
     * @return
     */
    public Observable<ResponseBody> downloadFile(String fileUrl, Map<String, Object> header) {
        return mRetrofit
                .create(FastRetrofitService.class)
                .downloadFile(fileUrl, header == null ? new HashMap<String, Object>() : header)
                .observeOn(Schedulers.computation())
                .subscribeOn(Schedulers.io());
    }
}
