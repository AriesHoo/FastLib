package com.aries.library.fast.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aries.library.fast.R;
import com.aries.library.fast.i.IMultiStatusView;
import com.aries.library.fast.util.SizeUtil;

/**
 * Created: AriesHoo on 2017/11/10 13:28
 * E-Mail: AriesHoo@126.com
 * Function: 多状态布局
 * Description:
 */
public class FastMultiStatusView implements IMultiStatusView {
    private Context mContext;
    private CharSequence mLoadingText;
    @ColorInt
    private int mLoadingTextColor;
    private int mLoadingTextSize;
    @ColorInt
    private int mLoadingProgressColor;
    private Drawable mLoadingProgressDrawable;

    private CharSequence mEmptyText;
    @ColorInt
    private int mEmptyTextColor;
    private int mEmptyTextSize;

    private CharSequence mErrorText;
    @ColorInt
    private int mErrorTextColor;
    private int mErrorTextSize;

    private CharSequence mNoNetText;
    @ColorInt
    private int mNoNetTextColor;
    private int mNoNetTextSize;

    private Builder mBuilder;

    @Override
    public View getLoadingView() {
        return loadingView();
    }

    @Override
    public View getEmptyView() {
        return emptyView();
    }

    @Override
    public View getErrorView() {
        return errorView();
    }

    @Override
    public View getNoNetView() {
        return noNetView();
    }

    public FastMultiStatusView(Context context) {
        this(new Builder(context));
    }

    public FastMultiStatusView(Builder builder) {
        this.mBuilder = builder;
        this.mContext = builder.mContext;
        mLoadingTextColor = builder.mLoadingTextColor;
        mEmptyTextColor = builder.mEmptyTextColor;
        mErrorTextColor = builder.mErrorTextColor;
        mNoNetTextColor = builder.mNoNetTextColor;
        mLoadingProgressColor = builder.mLoadingProgressColor;
        mLoadingProgressDrawable = builder.mLoadingProgressDrawable;
        mLoadingTextSize = builder.mLoadingTextSize;
        mEmptyTextSize = builder.mEmptyTextSize;
        mErrorTextSize = builder.mErrorTextSize;
        mNoNetTextSize = builder.mNoNetTextSize;
        mLoadingText = builder.mLoadingText;
        mEmptyText = builder.mEmptyText;
        mErrorText = builder.mErrorText;
        mNoNetText = builder.mNoNetText;
    }

