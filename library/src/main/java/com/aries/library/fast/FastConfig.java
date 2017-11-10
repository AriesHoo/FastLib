package com.aries.library.fast;

import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.aries.library.fast.entity.FastQuitConfigEntity;
import com.aries.library.fast.entity.FastTitleConfigEntity;
import com.aries.library.fast.i.IMultiStatusView;
import com.aries.library.fast.i.LoadMoreFoot;
import com.aries.library.fast.i.MultiStatusView;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.widget.FastLoadMoreView;
import com.aries.library.fast.widget.FastMultiStatusView;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.marno.easystatelibrary.EasyStatusView;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * Created: AriesHoo on 2017/10/25 10:27
 * E-Mail: AriesHoo@126.com
 * Function: 全局参数配置--可在Application处设置Library全局属性
 * Description:
 */
public class FastConfig {

    private static volatile FastConfig sInstance;
    private static Context mContext;

    public static FastConfig getInstance(Context context) {
        LoggerManager.i("FastConfig", "FastConfig:" + context);
        if (sInstance == null) {
            synchronized (FastConfig.class) {
                if (sInstance == null) {
                    sInstance = new FastConfig(context);
                }
            }
        }
        return sInstance;
    }

    private FastConfig(Context context) {
        if (context == null) {
            throw new NullPointerException(FastConstant.EXCEPTION_FAST_CONFIG_CONTEXT_NOT_NULL);
        }
        LoggerManager.i("FastConfig:" + context);
        if (context != null) {
            this.mContext = context.getApplicationContext();
            LoggerManager.i("FastConfig:" + mContext + ";isApplication:" + (mContext instanceof Application));
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
                setDefaultLoadMoreView(new LoadMoreFoot() {
                    @Override
                    public LoadMoreView createDefaultLoadMoreView(BaseQuickAdapter adapter) {
                        FastLoadMoreView.Builder builder = new FastLoadMoreView().getBuilder();
                        return builder
                                .setLoadTextColor(mContext.getResources().getColor(R.color.colorLoadMoreText))
                                .setLoadTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.dp_load_more_text_size))
                                .setLoadingProgressColor(mContext.getResources().getColor(R.color.colorLoadMoreProgress))
                                .setLoadingText(mContext.getText(R.string.fast_load_more_loading))
                                .setLoadFailText(mContext.getText(R.string.fast_load_more_load_failed))
                                .setLoadEndText(mContext.getText(R.string.fast_load_more_load_end)).build();
                    }
                });
            }
            if (FastUtil.isClassExist("com.marno.easystatelibrary.EasyStatusView")) {
                setDefaultMultiStatusView(new MultiStatusView() {
                    @NonNull
                    @Override
                    public IMultiStatusView createMultiStatusView(EasyStatusView easyStatusView) {
                        return new FastMultiStatusView.Builder(mContext)
                                .setTextColor(mContext.getResources().getColor(R.color.colorMultiText))
                                .setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.dp_multi_text_size))
                                .setLoadingProgressColor(mContext.getResources().getColor(R.color.colorMultiProgress))
                                .setLoadingText(mContext.getText(R.string.fast_multi_loading))
                                .setEmptyText(mContext.getText(R.string.fast_multi_empty))
                                .setEmptyText(mContext.getText(R.string.fast_multi_error))
                                .setNoNetText(mContext.getText(R.string.fast_multi_network))
                                .setLoadingProgressColor(Color.MAGENTA)
                                .setLoadingTextColor(Color.BLUE)
                                .build();
                    }
                });
            }
            setQuitConfig(new FastQuitConfigEntity()
                    .setQuitDelay(2000)
                    .setQuitMessage(mContext.getString(R.string.fast_quit_app))
                    .setBackToTaskEnable(false)
                    .setSnackBarBackgroundColor(Color.argb(210, 0, 0, 0))
                    .setSnackBarEnable(false)
                    .setSnackBarMessageColor(Color.WHITE));
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            setContentViewBackgroundResource(-1);
            setSwipeBackEnable(false, null);
            setPlaceholderColor(mContext.getResources().getColor(R.color.colorPlaceholder));
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
    private LoadMoreFoot mDefaultLoadMoreView;
    /**
     * SmartRefreshLayout默认刷新头
     */
    private DefaultRefreshHeaderCreater mDefaultRefreshHeader;
    /**
     * 多状态布局--加载中/空数据/错误/无网络
     */
    private MultiStatusView mDefaultMultiStatusView;

    public FastTitleConfigEntity getTitleConfig() {
        return mTitleConfig;
    }

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
     *
     * @param mRequestedOrientation 默认自动 ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
     *                              竖屏 ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
     *                              横屏 ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
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
     *
     * @param swipeBackEnable
     * @param application     swipeBackEnable为true application必传
     * @return
     */
    public FastConfig setSwipeBackEnable(boolean swipeBackEnable, Application application) {
        if (swipeBackEnable && application == null) {
            throw new NullPointerException(FastConstant.EXCEPTION_SWIPE_BACK_APPLICATION_NOT_NULL);
        }
        mIsSwipeBackEnable = swipeBackEnable;
        if (application != null) {
            BGASwipeBackManager.getInstance().init(application);//初始化滑动返回关闭Activity功能
            // 导航栏处理--不设置会预留一块导航栏高度的空白
//            BGASwipeBackManager.ignoreNavigationBarModels(Arrays.asList(Build.MODEL));
        }
        return sInstance;
    }


    public LoadMoreFoot getDefaultLoadMoreView() {
        return mDefaultLoadMoreView;
    }

    public FastConfig setDefaultLoadMoreView(LoadMoreFoot mDefaultLoadMoreView) {
        if (mDefaultLoadMoreView != null) {
            this.mDefaultLoadMoreView = mDefaultLoadMoreView;
        }
        return this;
    }

    public DefaultRefreshHeaderCreater getDefaultRefreshHeader() {
        return mDefaultRefreshHeader;
    }

    /**
     * 设置SmartRefreshLayout 下拉刷新头
     *
     * @param mDefaultRefreshHeader
     * @return
     */
    public FastConfig setDefaultRefreshHeader(DefaultRefreshHeaderCreater mDefaultRefreshHeader) {
        this.mDefaultRefreshHeader = mDefaultRefreshHeader;
        return sInstance;
    }

    public MultiStatusView getDefaultMultiStatusView() {
        return mDefaultMultiStatusView;
    }

    /**
     * 设置多状态布局--加载中/空数据/错误/无网络
     *
     * @param mDefaultMultiStatusView
     * @return
     */
    public FastConfig setDefaultMultiStatusView(MultiStatusView mDefaultMultiStatusView) {
        if (mDefaultMultiStatusView != null) {
            this.mDefaultMultiStatusView = mDefaultMultiStatusView;
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


}
