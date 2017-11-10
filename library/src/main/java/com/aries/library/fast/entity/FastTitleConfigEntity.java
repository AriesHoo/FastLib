package com.aries.library.fast.entity;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.aries.library.fast.i.IFastTitleView;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.module.activity.FastTitleActivity;

/**
 * Created: AriesHoo on 2017/10/30 14:28
 * E-Mail: AriesHoo@126.com
 * Function: TitleBarView属性--用于全局配置
 * Description: 最终调用 {@link com.aries.library.fast.delegate.FastTitleDelegate#FastTitleDelegate(View, Activity, IFastTitleView)}
 */
public class FastTitleConfigEntity {

    private int mTitleHeight;
    @DrawableRes
    private int mTitleBackgroundResource;
    @DrawableRes
    private int mLeftTextDrawable;
    @ColorInt
    private int mTitleTextColor;
    private boolean mIsLeftTextFinishEnable;
    private boolean mIsLightStatusBarEnable;
    private float mTitleElevation = 0f;
    private int mCenterLayoutPadding;//中间部分是Layout左右padding
    private int mCenterGravityLeftPadding;//中间部分左对齐时Layout左padding
    private boolean mIsCenterGravityLeft;

    private int mOutPadding;
    private int mActionPadding;
    private boolean mStatusAlwaysEnable;
    private int mStatusAlpha;
    @ColorInt
    private int mDividerColor;
    private int mDividerHeight;
    @ColorInt
    private int mLeftTextColor;
    private int mLeftTextSize;
    @ColorInt
    private int mTitleMainTextColor;
    private int mTitleMainTextSize;
    private boolean mTitleMainTextFakeBold;
    private boolean mTitleMainTextMarquee;
    @ColorInt
    private int mTitleSubTextColor;
    private int mTitleSubTextSize;
    private boolean mTitleSubTextFakeBold;
    @ColorInt
    private int mRightTextColor;
    private int mRightTextSize;
    @ColorInt
    private int mActionTextColor;
    private int mActionTextSize;

    /**
     * 设置TitleBarView高度
     *
     * @param mTitleHeight {@link com.aries.library.fast.delegate.FastTitleDelegate#FastTitleDelegate(View, Activity, IFastTitleView)}
     * @return
     */
    public FastTitleConfigEntity setTitleHeight(int mTitleHeight) {
        this.mTitleHeight = mTitleHeight;
        return this;
    }

    /**
     * 设置TitleBarView背景资源
     *
     * @param titleBackgroundResource {@link View#setBackgroundResource(int)}
     * @return
     */
    public FastTitleConfigEntity setTitleBackgroundResource(@DrawableRes int titleBackgroundResource) {
        mTitleBackgroundResource = titleBackgroundResource;
        return this;
    }

    /**
     * 设置TitleActivity TitleBarView左边返回箭头icon {@link FastTitleActivity#getLeftIcon()}
     *
     * @param mLeftTextDrawable {@link com.aries.ui.view.title.TitleBarView#setLeftTextDrawable(int)}
     * @return
     */
    public FastTitleConfigEntity setLeftTextDrawable(@DrawableRes int mLeftTextDrawable) {
        this.mLeftTextDrawable = mLeftTextDrawable;
        return this;
    }

    /**
     * 设置TitleBarView 所有TextView颜色值影响所有的setTextColor 注意调用顺序
     *
     * @param mTitleTextColor
     */
    public FastTitleConfigEntity setTitleTextColor(@ColorInt int mTitleTextColor) {
        this.mTitleTextColor = mTitleTextColor;
        setActionTextColor(mTitleTextColor);
        setLeftTextColor(mTitleTextColor);
        setTitleMainTextColor(mTitleTextColor);
        setTitleSubTextColor(mTitleTextColor);
        setRightTextColor(mTitleTextColor);
        return this;
    }

