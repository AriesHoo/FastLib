package com.aries.library.fast.demo;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.retrofit.FastMultiUrl;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;


/**
 * Created: AriesHoo on 2017/7/20 14:11
 * Function: 基础配置Application
 * Desc:
 */
public class App extends Application {

    private static Context mContext;
    private String TAG = "FastLib";
    private static int imageHeight = 0;
    private long start;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Logger日志打印
        LoggerManager.init(TAG, BuildConfig.DEBUG);
        start = System.currentTimeMillis();
        LoggerManager.d(TAG, "start:" + start);
        mContext = this;
        //初始化toast工具
        ToastUtil.init(mContext, true, ToastUtil.newBuilder()
                        .setRadius(SizeUtil.dp2px(6))
//                .setPaddingLeft(SizeUtil.dp2px(24))
//                .setPaddingRight(SizeUtil.dp2px(24))
//                .setTextSize(SizeUtil.dp2px(16))
                        .setGravity(Gravity.BOTTOM)
        );
        //初始化Retrofit配置
        FastRetrofit.getInstance()
                //配置全局网络请求BaseUrl
                .setBaseUrl(BuildConfig.BASE_URL)
                //信任所有证书--也可设置setCertificates(单/双向验证)
                .trustAllSSL()
                //设置统一请求头
                //.setHeaders(header)
                //设置请求全局log-可设置tag及Level类型
                .setLogEnable(BuildConfig.DEBUG)
                //.setLogEnable(BuildConfig.DEBUG, TAG, HttpLoggingInterceptor.Level.BASIC)
                //设置统一超时--也可单独调用read/write/connect超时(可以设置时间单位TimeUnit)
                //默认20 s
                .setTimeout(20);

        //以下为配置多BaseUrl
        //step1
        FastMultiUrl.getInstance()
                .putBaseUrl("taobao", "http://www.taobao.com")
                .putBaseUrl("baidu", "http://www.baidu.com");
        //step2
        // 需要step1中baseUrl的方法需要在对应service里增加
        // @Headers({FastMultiUrl.BASE_URL_NAME_HEADER + "taobao"})
        //增加一个Header配置注意FastMultiUrl.BASE_URL_NAME_HEADER是必须后面"taobao"作为标记
        // 参考com.aries.library.fast.demo.retrofit.service.ApiService
        // FastMultiUrl里增加的拦截器才找得到对应的BaseUrl
        LoggerManager.d(TAG, "total:" + (System.currentTimeMillis() - start));
        //最简单模式-必须进行初始化
        FastManager.init(this);
        //以下为更丰富自定义方法
        //全局配置参数
        AppImpl impl = new AppImpl(mContext);
        FastManager.getInstance()
                //设置Adapter加载更多视图--默认设置了FastLoadMoreView
                .setLoadMoreFoot(impl)
                //设置RecyclerView加载过程多布局属性
                .setMultiStatusView(impl)
                //设置全局网络请求等待Loading提示框如登录等待loading--观察者必须为FastLoadingObserver及其子类
                .setLoadingDialog(impl)
                //设置SmartRefreshLayout刷新头-自定加载使用BaseRecyclerViewAdapterHelper
                .setDefaultRefreshHeader(impl)
                //设置全局TitleBarView相关配置
                .setTitleBarViewControl(impl)
                //设置虚拟导航栏控制
                .setNavigationBarControl(impl)
                //设置Activity滑动返回控制
                .setSwipeBackControl(impl)
                //设置Activity/Fragment相关配置(横竖屏+背景+生命周期)
                .setActivityFragmentControl(impl)
                //设置http请求结果全局控制
                .setHttpRequestControl(impl)
                //设置主页返回键控制
                .setQuitAppControl(impl);
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

    public static boolean isSupportElevation() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static Context getContext() {
        return mContext;
    }
}
