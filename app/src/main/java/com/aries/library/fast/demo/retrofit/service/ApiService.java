package com.aries.library.fast.demo.retrofit.service;


import com.aries.library.fast.demo.base.BaseMovieEntity;
import com.aries.library.fast.demo.constant.ApiConstant;
import com.aries.library.fast.demo.entity.BaseReadArticleEntity;
import com.aries.library.fast.demo.entity.UpdateEntity;
import com.aries.library.fast.retrofit.FastRetrofit;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @Author: AriesHoo on 2018/7/30 14:01
 * @E-Mail: AriesHoo@126.com
 * Function: 接口定义
 * Description:
 */
public interface ApiService {

    /**
     * 获取电影数据
     *
     * @param url
     * @param map
     * @return
     */
    @GET("{url}")
    Observable<BaseMovieEntity> getMovie(@Path("url") String url, @QueryMap Map<String, Object> map);

    /**
     * 获取新闻数据
     *
     * @param url
     * @param map
     * @return
     */
    @GET("{url}")
    Observable<BaseReadArticleEntity> getArticle(@Path("url") String url, @QueryMap Map<String, Object> map);

    /**
     * 检查应用更新--同时设置了Method及Header模式重定向请求Url,默认Method优先;
     * 可通过{@link FastRetrofit#setHeaderPriorityEnable(boolean)}设置Header模式优先
     *
     * @param map
     * @return
     */
    @GET(ApiConstant.API_UPDATE_APP)
    @Headers({FastRetrofit.BASE_URL_NAME_HEADER + ApiConstant.API_UPDATE_APP_KEY})
    Observable<UpdateEntity> updateApp(@QueryMap Map<String, Object> map);
}
