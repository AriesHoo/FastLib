package com.aries.library.fast.demo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.demo.constant.ApiConstant;
import com.aries.library.fast.demo.constant.SPConstant;
import com.aries.library.fast.demo.impl.ActivityControlImpl;
import com.aries.library.fast.demo.impl.AppImpl;
import com.aries.library.fast.demo.impl.HttpRequestControlImpl;
import com.aries.library.fast.demo.impl.SwipeBackControlImpl;
import com.aries.library.fast.demo.module.WebViewActivity;
import com.aries.library.fast.demo.module.main.MainActivity;
import com.aries.library.fast.demo.util.NotificationUtil;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.util.FastFormatUtil;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.SizeUtil;
import com.didichuxing.doraemonkit.DoraemonKit;
import com.didichuxing.doraemonkit.kit.webdoor.WebDoorManager;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @Author: AriesHoo on 2018/7/31 10:43
 * @E-Mail: AriesHoo@126.com
 * Function:基础配置Application
 * Description:
 */
public class App extends Application {

    private static Context mContext;
    private static String TAG = "FastLib";
    private static int imageHeight = 0;
    private long start;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Logger日志打印
        LoggerManager.init(TAG, BuildConfig.DEBUG,
                PrettyFormatStrategy.newBuilder()
                        .methodOffset(0)
                        .showThreadInfo(true)
                        .methodCount(3));
        start = System.currentTimeMillis();
        LoggerManager.i(TAG, "start:" + start);
        mContext = this;

        //最简单UI配置模式-必须进行初始化
        FastManager.init(this);
        //以下为更丰富自定义方法
        //全局UI配置参数-按需求设置
        AppImpl impl = new AppImpl(mContext);
        ActivityControlImpl activityControl = new ActivityControlImpl();
        FastManager.getInstance()
                //设置Adapter加载更多视图--默认设置了FastLoadMoreView
                .setLoadMoreFoot(impl)
                //全局设置RecyclerView
                .setFastRecyclerViewControl(impl)
                //设置RecyclerView加载过程多布局属性
                .setMultiStatusView(impl)
                //设置全局网络请求等待Loading提示框如登录等待loading--观察者必须为FastLoadingObserver及其子类
                .setLoadingDialog(impl)
                //设置SmartRefreshLayout刷新头-自定加载使用BaseRecyclerViewAdapterHelper
                .setDefaultRefreshHeader(impl)
                //设置全局TitleBarView相关配置
                .setTitleBarViewControl(impl)
                //设置Activity滑动返回控制-默认开启滑动返回功能不需要设置透明主题
                .setSwipeBackControl(new SwipeBackControlImpl())
                //设置Activity/Fragment相关配置(横竖屏+背景+虚拟导航栏+状态栏+生命周期)
                .setActivityFragmentControl(activityControl)
                //设置BasisActivity 子类按键监听
                .setActivityKeyEventControl(activityControl)
                //配置BasisActivity 子类事件派发相关
                .setActivityDispatchEventControl(activityControl)
                //设置http请求结果全局控制
                .setHttpRequestControl(new HttpRequestControlImpl())
                //设置主页返回键控制-默认效果为2000 毫秒时延退出程序
                .setQuitAppControl(impl)
                //设置ToastUtil全局控制
                .setToastControl(impl);

        //初始化Retrofit配置
        FastRetrofit.getInstance()
                //配置全局网络请求BaseUrl
                .setBaseUrl(BuildConfig.BASE_URL)
                //信任所有证书--也可设置setCertificates(单/双向验证)
                .setCertificates()
                //设置统一请求头
//                .addHeader(header)
//                .addHeader(key,value)
                //设置请求全局log-可设置tag及Level类型
                .setLogEnable(true)
//                .setLogEnable(BuildConfig.DEBUG, TAG, HttpLoggingInterceptor.Level.BODY)
                //设置统一超时--也可单独调用read/write/connect超时(可以设置时间单位TimeUnit)
                //默认20 s
                .setTimeout(30);

        //注意设置baseUrl要以/ 结尾 service 里的方法不要以/打头不然拦截到的url会有问题
        //以下为配置多BaseUrl--默认方式一优先级高 可通过FastRetrofit.getInstance().setHeaderPriorityEnable(true);设置方式二优先级
        //方式一 通过Service 里的method-(如:) 设置 推荐 使用该方式不需设置如方式二的额外Header
        FastRetrofit.getInstance()
                .putBaseUrl(ApiConstant.API_UPDATE_APP, BuildConfig.BASE__UPDATE_URL);

