package com.aries.library.fast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;

import com.aries.library.fast.basis.BasisActivity;
import com.aries.library.fast.basis.BasisFragment;
import com.aries.library.fast.delegate.FastRefreshLoadDelegate;
import com.aries.library.fast.delegate.FastTitleDelegate;
import com.aries.library.fast.entity.FastQuitConfigEntity;
import com.aries.library.fast.entity.FastTitleConfigEntity;
import com.aries.library.fast.i.HttpErrorControl;
import com.aries.library.fast.i.IFastTitleView;
import com.aries.library.fast.i.IMultiStatusView;
import com.aries.library.fast.i.LoadMoreFoot;
import com.aries.library.fast.i.LoadingDialog;
import com.aries.library.fast.i.MultiStatusView;
import com.aries.library.fast.i.NavigationBarControl;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.widget.FastLoadDialog;
import com.aries.library.fast.widget.FastLoadMoreView;
import com.aries.library.fast.widget.FastMultiStatusView;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.Arrays;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * Created: AriesHoo on 2017/10/25 10:27
 * E-Mail: AriesHoo@126.com
 * Function: 全局参数配置--可在Application处设置Library全局属性
 * Description:
 * 1、2018-2-24 18:55:26新增setSwipeBackEnable 全局控制Activity堆栈及滑动返回
 */
public class FastConfig {

    private static volatile FastConfig sInstance;
    private static Context mContext;
    private Application mApplication;

    public static FastConfig getInstance(@Nullable Context context) {
        if (sInstance == null) {
            synchronized (FastConfig.class) {
                if (sInstance == null) {
                    sInstance = new FastConfig(context);
                }
            }
        }
        return sInstance;
    }

