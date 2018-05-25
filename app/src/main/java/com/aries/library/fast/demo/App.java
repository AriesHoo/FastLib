package com.aries.library.fast.demo;

import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;

import com.aries.library.fast.FastConfig;
import com.aries.library.fast.entity.FastQuitConfigEntity;
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

        //主页返回键是否退回桌面(程序后台)
        boolean isBackTask = false;

        //全局配置参数
        AppImpl impl = new AppImpl(mContext);
        //全局主页面返回键操作设置--推荐先获取library里默认主页面点击返回键配置FastQuitConfigEntity配置再按需修改的模式 FastQuitConfigEntity
        FastQuitConfigEntity quitConfig = FastConfig.getInstance(mContext).getQuitConfig();
        FastConfig.getInstance(mContext)
                //设置Activity是否支持滑动返回-添加透明主题参考Demo样式;
                .setSwipeBackEnable(true, this)
                //设置Activity 点击返回键提示退出程序或返回桌面相关参数
                .setQuitConfig(quitConfig
                        //设置是否退回桌面否则直接退出程序
                        .setBackToTaskEnable(isBackTask)
                        //设置退回桌面是否有一次提示setBackToTaskEnable(true)才有意义
                        .setBackToTaskDelayEnable(isBackTask)
                        .setQuitDelay(2000)
                        .setQuitMessage(isBackTask ? getText(R.string.fast_back_home) : getText(R.string.fast_quit_app))
                        .setSnackBarBackgroundColor(Color.argb(220, 0, 0, 0))
                        .setSnackBarEnable(false)
                        .setSnackBarMessageColor(Color.WHITE))
                //设置Glide背景色
                .setPlaceholderColor(getResources().getColor(R.color.colorPlaceholder))
                //设置Glide圆角背景弧度
                .setPlaceholderRoundRadius(mContext.getResources().getDimension(R.dimen.dp_placeholder_radius))
                //设置Activity横竖屏模式
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                //设置Activity或Fragment根布局背景资源
                .setContentViewBackgroundResource(R.color.colorBackground)
                //设置Adapter加载更多视图--默认设置了FastLoadMoreView
                .setLoadMoreFoot(impl)
                //设置RecyclerView加载过程多布局属性
                .setMultiStatusView(impl)
                //设置全局网络请求等待Loading提示框如登录等待loading--观察者必须为FastLoadingObserver及其子类
                .setLoadingDialog(impl)
                //设置Retrofit全局异常处理-观察者必须为FastObserver及其子类
                .setHttpErrorControl(impl)
                //设置SmartRefreshLayout刷新头-自定加载使用BaseRecyclerViewAdapterHelper
                .setDefaultRefreshHeader(impl)
                .setTitleBarViewControl(impl)
                //设置虚拟导航栏控制
                .setNavigationBarControl(impl);
        LoggerManager.d(TAG, "total:" + (System.currentTimeMillis() - start));
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
