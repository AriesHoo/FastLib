package com.aries.library.fast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.aries.library.fast.delegate.FastRefreshLoadDelegate;
import com.aries.library.fast.i.ActivityFragmentControl;
import com.aries.library.fast.i.HttpRequestControl;
import com.aries.library.fast.i.IHttpRequestControl;
import com.aries.library.fast.i.IMultiStatusView;
import com.aries.library.fast.i.LoadMoreFoot;
import com.aries.library.fast.i.LoadingDialog;
import com.aries.library.fast.i.MultiStatusView;
import com.aries.library.fast.i.NavigationBarControl;
import com.aries.library.fast.i.OnHttpRequestListener;
import com.aries.library.fast.i.QuitAppControl;
import com.aries.library.fast.i.SwipeBackControl;
import com.aries.library.fast.i.TitleBarViewControl;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.ToastUtil;
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

import java.util.List;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created: AriesHoo on 2018/6/6 10:40
 * E-Mail: AriesHoo@126.com
 * Function:各种配置属性
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
     * 配置TitleBarView相关属性
     */
    private TitleBarViewControl mTitleBarViewControl;
    /**
     * 配置虚拟导航栏(NavigationViewHelper)
     */
    private NavigationBarControl mNavigationBarControl;
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
    private QuitAppControl mQuitAppControl;

    public Application getApplication() {
        return mApplication;
    }

    /**
     * 滑动返回基础配置查看{@link FastLifecycleCallbacks#onActivityCreated(Activity, Bundle)}
     *
     * @param application
     */
    public static void init(Application application) {
        if (mApplication == null && application != null) {//保证只执行一次初始化属性
            mApplication = application;
            sInstance = new FastManager();
            //配置默认滑动返回
            if (FastUtil.isClassExist("cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper")) {//滑动返回
                BGASwipeBackHelper.init(mApplication, null);//初始化滑动返回关闭Activity功能
                sInstance.setSwipeBackControl(new SwipeBackControl() {
                    @Override
                    public void setSwipeBack(Activity activity, BGASwipeBackHelper swipeBackHelper) {
                    }
                });
            }
            //配置默认智能下拉刷新
            if (FastUtil.isClassExist("com.scwang.smartrefresh.layout.SmartRefreshLayout")) {
                sInstance.setDefaultRefreshHeader(new DefaultRefreshHeaderCreater() {
                    @NonNull
                    @Override
                    public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                        return new ClassicsHeader(mApplication).setSpinnerStyle(SpinnerStyle.Translate);
                    }
                });
            }
            //配置BaseQuickAdapter
            if (FastUtil.isClassExist("com.chad.library.adapter.base.BaseQuickAdapter")) {
                sInstance.setLoadMoreFoot(new LoadMoreFoot() {
                    @Override
                    public LoadMoreView createDefaultLoadMoreView(BaseQuickAdapter adapter) {
                        return new FastLoadMoreView(mApplication).getBuilder().build();
                    }
                });
            }
            //配置多状态属性
            if (FastUtil.isClassExist("com.marno.easystatelibrary.EasyStatusView")) {
                sInstance.setMultiStatusView(new MultiStatusView() {
                    @NonNull
                    @Override
                    public IMultiStatusView createMultiStatusView() {
                        return new FastMultiStatusView(mApplication).getBuilder().build();
                    }
                });
            }
            //配置TitleBarView
            sInstance.setTitleBarViewControl(new TitleBarViewControl() {
                @Override
                public boolean createTitleBarViewControl(TitleBarView titleBar, Class<?> cls) {
                    return false;
                }
            });
            //配置虚拟导航栏
            sInstance.setNavigationBarControl(new NavigationBarControl() {
                @NonNull
                @Override
                public void setNavigationBar(Activity activity, NavigationViewHelper helper) {
                }
            });
            //配置Activity/Fragment相关
            sInstance.setActivityFragmentControl(new ActivityFragmentControl() {
                @Override
                public void setContentViewBackground(View contentView, Class<?> cls) {
                    //避免背景色重复
                    if (!Fragment.class.isAssignableFrom(cls) && !android.app.Fragment.class.isAssignableFrom(cls)) {
                        contentView.setBackgroundResource(R.color.colorBackground);
                    }
                }

                @Override
                public void setRequestedOrientation(Activity activity) {

                }

                @Override
                public Application.ActivityLifecycleCallbacks getActivityLifecycleCallbacks() {
                    return null;
                }

                @Override
                public FragmentManager.FragmentLifecycleCallbacks getFragmentLifecycleCallbacks() {
                    return null;
                }
            });
            sInstance.setHttpRequestControl(new HttpRequestControl() {
                @Override
                public void httpRequestSuccess(IHttpRequestControl httpRequestControl, List<?> list, OnHttpRequestListener listener) {

                }

                @Override
                public boolean httpRequestError(IHttpRequestControl httpRequestControl, Throwable e) {
                    return true;
                }
            });
            //配置退出程序相关
            sInstance.setQuitAppControl(new QuitAppControl() {
                @Override
                public long quipApp(boolean isFirst, Activity activity) {
                    if (isFirst) {
                        ToastUtil.show(R.string.fast_quit_app);
                    } else {
                        FastStackUtil.getInstance().exit();
                    }
                    return 2000;
                }
            });
            //注册activity生命周期
            mApplication.registerActivityLifecycleCallbacks(new FastLifecycleCallbacks());
            //初始化Toast工具
            ToastUtil.init(mApplication);
        }
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
     * @param control
     * @return
     */
    public FastManager setDefaultRefreshHeader(DefaultRefreshHeaderCreater control) {
        if (control != null) {
            this.mDefaultRefreshHeader = control;
        }
        return sInstance;
    }

    public MultiStatusView getMultiStatusView() {
        return mMultiStatusView;
    }

    /**
     * 设置多状态布局--加载中/空数据/错误/无网络
     * 最终调用{@link FastRefreshLoadDelegate#initStatusView()}
     *
     * @param control
     * @return
     */
    public FastManager setMultiStatusView(MultiStatusView control) {
        if (control != null) {
            this.mMultiStatusView = control;
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
        if (control != null) {
            mTitleBarViewControl = control;
        }
        return this;
    }

    public NavigationBarControl getNavigationBarControl() {
        return mNavigationBarControl;
    }

    /**
     * 配置虚拟导航栏(NavigationViewHelper)
     * {@link NavigationViewHelper}
     *
     * @param control
     * @return
     */
    public FastManager setNavigationBarControl(NavigationBarControl control) {
        if (control != null) {
            mNavigationBarControl = control;
        }
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
        if (control != null) {
            mSwipeBackControl = control;
        }
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
        if (control != null) {
            mActivityFragmentControl = control;
        }
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
        if (control != null) {
            mHttpRequestControl = control;
        }
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
        if (control != null) {
            mQuitAppControl = control;
        }
        return this;
    }
}
