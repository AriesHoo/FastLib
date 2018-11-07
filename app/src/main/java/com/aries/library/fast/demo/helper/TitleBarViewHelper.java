package com.aries.library.fast.demo.helper;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.library.fast.demo.base.BaseHelper;
import com.aries.library.fast.demo.util.ViewColorUtil;
import com.aries.library.fast.demo.widget.OverScrollView;
import com.aries.library.fast.util.SizeUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;

/**
 * @Author: AriesHoo on 2018/11/2 14:03
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public class TitleBarViewHelper extends BaseHelper {

    private RecyclerView mRecyclerView;
    private OverScrollView mOverScrollView;
    private TitleBarView mTitleBarView;
    private boolean mShowTextEnable;
    /**
     * 滑动的最小距离
     */
    private int mMinHeight = SizeUtil.dp2px(24);
    /**
     * 滑动的最大距离
     */
    private int mMaxHeight = 20;
    /**
     * 转换透明度
     */
    private int mTransAlpha = 112;

    private OnScrollListener mOnScrollListener;
    private boolean mIsLightMode;

    public interface OnScrollListener {
        /**
         * 滚动回调
         *
         * @param alpha
         * @param isLightMode
         */
        void onScrollChange(int alpha, boolean isLightMode);
    }

    public TitleBarViewHelper(Activity context) {
        super(context);
    }

    public TitleBarViewHelper setTitleBarView(TitleBarView titleBarView) {
        this.mTitleBarView = titleBarView;
        return this;
    }

    public TitleBarViewHelper setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        if (mRecyclerView == null) {
            return this;
        }
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int mAlpha;
            int mScrollY;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollY += dy;
                mAlpha = setChange(mScrollY);
            }
        });
        return this;
    }

    public TitleBarViewHelper setOverScrollView(OverScrollView overScrollView) {
        mOverScrollView = overScrollView;
        if (mOverScrollView == null) {
            return this;
        }
        overScrollView.addOnScrollChangeListener(new OverScrollView.OnScrollChangeListener() {
            int mAlpha;

            @Override
            public void onScrollChange(View v, int scrollX, int mScrollY, int oldScrollX, int oldScrollY) {
                mAlpha = setChange(mScrollY);
            }
        });
        return this;
    }

    public TitleBarViewHelper setShowTextEnable(boolean enable) {
        this.mShowTextEnable = enable;
        return this;
    }

    public TitleBarViewHelper setMinHeight(int minHeight) {
        mMinHeight = minHeight;
        return this;
    }

    public TitleBarViewHelper setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
        return this;
    }

    public TitleBarViewHelper setTransAlpha(int transAlpha) {
        mTransAlpha = transAlpha;
        return this;
    }

    public TitleBarViewHelper setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
        return this;
    }

    private int setChange(int mScrollY) {
        int mAlpha;
        // 滑动距离小于定义得最小距离
        if (mScrollY <= mMinHeight) {
            mAlpha = 0;
        }
        // 滑动距离大于定义得最大距离
        else if (mScrollY > mMaxHeight) {
            mAlpha = 255;
        }
        // 滑动距离处于最小和最大距离之间
        else {
            // （滑动距离 - 开始变化距离）/ 最大限制距离 = mAlpha/255
            mAlpha = (mScrollY - mMinHeight) * 255 / (mMaxHeight - mMinHeight);
        }
        //白色背景
        if (mAlpha >= mTransAlpha) {
            if (!mIsLightMode) {
                StatusBarUtil.setStatusBarLightMode(mContext);
                mIsLightMode = true;
                if (mTitleBarView != null) {
                    mTitleBarView.setStatusAlpha(StatusBarUtil.isSupportStatusBarFontChange() ? 0 : 112);
                }
            }
        } else {
            if (mIsLightMode) {
                StatusBarUtil.setStatusBarDarkMode(mContext);
                mIsLightMode = false;
                if (mTitleBarView != null) {
                    mTitleBarView.setStatusAlpha(0);
                }
            }
        }
        if (mTitleBarView != null) {
            ViewColorUtil.getInstance().changeColor(mTitleBarView, mAlpha, mIsLightMode, mShowTextEnable);
            mTitleBarView.setDividerVisible(mAlpha >= 250).getBackground().setAlpha(mAlpha);
        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollChange(mAlpha, mIsLightMode);
        }
        return mAlpha;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTitleBarView = null;
        mRecyclerView = null;
    }
}
