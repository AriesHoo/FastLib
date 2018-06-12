package com.aries.library.fast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.aries.library.fast.basis.BasisActivity;
import com.aries.library.fast.basis.BasisFragment;
import com.aries.library.fast.delegate.FastRefreshLoadDelegate;
import com.aries.library.fast.i.IMultiStatusView;
import com.aries.library.fast.i.LoadMoreFoot;
import com.aries.library.fast.i.LoadingDialog;
import com.aries.library.fast.i.MultiStatusView;
import com.aries.library.fast.i.NavigationBarControl;
import com.aries.library.fast.i.TitleBarViewControl;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.FastUtil;
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
 * Created: AriesHoo on 2018/6/6 10:40
 * E-Mail: AriesHoo@126.com
 * Function:各种配置属性
 * Description:
 */
public class FastManager {

    private static volatile FastManager sInstance;

    private FastManager() {
    }

    public static FastManager getInstance() {
        if (sInstance == null) {
            synchronized (FastManager.class) {
                if (sInstance == null) {
                    sInstance = new FastManager();
                }
            }
        }
        return sInstance;
    }

    private boolean mIsSetSwipeBack = false;
    private Application mApplication;

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

    private TitleBarViewControl mTitleBarViewControl;
    private NavigationBarControl mNavigationBarControl;

    public Application getApplication() {
        return mApplication;
    }

    /**
     * 最简单-初始化
     *
     * @param application
     * @return
     */
    public FastManager init(Application application) {
        if (mApplication == null && application != null) {
            mApplication = application;
            setSwipeBackEnable(false);
            if (FastUtil.isClassExist("com.scwang.smartrefresh.layout.SmartRefreshLayout")) {
                setDefaultRefreshHeader(new DefaultRefreshHeaderCreater() {
                    @NonNull
                    @Override
                    public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                        return new ClassicsHeader(mApplication).setSpinnerStyle(SpinnerStyle.Translate);
                    }
                });
            }
            if (FastUtil.isClassExist("com.chad.library.adapter.base.BaseQuickAdapter")) {
                setLoadMoreFoot(new LoadMoreFoot() {
                    @Override
                    public LoadMoreView createDefaultLoadMoreView(BaseQuickAdapter adapter) {
                        return new FastLoadMoreView(mApplication).getBuilder().build();
                    }
                });
            }
            if (FastUtil.isClassExist("com.marno.easystatelibrary.EasyStatusView")) {
                setMultiStatusView(new MultiStatusView() {
                    @NonNull
                    @Override
                    public IMultiStatusView createMultiStatusView() {
                        return new FastMultiStatusView(mApplication).getBuilder().build();
                    }
                });
            }

            setTitleBarViewControl(new TitleBarViewControl() {
                @Override
                public boolean createTitleBarViewControl(TitleBarView titleBar, boolean isActivity) {
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
    public FastManager setContentViewBackgroundResource(@DrawableRes int contentViewBackgroundResource) {
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
    public FastManager setRequestedOrientation(int mRequestedOrientation) {
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
     * @return
     */
    public FastManager setSwipeBackEnable(boolean swipeBackEnable) {
        if (mApplication == null) {
            throw new NullPointerException(FastConstant.EXCEPTION_NOT_INIT_FAST_MANAGER);
        }
        mIsSwipeBackEnable = swipeBackEnable;
        //先保证
        if (!mIsSetSwipeBack && FastUtil.isClassExist("cn.bingoogolapple.swipebacklayout.BGASwipeBackManager")) {
            mIsSetSwipeBack = true;
            BGASwipeBackManager.getInstance().init(mApplication);//初始化滑动返回关闭Activity功能
            // 导航栏处理--不设置会预留一块导航栏高度的空白
            BGASwipeBackManager.ignoreNavigationBarModels(Arrays.asList(Build.MODEL));
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
    public FastManager setLoadMoreFoot(LoadMoreFoot mLoadMoreFoot) {
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
    public FastManager setDefaultRefreshHeader(DefaultRefreshHeaderCreater mDefaultRefreshHeader) {
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
    public FastManager setMultiStatusView(MultiStatusView mMultiStatusView) {
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
    public FastManager setLoadingDialog(LoadingDialog mLoadingDialog) {
        if (mLoadingDialog != null) {
            this.mLoadingDialog = mLoadingDialog;
        }
        return this;
    }

    public TitleBarViewControl getTitleBarViewControl() {
        return mTitleBarViewControl;
    }

    public FastManager setTitleBarViewControl(TitleBarViewControl control) {
        if (control != null) {
            mTitleBarViewControl = control;
        }
        return this;
    }

    public NavigationBarControl getNavigationBarControl() {
        return mNavigationBarControl;
    }

    public FastManager setNavigationBarControl(NavigationBarControl navigationBarControl) {
        if (navigationBarControl != null) {
            mNavigationBarControl = navigationBarControl;
        }
        return this;
    }
}
