package com.aries.library.fast.demo.retrofit.repository;

import com.aries.library.fast.demo.base.BaseMovieEntity;
import com.aries.library.fast.demo.entity.UpdateEntity;
import com.aries.library.fast.demo.retrofit.service.ApiService;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.retrofit.FastTransformer;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;


/**
 * Created: AriesHoo on 2017/9/7 11:00
 * Function:Retrofit api调用示例
 * Desc:
 */

public class ApiRepository extends BaseRepository {

    private static volatile ApiRepository instance;
    private ApiService mApiService;

    private ApiRepository() {
        mApiService = getApiService();
    }

    public static ApiRepository getInstance() {
        if (instance == null) {
            synchronized (ApiRepository.class) {
                if (instance == null) {
                    instance = new ApiRepository();
                }
            }
        }
        return instance;
    }

    private ApiService getApiService() {
//        if (mApiService == null) {
        mApiService = FastRetrofit.getInstance().createService(ApiService.class);
//        }
        return mApiService;
    }

    /**
     * 获取电影列表
     *
     * @param url   拼接URL
     * @param start
     * @param count
     * @return
     */
    public Observable<BaseMovieEntity> getMovie(String url, int start, int count) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("count", count);
        Map<String, Object> map = new HashMap<>();
        map.put("ticket", "ticker");
        return FastTransformer.switchSchedulers(getApiService().getMovie(url, params, map));
    }

    public Observable<UpdateEntity> updateApp() {
        return FastTransformer.switchSchedulers(getApiService().updateApp());
    }
}
