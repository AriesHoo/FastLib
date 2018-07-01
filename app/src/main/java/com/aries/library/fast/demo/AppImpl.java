package com.aries.library.fast.demo;

import android.accounts.AccountsException;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aries.library.fast.demo.helper.RefreshHeaderHelper;
import com.aries.library.fast.demo.module.SplashActivity;
import com.aries.library.fast.i.ActivityFragmentControl;
import com.aries.library.fast.i.HttpRequestControl;
import com.aries.library.fast.i.IHttpRequestControl;
import com.aries.library.fast.i.IMultiStatusView;
import com.aries.library.fast.i.LoadMoreFoot;
import com.aries.library.fast.i.LoadingDialog;
import com.aries.library.fast.i.MultiStatusView;
import com.aries.library.fast.i.OnHttpRequestListener;
import com.aries.library.fast.i.QuitAppControl;
import com.aries.library.fast.i.SwipeBackControl;
import com.aries.library.fast.i.TitleBarViewControl;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.module.activity.FastMainActivity;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.NetworkUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.library.fast.widget.FastLoadDialog;
import com.aries.library.fast.widget.FastLoadMoreView;
import com.aries.library.fast.widget.FastMultiStatusView;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.helper.status.StatusViewHelper;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.util.RomUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.progress.UIProgressDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.flyco.tablayout.CommonTabLayout;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.luck.picture.lib.PictureBaseActivity;
import com.luck.picture.lib.PicturePreviewActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.umeng.analytics.MobclickAgent;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;
import retrofit2.HttpException;

/**
 * Created: AriesHoo on 2017/11/30 11:43
 * E-Mail: AriesHoo@126.com
 * Function: 应用全局配置管理实现
 * Description:
 * 1、新增友盟统计功能对接
 */
public class AppImpl implements DefaultRefreshHeaderCreator, LoadMoreFoot, MultiStatusView, LoadingDialog,
        TitleBarViewControl, SwipeBackControl, ActivityFragmentControl, HttpRequestControl, QuitAppControl {

    private Context mContext;
    private String TAG = this.getClass().getSimpleName();

    public AppImpl(@Nullable Context context) {
        this.mContext = context;
    }

    /**
     * 下拉刷新头配置
     *
     * @param context
     * @param layout
     * @return
     */
    @NonNull
    @Override
    public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
        layout.setEnableHeaderTranslationContent(false)
                .setEnableOverScrollDrag(false);
        return RefreshHeaderHelper.getInstance().getRefreshHeader(mContext);
    }

    /**
     * Adapter加载更多配置
     *
     * @param adapter
     * @return
     */
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
                .setLoadingTextFakeBold(true)
                .setLoadingSize(SizeUtil.dp2px(20))
