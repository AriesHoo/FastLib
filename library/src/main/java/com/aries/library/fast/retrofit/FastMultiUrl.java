package com.aries.library.fast.retrofit;

import android.text.TextUtils;

import com.aries.library.fast.FastConstant;
import com.aries.library.fast.manager.LoggerManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created: AriesHoo on 2017/9/22 11:07
 * Function:FastMultiUrl 以简洁的 Api,让 Retrofit 不仅支持多 BaseUrl
 * 还可以在 App 运行时动态切换任意 BaseUrl,在多 BaseUrl 场景下也不会影响到其他不需要切换的 BaseUrl
 * Desc: 设置支持多BaseUrl
 */
public class FastMultiUrl {

    private static final String TAG = "FastMultiUrl";
    private static final String BASE_URL_NAME = "BASE_URL_NAME";
    private static final String GLOBAL_BASE_URL_NAME = "GLOBAL_BASE_URL_NAME";
    /**
     * 用于单独设置其它BaseUrl的Service设置Header标记
     */
    public static final String BASE_URL_NAME_HEADER = BASE_URL_NAME + ": ";

    /**
     * 是否开启拦截开始运行,可以随时停止运行,比如你在 App 启动后已经不需要在动态切换 baseUrl 了
     */
    private boolean isIntercept = true;
    private final Map<String, HttpUrl> mBaseUrlMap = new HashMap<>();
    private final Interceptor mInterceptor;
    private final List<OnUrlChangedListener> mListeners = new ArrayList<>();
    private FastUrlParser mUrlParser;
    private static volatile FastMultiUrl instance;

    public interface OnUrlChangedListener {
        /**
         * 当 Url 的 BaseUrl 被改变时回调
         * 调用时间是在接口请求服务器之前
         *
         * @param newUrl
         * @param oldUrl
         */
        void onUrlChanged(HttpUrl newUrl, HttpUrl oldUrl);
    }