    /**
     * 设置FastTitleActivity 左边TextView点击事件是否finish
     *
     * @param mIsLeftTextFinishEnable {@link FastTitleActivity#getLeftClickListener()}
     * @return
     */
    public FastTitleConfigEntity setLeftTextFinishEnable(boolean mIsLeftTextFinishEnable) {
        this.mIsLeftTextFinishEnable = mIsLeftTextFinishEnable;
        return this;
    }

    /**
     * 设置状态栏是否浅色模式(深色状态栏文字及图标)
     *
     * @param lightStatusBarEnable {@link com.aries.ui.util.StatusBarUtil#setStatusBarLightMode(Activity)}
     * @return
     */
    public FastTitleConfigEntity setLightStatusBarEnable(boolean lightStatusBarEnable) {
        mIsLightStatusBarEnable = lightStatusBarEnable;
        return this;
    }

    /**
     * 设置TitleBarView 海拔Elevation 5.0以上有效
     *
     * @param mTitleElevation 参考{@link android.view.View#setElevation(float)};
     * @return
     */
    public FastTitleConfigEntity setTitleElevation(float mTitleElevation) {
        LoggerManager.i("mTitleElevation:"+mTitleElevation);
        this.mTitleElevation = mTitleElevation;
        return this;
    }

    /**
     * 设置中间title Layout 左右padding--title居中时
     *
     * @param mCenterLayoutPadding {@link com.aries.ui.view.title.TitleBarView#setCenterLayoutPadding(int)}
     * @return
     */
    public FastTitleConfigEntity setCenterLayoutPadding(int mCenterLayoutPadding) {
        this.mCenterLayoutPadding = mCenterLayoutPadding;
        return this;
    }

    /**
     * 设置中间Title 左对齐时 Layout 左右padding
     *
     * @param mCenterGravityLeftPadding {@link com.aries.ui.view.title.TitleBarView#setCenterGravityLeftPadding(int)}
     * @return
     */
    public FastTitleConfigEntity setCenterGravityLeftPadding(int mCenterGravityLeftPadding) {
        this.mCenterGravityLeftPadding = mCenterGravityLeftPadding;
        return this;
    }

    /**
     * 设置标题靠左显示
     *
     * @param mIsCenterGravityLeft {@link com.aries.ui.view.title.TitleBarView#setCenterGravityLeft(boolean)}
     * @return
     */
    public FastTitleConfigEntity setCenterGravityLeft(boolean mIsCenterGravityLeft) {
        this.mIsCenterGravityLeft = mIsCenterGravityLeft;
        return this;
    }

    /**
     * 设置左边layout及右边Layout paddingLeft及paddingRight
     *
     * @param mOutPadding {@link com.aries.ui.view.title.TitleBarView#setOutPadding(int)}
     */
    public FastTitleConfigEntity setOutPadding(int mOutPadding) {
        this.mOutPadding = mOutPadding;
        return this;
    }

    /**
     * 设置addAction间距
     *
     * @param mActionPadding {@link com.aries.ui.view.title.TitleBarView#setActionPadding(int)}
     */
    public FastTitleConfigEntity setActionPadding(int mActionPadding) {
        this.mActionPadding = mActionPadding;
        return this;
    }

    /**
     * 设置状态栏半透明效果是否始终有效--默认否即如状态栏白色时候透明度效果有效
     *
     * @param mStatusAlwaysEnable
     */
    public FastTitleConfigEntity setStatusAlwaysEnable(boolean mStatusAlwaysEnable) {
        this.mStatusAlwaysEnable = mStatusAlwaysEnable;
        return this;
    }

    /**
     * 设置状态栏透明度--配合setStatusAlways使用
     *
     * @param mStatusAlpha 0-255  半透明设置默认值102 {@link com.aries.ui.view.title.TitleBarView#setStatusAlpha(int)}
     */
    public FastTitleConfigEntity setStatusAlpha(int mStatusAlpha) {
        this.mStatusAlpha = mStatusAlpha;
        return this;
    }

