package com.aries.library.fast.demo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aries.library.fast.FastConfig;
import com.aries.library.fast.demo.helper.RefreshHeaderHelper;
import com.aries.library.fast.entity.FastQuitConfigEntity;
import com.aries.library.fast.entity.FastTitleConfigEntity;
import com.aries.library.fast.i.IMultiStatusView;
import com.aries.library.fast.i.LoadMoreFoot;
import com.aries.library.fast.i.LoadingDialog;
import com.aries.library.fast.i.MultiStatusView;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.library.fast.widget.FastLoadDialog;
import com.aries.library.fast.widget.FastLoadMoreView;
import com.aries.library.fast.widget.FastMultiStatusView;
import com.aries.ui.widget.progress.UIProgressView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;


/**
 * Created: AriesHoo on 2017/7/20 14:11
 * Function: 基础配置Application
 * Desc:
 */
public class App extends Application {

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

        //主页返回键是否退回桌面(程序后台)
        boolean isBackTask = false;
        //全局配置参数
        //全局标题栏TitleBarView设置--推荐先获取library里默认标题栏TitleBarView配置再按需修改的模式 FastTitleConfigEntity
        FastTitleConfigEntity titleConfig = FastConfig.getInstance(mContext).getTitleConfig();
        //全局主页面返回键操作设置--推荐先获取library里默认主页面点击返回键配置FastQuitConfigEntity配置再按需修改的模式 FastQuitConfigEntity
        FastQuitConfigEntity quitConfig = FastConfig.getInstance(mContext).getQuitConfig();
        FastConfig.getInstance(mContext)
                // 设置全局TitleBarView-其它属性请查看getInstance默认设置
                .setTitleConfig(titleConfig
                        //设置TitleBarView 所有TextView颜色
                        .setTitleTextColor(getResources().getColor(R.color.colorTitleText))
                        //设置TitleBarView背景资源
                        .setTitleBackgroundResource(R.color.colorTitleBackground)
                        //设置是否状态栏浅色模式(深色状态栏文字及图标)
                        .setLightStatusBarEnable(true)
                        .setDividerHeight(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? SizeUtil.dp2px(0.5f) : 0)
                        //设置TitleBarView海拔高度
                        .setTitleElevation(mContext.getResources().getDimension(R.dimen.dp_elevation)))
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
                //设置Activity是否支持滑动返回-添加透明主题参考Demo样式;
                .setSwipeBackEnable(true, this)
                //设置Activity横竖屏模式
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                //设置Activity或Fragment根布局背景资源
//                .setContentViewBackgroundResource(R.color.colorBackground)
                //设置Adapter加载更多视图--默认设置了FastLoadMoreView
                .setLoadMoreFoot(new LoadMoreFoot() {
                    @Nullable
                    @Override
                    public LoadMoreView createDefaultLoadMoreView(BaseQuickAdapter adapter) {
                        if (adapter != null) {
                            //设置动画是否一直开启
                            adapter.isFirstOnly(false);
                            //设置动画
                            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                        }
                        //方式一:设置FastLoadMoreView--可参考FastLoadMoreView.Builder相应set方法
                        //默认配置请参考FastLoadMoreView.Builder(mContext)里初始化
                        return new FastLoadMoreView.Builder(mContext)
//                                //设置Loading 颜色-5.0以上有效
//                                .setLoadingProgressColor(Color.MAGENTA)
//                                //设置Loading drawable--会使Loading颜色失效
//                                .setLoadingProgressDrawable(mContext.getResources().getDrawable(R.drawable.dialog_loading_wei_bo))
//                                //设置全局TextView颜色
//                                .setLoadTextColor(Color.MAGENTA)
//                                //设置全局TextView文字字号
//                                .setLoadTextSize(SizeUtil.dp2px(14))
//                                .setLoadingText("努力加载中...")
//                                .setLoadingTextColor(Color.GREEN)
//                                .setLoadingTextSize(SizeUtil.dp2px(14))
//                                .setLoadEndText("我是有底线的")
//                                .setLoadEndTextColor(Color.GREEN)
//                                .setLoadEndTextSize(SizeUtil.dp2px(14))
//                                .setLoadFailText("哇哦!出错了")
//                                .setLoadFailTextColor(Color.RED)
//                                .setLoadFailTextSize(SizeUtil.dp2px(14))
                                .build();
                        //方式二:使用adapter自带--其实我默认设置的和这个基本一致只是提供了相应设置方法
//                        return new SimpleLoadMoreView();
                        //方式三:参考SimpleLoadMoreView或FastLoadMoreView完全自定义自己的LoadMoreView
//                        return MyLoadMoreView();
                    }
                })
                //设置RecyclerView加载过程多布局属性
                .setMultiStatusView(new MultiStatusView() {
                    @NonNull
                    @Override
                    public IMultiStatusView createMultiStatusView() {
                        //根据具体情况可设置更多属性具体请参考FastMultiStatusView.Builder里set方法
                        //默认设置请参考Builder(Context context)里初始化
                        return new FastMultiStatusView.Builder(mContext)
//                                .setTextColor(getResources().getColor(R.color.colorMultiText))
//                                .setTextSize(getResources().getDimensionPixelSize(R.dimen.dp_multi_text_size))
//                                .setLoadingProgressColor(getResources().getColor(R.color.colorMultiProgress))
//                                .setLoadingTextColor(getResources().getColor(R.color.colorMultiProgress))
//                                .setLoadingText(getText(R.string.fast_multi_loading))
//                                .setEmptyText(getText(R.string.fast_multi_empty))
//                                .setErrorText(getText(R.string.fast_multi_error))
//                                .setNoNetText(getText(R.string.fast_multi_network))
//                                .setTextMargin(getResources().getDimensionPixelSize(R.dimen.dp_multi_margin))
//                                .setImageWidthHeight(getResources().getDimensionPixelSize(R.dimen.dp_multi_image_size))
//                                .setEmptyImageDrawable(FastUtil.getTintDrawable(
//                                        getResources().getDrawable(R.drawable.fast_img_multi_empty), getResources().getColor(R.color.colorMultiText)))
//                                .setErrorImageDrawable(FastUtil.getTintDrawable(
//                                        getResources().getDrawable(R.drawable.fast_img_multi_error), getResources().getColor(R.color.colorMultiText)))
//                                .setNoNetImageDrawable(FastUtil.getTintDrawable(
//                                        getResources().getDrawable(R.drawable.fast_img_multi_network), getResources().getColor(R.color.colorMultiText)))
                                .build();
                    }
                })
                //设置全局网络请求等待Loading提示框如登录等待loading
                .setLoadingDialog(new LoadingDialog() {
                    @Nullable
                    @Override
                    public FastLoadDialog createLoadingDialog(@Nullable Activity activity) {
                        //第一种
//                        return new FastLoadDialog(activity);
                        //第二种 使用UIProgressView里的四种模式Loading效果
                        return new FastLoadDialog(activity, UIProgressView.STYLE_WEI_BO)
                                .setMessage("请求数据中,请稍候...");
//                        ProgressDialog progressDialog = new ProgressDialog(activity);
//                        progressDialog.setMessage("加载中...");
//                        //第三种--系统ProgressDialog不过系统已标记为过时类不建议使用
//                        return new FastLoadDialog(activity, progressDialog);
//                        第四种--完全自定义Dialog形式
//                        return new FastLoadDialog(activity, MyDialog);
                    }
                })
                //设置SmartRefreshLayout刷新头-自定加载使用BaseRecyclerViewAdapterHelper
                .setDefaultRefreshHeader(new DefaultRefreshHeaderCreater() {
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

    public static boolean isSupportElevation() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static Context getContext() {
        return mContext;
    }
}
