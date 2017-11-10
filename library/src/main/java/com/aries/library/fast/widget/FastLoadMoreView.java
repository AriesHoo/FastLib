package com.aries.library.fast.widget;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.TypedValue;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aries.library.fast.R;
import com.aries.library.fast.util.SizeUtil;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

/**
 * Created: AriesHoo on 2017/11/1 11:40
 * E-Mail: AriesHoo@126.com
 * Function: 用于快速设置 BaseRecyclerViewAdapterHelper LoadMoreView相关布局属性
 * Description:
 */
public class FastLoadMoreView extends LoadMoreView {

    private boolean mIsInitView = false;

    private CharSequence mLoadingText;
    @ColorInt
    private int mLoadingTextColor;
    private int mLoadingTextSize;
    @ColorInt
    private int mLoadingProgressColor;
    private Drawable mLoadingProgressDrawable;
    private CharSequence mLoadFailText;
    @ColorInt
    private int mLoadFailTextColor;
    private int mLoadFailTextSize;
    private CharSequence mLoadEndText;
    @ColorInt
    private int mLoadEndTextColor;
    private int mLoadEndTextSize;
    private Builder mBuilder;

    public FastLoadMoreView() {
        this(new Builder());
    }

    FastLoadMoreView(Builder builder) {
        mBuilder = builder;
        this.mLoadingText = builder.mLoadingText;
        this.mLoadingTextColor = builder.mLoadingTextColor;
        this.mLoadingTextSize = builder.mLoadingTextSize;
        this.mLoadingProgressColor = builder.mLoadingProgressColor;
        this.mLoadingProgressDrawable = builder.mLoadingProgressDrawable;
        this.mLoadFailText = builder.mLoadFailText;
        this.mLoadFailTextColor = builder.mLoadFailTextColor;
        this.mLoadFailTextSize = builder.mLoadFailTextSize;
        this.mLoadEndText = builder.mLoadEndText;
        this.mLoadEndTextColor = builder.mLoadEndTextColor;
        this.mLoadEndTextSize = builder.mLoadEndTextSize;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fast_layout_load_more_view;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.lLayout_loadingFastLoadMore;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.fLayout_loadFailFastLoadMore;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.fLayout_loadEndFastLoadMore;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        super.convert(holder);
        if (!mIsInitView) {
            initView(holder);
        }
    }

