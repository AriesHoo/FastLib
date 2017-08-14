package com.aries.library.fast.demo.retrofit.service;


import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created: AriesHoo on 2017/8/7 11:06
 * Function:
 * Desc:
 */
public interface CheckService {

    @FormUrlEncoded
    @POST("/XXX/XXX/XXX.do")
    Observable<String> checkMission(@FieldMap HashMap<String, Object> params);
}