    public interface FastUrlParser {
        /**
         * 将 {@link FastMultiUrl#mBaseUrlMap} 中映射的 Url 解析成完整的{@link HttpUrl}
         * 用来替换 @{@link Request#url} 达到动态切换 Url
         *
         * @param domainUrl
         * @return
         */
        HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url);
    }

    public static FastMultiUrl getInstance() {
        if (instance == null) {
            synchronized (FastMultiUrl.class) {
                if (instance == null) {
                    instance = new FastMultiUrl();
                }
            }
        }
        return instance;
    }

    private FastMultiUrl() {
        setUrlParser(new FastUrlParser() {
            @Override
            public HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url) {
                // 如果 HttpUrl.parse(url); 解析为 null 说明,url 格式不正确,正确的格式为 "https://github.com:443"
                // http 默认端口 80,https 默认端口 443 ,如果端口号是默认端口号就可以将 ":443" 去掉
                // 只支持 http 和 https
                if (null == domainUrl) return url;
                return url.newBuilder()
                        .scheme(domainUrl.scheme())
                        .host(domainUrl.host())
                        .port(domainUrl.port())
                        .build();
            }
        });
        this.mInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                if (!isIntercept()) // 可以在 App 运行时,随时通过 setIntercept(false) 来结束本管理器的拦截
                    return chain.proceed(chain.request());
                return chain.proceed(processRequest(chain.request()));
            }
        };
    }

    /**
     * 将 {@link okhttp3.OkHttpClient.Builder} 传入,配置一些本管理器需要的参数
     *
     * @param builder
     * @return
     */
    public OkHttpClient.Builder with(OkHttpClient.Builder builder) {
        return builder
                .addInterceptor(mInterceptor);
    }

    /**
     * 对 {@link Request} 进行一些必要的加工
     *
     * @param request
     * @return
     */
    public Request processRequest(Request request) {
        Request.Builder newBuilder = request.newBuilder();
        String keyName = getBaseUrlKeyFromHeaders(request);
        HttpUrl httpUrl;
        // 如果有 header,获取 header 中配置的url,否则检查全局的 BaseUrl,未找到则为null
        if (!TextUtils.isEmpty(keyName)) {
            httpUrl = getBaseUrl(keyName);
            newBuilder.removeHeader(BASE_URL_NAME);
        } else {
            httpUrl = getBaseUrl(GLOBAL_BASE_URL_NAME);
        }
        if (null != httpUrl) {
            HttpUrl newUrl = mUrlParser.parseUrl(httpUrl, request.url());
            LoggerManager.d(FastMultiUrl.TAG, "New Url is { " + newUrl.toString() + " },Old Url is { " + request.url().toString() + " }");
            Object[] listeners = listenersToArray();
            if (listeners != null) {
                for (int i = 0; i < listeners.length; i++) {
                    ((OnUrlChangedListener) listeners[i]).onUrlChanged(newUrl, request.url()); // 通知监听器此 Url 的 BaseUrl 已被改变
                }
            }
            return newBuilder
                    .url(newUrl)
                    .build();
        }
        return newBuilder.build();
    }

    /**
     * 是否拦截--可以在固定的时机停止{@link #setIntercept}
     *
     * @return
     */
    public boolean isIntercept() {
        return isIntercept;
    }

    /**
     * 控制管理器是否拦截,在每个域名地址都已经确定,不需要再动态更改时可设置为 false
     *
     * @param enable
     */
    public void setIntercept(boolean enable) {
        this.isIntercept = enable;
    }

    /**
     * 获取存放BaseUrl的集合
     *
     * @return
     */
    public Map<String, HttpUrl> getBaseUrlMap() {
        return mBaseUrlMap;
    }

    /**
     * 全局动态替换 BaseUrl,优先级 Header中配置的url > 全局配置的url
     * 除了作为备用的 BaseUrl ,当你项目中只有一个 BaseUrl ,但需要动态改变
     * 这种方式不用在每个接口方法上加 Header,也是个很好的选择
     *
     * @param url
     */
    public void setGlobalBaseUrl(String url) {
        synchronized (mBaseUrlMap) {
            mBaseUrlMap.put(GLOBAL_BASE_URL_NAME, checkUrl(url));
        }
    }

    /**
     * 获取全局 BaseUrl
     */
    public HttpUrl getGlobalBaseUrl() {
        return mBaseUrlMap.get(GLOBAL_BASE_URL_NAME);
    }

    /**
     * 移除全局 BaseUrl
     */
    public void removeGlobalBaseUrl() {
        synchronized (mBaseUrlMap) {
            mBaseUrlMap.remove(GLOBAL_BASE_URL_NAME);
        }
    }

    /**
     * 存放 BaseUrl 的映射关系
     *
     * @param urlKey
     * @param urlValue
     */
    public void putBaseUrl(String urlKey, String urlValue) {
        synchronized (mBaseUrlMap) {
            mBaseUrlMap.put(urlKey, checkUrl(urlValue));
        }
    }

    /**
     * 取出对应 urlKey 的 Url
     *
     * @param urlKey
     * @return
     */
    public HttpUrl getBaseUrl(String urlKey) {
        return mBaseUrlMap.get(urlKey);
    }

    /**
     * 根据key删除BaseUrl
     *
     * @param urlKey
     */
    public void removeBaseUrl(String urlKey) {
        synchronized (mBaseUrlMap) {
            mBaseUrlMap.remove(urlKey);
        }
    }

    /**
     * 清除所有BaseUrl
     */
    public void clearAllBaseUrl() {
        mBaseUrlMap.clear();
    }

    /**
     * 检查是否包含某个url的key
     *
     * @param urlKey
     * @return
     */
    public boolean containsBaseUrl(String urlKey) {
        return mBaseUrlMap.containsKey(urlKey);
    }

    /**
     * 可自行实现 {@link FastUrlParser} 动态切换 Url 解析策略
     *
     * @param parser
     */
    public void setUrlParser(FastUrlParser parser) {
        this.mUrlParser = parser;
    }

    /**
     * 注册当 Url 的 BaseUrl 被改变时会被回调的监听器
     *
     * @param listener
     */
    public void registerUrlChangeListener(OnUrlChangedListener listener) {
        synchronized (mListeners) {
            mListeners.add(listener);
        }
    }

    /**
     * 注销当 Url 的 BaseUrl 被改变时会被回调的监听器
     *
     * @param listener
     */
    public void unregisterUrlChangedListener(OnUrlChangedListener listener) {
        synchronized (mListeners) {
            mListeners.remove(listener);
        }
    }

    private Object[] listenersToArray() {
        Object[] listeners = null;
        synchronized (mListeners) {
            if (mListeners.size() > 0) {
                listeners = mListeners.toArray();
            }
        }
        return listeners;
    }


    /**
     * 从 {@link Request#header(String)} 中取出BASE_URL_NAME
     *
     * @param request
     * @return
     */
    private String getBaseUrlKeyFromHeaders(Request request) {
        List<String> headers = request.headers(BASE_URL_NAME);
        if (headers == null || headers.size() == 0)
            return null;
        if (headers.size() > 1)
            throw new IllegalArgumentException("Only one " + BASE_URL_NAME + " in the headers");
        return request.header(BASE_URL_NAME);
    }

    /**
     * 校验url合法性
     *
     * @param url
     * @return
     */
    private HttpUrl checkUrl(String url) {
        HttpUrl parseUrl = HttpUrl.parse(url);
        if (null == parseUrl) {
            throw new NullPointerException(FastConstant.EXCEPTION_EMPTY_URL);
        } else {
            return parseUrl;
        }
    }
}
