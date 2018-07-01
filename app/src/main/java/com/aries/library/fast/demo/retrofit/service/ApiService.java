package com.aries.library.fast.demo.retrofit.service;


import com.aries.library.fast.demo.base.BaseMovieEntity;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
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
//    @Headers({FastMultiUrl.BASE_URL_NAME_HEADER+"taobao"})
    @POST("{url}")
    Observable<BaseMovieEntity> getMovie(@Path("url") String url, @FieldMap Map<String, Object> map, @HeaderMap Map<String, Object> header);

}
