package com.aries.library.fast.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aries.library.fast.R;
import com.aries.library.fast.i.IMultiStatusView;
import com.aries.library.fast.util.FastUtil;

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

    private int mLoadingSize;
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

    public FastMultiStatusView(@Nullable Context context) {
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
        mLoadingSize = builder.mLoadingSize;
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
                .setViewWidthAndHeight(contentView, R.id.pb_loadingMulti, mLoadingSize, mLoadingSize)
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
                .setViewWidthAndHeight(contentView, R.id.iv_emptyMulti, mImageWidth, mImageHeight)
                .setImageDrawable(contentView, R.id.iv_emptyMulti, mEmptyImageDrawable);
        return contentView;
    }

    private View errorView() {
        View contentView = View.inflate(mContext, R.layout.fast_layout_multi_error, null);
        setText(contentView, R.id.tv_errorMulti, mErrorText)
                .setTextColor(contentView, R.id.tv_errorMulti, mErrorTextColor)
                .setTextSize(contentView, R.id.tv_errorMulti, mErrorTextSize)
                .setViewMarginTop(contentView, R.id.tv_errorMulti, mTextMargin)
                .setViewWidthAndHeight(contentView, R.id.iv_errorMulti, mImageWidth, mImageHeight)
                .setImageDrawable(contentView, R.id.iv_errorMulti, mErrorImageDrawable);
        return contentView;
    }

    private View noNetView() {
        View contentView = View.inflate(mContext, R.layout.fast_layout_multi_network, null);
        setText(contentView, R.id.tv_networkMulti, mNoNetText)
                .setTextColor(contentView, R.id.tv_networkMulti, mNoNetTextColor)
                .setTextSize(contentView, R.id.tv_networkMulti, mNoNetTextSize)
                .setViewMarginTop(contentView, R.id.tv_networkMulti, mTextMargin)
                .setViewWidthAndHeight(contentView, R.id.iv_networkMulti, mImageWidth, mImageHeight)
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
        int mEmptyImageColor;
        Drawable mEmptyImageDrawable;

        CharSequence mErrorText;
        @ColorInt
        int mErrorTextColor;
        int mErrorTextSize;
        int mErrorImageColor;
        Drawable mErrorImageDrawable;

        CharSequence mNoNetText;
        @ColorInt
        int mNoNetTextColor;
        int mNoNetTextSize;
        int mNoNetImageColor;
        Drawable mNoNetImageDrawable;

        int mLoadingSize;
        int mImageWidth;
        int mImageHeight;
        int mTextMargin;

        public Builder(@Nullable Context context) {
            mContext = context;
            setTextColorResource(R.color.colorMultiText);
            setTextSizeResource(R.dimen.dp_multi_text_size);
            setLoadingProgressColorResource(R.color.colorMultiProgress);
            setLoadingText(R.string.fast_multi_loading);
            setEmptyText(R.string.fast_multi_empty);
            setErrorText(R.string.fast_multi_error);
            setNoNetText(R.string.fast_multi_network);
            setEmptyImageDrawable(R.drawable.fast_img_multi_empty);
            setErrorImageDrawable(R.drawable.fast_img_multi_error);
            setNoNetImageDrawable(R.drawable.fast_img_multi_network);
            setLoadingSizeResource(R.dimen.dp_multi_loading_size);
            setImageWidthHeightResource(R.dimen.dp_multi_image_size);
            setTextMarginResource(R.dimen.dp_multi_margin);
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
            mLoadingSize = fastMultiStatusView.mLoadingSize;
            mImageHeight = fastMultiStatusView.mImageHeight;
            mImageWidth = fastMultiStatusView.mImageWidth;
            mTextMargin = fastMultiStatusView.mTextMargin;
        }

        public Builder setTextColorResource(@ColorRes int mTextColorRes) {
            return setTextColor(getColor(mTextColorRes));
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

        public Builder setTextSizeResource(@DimenRes int mTextSizeRes) {
            return setTextSize(getDimensionPixelSize(mTextSizeRes));
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

        public Builder setLoadingText(@StringRes int mLoadingText) {
            return setLoadingText(getText(mLoadingText));
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

        public Builder setLoadingTextColorResource(@ColorRes int mLoadingTextColorRes) {
            return setLoadingTextColor(getColor(mLoadingTextColorRes));
        }

        /**
         * 设置加载中文本颜色--默认设置会同步设置ProgressBar颜色注意调用顺序
         *
         * @param mLoadingTextColor
         * @return
         */
        public Builder setLoadingTextColor(int mLoadingTextColor) {
            this.mLoadingTextColor = mLoadingTextColor;
            return setLoadingProgressColor(mLoadingTextColor);
        }

        public Builder setLoadingTextSizeResource(@DimenRes int mLoadingTextSizeRes) {
            return setLoadingTextSize(getDimensionPixelSize(mLoadingTextSizeRes));
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

        public Builder setLoadingProgressColorResource(@ColorRes int mLoadingProgressColorRes) {
            return setLoadingProgressColor(getColor(mLoadingProgressColorRes));
        }

        /**
         * 设置加载中loading颜色--5.0有效--
         * 调用{@link #setLoadingTextColor(int)}会同步设置Loading颜色注意调用顺序
         *
         * @param mLoadingProgressColor
         * @return
         */
        public Builder setLoadingProgressColor(@ColorInt int mLoadingProgressColor) {
            this.mLoadingProgressColor = mLoadingProgressColor;
            return this;
        }

        public Builder setLoadingProgressDrawable(@DrawableRes int mLoadingProgressDrawable) {
            return setLoadingProgressDrawable(getDrawable(mLoadingProgressDrawable));
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

        public Builder setEmptyText(@StringRes int mEmptyText) {
            return setEmptyText(getText(mEmptyText));
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

        public Builder setEmptyTextColorResource(@ColorRes int mEmptyTextColor) {
            return setEmptyTextColor(getColor(mEmptyTextColor));
        }

        /**
         * 设置空布局文本颜色
         *
         * @param mEmptyTextColor
         * @return
         */
        public Builder setEmptyTextColor(int mEmptyTextColor) {
            this.mEmptyTextColor = mEmptyTextColor;
            return setEmptyImageColor(mEmptyTextColor);
        }

        public Builder setEmptyTextSizeResource(@DimenRes int mEmptyTextSizeRes) {
            return setEmptyTextSize(getDimensionPixelSize(mEmptyTextSizeRes));
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
         * 设置空布局Image颜色--默认设置与TextView颜色相同注意与
         * {@link #setEmptyTextColor(int)}顺序及{@link #setEmptyImageDrawable(Drawable)}顺序
         *
         * @param mEmptyImageColorRes
         * @return
         */
        public Builder setEmptyImageColorResource(@ColorRes int mEmptyImageColorRes) {
            return setEmptyImageColor(getColor(mEmptyImageColorRes));
        }

        /**
         * 设置空布局Image颜色--默认设置与TextView颜色相同注意与
         * {@link #setEmptyTextColor(int)}顺序及{@link #setEmptyImageDrawable(Drawable)}顺序
         *
         * @param mEmptyImageColor
         * @return
         */
        public Builder setEmptyImageColor(@ColorInt int mEmptyImageColor) {
            this.mEmptyImageColor = mEmptyImageColor;
            return setEmptyImageDrawable(mEmptyImageDrawable);
        }

        public Builder setEmptyImageDrawable(@DrawableRes int mEmptyImageDrawableRes) {
            return setEmptyImageDrawable(getDrawable(mEmptyImageDrawableRes));
        }

        /**
         * 设置空布局Image 背景
         *
         * @param mEmptyImageDrawable
         * @return
         */
        public Builder setEmptyImageDrawable(Drawable mEmptyImageDrawable) {
            this.mEmptyImageDrawable = FastUtil.getTintDrawable(mEmptyImageDrawable, mEmptyImageColor);
            return this;
        }

        public Builder setErrorText(@StringRes int mErrorText) {
            return setErrorText(getText(mErrorText));
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

        public Builder setErrorTextColorResource(@ColorRes int mErrorTextColorRes) {
            return setErrorTextColor(getColor(mErrorTextColorRes));
        }

        /**
         * 设置加载错误文本颜色
         *
         * @param mErrorTextColor
         * @return
         */
        public Builder setErrorTextColor(int mErrorTextColor) {
            this.mErrorTextColor = mErrorTextColor;
            return setErrorImageColor(mErrorTextColor);
        }

        public Builder setErrorTextSizeResource(@DimenRes int mErrorTextSizeRes) {
            return setErrorTextSize(getDimensionPixelSize(mErrorTextSizeRes));
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
         * 设置空布局Image颜色--默认设置与TextView颜色相同注意与
         * {@link #setErrorTextColor(int)}顺序及{@link #setErrorImageDrawable(Drawable)}顺序
         *
         * @param mErrorImageColorRes
         * @return
         */
        public Builder setErrorImageColorResource(@ColorRes int mErrorImageColorRes) {
            return setErrorImageColor(getColor(mErrorImageColorRes));
        }

        /**
         * 设置空布局Image颜色--默认设置与TextView颜色相同注意与
         * {@link #setErrorTextColor(int)}顺序及{@link #setErrorImageDrawable(Drawable)}顺序
         *
         * @param mErrorImageColor
         * @return
         */
        public Builder setErrorImageColor(@ColorInt int mErrorImageColor) {
            this.mErrorImageColor = mErrorImageColor;
            return setErrorImageDrawable(mErrorImageDrawable);
        }

        public Builder setErrorImageDrawable(@DrawableRes int mErrorImageDrawable) {
            return setErrorImageDrawable(getDrawable(mErrorImageDrawable));
        }

        /**
         * 设置错误布局Image 背景
         *
         * @param mErrorImageDrawable
         * @return
         */
        public Builder setErrorImageDrawable(Drawable mErrorImageDrawable) {
            this.mErrorImageDrawable = FastUtil.getTintDrawable(mErrorImageDrawable, mErrorImageColor);
            return this;
        }

        public Builder setNoNetText(@StringRes int mNoNetText) {
            return setNoNetText(getText(mNoNetText));
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

        public Builder setNoNetTextColorResource(@ColorRes int mNoNetTextColorRes) {
            return setNoNetTextColor(getColor(mNoNetTextColorRes));
        }

        /**
         * 设置网络错误文本颜色
         *
         * @param mNoNetTextColor
         * @return
         */
        public Builder setNoNetTextColor(int mNoNetTextColor) {
            this.mNoNetTextColor = mNoNetTextColor;
            return setNoNetImageColor(mNoNetTextColor);
        }

        public Builder setNoNetTextSizeResource(@DimenRes int mNoNetTextSizeRes) {
            return setNoNetTextSize(getDimensionPixelSize(mNoNetTextSizeRes));
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
         * 设置空布局Image颜色--默认设置与TextView颜色相同注意与
         * {@link #setNoNetTextColor(int)}顺序及{@link #setNoNetImageDrawable(Drawable)}顺序
         *
         * @param mNoNetImageColorRes
         * @return
         */
        public Builder setNoNetImageColorResource(@ColorRes int mNoNetImageColorRes) {
            return setNoNetImageColor(getColor(mNoNetImageColorRes));
        }

        /**
         * 设置空布局Image颜色--默认设置与TextView颜色相同注意与
         * {@link #setNoNetTextColor(int)}顺序及{@link #setNoNetImageDrawable(Drawable)}顺序
         *
         * @param mNoNetImageColor
         * @return
         */
        public Builder setNoNetImageColor(@ColorInt int mNoNetImageColor) {
            this.mNoNetImageColor = mNoNetImageColor;
            return setNoNetImageDrawable(mNoNetImageDrawable);
        }

        public Builder setNoNetImageDrawable(@DrawableRes int mNoNetImageDrawable) {
            return setNoNetImageDrawable(getDrawable(mNoNetImageDrawable));
        }

        /**
         * 设置无网络Image 背景
         *
         * @param mNoNetImageDrawable
         * @return
         */
        public Builder setNoNetImageDrawable(Drawable mNoNetImageDrawable) {
            this.mNoNetImageDrawable = FastUtil.getTintDrawable(mNoNetImageDrawable, mNoNetImageColor);
            return this;
        }

        /**
         * 设置ProgressBar 大小
         *
         * @param loadingSizeRes
         * @return
         */
        public Builder setLoadingSizeResource(@DimenRes int loadingSizeRes) {
            return setLoadingSize(getDimensionPixelSize(loadingSizeRes));
        }

        /**
         * 设置ProgressBar 大小
         *
         * @param loadingSize
         * @return
         */
        public Builder setLoadingSize(int loadingSize) {
            this.mLoadingSize = loadingSize;
            return this;
        }

        public Builder setImageWidthHeightResource(@DimenRes int imageWidthHeightRes) {
            return setImageWidthHeight(getDimensionPixelSize(imageWidthHeightRes));
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

        public Builder setImageWidthResource(@DimenRes int imageWidthRes) {
            return setImageWidth(getDimensionPixelSize(imageWidthRes));
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

        public Builder setImageHeightResource(@DimenRes int imageHeightRes) {
            return setImageHeight(getDimensionPixelSize(imageHeightRes));
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

        public Builder setTextMarginResource(@DimenRes int textMarginRes) {
            return setTextMargin(getDimensionPixelSize(textMarginRes));
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

        private Resources getResources() {
            return mContext.getResources();
        }

        private int getColor(@ColorRes int color) {
            return getResources().getColor(color);
        }

        public int getDimensionPixelSize(@DimenRes int dimen) {
            return getResources().getDimensionPixelSize(dimen);
        }

        private CharSequence getText(@StringRes int id) {
            return getResources().getText(id);
        }

        private Drawable getDrawable(@DrawableRes int res) {
            return getResources().getDrawable(res);
        }

        public FastMultiStatusView build() {
            return new FastMultiStatusView(this);
        }
    }
}
