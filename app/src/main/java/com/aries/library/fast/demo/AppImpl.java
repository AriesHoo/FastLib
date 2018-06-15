package com.aries.library.fast.demo;

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
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.aries.library.fast.demo.helper.RefreshHeaderHelper;
import com.aries.library.fast.demo.module.SplashActivity;
import com.aries.library.fast.i.ActivityFragmentControl;
import com.aries.library.fast.i.HttpErrorControl;
import com.aries.library.fast.i.HttpRequestControl;
import com.aries.library.fast.i.IHttpRequestControl;
import com.aries.library.fast.i.IMultiStatusView;
import com.aries.library.fast.i.LoadMoreFoot;
import com.aries.library.fast.i.LoadingDialog;
import com.aries.library.fast.i.MultiStatusView;
import com.aries.library.fast.i.NavigationBarControl;
import com.aries.library.fast.i.OnHttpRequestListener;
import com.aries.library.fast.i.SwipeBackControl;
import com.aries.library.fast.i.TitleBarViewControl;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.retrofit.FastError;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.library.fast.widget.FastLoadDialog;
import com.aries.library.fast.widget.FastLoadMoreView;
import com.aries.library.fast.widget.FastMultiStatusView;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.util.RomUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.google.gson.Gson;
import com.marno.easystatelibrary.EasyStatusView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created: AriesHoo on 2017/11/30 11:43
 * E-Mail: AriesHoo@126.com
 * Function: 应用全局配置管理实现
 * Description:
 */
