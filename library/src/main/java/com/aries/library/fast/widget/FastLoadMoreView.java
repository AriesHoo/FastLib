package com.aries.library.fast.widget;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aries.library.fast.R;
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView;
import com.chad.library.adapter.base.loadmore.LoadMoreStatus;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * @Author: AriesHoo on 2018/7/23 14:38
 * @E-Mail: AriesHoo@126.com
 * Function: 用于快速设置 BaseRecyclerViewAdapterHelper LoadMoreView相关布局属性
 * Description:
 */
public class FastLoadMoreView extends BaseLoadMoreView {

    private BaseViewHolder mHolder;

    private Context mContext;
    private int mLoadingSize;
    private CharSequence mLoadingText;
    @ColorInt
    private int mLoadingTextColor;
    private int mLoadingTextSize;
    private boolean mLoadingTextFakeBold;
    @ColorInt
    private int mLoadingProgressColor;
    private Drawable mLoadingProgressDrawable;
    private CharSequence mLoadFailText;
    @ColorInt
    private int mLoadFailTextColor;
    private int mLoadFailTextSize;
    private boolean mLoadFailTextFakeBold;
    private CharSequence mLoadEndText;
    @ColorInt
    private int mLoadEndTextColor;
    private int mLoadEndTextSize;
    private boolean mLoadEndTextFakeBold;
    private Builder mBuilder;

    public FastLoadMoreView(@Nullable Context context) {
        this(new Builder(context));
    }

    FastLoadMoreView(Builder builder) {
        this.mContext = builder.mContext;
        this.mBuilder = builder;
        this.mLoadingSize = builder.mLoadingSize;
        this.mLoadingText = builder.mLoadingText;
        this.mLoadingTextColor = builder.mLoadingTextColor;
        this.mLoadingTextSize = builder.mLoadingTextSize;
        this.mLoadingTextFakeBold = builder.mLoadingTextFakeBold;
        this.mLoadingProgressColor = builder.mLoadingProgressColor;
        this.mLoadingProgressDrawable = builder.mLoadingProgressDrawable;
        this.mLoadFailText = builder.mLoadFailText;
        this.mLoadFailTextColor = builder.mLoadFailTextColor;
        this.mLoadFailTextSize = builder.mLoadFailTextSize;
        this.mLoadFailTextFakeBold = builder.mLoadFailTextFakeBold;
        this.mLoadEndText = builder.mLoadEndText;
        this.mLoadEndTextColor = builder.mLoadEndTextColor;
        this.mLoadEndTextSize = builder.mLoadEndTextSize;
        this.mLoadEndTextFakeBold = builder.mLoadEndTextFakeBold;
    }

    @NotNull
    @Override
    public View getLoadComplete(@NotNull BaseViewHolder holder) {
        return holder.getView(R.id.fLayout_loadCompleteFastLoadMore);
    }

    @NotNull
    @Override
    public View getLoadEndView(@NotNull BaseViewHolder holder) {
        return holder.getView(R.id.fLayout_loadEndFastLoadMore);
    }

    @NotNull
    @Override
    public View getLoadFailView(@NotNull BaseViewHolder holder) {
        return holder.getView(R.id.fLayout_loadFailFastLoadMore);
    }

    @NotNull
    @Override
    public View getLoadingView(@NotNull BaseViewHolder holder) {
        return holder.getView(R.id.lLayout_loadingFastLoadMore);
    }