    private void initView(BaseViewHolder holder) {
        if (holder == null) {
            return;
        }

        holder.setText(R.id.tv_loadingFastLoadMore, mLoadingText)
                .setTextColor(R.id.tv_loadingFastLoadMore, mLoadingTextColor)
                .setText(R.id.tv_loadFailFastLoadMore, mLoadFailText)
                .setTextColor(R.id.tv_loadFailFastLoadMore, mLoadFailTextColor)
                .setText(R.id.tv_loadEndFastLoadMore, mLoadEndText)
                .setTextColor(R.id.tv_loadEndFastLoadMore, mLoadEndTextColor);
        TextView tvLoading = holder.getView(R.id.tv_loadingFastLoadMore);
        TextView tvLoadFail = holder.getView(R.id.tv_loadFailFastLoadMore);
        TextView tvLoadEnd = holder.getView(R.id.tv_loadEndFastLoadMore);
        ProgressBar pbLoading = holder.getView(R.id.pb_loadingFastLoadMore);
        tvLoading.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLoadingTextSize);
        tvLoadFail.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLoadFailTextSize);
        tvLoadEnd.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLoadEndTextSize);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pbLoading.setIndeterminateTintList(ColorStateList.valueOf(mLoadingProgressColor));
        }
        if (mLoadingProgressDrawable != null) {
            mLoadingProgressDrawable.setBounds(pbLoading.getIndeterminateDrawable().getBounds());
            pbLoading.setIndeterminateDrawable(mLoadingProgressDrawable);
        }
        mIsInitView = true;
    }


    public Builder getBuilder() {
        if (mBuilder == null) {
            mBuilder = new Builder(this);
        }
        return mBuilder;
    }

    public static final class Builder {
        private final int DEFAULT_TEXT_COLOR = Color.BLACK;
        private final int DEFAULT_TEXT_SIZE = 14;
        CharSequence mLoadingText;
        @ColorInt
        int mLoadingTextColor;
        int mLoadingTextSize;
        @ColorInt
        int mLoadingProgressColor;
        Drawable mLoadingProgressDrawable;
        CharSequence mLoadFailText;
        @ColorInt
        int mLoadFailTextColor;
        int mLoadFailTextSize;
        CharSequence mLoadEndText;
        @ColorInt
        int mLoadEndTextColor;
        int mLoadEndTextSize;

        public Builder() {
            mLoadingTextColor = DEFAULT_TEXT_COLOR;
            mLoadFailTextColor = DEFAULT_TEXT_COLOR;
            mLoadEndTextColor = DEFAULT_TEXT_COLOR;
            mLoadingProgressColor = DEFAULT_TEXT_COLOR;
            mLoadingTextSize = SizeUtil.dp2px(DEFAULT_TEXT_SIZE);
            mLoadFailTextSize = SizeUtil.dp2px(DEFAULT_TEXT_SIZE);
            mLoadEndTextSize = SizeUtil.dp2px(DEFAULT_TEXT_SIZE);
        }

        public Builder(FastLoadMoreView fastLoadMoreView) {
            this.mLoadingText = fastLoadMoreView.mLoadingText;
            this.mLoadingTextColor = fastLoadMoreView.mLoadingTextColor;
            this.mLoadingTextSize = fastLoadMoreView.mLoadingTextSize;
            this.mLoadingProgressColor = fastLoadMoreView.mLoadingProgressColor;
            this.mLoadingProgressDrawable = fastLoadMoreView.mLoadingProgressDrawable;
            this.mLoadFailText = fastLoadMoreView.mLoadFailText;
            this.mLoadFailTextColor = fastLoadMoreView.mLoadFailTextColor;
            this.mLoadFailTextSize = fastLoadMoreView.mLoadFailTextSize;
            this.mLoadEndText = fastLoadMoreView.mLoadEndText;
            this.mLoadEndTextColor = fastLoadMoreView.mLoadEndTextColor;
            this.mLoadEndTextSize = fastLoadMoreView.mLoadEndTextSize;
        }

        /**
         * 设置所有TextView 文本颜色
         *
         * @param mLoadTextColor
         * @return
         */
        public Builder setLoadTextColor(@ColorInt int mLoadTextColor) {
            setLoadingTextColor(mLoadTextColor);
            setLoadFailTextColor(mLoadTextColor);
            setLoadEndTextColor(mLoadTextColor);
            return this;
        }

        /**
         * 设置所有TextView 文本尺寸
         *
         * @param mLoadTextSize
         * @return
         */
        public Builder setLoadTextSize(int mLoadTextSize) {
            setLoadingTextSize(mLoadTextSize);
            setLoadFailTextSize(mLoadTextSize);
            setLoadEndTextSize(mLoadTextSize);
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
        public Builder setLoadingTextColor(@ColorInt int mLoadingTextColor) {
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
         * 设置加载ProgressBar颜色--5.0及以上支持
         *
         * @param mLoadingProgressColor
         * @return
         */
        public Builder setLoadingProgressColor(@ColorInt int mLoadingProgressColor) {
            this.mLoadingProgressColor = mLoadingProgressColor;
            return this;
        }

        /**
         * 设置loading Drawable
         *
         * @param mLoadingProgressDrawable
         * @return
         */
        public Builder setLoadingProgressDrawable(Drawable mLoadingProgressDrawable) {
            this.mLoadingProgressDrawable = mLoadingProgressDrawable;
            return this;
        }

        /**
         * 设置加载失败文本
         *
         * @param mLoadFailText
         * @return
         */
        public Builder setLoadFailText(CharSequence mLoadFailText) {
            this.mLoadFailText = mLoadFailText;
            return this;
        }

        /**
         * 设置加载失败文本颜色
         *
         * @param mLoadFailTextColor
         * @return
         */
        public Builder setLoadFailTextColor(@ColorInt int mLoadFailTextColor) {
            this.mLoadFailTextColor = mLoadFailTextColor;
            return this;
        }

        /**
         * 设置加载失败文本尺寸
         *
         * @param mLoadFailTextSize
         * @return
         */
        public Builder setLoadFailTextSize(int mLoadFailTextSize) {
            this.mLoadFailTextSize = mLoadFailTextSize;
            return this;
        }

        /**
         * 设置加载结束文本
         *
         * @param mLoadEndText
         * @return
         */
        public Builder setLoadEndText(CharSequence mLoadEndText) {
            this.mLoadEndText = mLoadEndText;
            return this;
        }

        /**
         * 设置加载结束文本颜色
         *
         * @param mLoadEndTextColor
         * @return
         */
        public Builder setLoadEndTextColor(@ColorInt int mLoadEndTextColor) {
            this.mLoadEndTextColor = mLoadEndTextColor;
            return this;
        }

        /**
         * 设置加载结束文件尺寸
         *
         * @param mLoadEndTextSize
         * @return
         */
        public Builder setLoadEndTextSize(int mLoadEndTextSize) {
            this.mLoadEndTextSize = mLoadEndTextSize;
            return this;
        }

        public FastLoadMoreView build() {
            return new FastLoadMoreView(this);
        }
    }
}
