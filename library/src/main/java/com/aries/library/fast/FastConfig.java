package com.aries.library.fast;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.aries.library.fast.entity.FastTitleConfigEntity;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.animation.BaseAnimation;
import com.chad.library.adapter.base.animation.ScaleInAnimation;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * Created: AriesHoo on 2017/10/25 10:27
 * E-Mail: AriesHoo@126.com
 * Function: 全局参数配置--可在Application处设置Library全局属性
 * Description:
 */
public class FastConfig {

    private static volatile FastConfig sInstance;
    private Context mContext;
    private FastTitleConfigEntity mTitleConfig;
    /**
     * Activity或Fragment根布局背景
     */
    @DrawableRes
    private int mContentViewBackgroundResource;

    /**
     * Adapter加载动画
     */
    private boolean mIsAdapterAnimationEnable;
    /**
     * Activity是否支持滑动返回
     */
    private boolean mIsSwipeBackEnable;
    /**
     * Adapter加载动画效果
     */
    private BaseAnimation mDefaultAdapterAnimation = new ScaleInAnimation();
    /**
     * SmartRefreshLayout默认刷新头
     */
    private DefaultRefreshHeaderCreater mDefaultRefreshHeader;
    /**
     * 占位图颜色
     */
    @ColorInt
    private int mPlaceholderColor = Color.GRAY;

    private float mPlaceholderRoundRadius = 4f;

    public FastTitleConfigEntity getTitleConfig() {
        return mTitleConfig;
    }

    public FastConfig setTitleConfig(FastTitleConfigEntity mTitleConfig) {
        if (mTitleConfig != null) {
            this.mTitleConfig = mTitleConfig;
        }
        return sInstance;
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
    public FastConfig setContentViewBackgroundResource(int contentViewBackgroundResource) {
        mContentViewBackgroundResource = contentViewBackgroundResource;
        return sInstance;
    }


    public boolean isAdapterAnimationEnable() {
        return mIsAdapterAnimationEnable;
    }

    /**
     * 设置Adapter 是否允许加载动画
     *
     * @param adapterAnimationEnable
     * @return
     */
    public FastConfig setAdapterAnimationEnable(boolean adapterAnimationEnable) {
        mIsAdapterAnimationEnable = adapterAnimationEnable;
        return this;
    }


    public boolean isSwipeBackEnable() {
        return mIsSwipeBackEnable;
    }

    /**
     * 设置Activity 是否支持滑动返回功能
     *
     * @param swipeBackEnable
     * @return
     */
    public FastConfig setSwipeBackEnable(boolean swipeBackEnable) {
        mIsSwipeBackEnable = swipeBackEnable;
        return sInstance;
    }

    public BaseAnimation getDefaultAdapterAnimation() {
        return mDefaultAdapterAnimation;
    }

    /**
     * 设置Adapter加载动画效果
     *
     * @param mDefaultAdapterAnimation
     * @return
     */
    public FastConfig setDefaultAdapterAnimation(BaseAnimation mDefaultAdapterAnimation) {
        this.mDefaultAdapterAnimation = mDefaultAdapterAnimation;
        setAdapterAnimationEnable(mDefaultAdapterAnimation != null);
        return sInstance;
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

    public int getPlaceholderColor() {
        return mPlaceholderColor;
    }

    /**
     * 设置占位图颜色
     *
     * @param mPlaceholderColor
     * @return
     */
    public FastConfig setPlaceholderColor(int mPlaceholderColor) {
        this.mPlaceholderColor = mPlaceholderColor;
        return sInstance;
    }

    public float getPlaceholderRoundRadius() {
        return mPlaceholderRoundRadius;
    }

    /**
     * 设置占位图圆角弧度
     *
     * @param mPlaceholderRoundRadius
     * @return
     */
    public FastConfig setPlaceholderRoundRadius(float mPlaceholderRoundRadius) {
        this.mPlaceholderRoundRadius = mPlaceholderRoundRadius;
        return sInstance;
    }

    private FastConfig(Context context) {
        if (context != null) {
            this.mContext = context.getApplicationContext();
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
            if (FastUtil.isClassExist("com.scwang.smartrefresh.layout.SmartRefreshLayout")) {
                setDefaultRefreshHeader(new DefaultRefreshHeaderCreater() {
                    @NonNull
                    @Override
                    public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                        return new ClassicsHeader(mContext).setSpinnerStyle(SpinnerStyle.Translate);
                    }
                });
            }
            setPlaceholderColor(mContext.getResources().getColor(R.color.colorPlaceholder));
            setPlaceholderRoundRadius(mContext.getResources().getDimension(R.dimen.dp_placeholder_radius));
        }
    }

    public static FastConfig getInstance(Context context) {
        if (sInstance == null) {
            synchronized (FastConfig.class) {
                if (sInstance == null) {
                    sInstance = new FastConfig(context);
                }
            }
        }
        return sInstance;
    }
}
