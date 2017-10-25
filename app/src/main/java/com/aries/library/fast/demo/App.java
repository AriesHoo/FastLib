package com.aries.library.fast.demo;

import android.content.Context;
import android.support.annotation.NonNull;

import com.aries.library.fast.FastApplication;
import com.aries.library.fast.FastConfig;
import com.aries.library.fast.demo.helper.RefreshHeaderHelper;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;
import com.chad.library.adapter.base.animation.SlideInRightAnimation;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;


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
        mContext = this;

        //初始化Logger日志打印
        LoggerManager.init(TAG, BuildConfig.DEBUG);
        //初始化toast工具
        ToastUtil.init(mContext, true);
        //初始化Retrofit配置
        FastRetrofit.getInstance()
                .setBaseUrl(BuildConfig.BASE_URL)
                .trustAllSSL()//信任所有证书--也可设置useSingleSignedSSL(单向认证)或useBothSignedSSL(双向验证)
                //.setHeaders(header)//设置统一请求头
                .setLogEnable(BuildConfig.DEBUG)//设置请求全局log-可设置tag及Level类型
                //.setLogEnable(BuildConfig.DEBUG, TAG, HttpLoggingInterceptor.Level.BASIC)
                .setTimeout(30);//设置统一超时--也可单独调用read/write/connect超时(可以设置时间单位TimeUnit)

        //以下为配置多BaseUrl
        //step1
        //FastMultiUrl.getInstance().putBaseUrl("test", "http://www.baidu.com");
        //step2
        // 需要step1中baseUrl的方法需要在对应service里增加
        // @Headers({FastMultiUrl.BASE_URL_NAME_HEADER + "test"})
        //增加一个Header配置注意FastMultiUrl.BASE_URL_NAME_HEADER是必须后面"test"作为标记
        // FastMultiUrl里增加的拦截器才找得到对应的BaseUrl

        LoggerManager.d("context:" + this);
        //全局配置参数
        FastConfig.getInstance(mContext)
                .setTitleBackgroundResource(R.color.colorTitleBackground)//设置TitleBarView背景资源
                .setContentViewBackgroundResource(R.color.colorBackground)//设置Activity或Fragment根布局背景资源
                .setTitleTextColor(mContext.getResources().getColor(R.color.colorTitleText))//设置TitleBarView 所有TextView颜色
                .setLightStatusBarEnable(true)//设置是否状态栏浅色模式(深色状态栏文字及图标)
                .setSwipeBackEnable(true)//设置Activity是否支持滑动返回
                .setAdapterAnimationEnable(true) //是否设置Adapter加载动画
                .setDefaultAdapterAnimation(new SlideInRightAnimation())//设置全局Adapter加载动画--设置该方法内部同步调用setAdapterAnimationEnable(true)
                .setDefaultRefreshHeader(new DefaultRefreshHeaderCreater() {//设置SmartRefreshLayout刷新头-自定加载使用BaseRecyclerViewAdapterHelper
                    @NonNull
                    @Override
                    public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                        layout.setEnableHeaderTranslationContent(false);
                        return RefreshHeaderHelper.getInstance().getRefreshHeader(mContext);
                    }
                })
                .setTitleElevation(mContext.getResources().getDimension(R.dimen.dp_elevation));
    }

    /**
     * 获取banner及个人中心设置ImageView宽高
     *
     * @return
     */
    public static int getImageHeight() {
        imageHeight = (int) (SizeUtil.getScreenWidth() * 0.55);
        return imageHeight;
    }

    public static Context getContext() {
        return mContext;
    }
}
