package com.aries.library.fast;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.aries.library.fast.util.FastUtil;
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
    /**
     * Activity或Fragment根布局背景
     */
    @DrawableRes
    private int mContentViewBackgroundResource;
    /**
     * TitleBarView背景
     */
    @DrawableRes
    private int mTitleBackgroundResource;

    /**
     * TitleBarView所有TextView颜色
     */
    @ColorInt
    private int mTitleTextColor;
    /**
     * Adapter加载动画
     */
    private boolean mIsAdapterAnimationEnable;
    /**
     * 状态浅色背景(深色的状态栏文字及图标)
     */
    private boolean mIsLightStatusBarEnable;
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

    private float mTitleElevation = 0f;

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

    public int getTitleBackgroundResource() {
        return mTitleBackgroundResource;
    }

    /**
     * 设置TitleBarView背景资源
     *
     * @param titleBackgroundResource
     * @return
     */
    public FastConfig setTitleBackgroundResource(int titleBackgroundResource) {
        mTitleBackgroundResource = titleBackgroundResource;
        return sInstance;
    }

    public int getTitleTextColor() {
        return mTitleTextColor;
    }

    /**
     * 设置TitleBarView 所有TextView颜色值
     *
     * @param mTitleTextColor
     */
    public FastConfig setTitleTextColor(int mTitleTextColor) {
        this.mTitleTextColor = mTitleTextColor;
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


    public boolean isLightStatusBarEnable() {
        return mIsLightStatusBarEnable;
    }

    /**
     * 设置状态栏是否浅色模式(深色状态栏文字及图标)
     *
     * @param lightStatusBarEnable
     * @return
     */
    public FastConfig setLightStatusBarEnable(boolean lightStatusBarEnable) {
        mIsLightStatusBarEnable = lightStatusBarEnable;
        return sInstance;
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

    public float getTitleElevation() {
        return mTitleElevation;
    }

    /**
     * 设置TitleBarView 海拔Elevation参考{@link android.view.View#setElevation(float)};
     *
     * @param mTitleElevation
     * @return
     */
    public FastConfig setTitleElevation(float mTitleElevation) {
        this.mTitleElevation = mTitleElevation;
        return sInstance;
    }

    private FastConfig(Context context) {
        if (context != null) {
            this.mContext = context.getApplicationContext();
            setTitleBackgroundResource(R.color.colorTitleBackground);
            setTitleTextColor(mContext.getResources().getColor(R.color.colorTitleText));
            if (FastUtil.isClassExist("com.scwang.smartrefresh.layout.SmartRefreshLayout")) {
                setDefaultRefreshHeader(new DefaultRefreshHeaderCreater() {
                    @NonNull
                    @Override
                    public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                        return new ClassicsHeader(mContext).setSpinnerStyle(SpinnerStyle.Translate);
                    }
                });
            }
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