        //方式二 通过 Service 里添加特定header设置
        //step1
        FastRetrofit.getInstance()
                //设置Header模式优先-默认Method方式优先
                .setHeaderPriorityEnable(true)
                .putHeaderBaseUrl(ApiConstant.API_UPDATE_APP_KEY, BuildConfig.BASE__UPDATE_URL);
        //step2
        // 需要step1中baseUrl的方法需要在对应service里增加
        // @Headers({FastRetrofit.BASE_URL_NAME_HEADER + ApiConstant.API_UPDATE_APP_KEY})
        //增加一个Header配置注意FastRetrofit.BASE_URL_NAME_HEADER是必须为step1调用putHeaderBaseUrl方法设置的key
        // 参考com.aries.library.fast.demo.retrofit.service.ApiService#updateApp

        //初始化友盟统计
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(mContext, "5b349499b27b0a085f000052", "FastLib"));
        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext());
        //初始化通知栏控制
        NotificationUtil.getInstance().init(getApplicationContext());

        //设置用户下载App的初始渠道
        String appChannel = (String) SPUtil.get(getApplicationContext(), SPConstant.SP_KEY_APP_CHANNEL, "");
        LoggerManager.i(TAG, "appChannel0:" + appChannel + ";week:" + FastFormatUtil.formatWeek(System.currentTimeMillis()) + ";:" + Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        if (TextUtils.isEmpty(appChannel)) {
            appChannel = CrashReport.getAppChannel();
            Log.i(TAG, "appChannel1:" + appChannel);
            SPUtil.put(getApplicationContext(), SPConstant.SP_KEY_APP_CHANNEL, appChannel);
        } else {
            CrashReport.setAppChannel(getApplicationContext(), appChannel);
        }
        LoggerManager.i(TAG, "appChannel2:" + appChannel);
        LoggerManager.i(TAG, "total:" + (System.currentTimeMillis() - start));

        //初始化哆啦A梦
        DoraemonKit.install(this);
        // H5任意门功能需要，非必须
        DoraemonKit.setWebDoorCallback(new WebDoorManager.WebDoorCallback() {
            @Override
            public void overrideUrlLoading(Context context, String s) {
                // 使用自己的H5容器打开这个链接
                LoggerManager.i("overrideUrlLoading", "url:" + s);
                WebViewActivity.start(FastStackUtil.getInstance().getCurrent(),s);
            }
        });
        setShortcut();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    private void setShortcut() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = mContext.getSystemService(ShortcutManager.class);
            List<ShortcutInfo> list = new ArrayList<>();
            ShortcutInfo shortGit;
            ShortcutInfo shortBlog;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.jianshu.com/u/a229eee96115"));
            intent.setClassName(getPackageName(), MainActivity.class.getName());
            intent.putExtra("url", "https://www.jianshu.com/u/a229eee96115");

            Intent intentGit = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/AriesHoo"));
            intentGit.setClassName(getPackageName(), MainActivity.class.getName());
            intentGit.putExtra("url", "https://github.com/AriesHoo");


            shortGit = new ShortcutInfo.Builder(this, "github")
                    .setShortLabel("GitHub")
                    .setLongLabel("GitHub-AriesHoo")
                    .setIcon(Icon.createWithResource(mContext, R.drawable.ic_github))
                    .setIntent(intentGit)
                    .build();
            shortBlog = new ShortcutInfo.Builder(this, "jianshu")
                    .setShortLabel("简书")
                    .setLongLabel("简书-AriesHoo")
                    .setIcon(Icon.createWithResource(mContext, R.drawable.ic_book))
                    .setIntent(intent)
                    .build();
            list.add(shortGit);
            list.add(shortBlog);
            shortcutManager.setDynamicShortcuts(list);
        }
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

    /**
     * 是否控制底部导航栏---目前发现小米8上检查是否有导航栏出现问题
     *
     * @return
     */
    public static boolean isControlNavigation() {
        LoggerManager.i(TAG, "mode:" + Build.MODEL);
        return true;
    }

    public static Context getContext() {
        return mContext;
    }
}
