package com.aries.library.fast.delegate;

import android.app.Activity;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.aries.library.fast.FastConfig;
import com.aries.library.fast.entity.FastTitleConfigEntity;
import com.aries.library.fast.i.IFastTitleView;
import com.aries.library.fast.util.FastUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2017/7/26 10:39
 * Function: 带TitleBarView 的Activity及Fragment代理类
 * Desc:
 */
public class FastTitleDelegate {
    public TitleBarView mTitleBar;
    public int mType = StatusBarUtil.STATUS_BAR_TYPE_DEFAULT;//type >0 表示支持状态栏黑白字切换

    public FastTitleDelegate(View rootView, Activity mContext, IFastTitleView iTitleBarView) {
        if (iTitleBarView.isLightStatusBarEnable()) {
            mType = StatusBarUtil.setStatusBarLightMode(mContext);
        }
        getTitleBarView(rootView);
        if (mTitleBar == null) {
            return;
        }

        FastTitleConfigEntity titleConfig = FastConfig.getInstance(mContext).getTitleConfig();
        mTitleBar.setHeight(titleConfig.getTitleHeight())
                .setCenterLayoutPadding(titleConfig.getCenterLayoutPadding())
                .setCenterGravityLeftPadding(titleConfig.getCenterGravityLeftPadding())
                .setCenterGravityLeft(titleConfig.isCenterGravityLeft())
                .setOnLeftTextClickListener(iTitleBarView.getLeftClickListener())
                .setTitleMainTextFakeBold(titleConfig.isTitleMainTextFakeBold())
                .setTitleMainTextMarquee(titleConfig.isTitleMainTextMarquee())
                .setTitleMainTextSize(TypedValue.COMPLEX_UNIT_PX, titleConfig.getTitleMainTextSize())
                .setTitleMainTextColor(titleConfig.getTitleMainTextColor())
                .setTitleSubTextFakeBold(titleConfig.isTitleSubTextFakeBold())
                .setTitleSubTextSize(TypedValue.COMPLEX_UNIT_PX, titleConfig.getTitleSubTextSize())
                .setTitleSubTextColor(titleConfig.getTitleSubTextColor())
                .setLeftTextSize(TypedValue.COMPLEX_UNIT_PX, titleConfig.getLeftTextSize())
                .setLeftTextColor(titleConfig.getLeftTextColor())
                .setRightTextSize(TypedValue.COMPLEX_UNIT_PX, titleConfig.getRightTextSize())
                .setRightTextColor(titleConfig.getRightTextColor())
                .setActionTextSize(titleConfig.getActionTextSize())
                .setActionTextColor(titleConfig.getActionTextColor())
                .setDividerBackgroundColor(titleConfig.getDividerColor())
                .setDividerHeight(titleConfig.getDividerHeight())
                .setActionPadding(titleConfig.getActionPadding())
                .setOutPadding(titleConfig.getOutPadding())
                .setLeftTextDrawable(iTitleBarView.getLeftIcon() > 0
                        ? FastUtil.getTintDrawable(mContext.getResources().getDrawable(iTitleBarView.getLeftIcon()),
                        FastConfig.getInstance(mContext).getTitleConfig().getTitleTextColor())
                        : null)
                .setBgResource(titleConfig.getTitleBackgroundResource());
        //是否状态栏一直设置半透明效果--默认只是在状态栏白色背景黑色文字图标情况下设置
        if (titleConfig.isStatusAlwaysEnable()) {
            mTitleBar.setStatusAlpha(titleConfig.getStatusAlpha());
        } else if (iTitleBarView.isLightStatusBarEnable()
                && mType <= StatusBarUtil.STATUS_BAR_TYPE_DEFAULT) {
            //设置浅色状态栏又无法设置文字深色模式需将状态栏透明度调低避免状态栏文字颜色不可见问题
            //Android 5.0半透明效果alpha为102
            mTitleBar.setStatusAlpha(titleConfig.getStatusAlpha());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTitleBar.setElevation(titleConfig.getTitleElevation());
        }
        iTitleBarView.beforeSetTitleBar(mTitleBar);
        iTitleBarView.setTitleBar(mTitleBar);
    }

    /**
     * 获取布局里的TitleBarView
     *
     * @param rootView
     * @return
     */
    private void getTitleBarView(View rootView) {
        if (rootView instanceof TitleBarView && mTitleBar == null) {
            mTitleBar = (TitleBarView) rootView;
        } else if (rootView instanceof ViewGroup) {
            ViewGroup contentView = (ViewGroup) rootView;
            int childCount = contentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = contentView.getChildAt(i);
                getTitleBarView(childView);
            }
        }
    }
}