public class AppImpl implements DefaultRefreshHeaderCreater, LoadMoreFoot, MultiStatusView, LoadingDialog, HttpErrorControl,
        TitleBarViewControl, NavigationBarControl, SwipeBackControl, ActivityFragmentControl, HttpRequestControl {

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
        layout.setEnableHeaderTranslationContent(false);
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
        //第一种
//                        return new FastLoadDialog(activity);
        //第二种 使用UIProgressView里的四种模式Loading效果
        return new FastLoadDialog(activity)
                .setCanceledOnTouchOutside(false)
                .setMessage("请求数据中,请稍候...");
    }

    @Override
    public boolean createHttpErrorControl(int errorRes, int errorCode, @io.reactivex.annotations.NonNull Throwable e, Context context, Object... args) {
        LoggerManager.e(TAG, "args:" + args + ";context:" + context.getClass().getSimpleName());
        if (args != null) {//可以将具体调用界面部分视图传递到全局控制
            if (args.length >= 5) {
                if (args[1] instanceof SmartRefreshLayout) {
                    LoggerManager.e(TAG, "args:" + args[1]);
                    ((SmartRefreshLayout) args[1]).finishRefresh();
                }
                if (args[2] instanceof BaseQuickAdapter) {
                    LoggerManager.e(TAG, "args:" + args[2]);
                    ((BaseQuickAdapter) args[2]).loadMoreComplete();
                }
                if (args[3] instanceof EasyStatusView) {
                    LoggerManager.e(TAG, "args:" + args[3]);
                    ((EasyStatusView) args[3]).error();
                    if ((Integer) args[4] == 0) {
                        if (errorCode == FastError.EXCEPTION_ACCOUNTS) {

                        } else if (errorCode == FastError.EXCEPTION_JSON_SYNTAX) {

                        } else if (errorCode == FastError.EXCEPTION_OTHER_ERROR) {

                        } else if (errorCode == FastError.EXCEPTION_TIME_OUT) {

                        } else {
                            ((EasyStatusView) args[3]).noNet();
                        }
                    } else {
                        ToastUtil.show(errorRes);
                    }
                }
            }
        }
        //返回值true则FastObserver不会回调_onError所有逻辑处理都在全局位置处理
        return false;
    }

    /**
     * 控制全局TitleBarView
     *
     * @param titleBar
     * @param isActivity
     * @return
     */
    @Override
    public boolean createTitleBarViewControl(TitleBarView titleBar, boolean isActivity) {
        //默认的MD风格返回箭头icon如使用该风格可以不用设置
        Drawable mDrawable = FastUtil.getTintDrawable(mContext.getResources().getDrawable(R.drawable.fast_ic_back),
                mContext.getResources().getColor(R.color.colorTitleText));
        //是否支持状态栏白色
        boolean isSupport = StatusBarUtil.isSupportStatusBarFontChange();
        //设置TitleBarView 所有TextView颜色
        titleBar.setStatusBarLightMode(isSupport)
                //不支持黑字的设置白透明
                .setStatusAlpha(isSupport ? 0 : 102)
                .setLeftTextDrawable(isActivity ? mDrawable : null)
                .setDividerHeight(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? SizeUtil.dp2px(0.5f) : 0);
        ViewCompat.setElevation(titleBar, mContext.getResources().getDimension(R.dimen.dp_elevation));
        return false;
    }

    @NonNull
    @Override
    public NavigationViewHelper createNavigationBarControl(Activity activity, View bottomView) {
        NavigationViewHelper helper = NavigationViewHelper.with(activity)
                .setLogEnable(BuildConfig.DEBUG)
                //是否控制虚拟导航栏true 后续属性有效--第一优先级
                .setControlEnable(true)
                //是否全透明导航栏优先级第二--同步设置setNavigationViewColor故注意调用顺序
                //华为的半透明和全透明类似
                .setTransEnable(isTrans())
                //是否增加假的NavigationView用于沉浸至虚拟导航栏遮住
                .setPlusNavigationViewEnable(
                        activity.getClass() == SplashActivity.class ? false :
                                RomUtil.isEMUI() && isTrans())
                //设置是否控制底部输入框--默认属性
                .setControlBottomEditTextEnable(true)
                //设置最下边View用于增加paddingBottom--建议activity 根布局
                .setBottomView(bottomView)
                //影响setPlusNavigationViewEnable(true)单个条件
                //或者(setPlusNavigationViewEnable(false)&&setControlEnable(true))--两个前置条件
                //半透明默认设置102
                .setNavigationViewColor(Color.argb(isTrans() ? 0 : 102, 0, 0, 0))
                //setPlusNavigationViewEnable(true)才有效注意与setNavigationViewColor调用顺序
//                .setNavigationViewDrawable(mContext.getResources().getDrawable(R.drawable.img_bg_login))
                //setPlusNavigationViewEnable(true)有效
                .setNavigationLayoutColor(Color.WHITE);
        return helper;
    }

    protected boolean isTrans() {
        return RomUtil.isEMUI() && (RomUtil.getEMUIVersion().compareTo("EmotionUI_4.1") > 0);
    }


    @Override
    public void setSwipeBack(Activity activity, BGASwipeBackHelper swipeBackHelper) {
        swipeBackHelper.setSwipeBackEnable(true);
    }

    @Override
    public void setContentViewBackground(View contentView, boolean isFragment) {

    }

    @Override
    public void setRequestedOrientation(Activity activity) {
        //先判断xml没有设置屏幕模式避免将开发者本身想设置的覆盖掉
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

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

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

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
        return null;
    }

    @Override
    public void httpRequestSuccess(IHttpRequestControl httpRequestControl, List<?> list, OnHttpRequestListener listener) {
        if (httpRequestControl == null) return;
        SmartRefreshLayout smartRefreshLayout = httpRequestControl.getRefreshLayout();
        BaseQuickAdapter adapter = httpRequestControl.getRecyclerAdapter();
        EasyStatusView statusView = httpRequestControl.getStatusView();
        int page = httpRequestControl.getCurrentPage();
        int size = httpRequestControl.getPageSize();

        LoggerManager.i(TAG, "smartRefreshLayout:" + smartRefreshLayout + ";adapter:" + adapter + ";status:" + statusView + ";page:" + page + ";data:" + new Gson().toJson(list));
        smartRefreshLayout.finishRefresh();
        adapter.loadMoreComplete();
        if (list == null || list.size() == 0) {
            if (page == 0) {//第一页没有
                adapter.setNewData(new ArrayList());
                statusView.empty();
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
        statusView.content();
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

    }
}
