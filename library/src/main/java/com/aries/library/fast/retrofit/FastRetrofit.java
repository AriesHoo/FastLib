package com.aries.library.fast.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.aries.library.fast.util.SSLUtil;

import java.io.InputStream;
import java.util.HashMap;
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
 * E-Mail: AriesHoo@126.com
 * Function:retrofit封装
 * Description:
 * 1、2017-11-16 12:48:31 AriesHoo 修改读、写、链接超时时间均为readTimeout的BUG会造成超时设置无效BUG
 * 2、修改初始化FastMultiUrl类的位置到createService以免超时控制不到问题
 * 3、2018-6-30 21:32:36 调整设置全局请求头方式并新增设置单个请求头方法
 * 4、2018-6-30 21:32:47 修复设置全局请求头不生效BUG
 */
public class FastRetrofit {

    private static volatile FastRetrofit sManager;
    private static volatile Retrofit sRetrofit;
    private static volatile Retrofit.Builder sRetrofitBuilder;
    private static OkHttpClient.Builder sClientBuilder;
    private static OkHttpClient sClient;
    private long mDelayTime = 20;
    private HttpLoggingInterceptor mLoggingInterceptor;
    private Map<String, Object> mHeaderMap = new HashMap<>();
    private Interceptor mHeaderInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) {
            Request.Builder request = chain.request().newBuilder();
            if (mHeaderMap != null && mHeaderMap.size() > 0) {
                for (Map.Entry<String, Object> entry : mHeaderMap.entrySet()) {
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
    };

    private FastRetrofit() {
        sClientBuilder = new OkHttpClient.Builder();
        sClientBuilder.addInterceptor(mHeaderInterceptor);
        sRetrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        setTimeout(mDelayTime);
        FastMultiUrl.getInstance().with(sClientBuilder);
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

    /**
     * 对外暴露 OkHttpClient,方便自定义
     *
     * @return
     */
    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        return getInstance().sClientBuilder;
    }

    public static OkHttpClient getOkHttpClient() {
        return getOkHttpClientBuilder().build();
    }

    /**
     * 对外暴露 Retrofit,方便自定义
     *
     * @return
     */
    public static Retrofit.Builder getRetrofitBuilder() {
        sRetrofitBuilder.client(getOkHttpClient());
        return getInstance().sRetrofitBuilder;
    }

    public static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            sRetrofit = getRetrofitBuilder().build();
        }
        return sRetrofit;
    }

    public <T> T createService(Class<T> apiService) {
        return getRetrofit().create(apiService);
    }

    public FastRetrofit addHeader(String key, Object value) {
        if (!TextUtils.isEmpty(key) && value != null) {
            mHeaderMap.put(key, value);
        }
        return this;
    }

    /**
     * 添加统一的请求头
     *
     * @param map
     * @return
     */
    public FastRetrofit addHeader(final Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            mHeaderMap.putAll(map);
        }
        return this;
    }

    /**
     * 设置全局BaseUrl
     *
     * @param baseUrl
     * @return
     */
    public FastRetrofit setBaseUrl(String baseUrl) {
        sRetrofitBuilder.baseUrl(baseUrl);
        FastMultiUrl.getInstance().setGlobalBaseUrl(baseUrl);
        return this;
    }

    /**
     * 添加统一的请求头
     *
     * @param map
     * @return
     */
    @Deprecated
    public FastRetrofit setHeaders(final Map<String, Object> map) {
        return addHeader(map);
    }

    /**
     * 设置自定义OkHttpClient--2.1.9-beta版本后废弃可调用
     * 可以调用以下方法实现自定义
     * {@link #getOkHttpClient()}{@link #getOkHttpClientBuilder()}
     * {@link #getRetrofitBuilder()}{@link #getRetrofit()}
     *
     * @param okClient
     * @return
     */
    @Deprecated
    public FastRetrofit setHttpClient(OkHttpClient okClient) {
        if (okClient != null && sClient != okClient) {
            sClient = okClient;
            sClientBuilder = okClient.newBuilder();
            sClientBuilder.retryOnConnectionFailure(true);
            sRetrofitBuilder.client(okClient);
            sClientBuilder.addInterceptor(mHeaderInterceptor);
        }
        return this;
    }

    public FastRetrofit setTimeout(long second) {
        mDelayTime = second;
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
        sClientBuilder.writeTimeout(second, unit);
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
        sClientBuilder.connectTimeout(second, unit);
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
        if (sClientBuilder.interceptors().contains(mLoggingInterceptor)) {
            sClientBuilder.interceptors().remove(mLoggingInterceptor);
        }
        if (enable) {
            if (mLoggingInterceptor == null) {
                final String finalTag = tag;
                mLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d(finalTag, message);
                    }
                });
            }
            mLoggingInterceptor.setLevel(level);
            sClientBuilder.addInterceptor(mLoggingInterceptor);
        } else {
            if (mLoggingInterceptor != null) {
                mLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            }
        }
        return this;
    }

    /**
     * 信任所有证书,不安全有风险
     * 使用 {@link FastRetrofit#setCertificates()}替换
     *
     * @return
     */
    @Deprecated
    public FastRetrofit trustAllSSL() {
        return setCertificates();
    }

    /**
     * 默认信任所有证书-不安全
     *
     * @return
     */
    public FastRetrofit setCertificates() {
        SSLUtil.SSLParams sslParams = SSLUtil.getSslSocketFactory();
        sClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    /**
     * @param trustManager 使用自己设置的X509TrustManager
     * @return
     */
    public FastRetrofit setCertificates(X509TrustManager trustManager) {
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
    public FastRetrofit setCertificates(InputStream... certificates) {
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
    public FastRetrofit setCertificates(InputStream bksFile, String password, InputStream... certificates) {
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
    public FastRetrofit setCertificates(InputStream bksFile, String password, X509TrustManager trustManager) {
        SSLUtil.SSLParams sslParams = SSLUtil.getSslSocketFactory(bksFile, password, trustManager);
        sClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }
}
