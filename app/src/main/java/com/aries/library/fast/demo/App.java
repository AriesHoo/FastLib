package com.aries.library.fast.demo;

import android.content.Context;

import com.aries.library.fast.FastApplication;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.util.SizeUtil;
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
    private static int imageHeight = 0;

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
//                .setHeaders(header)//设置统一请求头
                .setLogEnable(BuildConfig.DEBUG)//设置请求全局log-可设置tag及Level类型
//                .setLogEnable(BuildConfig.DEBUG, TAG, HttpLoggingInterceptor.Level.BASIC)
                .setTimeout(30);//设置统一超时--也可单独调用read/write/connect超时(可以设置时间单位TimeUnit)

        //以下为配置多BaseUrl
        //step1
        //FastMultiUrl.getInstance().putBaseUrl("test", "http://www.baidu.com");

        //step2
        // 需要step1中baseUrl的方法需要在对应service里增加
        // @Headers({FastMultiUrl.BASE_URL_NAME_HEADER + "test"})
        //增加一个Header配置注意FastMultiUrl.BASE_URL_NAME_HEADER是必须后面"test"作为标记
        // FastMultiUrl里增加的拦截器才找得到对应的BaseUrl

    }

    /**
     * 获取banner及个人中心设置ImageView宽高
     *
     * @return
     */
    public static int getImageHeight() {
        if (imageHeight == 0) {
            imageHeight = (int) (SizeUtil.getScreenWidth() * 0.55);
        }
        return imageHeight;
    }
}