    /**
     * 设置下划线颜色
     *
     * @param mDividerColor {@link com.aries.ui.view.title.TitleBarView#setDividerColor(int)}
     */
    public FastTitleConfigEntity setDividerColor(@ColorInt int mDividerColor) {
        this.mDividerColor = mDividerColor;
        return this;
    }

    /**
     * 设置下划线高度
     *
     * @param mDividerHeight {@link com.aries.ui.view.title.TitleBarView}
     */
    public FastTitleConfigEntity setDividerHeight(int mDividerHeight) {
        this.mDividerHeight = mDividerHeight;
        return this;
    }

    /**
     * 设置左边位置大小
     *
     * @param mLeftTextSize {@link com.aries.ui.view.title.TitleBarView#setLeftTextSize(int, float)}
     */
    public FastTitleConfigEntity setLeftTextSize(int mLeftTextSize) {
        this.mLeftTextSize = mLeftTextSize;
        return this;
    }

    /**
     * 设置左边文字颜色
     *
     * @param mLeftTextColor {@link com.aries.ui.view.title.TitleBarView#setLeftTextColor(int)}
     */
    public FastTitleConfigEntity setLeftTextColor(@ColorInt int mLeftTextColor) {
        this.mLeftTextColor = mLeftTextColor;
        return this;
    }

    /**
     * 设置主标题大小
     *
     * @param mTitleMainTextSize {@link com.aries.ui.view.title.TitleBarView#setTitleMainTextSize(int, float)}
     */
    public FastTitleConfigEntity setTitleMainTextSize(int mTitleMainTextSize) {
        this.mTitleMainTextSize = mTitleMainTextSize;
        return this;
    }

    /**
     * 设置主标题大小
     *
     * @param mTitleMainTextColor {@link com.aries.ui.view.title.TitleBarView#setTitleMainTextColor(int)}
     */
    public FastTitleConfigEntity setTitleMainTextColor(@ColorInt int mTitleMainTextColor) {
        this.mTitleMainTextColor = mTitleMainTextColor;
        return this;
    }

    /**
     * 设置主标题是否粗体
     *
     * @param mTitleMainTextFakeBold {@link com.aries.ui.view.title.TitleBarView#setTitleMainTextFakeBold(boolean)}
     */
    public FastTitleConfigEntity setTitleMainTextFakeBold(boolean mTitleMainTextFakeBold) {
        this.mTitleMainTextFakeBold = mTitleMainTextFakeBold;
        return this;
    }

    /**
     * 设置主标题是否跑马灯
     *
     * @param mTitleMainTextMarquee {@link com.aries.ui.view.title.TitleBarView#setTitleMainTextMarquee(boolean)}
     */
    public FastTitleConfigEntity setTitleMainTextMarquee(boolean mTitleMainTextMarquee) {
        this.mTitleMainTextMarquee = mTitleMainTextMarquee;
        return this;
    }

    /**
     * 设置副标题大小
     *
     * @param mTitleSubTextSize {@link com.aries.ui.view.title.TitleBarView#setTitleSubTextSize(int, float)}
     */
    public FastTitleConfigEntity setTitleSubTextSize(int mTitleSubTextSize) {
        this.mTitleSubTextSize = mTitleSubTextSize;
        return this;
    }

    /**
     * 设置副标题颜色
     *
     * @param mTitleSubTextColor {@link com.aries.ui.view.title.TitleBarView#setTitleSubTextColor(int)}
     */
    public FastTitleConfigEntity setTitleSubTextColor(@ColorInt int mTitleSubTextColor) {
        this.mTitleSubTextColor = mTitleSubTextColor;
        return this;
    }

