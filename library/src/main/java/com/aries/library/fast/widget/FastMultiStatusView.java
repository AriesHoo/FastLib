package com.aries.library.fast.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private Drawable mEmptyImageDrawable;

    private CharSequence mErrorText;
    @ColorInt
    private int mErrorTextColor;
    private int mErrorTextSize;
    private Drawable mErrorImageDrawable;

    private CharSequence mNoNetText;
    @ColorInt
    private int mNoNetTextColor;
    private int mNoNetTextSize;
    private Drawable mNoNetImageDrawable;

    private int mImageWidth;
    private int mImageHeight;
    private int mTextMargin;

    private Builder mBuilder;

    @NonNull
    @Override
    public View getLoadingView() {
        return loadingView();
    }

    @NonNull
    @Override
    public View getEmptyView() {
        return emptyView();
    }

    @NonNull
    @Override
    public View getErrorView() {
        return errorView();
    }

    @NonNull
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
        mEmptyImageDrawable = builder.mEmptyImageDrawable;
        mErrorImageDrawable = builder.mErrorImageDrawable;
        mNoNetImageDrawable = builder.mNoNetImageDrawable;
        mImageHeight = builder.mImageHeight;
        mImageWidth = builder.mImageWidth;
        mTextMargin = builder.mTextMargin;
    }

    private View loadingView() {
        View contentView = View.inflate(mContext, R.layout.fast_layout_multi_loading, null);
        ProgressBar pbLoading = findViewById(contentView, R.id.pb_loadingMulti);
        setText(contentView, R.id.tv_loadingMulti, mLoadingText)
                .setTextColor(contentView, R.id.tv_loadingMulti, mLoadingTextColor)
                .setTextSize(contentView, R.id.tv_loadingMulti, mLoadingTextSize)
                .setViewMarginTop(contentView, R.id.tv_loadingMulti, mTextMargin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pbLoading.setIndeterminateTintList(ColorStateList.valueOf(mLoadingProgressColor));
        }
        if (mLoadingProgressDrawable != null) {
            mLoadingProgressDrawable.setBounds(pbLoading.getIndeterminateDrawable().getBounds());
            pbLoading.setIndeterminateDrawable(mLoadingProgressDrawable);
        }
        return contentView;
    }

    private View emptyView() {
        View contentView = View.inflate(mContext, R.layout.fast_layout_multi_empty, null);
        setText(contentView, R.id.tv_emptyMulti, mEmptyText)
                .setTextColor(contentView, R.id.tv_emptyMulti, mEmptyTextColor)
                .setTextSize(contentView, R.id.tv_emptyMulti, mEmptyTextSize)
                .setViewMarginTop(contentView, R.id.tv_emptyMulti, mTextMargin)
                .setViewWidthAndHeight(contentView, R.id.tv_emptyMulti, mImageWidth, mImageHeight)
                .setImageDrawable(contentView, R.id.iv_emptyMulti, mEmptyImageDrawable);
        return contentView;
    }

    private View errorView() {
        View contentView = View.inflate(mContext, R.layout.fast_layout_multi_error, null);
        setText(contentView, R.id.tv_errorMulti, mErrorText)
                .setTextColor(contentView, R.id.tv_errorMulti, mErrorTextColor)
                .setTextSize(contentView, R.id.tv_errorMulti, mErrorTextSize)
                .setViewMarginTop(contentView, R.id.tv_errorMulti, mTextMargin)
                .setViewWidthAndHeight(contentView, R.id.tv_errorMulti, mImageWidth, mImageHeight)
                .setImageDrawable(contentView, R.id.iv_errorMulti, mErrorImageDrawable);
        return contentView;
    }

    private View noNetView() {
        View contentView = View.inflate(mContext, R.layout.fast_layout_multi_network, null);
        setText(contentView, R.id.tv_networkMulti, mNoNetText)
                .setTextColor(contentView, R.id.tv_networkMulti, mNoNetTextColor)
                .setTextSize(contentView, R.id.tv_networkMulti, mNoNetTextSize)
                .setViewMarginTop(contentView, R.id.tv_networkMulti, mTextMargin)
                .setViewWidthAndHeight(contentView, R.id.tv_networkMulti, mImageWidth, mImageHeight)
                .setImageDrawable(contentView, R.id.iv_networkMulti, mNoNetImageDrawable);
        return contentView;
    }

    private FastMultiStatusView setText(View contentView, int id, CharSequence text) {
        TextView txt = findViewById(contentView, id);
        txt.setText(text);
        return this;
    }

    private FastMultiStatusView setTextColor(View contentView, int id, int color) {
        TextView txt = findViewById(contentView, id);
        txt.setTextColor(color);
        return this;
    }

    private FastMultiStatusView setTextSize(View contentView, int id, int size) {
        TextView txt = findViewById(contentView, id);
        txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    private FastMultiStatusView setImageDrawable(View contentView, int id, Drawable drawable) {
        ImageView img = findViewById(contentView, id);
        img.setImageDrawable(drawable);
        return this;
    }

    private FastMultiStatusView setViewMarginTop(View contentView, int id, int margin) {
        View view = findViewById(contentView, id);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin = margin;
        return this;
    }

    private FastMultiStatusView setViewWidthAndHeight(View contentView, int id, int width, int height) {
        View view = findViewById(contentView, id);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        return this;
    }

    public Builder getBuilder() {
        if (mBuilder == null) {
            mBuilder = new Builder(this);
        }
        return mBuilder;
    }

    @Nullable
    private <T extends View> T findViewById(View mContentView, @IdRes int viewId) {
        return (T) mContentView.findViewById(viewId);
    }

    public static final class Builder {
        private final int DEFAULT_TEXT_COLOR = Color.BLACK;
        private final int DEFAULT_TEXT_SIZE = 14;
        private final int DEFAULT_IMAGE_SIZE = 72;
        private final int DEFAULT_TEXT_MARGIN = 12;
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
        Drawable mEmptyImageDrawable;

        CharSequence mErrorText;
        @ColorInt
        int mErrorTextColor;
        int mErrorTextSize;
        Drawable mErrorImageDrawable;

        CharSequence mNoNetText;
        @ColorInt
        int mNoNetTextColor;
        int mNoNetTextSize;
        Drawable mNoNetImageDrawable;

        int mImageWidth;
        int mImageHeight;
        int mTextMargin;

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
            mImageHeight = SizeUtil.dp2px(DEFAULT_IMAGE_SIZE);
            mImageWidth = SizeUtil.dp2px(DEFAULT_IMAGE_SIZE);
            mTextMargin = SizeUtil.dp2px(DEFAULT_TEXT_MARGIN);
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
            mEmptyImageDrawable = fastMultiStatusView.mEmptyImageDrawable;
            mErrorImageDrawable = fastMultiStatusView.mErrorImageDrawable;
            mNoNetImageDrawable = fastMultiStatusView.mNoNetImageDrawable;
            mImageHeight = fastMultiStatusView.mImageHeight;
            mImageWidth = fastMultiStatusView.mImageWidth;
            mTextMargin = fastMultiStatusView.mTextMargin;
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
         * 设置空布局Image 背景
         *
         * @param mEmptyImageDrawable
         * @return
         */
        public Builder setEmptyImageDrawable(Drawable mEmptyImageDrawable) {
            this.mEmptyImageDrawable = mEmptyImageDrawable;
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
         * 设置错误布局Image 背景
         *
         * @param mErrorImageDrawable
         * @return
         */
        public Builder setErrorImageDrawable(Drawable mErrorImageDrawable) {
            this.mErrorImageDrawable = mErrorImageDrawable;
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

        /**
         * 设置无网络Image 背景
         *
         * @param mNoNetImageDrawable
         * @return
         */
        public Builder setNoNetImageDrawable(Drawable mNoNetImageDrawable) {
            this.mNoNetImageDrawable = mNoNetImageDrawable;
            return this;
        }

        /**
         * 同时设置提示图片宽高
         *
         * @param imageWidthHeight
         * @return
         */
        public Builder setImageWidthHeight(int imageWidthHeight) {
            setImageWidth(imageWidthHeight);
            setImageHeight(imageWidthHeight);
            return this;
        }

        /**
         * 设置提示图片宽度
         *
         * @param imageWidth
         * @return
         */
        public Builder setImageWidth(int imageWidth) {
            this.mImageWidth = imageWidth;
            return this;
        }

        /**
         * 设置提示图片高度
         *
         * @param imageHeight
         * @return
         */
        public Builder setImageHeight(int imageHeight) {
            this.mImageHeight = imageHeight;
            return this;
        }

        /**
         * 设置TextView marginTop
         *
         * @param textMargin
         * @return
         */
        public Builder setTextMargin(int textMargin) {
            this.mTextMargin = textMargin;
            return this;
        }

        public FastMultiStatusView build() {
            return new FastMultiStatusView(this);
        }
    }
}