    @NotNull
    @Override
    public View getRootView(@NotNull ViewGroup viewGroup) {
        return  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fast_layout_load_more_view, viewGroup, false);
//        return View.inflate(viewGroup.getContext(), R.layout.fast_layout_load_more_view, null);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, LoadMoreStatus loadMoreStatus) {
        super.convert(holder, position, loadMoreStatus);
        if (holder != mHolder) {
            initView(holder);
        }
        mHolder = holder;
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
        tvLoading.getPaint().setFakeBoldText(mLoadingTextFakeBold);
        tvLoadFail.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLoadFailTextSize);
        tvLoadFail.getPaint().setFakeBoldText(mLoadFailTextFakeBold);
        tvLoadEnd.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLoadEndTextSize);
        tvLoadEnd.getPaint().setFakeBoldText(mLoadEndTextFakeBold);
        if (mLoadingSize >= 0) {
            pbLoading.getIndeterminateDrawable().setBounds(0, 0, mLoadingSize, mLoadingSize);
            ViewGroup.LayoutParams params = pbLoading.getLayoutParams();
            params.width = mLoadingSize;
            params.height = mLoadingSize;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pbLoading.setIndeterminateTintList(ColorStateList.valueOf(mLoadingProgressColor));
        }
        if (mLoadingProgressDrawable != null) {
            mLoadingProgressDrawable.setBounds(pbLoading.getIndeterminateDrawable().getBounds());
            pbLoading.setIndeterminateDrawable(mLoadingProgressDrawable);
        }
    }


    public Builder getBuilder() {
        if (mBuilder == null) {
            mBuilder = new Builder(this);
        }
        return mBuilder;
    }

    public static final class Builder {

        Context mContext;
        int mLoadingSize;
        CharSequence mLoadingText;
        @ColorInt
        int mLoadingTextColor;
        int mLoadingTextSize;
        boolean mLoadingTextFakeBold;
        @ColorInt
        int mLoadingProgressColor;
        Drawable mLoadingProgressDrawable;
        CharSequence mLoadFailText;
        @ColorInt
        int mLoadFailTextColor;
        int mLoadFailTextSize;
        boolean mLoadFailTextFakeBold;
        CharSequence mLoadEndText;
        @ColorInt
        int mLoadEndTextColor;
        int mLoadEndTextSize;
        boolean mLoadEndTextFakeBold;

        public Builder(@Nullable Context context) {
            mContext = context;
            setLoadTextColorResource(R.color.colorLoadMoreText);
            setLoadEndTextColorResource(android.R.color.darker_gray);
            setLoadTextSizeResource(R.dimen.dp_load_more_text_size);
            setLoadTextFakeBold(false);
            setLoadingProgressColorResource(R.color.colorLoadMoreProgress);

            setLoadingSize(-1);
            setLoadingText(R.string.fast_load_more_loading);
            setLoadFailText(R.string.fast_load_more_load_failed);
            setLoadEndText(R.string.fast_load_more_load_end);
        }

        public Builder(FastLoadMoreView fastLoadMoreView) {
            this.mContext = fastLoadMoreView.mContext;
            this.mLoadingSize = fastLoadMoreView.mLoadingSize;
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
         * 设置所有TextView颜色资源
         *
         * @param mLoadTextColorRes
         * @return
         */
        public Builder setLoadTextColorResource(@ColorRes int mLoadTextColorRes) {
            return setLoadTextColor(getColor(mLoadTextColorRes));
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
         * 设置所有TextView 文本尺寸资源
         *
         * @param mLoadTextSizeRes
         * @return
         */
        public Builder setLoadTextSizeResource(@DimenRes int mLoadTextSizeRes) {
            return setLoadTextSize(getDimensionPixelSize(mLoadTextSizeRes));
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
         * 设置所有TextView 文本粗体
         *
         * @param mLoadTextFakeBold
         * @return
         */
        public Builder setLoadTextFakeBold(boolean mLoadTextFakeBold) {
            setLoadingTextFakeBold(mLoadTextFakeBold);
            setLoadFailTextFakeBold(mLoadTextFakeBold);
            setLoadEndTextFakeBold(mLoadTextFakeBold);
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

        /**
         * 设置加载中文本资源
         *
         * @param mLoadingText
         * @return
         */
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

        /**
         * 设置加载中文本颜色资源
         *
         * @param mLoadingTextColorRes
         * @return
         */
        public Builder setLoadingTextColorResource(@ColorRes int mLoadingTextColorRes) {
            return setLoadingTextColor(getColor(mLoadingTextColorRes));
        }

        /**
         * 设置加载中文本颜色--同步设置 ProgressBar颜色
         * {@link #setLoadingProgressColor(int)} 注意调用顺序
         *
         * @param mLoadingTextColor
         * @return
         */
        public Builder setLoadingTextColor(@ColorInt int mLoadingTextColor) {
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

        /**
         * 设置加载中文本粗体
         *
         * @param mLoadingTextFakeBold
         * @return
         */
        public Builder setLoadingTextFakeBold(boolean mLoadingTextFakeBold) {
            this.mLoadingTextFakeBold = mLoadingTextFakeBold;
            return this;
        }

        /**
         * 设置加载ProgressBar颜色资源
         *
         * @param mLoadingProgressColorRes
         * @return
         */
        public Builder setLoadingProgressColorResource(@ColorRes int mLoadingProgressColorRes) {
            return setLoadingProgressColor(getColor(mLoadingProgressColorRes));
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

        public Builder setLoadingProgressDrawable(@DrawableRes int mLoadingProgressDrawable) {
            return setLoadingProgressDrawable(getResources().getDrawable(mLoadingProgressDrawable));
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

        public Builder setLoadFailText(@StringRes int mLoadFailText) {
            return setLoadFailText(getText(mLoadFailText));
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
         * 设置加载失败文本颜色资源
         *
         * @param mLoadFailTextColorRes
         * @return
         */
        public Builder setLoadFailTextColorResource(@ColorRes int mLoadFailTextColorRes) {
            return setLoadFailTextColor(getColor(mLoadFailTextColorRes));
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

        public Builder setLoadFailTextSizeResource(@DimenRes int mLoadFailTextSizeRes) {
            return setLoadFailTextSize(getDimensionPixelSize(mLoadFailTextSizeRes));
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
         * 设置加载失败文本粗体
         *
         * @param mLoadFailTextFakeBold
         * @return
         */
        public Builder setLoadFailTextFakeBold(boolean mLoadFailTextFakeBold) {
            this.mLoadFailTextFakeBold = mLoadFailTextFakeBold;
            return this;
        }

        public Builder setLoadEndText(@StringRes int mLoadEndText) {
            return setLoadEndText(getText(mLoadEndText));
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
         * 设置加载结束文本颜色资源
         *
         * @param mLoadEndTextColorRes
         * @return
         */
        public Builder setLoadEndTextColorResource(@ColorRes int mLoadEndTextColorRes) {
            return setLoadEndTextColor(getColor(mLoadEndTextColorRes));
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

        public Builder setLoadEndTextSizeResource(@DimenRes int mLoadEndTextSizeRes) {
            return setLoadEndTextSize(getDimensionPixelSize(mLoadEndTextSizeRes));
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

        /**
         * 设置加载结束文本粗体
         *
         * @param mLoadEndTextFakeBold
         * @return
         */
        public Builder setLoadEndTextFakeBold(boolean mLoadEndTextFakeBold) {
            this.mLoadEndTextFakeBold = mLoadEndTextFakeBold;
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

        public FastLoadMoreView build() {
            return new FastLoadMoreView(this);
        }
    }
}
