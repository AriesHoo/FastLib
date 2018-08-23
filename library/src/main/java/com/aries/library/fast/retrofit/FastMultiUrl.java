package com.aries.library.fast.retrofit;

import android.text.TextUtils;

import com.aries.library.fast.FastConstant;
import com.aries.library.fast.manager.LoggerManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author: AriesHoo on 2018/7/23 14:08
 * @E-Mail: AriesHoo@126.com
 * Function:FastMultiUrl 以简洁的 Api,让 Retrofit 不仅支持多 BaseUrl
 * 还可以在 App 运行时动态切换任意 BaseUrl,在多 BaseUrl 场景下也不会影响到其他不需要切换的 BaseUrl
 * Description:设置支持多BaseUrl
 * 1、2017-11-24 14:40:06 AriesHoo 新增部分set put方法返回本对象以便链式调用
 * 2、2018-7-2 15:15:46 修改设置BaseUrl逻辑解决随时切换问题
 * 3、2018-7-2 16:58:01 删除BaseUrl变更监听相关代码
 * 4、2018-7-3 12:24:24 新增单独method 方法设置请求BaseUrl方法
 * 5、2018-7-9 15:13:45 修改解析method方法增加对get方法兼容
 * 6、2018-7-31 09:10:45 调整通过header及method重定向请求Url方法
 * 7、2018-8-23 11:18:40 修改设置BaseUrl重定向问题{@link #processRequest(Request)}
 */
class FastMultiUrl {

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
    private boolean mIsIntercept = false;
    /**
     * 是否Service Header设置多BaseUrl优先
     */
    private boolean mHeaderPriorityEnable;
    /**
     * header模式baseUrl
     */
    private final Map<String, HttpUrl> mHeaderBaseUrlMap = new HashMap<>();
    /**
     * 通过method控制不同BaseUrl集合
     */
    private final Map<String, HttpUrl> mBaseUrlMap = new HashMap<>();
    private final Interceptor mInterceptor;
    private FastUrlParser mUrlParser;
    private static volatile FastMultiUrl sInstance;
    /**
     * retrofit 设置的BaseUrl
     */
    private String mBaseUrl;

    public static FastMultiUrl getInstance() {
        if (sInstance == null) {
            synchronized (FastMultiUrl.class) {
                if (sInstance == null) {
                    sInstance = new FastMultiUrl();
                }
            }
        }
        return sInstance;
    }

    private FastMultiUrl() {
        //初始化解析器
        setUrlParser(new FastUrlParser() {
            @Override
            public HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url) {
                // 只支持 http 和 https
                if (null == domainUrl) {
                    return url;
                }
                //解析得到service里的方法名(即@POST或@GET里的内容)--如果@GET 并添加参数 则为方法名+参数拼接
                String method = !TextUtils.isEmpty(mBaseUrl) ? url.toString().replace(mBaseUrl.toString(), "") : "";
                return checkUrl(domainUrl.toString() + method);
            }
        });
        this.mInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                // 可以在 App 运行时,随时通过 setIntercept(false) 来结束本管理器的拦截
                if (!isIntercept()) {
                    return chain.proceed(request);
                }
                //如果请求url不包含默认的BaseUrl也不进行拦截
                if (request != null && request.url() != null && !request.url().toString().contains(mBaseUrl)) {
                    LoggerManager.i(TAG, "无统一BaseUrl不拦截:" + request.url() + ";BaseUrl:" + mBaseUrl);
                    return chain.proceed(request);
                }
                return chain.proceed(processRequest(request));
            }
        };
    }

    /**
     * 将 {@link okhttp3.OkHttpClient.Builder} 传入,配置一些本管理器需要的参数
     *
     * @param builder
     * @return
     */
    public FastMultiUrl with(OkHttpClient.Builder builder) {
        builder.addInterceptor(mInterceptor);
        return sInstance;
    }

    /**
     * 对 {@link Request} 进行一些必要的加工
     *
     * @param request
     * @return
     */
    public Request processRequest(Request request) {
        Request.Builder newBuilder = request.newBuilder();
        HttpUrl httpUrl = getHeaderHttpUrl(request, newBuilder);
        if (null != httpUrl) {
            HttpUrl newUrl = mUrlParser.parseUrl(httpUrl, request.url());
            LoggerManager.i(FastMultiUrl.TAG, "Header 模式重定向Url:Base Url is { " + mBaseUrl + " }" + ";New Url is { " + newUrl + " }" + ";Old Url is { " + request.url() + " }");
            return newBuilder
                    .url(newUrl)
                    .build();
        }
        httpUrl = getMethodHttpUrl(request);
        if (null != httpUrl) {
            LoggerManager.i(FastMultiUrl.TAG, "Method 模式重定向Url:Base Url is { " + mBaseUrl + " }" + ";New Url is { " + httpUrl + " }" + ";Old Url is { " + request.url() + " }");
            return newBuilder
                    .url(httpUrl)
                    .build();
        }
        //重定向BaseUrl mBaseUrl是okhttp里设置的base url--程序运行过程中只有一个值--用于拆分method
        HttpUrl httpUrlBase = getGlobalBaseUrl();
        if (httpUrlBase != null && !httpUrlBase.toString().equals(mBaseUrl)) {
            HttpUrl httpNew = checkUrl(request.url().toString().replace(mBaseUrl, httpUrlBase.toString()));
            LoggerManager.i(FastMultiUrl.TAG, "重定向Url:Base Url is { " + httpUrlBase.toString() + " }" + ";New Url is { " + httpNew + " }" + ";Old Url is { " + request.url() + " }");
            return newBuilder
                    .url(httpNew)
                    .build();
        }
        return newBuilder.build();
    }

    /**
     * 获取以Header 优先的HttpUrl
     *
     * @param request
     * @param newBuilder
     * @return
     */
    private HttpUrl getHeaderHttpUrl(Request request, Request.Builder newBuilder) {
        if (request == null) {
            return null;
        }
        HttpUrl httpUrl = null;
        //header 标记优先
        if (mHeaderPriorityEnable) {
            String keyName = getHeaderBaseUrlKey(request);
            // 如果有 header,获取 header 中配置的url,否则检查全局的 BaseUrl,未找到则为null
            if (!TextUtils.isEmpty(keyName)) {
                httpUrl = getHeaderBaseUrl(keyName);
                newBuilder.removeHeader(BASE_URL_NAME);
            } else {
                httpUrl = getHeaderBaseUrl(GLOBAL_BASE_URL_NAME);
            }
        }
        return httpUrl;
    }

    /**
     * 获取以method 优先的HttpUrl
     *
     * @param request
     * @return
     */
    private HttpUrl getMethodHttpUrl(Request request) {
        if (request == null) {
            return null;
        }
        HttpUrl httpUrl = null;
        HttpUrl url = request.url();
        //解析得到service里的方法名(即@POST或@GET里的内容)
        String method = !TextUtils.isEmpty(mBaseUrl) ? url.toString().replace(mBaseUrl.toString(), "") : "";

        String methodKey = method;
        //包含? 很大可能是get请求增加了参数需过滤掉
        boolean isContainKey = methodKey.contains("?");
        if (isContainKey) {
            methodKey = methodKey.substring(0, methodKey.indexOf("?"));
        }
        LoggerManager.d(TAG, "Base Url is { " + mBaseUrl + " }" + ";Old Url is{" + url.newBuilder().toString() + "};Method is <<" + methodKey + ">>");
        //如果
        if (!mHeaderPriorityEnable && mBaseUrlMap.containsKey(methodKey)) {
            return checkUrl(getBaseUrl(methodKey).toString() + method);
        }
        return httpUrl;
    }

    /**
     * 是否拦截--可以在固定的时机停止{@link #setIntercept}
     *
     * @return
     */
    public boolean isIntercept() {
        return mIsIntercept;
    }

    /**
     * 控制管理器是否拦截,在每个域名地址都已经确定,不需要再动态更改时可设置为 false
     *
     * @param enable
     */
    public FastMultiUrl setIntercept(boolean enable) {
        this.mIsIntercept = enable;
        return sInstance;
    }

    /**
     * 是否Service Header设置多BaseUrl优先--默认method优先
     *
     * @param enable
     * @return
     */
    public FastMultiUrl setHeaderPriorityEnable(boolean enable) {
        this.mHeaderPriorityEnable = enable;
        return sInstance;
    }

    /**
     * 全局动态替换 BaseUrl,优先级 Header中配置的url > 全局配置的url
     * 除了作为备用的 BaseUrl ,当你项目中只有一个 BaseUrl ,但需要动态改变
     * 这种方式不用在每个接口方法上加 Header,也是个很好的选择
     *
     * @param url
     */
    public FastMultiUrl setGlobalBaseUrl(String url) {
        synchronized (mBaseUrlMap) {
            mBaseUrlMap.put(GLOBAL_BASE_URL_NAME, checkUrl(url));
        }
        //保证唯一性为retrofit设置的baseUrl
        if (TextUtils.isEmpty(mBaseUrl)) {
            mBaseUrl = url;
        } else {
            setIntercept(true);
        }
        return sInstance;
    }

    /**
     * 获取全局BaseUrl
     *
     * @return
     */
    public HttpUrl getGlobalBaseUrl() {
        return mBaseUrlMap.get(GLOBAL_BASE_URL_NAME);
    }

    public FastMultiUrl putHeaderBaseUrl(Map<String, String> map) {
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                putHeaderBaseUrl(entry.getKey(), entry.getValue());
            }
        }
        return sInstance;
    }

    /**
     * 存放 BaseUrl 的映射关系
     *
     * @param urlKey
     * @param urlValue
     */
    public FastMultiUrl putHeaderBaseUrl(String urlKey, String urlValue) {
        synchronized (mHeaderBaseUrlMap) {
            mHeaderBaseUrlMap.put(urlKey, checkUrl(urlValue));
            setIntercept(true);
        }
        return sInstance;
    }

    /**
     * 取出对应 urlKey 的 Url
     *
     * @param urlKey
     * @return
     */
    public HttpUrl getHeaderBaseUrl(String urlKey) {
        return mHeaderBaseUrlMap.get(urlKey);
    }

    /**
     * 根据key删除BaseUrl
     *
     * @param urlKey
     */
    public FastMultiUrl removeHeaderBaseUrl(String urlKey) {
        synchronized (mHeaderBaseUrlMap) {
            mHeaderBaseUrlMap.remove(urlKey);
        }
        return sInstance;
    }

    /**
     * 清除所有BaseUrl
     */
    public FastMultiUrl clearAllHeaderBaseUrl() {
        mHeaderBaseUrlMap.clear();
        return sInstance;
    }

    /**
     * 可自行实现 {@link FastUrlParser} 动态切换 Url 解析策略
     *
     * @param parser
     */
    public FastMultiUrl setUrlParser(FastUrlParser parser) {
        this.mUrlParser = parser;
        return sInstance;
    }

    public FastMultiUrl putBaseUrl(Map<String, String> map) {
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                putBaseUrl(entry.getKey(), entry.getValue());
            }
        }
        return sInstance;
    }

    /**
     * 存放 BaseUrl 的映射关系
     *
     * @param urlKey
     * @param urlValue
     * @return
     */
    public FastMultiUrl putBaseUrl(String urlKey, String urlValue) {
        synchronized (mBaseUrlMap) {
            mBaseUrlMap.put(urlKey, checkUrl(urlValue));
            setIntercept(true);
        }
        return sInstance;
    }

    /**
     * @param urlKey
     * @return
     */
    public HttpUrl getBaseUrl(String urlKey) {
        return mBaseUrlMap.get(urlKey);
    }


    public FastMultiUrl removeBaseUrl(String urlKey) {
        synchronized (mBaseUrlMap) {
            mBaseUrlMap.remove(urlKey);
        }
        return sInstance;
    }

    /**
     * 清除所有BaseUrl
     */
    public FastMultiUrl clearAllBaseUrl() {
        mBaseUrlMap.clear();
        return sInstance;
    }

    /**
     * 从 {@link Request#header(String)} 中取出BASE_URL_NAME
     *
     * @param request
     * @return
     */
    private String getHeaderBaseUrlKey(Request request) {
        Headers heads = request.headers();
        if (heads != null) {
            LoggerManager.i(TAG, "header:" + heads.toString());
        }
        List<String> headers = request.headers(BASE_URL_NAME);
        if (headers == null || headers.size() == 0) {
            return null;
        }
        if (headers.size() > 1) {
            throw new IllegalArgumentException("Only one " + BASE_URL_NAME + " in the headers");
        }
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
