package com.aries.library.fast.demo.retrofit.service;


import com.aries.library.fast.demo.base.BaseMovieEntity;
import com.aries.library.fast.demo.constant.ApiConstant;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created: AriesHoo on 2017/8/23 13:53
 * Function: 接口定义
 * Desc:
 */
public interface ApiService {

    /**
     * 获取TOP 250
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstant.API_MOVIE_TOP)
    Observable<BaseMovieEntity> getTopMovie(@FieldMap Map<String, Object> map);

    /**
     * 获取正在热映
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstant.API_MOVIE_IN_THEATERS)
    Observable<BaseMovieEntity> getInTheatersMovie(@FieldMap Map<String, Object> map);

    /**
     * 获取即将上映
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstant.API_MOVIE_COMING_SOON)
    Observable<BaseMovieEntity> getComingSoonMovie(@FieldMap Map<String, Object> map);

}