    /**
     * 设置副标题是否粗体
     *
     * @param mTitleSubTextFakeBold {@link com.aries.ui.view.title.TitleBarView#setTitleSubTextFakeBold(boolean)}
     */
    public FastTitleConfigEntity setTitleSubTextFakeBold(boolean mTitleSubTextFakeBold) {
        this.mTitleSubTextFakeBold = mTitleSubTextFakeBold;
        return this;
    }

    /**
     * 设置右边文字大小
     *
     * @param mRightTextSize {@link com.aries.ui.view.title.TitleBarView#setRightTextSize(int, float)}
     */
    public FastTitleConfigEntity setRightTextSize(int mRightTextSize) {
        this.mRightTextSize = mRightTextSize;
        return this;
    }

    /**
     * 设置右边文字颜色
     *
     * @param mRightTextColor {@link com.aries.ui.view.title.TitleBarView#setRightTextColor(int)}
     */
    public FastTitleConfigEntity setRightTextColor(@ColorInt int mRightTextColor) {
        this.mRightTextColor = mRightTextColor;
        return this;
    }

    /**
     * 设置Action文字大小
     *
     * @param mActionTextSize {@link com.aries.ui.view.title.TitleBarView#setActionTextSize(int)}
     */
    public FastTitleConfigEntity setActionTextSize(int mActionTextSize) {
        this.mActionTextSize = mActionTextSize;
        return this;
    }

    /**
     * 设置Action文字颜色
     *
     * @param mActionTextColor {@link com.aries.ui.view.title.TitleBarView#setActionTextColor(int)}
     */
    public FastTitleConfigEntity setActionTextColor(@ColorInt int mActionTextColor) {
        this.mActionTextColor = mActionTextColor;
        return this;
    }

    public int getTitleHeight() {
        return mTitleHeight;
    }

    public int getTitleBackgroundResource() {
        return mTitleBackgroundResource;
    }

    public int getLeftTextDrawable() {
        return mLeftTextDrawable;
    }

    public int getTitleTextColor() {
        return mTitleTextColor;
    }

    public boolean isLeftTextFinishEnable() {
        return mIsLeftTextFinishEnable;
    }

    public boolean isLightStatusBarEnable() {
        return mIsLightStatusBarEnable;
    }

    public float getTitleElevation() {
        return mTitleElevation;
    }

    public int getCenterLayoutPadding() {
        return mCenterLayoutPadding;
    }

    public int getCenterGravityLeftPadding() {
        return mCenterGravityLeftPadding;
    }

    public boolean isCenterGravityLeft() {
        return mIsCenterGravityLeft;
    }

    public int getOutPadding() {
        return mOutPadding;
    }

    public int getActionPadding() {
        return mActionPadding;
    }

    public boolean isStatusAlwaysEnable() {
        return mStatusAlwaysEnable;
    }

    public int getStatusAlpha() {
        return mStatusAlpha;
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public int getDividerHeight() {
        return mDividerHeight;
    }

    public int getLeftTextSize() {
        return mLeftTextSize;
    }

    public int getLeftTextColor() {
        return mLeftTextColor;
    }

    public int getTitleMainTextSize() {
        return mTitleMainTextSize;
    }

    public int getTitleMainTextColor() {
        return mTitleMainTextColor;
    }

    public boolean isTitleMainTextFakeBold() {
        return mTitleMainTextFakeBold;
    }

    public boolean isTitleMainTextMarquee() {
        return mTitleMainTextMarquee;
    }

    public int getTitleSubTextSize() {
        return mTitleSubTextSize;
    }

    public int getTitleSubTextColor() {
        return mTitleSubTextColor;
    }

    public boolean isTitleSubTextFakeBold() {
        return mTitleSubTextFakeBold;
    }

    public int getRightTextSize() {
        return mRightTextSize;
    }

    public int getRightTextColor() {
        return mRightTextColor;
    }

    public int getActionTextSize() {
        return mActionTextSize;
    }

    public int getActionTextColor() {
        return mActionTextColor;
    }
}
