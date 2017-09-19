package com.aries.library.fast.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.aries.library.fast.util.SSLUtil;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created: AriesHoo on 2017/8/18 16:56
 * Function: retrofit封装
 * Desc:
 */

public class FastRetrofit {

    private static volatile FastRetrofit sManager;
    private static volatile Retrofit sRetrofit;
    private static volatile Retrofit.Builder sRetrofitBuilder;
    private static OkHttpClient.Builder sClientBuilder;
    private static OkHttpClient sClient;
    private long delayTime = 10;
    private HttpLoggingInterceptor loggingInterceptor;

    private FastRetrofit() {
        sClientBuilder = new OkHttpClient.Builder();
        setTimeout(delayTime);
        sRetrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    public static FastRetrofit getInstance() {
        if (sManager == null) {
            synchronized (FastRetrofit.class) {
                if (sManager == null) {
                    sManager = new FastRetrofit();
                }
            }
        }
        return sManager;
    }

    public <T> T createService(Class<T> apiService) {
        setHttpClient(sClientBuilder.build());
        if (sRetrofit == null) {
            sRetrofit = sRetrofitBuilder.build();
        }
        return sRetrofit.create(apiService);
    }

    /**
     * 设置全局BaseUrl
     *
     * @param baseUrl
     * @return
     */
    public FastRetrofit setBaseUrl(String baseUrl) {
        sRetrofitBuilder.baseUrl(baseUrl);
        return this;
    }

    /**
     * 设置自定义OkHttpClient
     *
     * @param okClient
     * @return
     */
    public FastRetrofit setHttpClient(OkHttpClient okClient) {
        if (okClient != null && sClient != okClient) {
            sClient = okClient;
            sClientBuilder = okClient.newBuilder();
            sClientBuilder.retryOnConnectionFailure(true);
            sRetrofitBuilder.client(okClient);
        }
        return this;
    }

    /**
     * 添加统一的请求头
     *
     * @param headerMaps
     * @return
     */
    public FastRetrofit setHeaders(final Map<String, Object> headerMaps) {
        sClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) {
                Request.Builder request = chain.request().newBuilder();
                if (headerMaps != null && headerMaps.size() > 0) {
                    for (Map.Entry<String, Object> entry : headerMaps.entrySet()) {
                        request.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                }
                try {
                    return chain.proceed(request.build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        return this;
    }

    public FastRetrofit setTimeout(long second) {
        delayTime = second;
        return setTimeout(second, TimeUnit.SECONDS);
    }

    /**
     * 设置所有超时
     *
     * @param second
     * @param unit
     * @return
     */
    public FastRetrofit setTimeout(long second, TimeUnit unit) {
        setReadTimeout(second, unit);
        setWriteTimeout(second, unit);
        setConnectTimeout(second, unit);
        return this;
    }


    public FastRetrofit setReadTimeout(long second) {
        return setReadTimeout(second, TimeUnit.SECONDS);
    }

    /**
     * 设置读取超时时间
     *
     * @param second
     * @param unit   单位
     * @return
     */
    public FastRetrofit setReadTimeout(long second, TimeUnit unit) {
        sClientBuilder.readTimeout(second, unit);
        return this;
    }

    public FastRetrofit setWriteTimeout(long second) {
        return setWriteTimeout(second, TimeUnit.SECONDS);
    }

    /**
     * 设置写入超时时间
     *
     * @param second
     * @param unit
     * @return
     */
    public FastRetrofit setWriteTimeout(long second, TimeUnit unit) {
        sClientBuilder.readTimeout(second, unit);
        return this;
    }

    public FastRetrofit setConnectTimeout(long second) {
        return setConnectTimeout(second, TimeUnit.SECONDS);
    }

    /**
     * 设置连接超时时间
     *
     * @param second
     * @param unit
     * @return
     */
    public FastRetrofit setConnectTimeout(long second, TimeUnit unit) {
        sClientBuilder.readTimeout(second, unit);
        return this;
    }

    public FastRetrofit setLogEnable(boolean enable) {
        return setLogEnable(enable, null, HttpLoggingInterceptor.Level.BODY);
    }

    /**
     * 设置是否打印日志
     *
     * @param enable
     * @param tag
     * @return
     */
    public FastRetrofit setLogEnable(boolean enable, String tag, HttpLoggingInterceptor.Level level) {
        if (TextUtils.isEmpty(tag)) {
            tag = getClass().getSimpleName();
        }
        if (sClientBuilder.interceptors().contains(loggingInterceptor)) {
            sClientBuilder.interceptors().remove(loggingInterceptor);
        }
        if (enable) {
            if (loggingInterceptor == null) {
                final String finalTag = tag;
                loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d(finalTag, message);
                    }
                });
            }
            loggingInterceptor.setLevel(level);
            sClientBuilder.addInterceptor(loggingInterceptor);
        }
        return this;
    }

    /**
     * 信任所有证书,不安全有风险
     *
     * @return
     */
    public FastRetrofit trustAllSSL() {
        SSLUtil.SSLParams sslParams = SSLUtil.getSslSocketFactory();
        sClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    /**
     * @param trustManager 使用自己设置的X509TrustManager
     * @return
     */
    public FastRetrofit useSingleSignedSSL(X509TrustManager trustManager) {
        SSLUtil.SSLParams sslParams = SSLUtil.getSslSocketFactory(trustManager);
        sClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    /**
     * 使用预埋证书,校验服务端证书(自签名证书)-单向认证
     *
     * @param certificates 用含有服务端公钥的证书校验服务端证书
     * @return
     */
    public FastRetrofit useSingleSignedSSL(InputStream... certificates) {
        SSLUtil.SSLParams sslParams = SSLUtil.getSslSocketFactory(certificates);
        sClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    /**
     * 使用bks证书和密码管理客户端证书(双向认证),使用预埋证书,校验服务端证书(自签名证书)
     * 客户端使用bks证书校验服务端证书;
     *
     * @param bksFile
     * @param password
     * @param certificates 用含有服务端公钥的证书校验服务端证书
     * @return
     */
    public FastRetrofit useBothSignedSSL(InputStream bksFile, String password, InputStream... certificates) {
        SSLUtil.SSLParams sslParams = SSLUtil.getSslSocketFactory(bksFile, password, certificates);
        sClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    /**
     * 客户端使用bks证书校验服务端证书
     *
     * @param bksFile
     * @param password
     * @param trustManager 如果需要自己校验,那么可以自己实现相关校验;如果不需要自己校验,那么传null即可
     * @return
     */
    public FastRetrofit useBothSignedSSL(InputStream bksFile, String password, X509TrustManager trustManager) {
        SSLUtil.SSLParams sslParams = SSLUtil.getSslSocketFactory(bksFile, password, trustManager);
        sClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }
}
