package com.aries.library.fast.retrofit;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created: AriesHoo on 2018/7/2 16:55
 * E-Mail: AriesHoo@126.com
 * Function: 多BaseUrl解析器--开发者可自定义
 * Description:
 */
public interface FastUrlParser {

    /**
     * 将 {@link FastMultiUrl#mBaseUrlMap} 中映射的 Url 解析成完整的{@link HttpUrl}
     * 用来替换 @{@link Request#url} 里的BaseUrl以达到动态切换 Url的目的
     *
     * @param domainUrl
     * @return
     */
    HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url);
}
