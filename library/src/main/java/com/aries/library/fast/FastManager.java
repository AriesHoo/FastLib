package com.aries.library.fast;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.aries.library.fast.delegate.FastRefreshLoadDelegate;
import com.aries.library.fast.i.ActivityFragmentControl;
import com.aries.library.fast.i.HttpRequestControl;
import com.aries.library.fast.i.LoadMoreFoot;
import com.aries.library.fast.i.LoadingDialog;
import com.aries.library.fast.i.MultiStatusView;
import com.aries.library.fast.i.QuitAppControl;
import com.aries.library.fast.i.SwipeBackControl;
import com.aries.library.fast.i.TitleBarViewControl;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.library.fast.widget.FastLoadDialog;
import com.aries.ui.widget.progress.UIProgressDialog;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * @Author: AriesHoo on 2018/7/30 13:49
 * @E-Mail: AriesHoo@126.com
 * Function: 各种UI相关配置属性
 * Description:
 */
public class FastManager {

    private static String TAG = "FastManager";
    private static volatile FastManager sInstance;

    private FastManager() {
    }

    public static FastManager getInstance() {
        if (sInstance == null) {
            throw new NullPointerException(FastConstant.EXCEPTION_NOT_INIT_FAST_MANAGER);
        }
        return sInstance;
    }

    private static Application mApplication;
    /**
     * Adapter加载更多View
     */
    private LoadMoreFoot mLoadMoreFoot;
    /**
     * SmartRefreshLayout默认刷新头
     */
    private DefaultRefreshHeaderCreator mDefaultRefreshHeader;
    /**
     * 多状态布局--加载中/空数据/错误/无网络
     */
    private MultiStatusView mMultiStatusView;
    /**
     * 配置全局通用加载等待Loading提示框
     */
    private LoadingDialog mLoadingDialog;
    /**
     * 配置TitleBarView相关属性
     */
    private TitleBarViewControl mTitleBarViewControl;
    /**
     * 配置Activity滑动返回相关属性
     */
    private SwipeBackControl mSwipeBackControl;
    /**
     * 配置Activity/Fragment(背景+Activity强制横竖屏+Activity 生命周期回调+Fragment生命周期回调)
     */
    private ActivityFragmentControl mActivityFragmentControl;
    /**
     * 配置网络请求
     */
    private HttpRequestControl mHttpRequestControl;
    /**
     * Activity 主页点击返回键控制
     */
    private QuitAppControl mQuitAppControl;

    public Application getApplication() {
        return mApplication;
    }

    /**
     * 滑动返回基础配置查看{@link FastLifecycleCallbacks#onActivityCreated(Activity, Bundle)}
     *
     * @param application
     */
    public static FastManager init(Application application) {
        //保证只执行一次初始化属性
        if (mApplication == null && application != null) {
            mApplication = application;
            sInstance = new FastManager();
            //预设置FastLoadDialog属性
            sInstance.setLoadingDialog(new LoadingDialog() {
                @Nullable
                @Override
                public FastLoadDialog createLoadingDialog(@Nullable Activity activity) {
                    return new FastLoadDialog(activity,
                            new UIProgressDialog.WeBoBuilder(activity)
                                    .setMessage(R.string.fast_loading)
                                    .create());
                }
            });
            //设置滑动返回监听
            BGASwipeBackHelper.init(mApplication, null);
            //注册activity生命周期
            mApplication.registerActivityLifecycleCallbacks(new FastLifecycleCallbacks());
            //初始化Toast工具
            ToastUtil.init(mApplication);
            //初始化Glide
            GlideManager.setPlaceholderColor(mApplication.getResources().getColor(R.color.colorPlaceholder));
            GlideManager.setPlaceholderRoundRadius(mApplication.getResources().getDimension(R.dimen.dp_placeholder_radius));
        }
        return getInstance();
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
        this.mLoadMoreFoot = mLoadMoreFoot;
        return this;
    }

    public DefaultRefreshHeaderCreator getDefaultRefreshHeader() {
        return mDefaultRefreshHeader;
    }

    /**
     * 设置SmartRefreshLayout 下拉刷新头
     * 最终调用{@link FastRefreshLoadDelegate#initRefreshHeader()}
     *
     * @param control
     * @return
     */
    public FastManager setDefaultRefreshHeader(DefaultRefreshHeaderCreator control) {
        this.mDefaultRefreshHeader = control;
        return sInstance;
    }

    public MultiStatusView getMultiStatusView() {
        return mMultiStatusView;
    }

    /**
     * 设置多状态布局--加载中/空数据/错误/无网络
     * 最终调用{@link FastRefreshLoadDelegate#initRefreshHeader()}
     *
     * @param control
     * @return
     */
    public FastManager setMultiStatusView(MultiStatusView control) {
        this.mMultiStatusView = control;
        return this;
    }

    public LoadingDialog getLoadingDialog() {
        return mLoadingDialog;
    }

    /**
     * 设置全局网络请求等待Loading提示框如登录等待loading
     * 最终调用{@link FastLoadingObserver#FastLoadingObserver(Activity)}
     *
     * @param control
     * @return
     */
    public FastManager setLoadingDialog(LoadingDialog control) {
        if (control != null) {
            this.mLoadingDialog = control;
        }
        return this;
    }

    public TitleBarViewControl getTitleBarViewControl() {
        return mTitleBarViewControl;
    }

    public FastManager setTitleBarViewControl(TitleBarViewControl control) {
        mTitleBarViewControl = control;
        return this;
    }

    public SwipeBackControl getSwipeBackControl() {
        return mSwipeBackControl;
    }

    /**
     * 配置Activity滑动返回相关属性
     *
     * @param control
     * @return
     */
    public FastManager setSwipeBackControl(SwipeBackControl control) {
        mSwipeBackControl = control;
        return this;
    }

    public ActivityFragmentControl getActivityFragmentControl() {
        return mActivityFragmentControl;
    }

    /**
     * 配置Activity/Fragment(背景+Activity强制横竖屏+Activity 生命周期回调+Fragment生命周期回调)
     *
     * @param control
     * @return
     */
    public FastManager setActivityFragmentControl(ActivityFragmentControl control) {
        mActivityFragmentControl = control;
        return this;
    }

    public HttpRequestControl getHttpRequestControl() {
        return mHttpRequestControl;
    }

    /**
     * 配置Http请求成功及失败相关回调-方便全局处理
     *
     * @param control
     * @return
     */
    public FastManager setHttpRequestControl(HttpRequestControl control) {
        mHttpRequestControl = control;
        return this;
    }


    public QuitAppControl getQuitAppControl() {
        return mQuitAppControl;
    }

    /**
     * 配置Http请求成功及失败相关回调-方便全局处理
     *
     * @param control
     * @return
     */
    public FastManager setQuitAppControl(QuitAppControl control) {
        mQuitAppControl = control;
        return this;
    }
}
