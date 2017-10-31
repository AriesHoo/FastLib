package com.aries.library.fast.demo;

import android.content.Context;
import android.support.annotation.NonNull;

import com.aries.library.fast.FastApplication;
import com.aries.library.fast.FastConfig;
import com.aries.library.fast.demo.helper.RefreshHeaderHelper;
import com.aries.library.fast.entity.FastTitleConfigEntity;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;
import com.chad.library.adapter.base.animation.ScaleInAnimation;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;


/**
 * Created: AriesHoo on 2017/7/20 14:11
 * Function: 基础配置Application
 * Desc: 是否继承根据情况而定--滑动返回设置请查看 {@link FastApplication}
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


        //全局配置参数
        //推荐先获取library里默认TitleBarView配置再按需修改的模式 FastTitleConfigEntity
        FastTitleConfigEntity titleConfig = FastConfig.getInstance(mContext).getTitleConfig();
        FastConfig.getInstance(mContext)
                // 其它属性请查看getInstance默认设置
                .setTitleConfig(titleConfig
                        //设置TitleBarView 所有TextView颜色
                        .setTitleTextColor(mContext.getResources().getColor(R.color.colorTitleText))
                        //设置TitleBarView背景资源
                        .setTitleBackgroundResource(R.color.colorTitleBackground)
                        //设置是否状态栏浅色模式(深色状态栏文字及图标)
                        .setLightStatusBarEnable(true)
                        //设置TitleBarView海拔高度
                        .setTitleElevation(mContext.getResources().getDimension(R.dimen.dp_elevation)))
                //设置Glide背景色
                .setPlaceholderColor(mContext.getResources().getColor(R.color.colorPlaceholder))
                //设置Glide圆角背景弧度
                .setPlaceholderRoundRadius(mContext.getResources().getDimension(R.dimen.dp_placeholder_radius))
                .setContentViewBackgroundResource(R.color.colorBackground)//设置Activity或Fragment根布局背景资源
                .setSwipeBackEnable(true)//设置Activity是否支持滑动返回--注意设置透明主题参考demo
                .setAdapterAnimationEnable(true) //是否设置Adapter加载动画
                .setDefaultAdapterAnimation(new ScaleInAnimation())//设置全局Adapter加载动画--设置该方法内部同步调用setAdapterAnimationEnable(true)
                .setDefaultRefreshHeader(new DefaultRefreshHeaderCreater() {//设置SmartRefreshLayout刷新头-自定加载使用BaseRecyclerViewAdapterHelper
                    @NonNull
                    @Override
                    public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                        layout.setEnableHeaderTranslationContent(false);
                        return RefreshHeaderHelper.getInstance().getRefreshHeader(mContext);
                    }
                });
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
