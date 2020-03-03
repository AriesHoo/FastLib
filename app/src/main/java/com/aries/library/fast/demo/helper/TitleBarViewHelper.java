package com.aries.library.fast.demo.helper;

import android.app.Activity;
import android.view.View;

import com.aries.library.fast.demo.base.BaseHelper;
import com.aries.library.fast.demo.util.ViewColorUtil;
import com.aries.library.fast.demo.widget.OverScrollView;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.util.SizeUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author: AriesHoo on 2018/11/2 14:03
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public class TitleBarViewHelper extends BaseHelper {

    private RecyclerView.OnScrollListener mScrollListener;
    private RecyclerView mRecyclerView;
    private OverScrollView mOverScrollView;
    private TitleBarView mTitleBarView;
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

    private boolean mMutateEnable;
    private boolean mShowTextEnable = true;

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
        if (mTitleBarView != null) {
            mTitleBarView.getBackground().mutate().setAlpha(0);
        }
        return this;
    }

    public TitleBarViewHelper setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        if (mRecyclerView == null) {
            return this;
        }
        if (mScrollListener == null) {
            mScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //滚动到顶部了
                    if (recyclerView.computeVerticalScrollOffset() == 0) {
                        LoggerManager.i("mScrollY:" + mScrollY + ";mAlpha:" + mAlpha);
                        mScrollY = 0;
                    } else {
                        mScrollY += dy;
                    }
                    mAlpha = setChange(mScrollY);
                }

            };
        }
        mRecyclerView.addOnScrollListener(mScrollListener);
        return this;
    }

    public int mAlpha;
    public int mScrollY;

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

    public TitleBarViewHelper setLightMode(boolean lightMode) {
        mIsLightMode = lightMode;
        return this;
    }

    public TitleBarViewHelper setMutateEnable(boolean enable) {
        mMutateEnable = enable;
        return this;
    }

    public TitleBarViewHelper setShowTextEnable(boolean enable) {
        mShowTextEnable = enable;
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
                    mTitleBarView.setStatusAlpha(StatusBarUtil.isSupportStatusBarFontChange() ? 0 : 60);
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
            ViewColorUtil.getInstance().changeColor(mTitleBarView, mAlpha, mIsLightMode, mShowTextEnable, mMutateEnable);
            mTitleBarView.setDividerVisible(mAlpha >= 250).getBackground().setAlpha(mAlpha);
        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollChange(mAlpha, mIsLightMode);
        }
        return mAlpha;
    }

    public TitleBarViewHelper resetTitleBar() {
        if (mTitleBarView != null) {
            mTitleBarView.setStatusBarLightMode(true)
                    .setStatusAlpha(StatusBarUtil.isSupportStatusBarFontChange() ? 0 : 255);
            ViewColorUtil.getInstance().changeColor(mTitleBarView, 255, true, mShowTextEnable, mMutateEnable);
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollChange(255, true);
            }
        }
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRecyclerView != null && mScrollListener != null) {
            mRecyclerView.removeOnScrollListener(mScrollListener);
        }
        mTitleBarView = null;
        mRecyclerView = null;
        mOverScrollView = null;
    }
}