    private FastConfig(@Nullable Context context) {
        if (context == null) {
            throw new NullPointerException(FastConstant.EXCEPTION_FAST_CONFIG_CONTEXT_NOT_NULL);
        }
        if (context != null) {
            this.mContext = context.getApplicationContext();
            if (FastUtil.isClassExist("com.aries.ui.view.title.TitleBarView")) {
                setTitleConfig(new FastTitleConfigEntity()
                        .setTitleBackgroundResource(R.color.colorTitleBackground)
                        .setLeftTextDrawable(R.drawable.fast_ic_back)
                        .setLeftTextFinishEnable(true)
                        .setCenterLayoutPadding(SizeUtil.dp2px(2))
                        .setCenterGravityLeftPadding(SizeUtil.dp2px(24))
                        .setCenterGravityLeft(false)
                        .setTitleTextColor(mContext.getResources().getColor(R.color.colorTitleText))
                        .setTitleMainTextSize(SizeUtil.dp2px(18))
                        .setTitleMainTextColor(mContext.getResources().getColor(R.color.colorTitleText))
                        .setTitleMainTextFakeBold(false)
                        .setTitleMainTextMarquee(false)
                        .setTitleSubTextSize(SizeUtil.dp2px(12))
                        .setTitleSubTextColor(mContext.getResources().getColor(R.color.colorTitleText))
                        .setTitleSubTextFakeBold(false)
                        .setLeftTextSize(SizeUtil.dp2px(14))
                        .setLeftTextColor(mContext.getResources().getColor(R.color.colorTitleText))
                        .setRightTextSize(SizeUtil.dp2px(14))
                        .setRightTextColor(mContext.getResources().getColor(R.color.colorTitleText))
                        .setActionTextSize(SizeUtil.dp2px(14))
                        .setActionTextColor(mContext.getResources().getColor(R.color.colorTitleText))
                        .setStatusAlpha(TitleBarView.DEFAULT_STATUS_BAR_ALPHA)
                        .setStatusAlwaysEnable(false)
                        .setTitleHeight(mContext.getResources().getDimensionPixelSize(R.dimen.dp_title_height))
                        .setTitleElevation(0)
                        .setLightStatusBarEnable(false)
                        .setOutPadding(SizeUtil.dp2px(12))
                        .setActionPadding(SizeUtil.dp2px(1))
                        .setDividerColor(mContext.getResources().getColor(R.color.colorTitleDivider))
                        .setDividerHeight(SizeUtil.dp2px(0.5f))
                );
            }
            if (FastUtil.isClassExist("com.scwang.smartrefresh.layout.SmartRefreshLayout")) {
                setDefaultRefreshHeader(new DefaultRefreshHeaderCreater() {
                    @NonNull
                    @Override
                    public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                        return new ClassicsHeader(mContext).setSpinnerStyle(SpinnerStyle.Translate);
                    }
                });
            }
            if (FastUtil.isClassExist("com.chad.library.adapter.base.BaseQuickAdapter")) {
                setLoadMoreFoot(new LoadMoreFoot() {
                    @Override
                    public LoadMoreView createDefaultLoadMoreView(BaseQuickAdapter adapter) {
                        return new FastLoadMoreView(mContext).getBuilder().build();
                    }
                });
            }
            if (FastUtil.isClassExist("com.marno.easystatelibrary.EasyStatusView")) {
                setMultiStatusView(new MultiStatusView() {
                    @NonNull
                    @Override
                    public IMultiStatusView createMultiStatusView() {
                        return new FastMultiStatusView(mContext).getBuilder().build();
                    }
                });
            }
            if (mContext instanceof Application) {
                setSwipeBackEnable(false, (Application) mContext);
            }
            setQuitConfig(new FastQuitConfigEntity()
                    .setQuitDelay(2000)
                    .setQuitMessage(getText(R.string.fast_quit_app))
                    .setBackToTaskEnable(false)
                    .setSnackBarBackgroundColor(Color.argb(210, 0, 0, 0))
                    .setSnackBarEnable(false)
                    .setSnackBarMessageColor(Color.WHITE));
            setLoadingDialog(new LoadingDialog() {
                @Nullable
                @Override
                public FastLoadDialog createLoadingDialog(@Nullable Activity activity) {
                    return new FastLoadDialog(activity)
                            .setMessage(getText(R.string.fast_loading));
                }
            });
            setHttpErrorControl(new HttpErrorControl() {
                @Override
                public boolean createHttpErrorControl(int errorRes, int errorCode, @NonNull Throwable e, Context con, Object[] args) {
                    return false;
                }
            });
            setNavigationBarControl(new NavigationBarControl() {
                @NonNull
                @Override
                public NavigationViewHelper createNavigationBarControl(Activity activity, View bottomView) {
                    return NavigationViewHelper.with(activity)
                            .setControlEnable(true)
                            .setTransEnable(false)
                            .setPlusNavigationViewEnable(false)
                            .setBottomView(bottomView);
                }
            });
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            setContentViewBackgroundResource(-1);
            setPlaceholderColor(getColor(R.color.colorPlaceholder));
            setPlaceholderRoundRadius(mContext.getResources().getDimension(R.dimen.dp_placeholder_radius));
        }
    }

    private FastTitleConfigEntity mTitleConfig;
    private FastQuitConfigEntity mQuitConfig;
    /**
     * Activity或Fragment根布局背景
     */
    @DrawableRes
    private int mContentViewBackgroundResource = -1;

    private int mRequestedOrientation;
    /**
     * Activity是否支持滑动返回
     */
    private boolean mIsSwipeBackEnable;

    /**
     * Adapter加载更多View
     */
    private LoadMoreFoot mLoadMoreFoot;
    /**
     * SmartRefreshLayout默认刷新头
     */
    private DefaultRefreshHeaderCreater mDefaultRefreshHeader;
    /**
     * 多状态布局--加载中/空数据/错误/无网络
     */
    private MultiStatusView mMultiStatusView;

    /**
     * 配置全局通用加载等待Loading提示框
     */
    private LoadingDialog mLoadingDialog;

    /**
     * 配置全局Http请求返回错误码处理
     */
    private HttpErrorControl mHttpErrorControl;

    private NavigationBarControl mNavigationBarControl;

    public FastTitleConfigEntity getTitleConfig() {
        return mTitleConfig;
    }

    /**
     * 设置全局TitleBarView相关属性
     * 最终调用{@link FastTitleDelegate#FastTitleDelegate(View, Activity, IFastTitleView)}
     *
     * @param mTitleConfig
     * @return
     */
    public FastConfig setTitleConfig(FastTitleConfigEntity mTitleConfig) {
        if (mTitleConfig != null) {
            this.mTitleConfig = mTitleConfig;
        }
        return sInstance;
    }

    public FastQuitConfigEntity getQuitConfig() {
        return mQuitConfig;
    }

    /**
     * 设置Activity 点击返回键提示退出程序或返回桌面相关参数
     * 最终调用{@link BasisActivity#quitApp()}
     *
     * @param mQuitConfig
     * @return
     */
    public FastConfig setQuitConfig(FastQuitConfigEntity mQuitConfig) {
        if (mQuitConfig != null) {
            this.mQuitConfig = mQuitConfig;
        }
        return this;
    }

    public int getContentViewBackgroundResource() {
        return mContentViewBackgroundResource;
    }

    /**
     * 设置 Activity或Fragment根布局背景资源
     * 最终调用{@link BasisActivity#beforeInitView()} {@link BasisFragment#beforeInitView()}
     *
     * @param contentViewBackgroundResource
     * @return
     */
    public FastConfig setContentViewBackgroundResource(@DrawableRes int contentViewBackgroundResource) {
        mContentViewBackgroundResource = contentViewBackgroundResource;
        return sInstance;
    }

    public int getRequestedOrientation() {
        return mRequestedOrientation;
    }

    /**
     * 设置Activity屏幕方向
     * 最终调用{@link BasisActivity#onCreate(Bundle)}
     *
     * @param mRequestedOrientation 默认自动 ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
     *                              竖屏 ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
     *                              横屏 ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
     *                              {@link ActivityInfo#screenOrientation ActivityInfo.screenOrientation}}
     * @return
     */
    public FastConfig setRequestedOrientation(int mRequestedOrientation) {
        this.mRequestedOrientation = mRequestedOrientation;
        return this;
    }

    public boolean isSwipeBackEnable() {
        return mIsSwipeBackEnable;
    }

    /**
     * 设置Activity 是否支持滑动返回功能
     * 最终调用{@link BasisActivity#initSwipeBack()}
     *
     * @param swipeBackEnable
     * @param application     application必传
     * @return
     */
    public FastConfig setSwipeBackEnable(boolean swipeBackEnable, Application application) {
        mIsSwipeBackEnable = swipeBackEnable;
        //先保证
        if (application != null && mApplication == null
                && FastUtil.isClassExist("cn.bingoogolapple.swipebacklayout.BGASwipeBackManager")) {
            BGASwipeBackManager.getInstance().init(application);//初始化滑动返回关闭Activity功能
            // 导航栏处理--不设置会预留一块导航栏高度的空白
            BGASwipeBackManager.ignoreNavigationBarModels(Arrays.asList(Build.MODEL));
            mApplication = application;
            mApplication.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    FastStackUtil.getInstance().push(activity);
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
                    FastStackUtil.getInstance().pop(activity, false);
                }
            });

        }
        return sInstance;
    }


    public LoadMoreFoot getLoadMoreFoot() {
        return mLoadMoreFoot;
    }

    /**
     * 设置Adapter统一加载更多相关脚布局
     * 最终调用{@link FastRefreshLoadDelegate#initRecyclerView()}
     *
     * @param mLoadMoreFoot
     * @return
     */
    public FastConfig setLoadMoreFoot(LoadMoreFoot mLoadMoreFoot) {
        if (mLoadMoreFoot != null) {
            this.mLoadMoreFoot = mLoadMoreFoot;
        }
        return this;
    }

    public DefaultRefreshHeaderCreater getDefaultRefreshHeader() {
        return mDefaultRefreshHeader;
    }

    /**
     * 设置SmartRefreshLayout 下拉刷新头
     * 最终调用{@link FastRefreshLoadDelegate#initRefreshHeader()}
     *
     * @param mDefaultRefreshHeader
     * @return
     */
    public FastConfig setDefaultRefreshHeader(DefaultRefreshHeaderCreater mDefaultRefreshHeader) {
        this.mDefaultRefreshHeader = mDefaultRefreshHeader;
        return sInstance;
    }

    public MultiStatusView getMultiStatusView() {
        return mMultiStatusView;
    }

    /**
     * 设置多状态布局--加载中/空数据/错误/无网络
     * 最终调用{@link FastRefreshLoadDelegate#initStatusView()}
     *
     * @param mMultiStatusView
     * @return
     */
    public FastConfig setMultiStatusView(MultiStatusView mMultiStatusView) {
        if (mMultiStatusView != null) {
            this.mMultiStatusView = mMultiStatusView;
        }
        return this;
    }

    public LoadingDialog getLoadingDialog() {
        return mLoadingDialog;
    }

    /**
     * 设置全局网络请求等待Loading提示框如登录等待loading
     * 最终调用{@link FastLoadingObserver#FastLoadingObserver(Activity)}
     *
     * @param mLoadingDialog
     * @return
     */
    public FastConfig setLoadingDialog(LoadingDialog mLoadingDialog) {
        if (mLoadingDialog != null) {
            this.mLoadingDialog = mLoadingDialog;
        }
        return this;
    }

    public HttpErrorControl getHttpErrorControl() {
        return mHttpErrorControl;
    }

    /**
     * 全局设置Retrofit网络请求返回错误码提示
     * 最终调用{@link FastObserver#onError(Throwable)}
     *
     * @param mHttpErrorControl 观察者必须为FastObserver及其子类
     * @return
     */
    public FastConfig setHttpErrorControl(HttpErrorControl mHttpErrorControl) {
        if (mHttpErrorControl != null) {
            this.mHttpErrorControl = mHttpErrorControl;
        }
        return this;
    }

    public NavigationBarControl getNavigationBarControl() {
        return mNavigationBarControl;
    }

    public FastConfig setNavigationBarControl(NavigationBarControl navigationBarControl) {
        if (navigationBarControl != null) {
            mNavigationBarControl = navigationBarControl;
        }
        return this;
    }

    /**
     * 设置加载图片占位图颜色
     *
     * @param mPlaceholderColor
     * @return
     */
    public FastConfig setPlaceholderColor(@ColorInt int mPlaceholderColor) {
        GlideManager.setPlaceholderColor(mPlaceholderColor);
        return sInstance;
    }

    /**
     * 设置加载图片占位图圆角弧度
     *
     * @param mPlaceholderRoundRadius
     * @return
     */
    public FastConfig setPlaceholderRoundRadius(float mPlaceholderRoundRadius) {
        GlideManager.setPlaceholderRoundRadius(mPlaceholderRoundRadius);
        return sInstance;
    }

    private Resources getResources() {
        return mContext.getResources();
    }

    private int getColor(@ColorRes int color) {
        return getResources().getColor(color);
    }

    private CharSequence getText(@StringRes int id) {
        return getResources().getText(id);
    }

}
