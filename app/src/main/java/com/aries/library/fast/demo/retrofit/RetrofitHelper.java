package com.aries.library.fast.demo.retrofit;


import com.aries.library.fast.demo.retrofit.service.CheckService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created: AriesHoo on 2017/6/23 16:50
 * Function: retrofit
 * Desc:
 */
public class RetrofitHelper {

    private static volatile Retrofit sRetrofit;
    private static volatile Retrofit.Builder sBuilder;
    private static volatile RetrofitHelper sHelper;

    private RetrofitHelper() {
        sBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    public static RetrofitHelper getInstance() {
        if (sHelper == null) {
            synchronized (RetrofitHelper.class) {
                if (sHelper == null) {
                    sHelper = new RetrofitHelper();
                }
            }
        }
        return sHelper;
    }

    protected static <T> T create(Class<T> apiService) {
        return sHelper.getInstance().sRetrofit.create(apiService);
    }

    public CheckService getCheckService() {
        sRetrofit = sBuilder
                .baseUrl("")
                .build();
        return create(CheckService.class);
    }

}