//                                .setLoadTextColor(Color.MAGENTA)
//                                //设置Loading 颜色-5.0以上有效
//                                .setLoadingProgressColor(Color.MAGENTA)
//                                //设置Loading drawable--会使Loading颜色失效
//                                .setLoadingProgressDrawable(R.drawable.dialog_loading_wei_bo)
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

    /**
     * 多状态布局配置
     *
     * @return
     */
    @NonNull
    @Override
    public IMultiStatusView createMultiStatusView() {
        //根据具体情况可设置更多属性具体请参考FastMultiStatusView.Builder里set方法
        //默认设置请参考Builder(Context context)里初始化
        return new FastMultiStatusView.Builder(mContext)
                .setLoadingTextFakeBold(false)
//                                .setTextColor(Color.MAGENTA)
//                                .setTextColorResource(R.color.colorMultiText)
//                                .setTextSizeResource(R.dimen.dp_multi_text_size)
//                                .setTextSize(getResources().getDimensionPixelSize(R.dimen.dp_multi_text_size))
//                                .setLoadingProgressColorResource(R.color.colorMultiProgress)
//                                .setLoadingProgressColor(getResources().getColor(R.color.colorMultiProgress))
//                                .setLoadingTextColor(getResources().getColor(R.color.colorMultiProgress))
//                                .setLoadingText(R.string.fast_multi_loading)
//                                .setEmptyText(R.string.fast_multi_empty)
//                                .setErrorText(R.string.fast_multi_error)
//                                .setNoNetText(R.string.fast_multi_network)
//                                .setTextMarginResource(R.dimen.dp_multi_margin)
//                                .setImageWidthHeightResource(R.dimen.dp_multi_image_size)
//                                .setEmptyImageColorResource(R.color.colorTitleText)
//                                .setEmptyImageDrawable(R.drawable.fast_img_multi_empty)
//                                .setErrorImageColorResource(R.color.colorTitleText)
//                                .setErrorImageDrawable(R.drawable.fast_img_multi_error)
//                                .setNoNetImageColorResource(R.color.colorTitleText)
//                                .setNoNetImageDrawable(R.drawable.fast_img_multi_network)
                .build();
    }

    @Nullable
    @Override
    public FastLoadDialog createLoadingDialog(@Nullable Activity activity) {
        return new FastLoadDialog(activity,
                new UIProgressDialog.WeBoBuilder(activity)
                        .setMessage("加载中")
                        .create())
                .setCanceledOnTouchOutside(false)
                .setMessage("请求数据中,请稍候...");
    }

    /**
     * 控制全局TitleBarView
     *
     * @param titleBar
     * @return
     */
    @Override
    public boolean createTitleBarViewControl(TitleBarView titleBar, Class<?> cls) {
        //默认的MD风格返回箭头icon如使用该风格可以不用设置
        Drawable mDrawable = FastUtil.getTintDrawable(mContext.getResources().getDrawable(R.drawable.fast_ic_back),
                mContext.getResources().getColor(R.color.colorTitleText));
        //是否支持状态栏白色
        boolean isSupport = StatusBarUtil.isSupportStatusBarFontChange();
        boolean isActivity = Activity.class.isAssignableFrom(cls);
        Activity activity = FastStackUtil.getInstance().getActivity(cls);
        //设置TitleBarView 所有TextView颜色
        titleBar.setStatusBarLightMode(isSupport)
                //不支持黑字的设置白透明
                .setStatusAlpha(isSupport ? 0 : 102)
                .setLeftTextDrawable(isActivity ? mDrawable : null)
                .setDividerHeight(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? SizeUtil.dp2px(0.5f) : 0);
        if (activity != null) titleBar.setTitleMainText(activity.getTitle());
        ViewCompat.setElevation(titleBar, mContext.getResources().getDimension(R.dimen.dp_elevation));
        return false;
    }

    /**
     * 设置Activity 全局滑动属性--包括三方库
     *
     * @param activity
     * @param swipeBackHelper BGASwipeBackHelper 控制详见{@link com.aries.library.fast.FastManager}
     */
    @Override
    public void setSwipeBack(Activity activity, BGASwipeBackHelper swipeBackHelper) {
        //以下为默认设置
        //需设置activity window背景为透明避免滑动过程中漏出背景也可减少背景层级降低过度绘制
        activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        swipeBackHelper.setSwipeBackEnable(true)
                .setShadowResId(R.color.colorBackground)
                .setIsNavigationBarOverlap(true);//底部导航条是否悬浮在内容上设置过NavigationViewHelper可以不用设置该属性
    }

    @Override
    public void onSwipeBackLayoutSlide(Activity activity, float slideOffset) {

    }

    @Override
    public void onSwipeBackLayoutCancel(Activity activity) {

    }

    @Override
    public void onSwipeBackLayoutExecuted(Activity activity) {

    }

    /**
     * 设置Fragment/Activity根布局背景
     *
     * @param contentView
     * @param cls
     */
    @Override
    public void setContentViewBackground(View contentView, Class<?> cls) {
        //避免背景色重复
        if (!Fragment.class.isAssignableFrom(cls) &&
                !android.app.Fragment.class.isAssignableFrom(cls)
                && contentView.getBackground() == null) {
            contentView.setBackgroundResource(R.color.colorBackground);
        }
    }

    /**
     * 设置屏幕方向--注意targetSDK设置27以上不能设置windowIsTranslucent=true属性不然应用直接崩溃
     * 错误为 Only fullscreen activities can request orientation
     * 默认自动 ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
     * 竖屏 ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
     * 横屏 ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
     * {@link ActivityInfo#screenOrientation ActivityInfo.screenOrientation}
     *
     * @param activity
     */
    @Override
    public void setRequestedOrientation(Activity activity) {
        //全局控制屏幕横竖屏
        //先判断xml没有设置屏幕模式避免将开发者本身想设置的覆盖掉
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 设置非FastLib且未实现Activity 状态栏功能的三方Activity 状态栏沉浸
     *
     * @param activity
     * @param helper
     * @return
     */
    @Override
    public boolean setStatusBar(Activity activity, StatusViewHelper helper, View topView) {
        boolean isSupportStatusBarFont = StatusBarUtil.isSupportStatusBarFontChange();
        StatusBarUtil.setStatusBarLightMode(activity);
        helper.setTransEnable(isSupportStatusBarFont)
                .setPlusStatusViewEnable(true)
                .setStatusLayoutColor(Color.WHITE);
        setStatusBarActivity(activity);
        return true;
    }

    /**
     * {@link com.aries.library.fast.FastLifecycleCallbacks#onActivityCreated(Activity, Bundle)}
     *
     * @param activity
     * @param helper
     */
    @Override
    public boolean setNavigationBar(Activity activity, NavigationViewHelper helper, View bottomView) {
        //其它默认属性请参考FastLifecycleCallbacks
        helper.setLogEnable(BuildConfig.DEBUG)
                .setTransEnable(isTrans(activity))
                .setPlusNavigationViewEnable(isTrans(activity))
                .setBottomView(FastMainActivity.class.isAssignableFrom(activity.getClass()) ? FindViewUtil.getTargetView(bottomView, CommonTabLayout.class) :
                        PicturePreviewActivity.class.isAssignableFrom(activity.getClass()) ? FindViewUtil.getTargetView(bottomView, R.id.select_bar_layout) : bottomView)
                .setNavigationViewColor(Color.argb(isTrans(activity) ? 0 : 102, 0, 0, 0))
                .setNavigationLayoutColor(Color.WHITE);
        return true;
    }

    /**
     * Activity 生命周期监听--可用于三方统计页面数据
     *
     * @return
     */
    @Override
    public Application.ActivityLifecycleCallbacks getActivityLifecycleCallbacks() {
        return new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (activity instanceof FragmentActivity) {
                    FragmentManager manager = ((FragmentActivity) activity).getSupportFragmentManager();
                    List<Fragment> list = manager.getFragments();
                    if (list == null || list.size() == 0)//有Fragment的FragmentActivity不需调用以下方法避免统计不准
                        MobclickAgent.onPageStart(activity.getClass().getSimpleName());
                } else {
                    MobclickAgent.onPageStart(activity.getClass().getSimpleName());
                }
                MobclickAgent.onResume(activity); //统计时长
            }

            @Override
            public void onActivityPaused(Activity activity) {
                //普通Activity直接onPageEnd
                if (activity instanceof FragmentActivity) {
                    FragmentManager manager = ((FragmentActivity) activity).getSupportFragmentManager();
                    List<Fragment> list = manager.getFragments();
                    if (list == null || list.size() == 0)//有Fragment的FragmentActivity不需调用以下方法避免统计不准
                        MobclickAgent.onPageEnd(activity.getClass().getSimpleName());
                } else {
                    MobclickAgent.onPageEnd(activity.getClass().getSimpleName());
                }
                MobclickAgent.onPause(activity); //统计时长
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (activity.isFinishing())
                    activity.overridePendingTransition(com.aries.library.fast.R.anim.bga_sbl_activity_swipeback_enter, com.aries.library.fast.R.anim.bga_sbl_activity_swipeback_exit);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        };
    }

    @Override
    public FragmentManager.FragmentLifecycleCallbacks getFragmentLifecycleCallbacks() {
        return new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentResumed(FragmentManager fm, Fragment f) {
                super.onFragmentResumed(fm, f);
                LoggerManager.i(TAG, "onFragmentResumed:统计Fragment:" + f.getClass().getSimpleName());
                MobclickAgent.onPageStart(f.getClass().getSimpleName());
            }

            @Override
            public void onFragmentPaused(FragmentManager fm, Fragment f) {
                super.onFragmentPaused(fm, f);
                LoggerManager.i(TAG, "onFragmentPaused:统计Fragment:" + f.getClass().getSimpleName());
                MobclickAgent.onPageEnd(f.getClass().getSimpleName());
            }
        };
    }

    @Override
    public void httpRequestSuccess(IHttpRequestControl httpRequestControl, List<? extends Object> list, OnHttpRequestListener listener) {
        if (httpRequestControl == null) return;
        SmartRefreshLayout smartRefreshLayout = httpRequestControl.getRefreshLayout();
        BaseQuickAdapter adapter = httpRequestControl.getRecyclerAdapter();
        StatusLayoutManager statusLayoutManager = httpRequestControl.getStatusLayoutManager();
        int page = httpRequestControl.getCurrentPage();
        int size = httpRequestControl.getPageSize();

        LoggerManager.i(TAG, "smartRefreshLayout:" + smartRefreshLayout + ";adapter:" + adapter + ";status:" + ";page:" + page +";class:"+httpRequestControl.getRequestClass());
        if (smartRefreshLayout != null)
            smartRefreshLayout.finishRefresh();
        if (adapter == null) return;
        adapter.loadMoreComplete();
        if (list == null || list.size() == 0) {
            if (page == 0) {//第一页没有
                adapter.setNewData(new ArrayList());
                statusLayoutManager.showEmptyLayout();
                if (listener != null) {
                    listener.onEmpty();
                }
            } else {
                adapter.loadMoreEnd();
                if (listener != null) {
                    listener.onNoMore();
                }
            }
            return;
        }
        statusLayoutManager.showSuccessLayout();
        if (smartRefreshLayout.isRefreshing() || page == 0) {
            adapter.setNewData(new ArrayList());
        }
        adapter.addData(list);
        if (listener != null) {
            listener.onNext();
        }
        if (list.size() < size) {
            adapter.loadMoreEnd();
            if (listener != null) {
                listener.onNoMore();
            }
        }
    }

    @Override
    public void httpRequestError(IHttpRequestControl httpRequestControl, Throwable e) {
        int reason = R.string.fast_exception_other_error;
//        int code = FastError.EXCEPTION_OTHER_ERROR;
        if (!NetworkUtil.isConnected(mContext)) {
            reason = R.string.fast_exception_network_not_connected;
        } else {
            if (e instanceof NetworkErrorException) {//网络异常--继承于AccountsException
                reason = R.string.fast_exception_network_error;
            } else if (e instanceof AccountsException) {//账户异常
                reason = R.string.fast_exception_accounts;
            } else if (e instanceof ConnectException) {//连接异常--继承于SocketException
                reason = R.string.fast_exception_connect;
            } else if (e instanceof SocketException) {//socket异常
                reason = R.string.fast_exception_socket;
            } else if (e instanceof HttpException) {// http异常
                reason = R.string.fast_exception_http;
            } else if (e instanceof UnknownHostException) {//DNS错误
                reason = R.string.fast_exception_unknown_host;
            } else if (e instanceof JsonSyntaxException
                    || e instanceof JsonIOException
                    || e instanceof JsonParseException) {//数据格式化错误
                reason = R.string.fast_exception_json_syntax;
            } else if (e instanceof SocketTimeoutException || e instanceof TimeoutException) {
                reason = R.string.fast_exception_time_out;
            } else if (e instanceof ClassCastException) {
                reason = R.string.fast_exception_class_cast;
            }
        }
        if (httpRequestControl == null || httpRequestControl.getStatusLayoutManager() == null) {
            ToastUtil.show(reason);
            return;
        }
        SmartRefreshLayout smartRefreshLayout = httpRequestControl.getRefreshLayout();
        BaseQuickAdapter adapter = httpRequestControl.getRecyclerAdapter();
        StatusLayoutManager statusLayoutManager = httpRequestControl.getStatusLayoutManager();
        int page = httpRequestControl.getCurrentPage();
//        int size = httpRequestControl.getPageSize();
        if (smartRefreshLayout != null)
            smartRefreshLayout.finishRefresh(false);
        if (adapter != null)
            adapter.loadMoreComplete();
        if (statusLayoutManager == null) return;
        if (page == 0) {//初始页
            if (!NetworkUtil.isConnected(mContext)) {
                statusLayoutManager.showCustomLayout(R.layout.fast_layout_multi_network);
            } else {
                statusLayoutManager.showErrorLayout();
            }
            return;
        }
        //可根据不同错误展示不同错误布局 statusLayoutManager.showCustomLayout(R.layout.xxx);
        statusLayoutManager.showErrorLayout();
    }

    /**
     * @param isFirst  是否首次提示
     * @param activity 操作的Activity
     * @return 延迟间隔--如不需要设置两次提示可设置0--最佳方式是直接在回调中执行你想要的操作
     */
    @Override
    public long quipApp(boolean isFirst, Activity activity) {
        //默认配置
        if (isFirst) {
            ToastUtil.show(com.aries.library.fast.R.string.fast_quit_app);
        } else {
            FastStackUtil.getInstance().exit();
        }
//        if (isFirst) {
//            ToastUtil.show(R.string.fast_back_home);
//        } else {
//            activity.moveTaskToBack(true);
//        }
        return 2000;
    }

    /**
     * 根据程序使用的三方库进行改造:本示例使用的三方库实现了自己的沉浸式状态栏及导航栏但和Demo的滑动返回不搭配故做相应调整
     *
     * @param activity
     */
    private void setStatusBarActivity(Activity activity) {
        if (PictureBaseActivity.class.isAssignableFrom(activity.getClass())) {
            View contentView = FastUtil.getRootView(activity);
            //该属性会影响适配滑动返回效果
            contentView.setFitsSystemWindows(false);
            ImageView imageView = contentView != null ? contentView.findViewById(R.id.picture_left_back) : null;
            if (imageView != null) {
                RelativeLayout layout = contentView.findViewById(R.id.rl_picture_title);
                if (layout != null) {
                    ViewCompat.setElevation(layout, activity.getResources().getDimension(R.dimen.dp_elevation));
                }
//                layout.setBackgroundColor(Color.MAGENTA);
                //调整返回箭头大小
                imageView.setPadding(SizeUtil.dp2px(15), SizeUtil.dp2px(4), SizeUtil.dp2px(4), SizeUtil.dp2px(4));
//                imageView.setBackgroundColor(Color.GREEN);
            }
        }
    }

    /**
     * 是否全透明-华为4.1以上可根据导航栏位置颜色设置导航图标颜色
     *
     * @return
     */
    protected boolean isTrans(Activity activity) {
        return RomUtil.isEMUI() && (RomUtil.getEMUIVersion().compareTo("EmotionUI_4.1") > 0) && activity.getClass() != SplashActivity.class;
    }

}
