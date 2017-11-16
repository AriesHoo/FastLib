package com.aries.library.fast.demo.retrofit.repository;

import com.aries.library.fast.demo.base.BaseMovieEntity;
import com.aries.library.fast.demo.constant.MovieConstant;
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
        return FastRetrofit.getInstance().createService(ApiService.class);
    }

    /**
     * 获取Top250电影
     *
     * @param start
     * @param count
     * @return
     */
    public Observable<BaseMovieEntity> getTopMovie(int start, int count) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("count", count);
        return FastTransformer.switchSchedulers(mApiService.getTopMovie(params));
    }

    /**
     * 获取正在热映电影
     *
     * @param start
     * @param count
     * @return
     */
    public Observable<BaseMovieEntity> getInTheatersMovie(int start, int count) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("count", count);
        return FastTransformer.switchSchedulers(getApiService().getInTheatersMovie(params));
    }

    /**
     * 获取即将上映电影
     *
     * @param start
     * @param count
     * @return
     */
    public Observable<BaseMovieEntity> getComingSoonMovie(int start, int count) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("count", count);
        return FastTransformer.switchSchedulers(getApiService().getComingSoonMovie(params));
    }

    public Observable<BaseMovieEntity> getBaseMovie(int type, int start, int count) {
        if (type == MovieConstant.MOVIE_IN_THEATERS) {
            return getInTheatersMovie(start, count);
        } else if (type == MovieConstant.MOVIE_COMING_SOON) {
            return getComingSoonMovie(start, count);
        }
        return getTopMovie(start, count);
    }
}