    private View loadingView() {
        View loadingView = View.inflate(mContext, R.layout.fast_layout_multi_loading, null);
        TextView text = findViewById(loadingView, R.id.tv_loadingMulti);
        ProgressBar pbLoading = findViewById(loadingView, R.id.pb_loadingMulti);
        text.setText(mLoadingText);
        text.setTextColor(mLoadingTextColor);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLoadingTextSize);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pbLoading.setIndeterminateTintList(ColorStateList.valueOf(mLoadingProgressColor));
        }
        if (mLoadingProgressDrawable != null) {
            mLoadingProgressDrawable.setBounds(pbLoading.getIndeterminateDrawable().getBounds());
            pbLoading.setIndeterminateDrawable(mLoadingProgressDrawable);
        }
        return loadingView;
    }

    private View emptyView() {
        View emptyView = View.inflate(mContext, R.layout.fast_layout_multi_empty, null);
        TextView text = findViewById(emptyView, R.id.tv_emptyMulti);
        text.setText(mEmptyText);
        text.setTextColor(mEmptyTextColor);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEmptyTextSize);
        return emptyView;
    }

    private View errorView() {
        View errorView = View.inflate(mContext, R.layout.fast_layout_multi_error, null);
        TextView text = findViewById(errorView, R.id.tv_errorMulti);
        text.setText(mErrorText);
        text.setTextColor(mErrorTextColor);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, mErrorTextSize);
        return errorView;
    }

    private View noNetView() {
        View noNetView = View.inflate(mContext, R.layout.fast_layout_multi_network, null);
        TextView text = findViewById(noNetView, R.id.tv_networkMulti);
        text.setText(mNoNetText);
        text.setTextColor(mNoNetTextColor);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNoNetTextSize);
        return noNetView;
    }


    @Nullable
    private <T extends View> T findViewById(View mContentView, @IdRes int viewId) {
        return (T) mContentView.findViewById(viewId);
    }

    public static final class Builder {
        private final int DEFAULT_TEXT_COLOR = Color.MAGENTA;
        private final int DEFAULT_TEXT_SIZE = 14;
        Context mContext;
        CharSequence mLoadingText;
        @ColorInt
        int mLoadingTextColor;
        int mLoadingTextSize;
        @ColorInt
        int mLoadingProgressColor;
        Drawable mLoadingProgressDrawable;

        CharSequence mEmptyText;
        @ColorInt
        int mEmptyTextColor;
        int mEmptyTextSize;

        CharSequence mErrorText;
        @ColorInt
        int mErrorTextColor;
        int mErrorTextSize;

        CharSequence mNoNetText;
        @ColorInt
        int mNoNetTextColor;
        int mNoNetTextSize;

        public Builder(Context context) {
            mContext = context;
            mLoadingTextColor = DEFAULT_TEXT_COLOR;
            mEmptyTextColor = DEFAULT_TEXT_COLOR;
            mErrorTextColor = DEFAULT_TEXT_COLOR;
            mNoNetTextColor = DEFAULT_TEXT_COLOR;
            mLoadingProgressColor = DEFAULT_TEXT_COLOR;
            mLoadingTextSize = SizeUtil.dp2px(DEFAULT_TEXT_SIZE);
            mEmptyTextSize = SizeUtil.dp2px(DEFAULT_TEXT_SIZE);
            mErrorTextSize = SizeUtil.dp2px(DEFAULT_TEXT_SIZE);
            mNoNetTextSize = SizeUtil.dp2px(DEFAULT_TEXT_SIZE);
        }

        public Builder(FastMultiStatusView fastMultiStatusView) {
            mContext = fastMultiStatusView.mContext;
            mLoadingTextColor = fastMultiStatusView.mLoadingTextColor;
            mEmptyTextColor = fastMultiStatusView.mEmptyTextColor;
            mErrorTextColor = fastMultiStatusView.mErrorTextColor;
            mNoNetTextColor = fastMultiStatusView.mNoNetTextColor;
            mLoadingProgressColor = fastMultiStatusView.mLoadingProgressColor;
            mLoadingProgressDrawable = fastMultiStatusView.mLoadingProgressDrawable;
            mLoadingTextSize = fastMultiStatusView.mLoadingTextSize;
            mEmptyTextSize = fastMultiStatusView.mEmptyTextSize;
            mErrorTextSize = fastMultiStatusView.mErrorTextSize;
            mNoNetTextSize = fastMultiStatusView.mNoNetTextSize;
            mLoadingText = fastMultiStatusView.mLoadingText;
            mEmptyText = fastMultiStatusView.mEmptyText;
            mErrorText = fastMultiStatusView.mErrorText;
            mNoNetText = fastMultiStatusView.mNoNetText;
        }

        /**
         * 设置所有TextView文本颜色
         *
         * @param mTextColor
         * @return
         */
        public Builder setTextColor(int mTextColor) {
            setLoadingTextColor(mTextColor);
            setEmptyTextColor(mTextColor);
            setErrorTextColor(mTextColor);
            setNoNetTextColor(mTextColor);
            return this;
        }

        /**
         * 设置所有TextView文本尺寸
         *
         * @param mTextSize
         * @return
         */
        public Builder setTextSize(int mTextSize) {
            setLoadingTextSize(mTextSize);
            setEmptyTextSize(mTextSize);
            setErrorTextSize(mTextSize);
            setNoNetTextSize(mTextSize);
            return this;
        }

        /**
         * 设置加载中文本
         *
         * @param mLoadingText
         * @return
         */
        public Builder setLoadingText(CharSequence mLoadingText) {
            this.mLoadingText = mLoadingText;
            return this;
        }

        /**
         * 设置加载中文本颜色
         *
         * @param mLoadingTextColor
         * @return
         */
        public Builder setLoadingTextColor(int mLoadingTextColor) {
            this.mLoadingTextColor = mLoadingTextColor;
            return this;
        }

        /**
         * 设置加载中文本尺寸
         *
         * @param mLoadingTextSize
         * @return
         */
        public Builder setLoadingTextSize(int mLoadingTextSize) {
            this.mLoadingTextSize = mLoadingTextSize;
            return this;
        }

        /**
         * 设置加载中loading颜色--5.0有效
         *
         * @param mLoadingProgressColor
         * @return
         */
        public Builder setLoadingProgressColor(int mLoadingProgressColor) {
            this.mLoadingProgressColor = mLoadingProgressColor;
            return this;
        }

        /**
         * 设置加载中loading drawable
         *
         * @param mLoadingProgressDrawable
         * @return
         */
        public Builder setLoadingProgressDrawable(Drawable mLoadingProgressDrawable) {
            this.mLoadingProgressDrawable = mLoadingProgressDrawable;
            return this;
        }

        /**
         * 设置空布局文字
         *
         * @param mEmptyText
         * @return
         */
        public Builder setEmptyText(CharSequence mEmptyText) {
            this.mEmptyText = mEmptyText;
            return this;
        }

        /**
         * 设置空布局文本颜色
         *
         * @param mEmptyTextColor
         * @return
         */
        public Builder setEmptyTextColor(int mEmptyTextColor) {
            this.mEmptyTextColor = mEmptyTextColor;
            return this;
        }

        /**
         * 设置空布局文本尺寸
         *
         * @param mEmptyTextSize
         * @return
         */
        public Builder setEmptyTextSize(int mEmptyTextSize) {
            this.mEmptyTextSize = mEmptyTextSize;
            return this;
        }

        /**
         * 设置加载错误文本
         *
         * @param mErrorText
         * @return
         */
        public Builder setErrorText(CharSequence mErrorText) {
            this.mErrorText = mErrorText;
            return this;
        }

        /**
         * 设置加载错误文本颜色
         *
         * @param mErrorTextColor
         * @return
         */
        public Builder setErrorTextColor(int mErrorTextColor) {
            this.mErrorTextColor = mErrorTextColor;
            return this;
        }

        /**
         * 设置加载错误文本尺寸
         *
         * @param mErrorTextSize
         * @return
         */
        public Builder setErrorTextSize(int mErrorTextSize) {
            this.mErrorTextSize = mErrorTextSize;
            return this;
        }

        /**
         * 设置网络错误文本
         *
         * @param mNoNetText
         * @return
         */
        public Builder setNoNetText(CharSequence mNoNetText) {
            this.mNoNetText = mNoNetText;
            return this;
        }

        /**
         * 设置网络错误文本颜色
         *
         * @param mNoNetTextColor
         * @return
         */
        public Builder setNoNetTextColor(int mNoNetTextColor) {
            this.mNoNetTextColor = mNoNetTextColor;
            return this;
        }

        /**
         * 设置无网络文本尺寸
         *
         * @param mNoNetTextSize
         * @return
         */
        public Builder setNoNetTextSize(int mNoNetTextSize) {
            this.mNoNetTextSize = mNoNetTextSize;
            return this;
        }

        public FastMultiStatusView build() {
            return new FastMultiStatusView(this);
        }
    }
}
