package com.aries.library.fast.retrofit;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.util.SSLUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author: AriesHoo on 2018/7/24 13:10
 * @E-Mail: AriesHoo@126.com
 * Function: Retrofit封装
 * Description:
 * 1、2017-11-16 12:48:31 AriesHoo 修改读、写、链接超时时间均为readTimeout的BUG会造成超时设置无效BUG
 * 2、修改初始化FastMultiUrl类的位置到createService以免超时控制不到问题
 * 3、2018-6-30 21:32:36 调整设置全局请求头方式并新增设置单个请求头方法
 * 4、2018-6-30 21:32:47 修复设置全局请求头不生效BUG
 * 5、2018-7-2 16:49:52 将控制多请求头功能由{@link FastMultiUrl}移植至此方便统一管理
 * 6、2018-7-3 12:22:55 新增Service 缓存机制-可控制是否使用缓存{@link #createService(Class, boolean)}
 * 7、2018-7-3 13:20:58 新增多BaseUrl设置方式header模式{@link #putHeaderBaseUrl(String, String) {@link #putHeaderBaseUrl(Map)}}
 * 及method模式{@link #putBaseUrl(String, String) {@link #putHeaderBaseUrl(Map)}}
 * 8、2018-7-3 16:06:56 新增下载文件功能{@link #downloadFile(String)}
 * 9、2018-7-11 08:59:00 修改{@link #removeHeader(String)}key判断错误问题
 * 10、2018-7-24 13:10:49 新增默认header User-Agent -避免某些服务器配置攻击,请求返回403 forbidden 问题
 */
public class FastRetrofit {

    public static final String BASE_URL_NAME_HEADER = FastMultiUrl.BASE_URL_NAME_HEADER;
    private static volatile FastRetrofit sManager;
    private static volatile Retrofit sRetrofit;
    private static volatile Retrofit.Builder sRetrofitBuilder;
    private static OkHttpClient.Builder sClientBuilder;
    private static OkHttpClient sClient;
    private long mDelayTime = 20;
    private Map<String, Object> mServiceMap = new HashMap<>();

    /**
     * 正式配置
     */
    private SSLUtil.SSLParams mSslParams = new SSLUtil.SSLParams();
    /**
     * 日志拦截器
     */
    private HttpLoggingInterceptor mLoggingInterceptor;
    /**
     * 统一header
     */
    private Map<String, Object> mHeaderMap = new HashMap<>();
    /**
     * header拦截器
     */
    private Interceptor mHeaderInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder request = chain.request().newBuilder();
            //避免某些服务器配置攻击,请求返回403 forbidden 问题
            addHeader("User-Agent", "Mozilla/5.0 (Android)");
            if (mHeaderMap.size() > 0) {
                for (Map.Entry<String, Object> entry : mHeaderMap.entrySet()) {
                    request.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            return chain.proceed(request.build());
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
        return sClientBuilder;
    }

    public static OkHttpClient getOkHttpClient() {
        if (sClient == null) {
            sClient = getOkHttpClientBuilder().build();
        }
        return sClient;
    }

    /**
     * 对外暴露 Retrofit,方便自定义
     *
     * @return
     */
    public static Retrofit.Builder getRetrofitBuilder() {
        sRetrofitBuilder.client(getOkHttpClient());
        return sRetrofitBuilder;
    }

    public static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            sRetrofit = getRetrofitBuilder().build();
        }
        return sRetrofit;
    }

    public <T> T createService(Class<T> apiService) {
        return createService(apiService, true);
    }

    /**
     * 创建Service
     *
     * @param apiService
     * @param useCacheEnable --是否使用缓存Service
     * @param <T>
     * @return
     */
    public <T> T createService(Class<T> apiService, boolean useCacheEnable) {
        if (useCacheEnable && apiService != null) {
            if (mServiceMap.containsKey(apiService.getName())) {
                LoggerManager.i("className:" + apiService.getName() + ";service取自缓存");
                return (T) mServiceMap.get(apiService.getName());
            }
            T tClass = getRetrofit().create(apiService);
            mServiceMap.put(apiService.getName(), tClass);
            return tClass;
        }
        return getRetrofit().create(apiService);
    }

    /**
     * @param fileUrl 下载全路径 配合{@link FastDownloadObserver}实现文件下载进度监听
     * @return
     */
    public Observable<ResponseBody> downloadFile(String fileUrl) {
        return downloadFile(fileUrl, null);
    }

    /**
     * 下载文件
     *
     * @param fileUrl 下载全路径 配合{@link FastDownloadObserver}实现文件下载进度监听
     * @return
     */
    public Observable<ResponseBody> downloadFile(String fileUrl, Map<String, Object> header) {
        return FastDownloadRetrofit.getInstance().downloadFile(fileUrl, header);
    }

    /**
     * 上传文件/参数 配合{@link FastUploadRequestBody}及{@link FastUploadRequestListener}实现上传进度监听
     *
     * @param uploadUrl
     * @param body
     * @return
     */
    public Observable<ResponseBody> uploadFile(String uploadUrl, @Nullable RequestBody body) {
        return uploadFile(uploadUrl, body, null);
    }

    /**
     * 上传文件/参数 配合{@link FastUploadRequestBody}及{@link FastUploadRequestListener}实现上传进度监听
     *
     * @param uploadUrl 请求全路径
     * @param body      请求body 可将文件及其他参数放进body
     * @param header    可设置额外请求头信息
     * @return
     */
    public Observable<ResponseBody> uploadFile(String uploadUrl, @Nullable final RequestBody body, Map<String, Object> header) {
        return getRetrofit()
                .create(FastRetrofitService.class)
                .uploadFile(uploadUrl, body, header == null ? new HashMap<String, Object>() : header)
                .compose(FastTransformer.<ResponseBody>switchSchedulers());
    }

    /**
     * 设置请求头{@link #mHeaderInterceptor}
     *
     * @param key
     * @param value
     * @return
     */
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
     * 清空全局请求头
     *
     * @param key
     * @return
     */
    public FastRetrofit removeHeader(String key) {
        if (!TextUtils.isEmpty(key) && mHeaderMap.containsKey(key)) {
            mHeaderMap.remove(key);
        }
        return this;
    }

    /**
     * 清空所有全局请求头
     *
     * @return
     */
    public FastRetrofit removeHeader() {
        mHeaderMap.clear();
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

    public FastRetrofit setRetryOnConnectionFailure(boolean enable) {
        sClientBuilder.retryOnConnectionFailure(enable);
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
        return setLogEnable(enable, this.getClass().getSimpleName(), HttpLoggingInterceptor.Level.BODY);
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
     * 获取证书配置
     *
     * @return
     */
    public SSLUtil.SSLParams getCertificates() {
        return mSslParams;
    }


    /**
     * 设置服务器证书 {@link OkHttpClient.Builder#sslSocketFactory(SSLSocketFactory, X509TrustManager)}
     *
     * @param sslSocketFactory
     * @param trustManager
     * @return
     */
    public FastRetrofit setCertificates(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
        mSslParams.sslSocketFactory = sslSocketFactory;
        mSslParams.trustManager = trustManager;
        sClientBuilder.sslSocketFactory(sslSocketFactory, trustManager);
        return this;
    }

    /**
     * 默认信任所有证书-不安全
     *
     * @return
     */
    public FastRetrofit setCertificates() {
        SSLUtil.SSLParams sslParams = SSLUtil.getSslSocketFactory();
        return setCertificates(sslParams.sslSocketFactory, sslParams.trustManager);
    }

    /**
     * @param trustManager 使用自己设置的X509TrustManager
     * @return
     */
    public FastRetrofit setCertificates(X509TrustManager trustManager) {
        SSLUtil.SSLParams sslParams = SSLUtil.getSslSocketFactory(trustManager);
        return setCertificates(sslParams.sslSocketFactory, sslParams.trustManager);
    }

    /**
     * 使用预埋证书,校验服务端证书(自签名证书)-单向认证
     *
     * @param certificates 用含有服务端公钥的证书校验服务端证书
     * @return
     */
    public FastRetrofit setCertificates(InputStream... certificates) {
        SSLUtil.SSLParams sslParams = SSLUtil.getSslSocketFactory(certificates);
        return setCertificates(sslParams.sslSocketFactory, sslParams.trustManager);
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
        return setCertificates(sslParams.sslSocketFactory, sslParams.trustManager);
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
        return setCertificates(sslParams.sslSocketFactory, sslParams.trustManager);
    }

    /**
     * 设置多BaseUrl-解析器
     *
     * @param parser
     * @return
     */
    public FastRetrofit setUrlParser(FastUrlParser parser) {
        FastMultiUrl.getInstance().setUrlParser(parser);
        return this;
    }

    /**
     * 控制管理器是否拦截,在每个域名地址都已经确定,不需要再动态更改时可设置为 false
     *
     * @param enable
     * @return
     */
    public FastRetrofit setUrlInterceptEnable(boolean enable) {
        FastMultiUrl.getInstance().setIntercept(enable);
        return this;
    }

    /**
     * 是否Service Header设置多BaseUrl优先--默认method优先
     *
     * @param enable
     * @return
     */
    public FastRetrofit setHeaderPriorityEnable(boolean enable) {
        FastMultiUrl.getInstance().setHeaderPriorityEnable(enable);
        return this;
    }

    /**
     * 存放多BaseUrl 映射关系service 设置header模式-需要才设置
     *
     * @param map
     * @return
     */
    public FastRetrofit putHeaderBaseUrl(Map<String, String> map) {
        FastMultiUrl.getInstance().putHeaderBaseUrl(map);
        return this;
    }

    /**
     * 存放多BaseUrl 映射关系设置header模式-需要才设置
     *
     * @param urlKey
     * @param urlValue
     * @return
     */
    public FastRetrofit putHeaderBaseUrl(String urlKey, String urlValue) {
        FastMultiUrl.getInstance().putHeaderBaseUrl(urlKey, urlValue);
        return this;
    }

    /**
     * 移除BaseUrl映射关系设置header模式{@link #putHeaderBaseUrl(String, String)} key对应
     *
     * @param urlKey
     * @return
     */
    public FastRetrofit removeHeaderBaseUrl(String urlKey) {
        FastMultiUrl.getInstance().removeHeaderBaseUrl(urlKey);
        return this;
    }

    /**
     * 移除所有BaseUrl 映射关系设置header模式:即仅使用{@link #setBaseUrl(String)}中设置
     *
     * @return
     */
    public FastRetrofit removeHeaderBaseUrl() {
        FastMultiUrl.getInstance().clearAllHeaderBaseUrl();
        return this;
    }

    /**
     * 存放多BaseUrl 映射关系method模式-需要才设置
     *
     * @param map
     * @return
     */
    public FastRetrofit putBaseUrl(Map<String, String> map) {
        FastMultiUrl.getInstance().putBaseUrl(map);
        return this;
    }

    /**
     * 存放多BaseUrl 映射关系method模式-需要才设置
     *
     * @param method   retrofit service 除baseUrl外的部分即@POST或@GET里的内容
     * @param urlValue
     * @return
     */
    public FastRetrofit putBaseUrl(String method, String urlValue) {
        FastMultiUrl.getInstance().putBaseUrl(method, urlValue);
        return this;
    }

    /**
     * 移除BaseUrl映射关系映射关系method模式{@link #putBaseUrl(String, String)} key对应
     *
     * @param method retrofit service 除baseUrl外的部分即@POST或@GET里的内容
     * @return
     */
    public FastRetrofit removeBaseUrl(String method) {
        FastMultiUrl.getInstance().removeBaseUrl(method);
        return this;
    }

    /**
     * 移除所有BaseUrl 映射关系method模式:即仅使用{@link #setBaseUrl(String)}中设置
     *
     * @return
     */
    public FastRetrofit removeBaseUrl() {
        FastMultiUrl.getInstance().clearAllBaseUrl();
        return this;
    }
}
