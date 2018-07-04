package com.aries.library.fast.demo.retrofit.service;


import com.aries.library.fast.demo.base.BaseMovieEntity;
import com.aries.library.fast.demo.constant.ApiConstant;
import com.aries.library.fast.demo.entity.UpdateEntity;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created: AriesHoo on 2017/8/23 13:53
 * Function: 接口定义
 * Desc:
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("{url}")
    Observable<BaseMovieEntity> getMovie(@Path("url") String url, @FieldMap Map<String, Object> map, @HeaderMap Map<String, Object> header);

    @GET(ApiConstant.API_UPDATE_APP)
//    @Headers({FastRetrofit.BASE_URL_NAME_HEADER + ApiConstant.API_UPDATE_APP_KEY})
    Observable<UpdateEntity> updateApp();
}
