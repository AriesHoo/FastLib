package com.aries.library.fast.demo;

import android.content.Context;

import com.aries.library.fast.FastApplication;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created: AriesHoo on 2017/7/20 14:11
 * Function: 基础配置Application
 * Desc:
 */
public class App extends FastApplication {
    private static Context mContext;
    private String TAG = "FastLib";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        //测试关键header
        Map<String, Object> header = new HashMap<>();
        header.put("head", "head");
        header.put("time", System.currentTimeMillis());
        LoggerManager.init(TAG, BuildConfig.DEBUG);
        ToastUtil.init(mContext, true);

        FastRetrofit.getInstance()
                .setBaseUrl(BuildConfig.BASE_URL)
                .trustAllSSL()//信任所有证书--也可设置useSingleSignedSSL(单向认证)或useBothSignedSSL(双向验证)
                .setHeaders(header)//设置统一请求头
                .setLogEnable(BuildConfig.DEBUG)//设置请求全局log-可设置tag及Level类型
//                .setLogEnable(BuildConfig.DEBUG, TAG, HttpLoggingInterceptor.Level.BASIC)
                .setTimeout(30);//设置统一超时--也可单独调用read/write/connect超时(可以设置时间单位TimeUnit)
    }
}
