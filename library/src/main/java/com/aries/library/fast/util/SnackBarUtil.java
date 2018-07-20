package com.aries.library.fast.util;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/**
 * @Author: AriesHoo on 2018/7/16 16:45
 * @E-Mail: AriesHoo@126.com
 * Function: SnackBar工具类
 * Description:
 */
public class SnackBarUtil {

    private static final int DEFAULT_COLOR = 0xFEFFFFFF;
    public static final int LENGTH_INDEFINITE = Snackbar.LENGTH_INDEFINITE;
    public static final int LENGTH_SHORT = Snackbar.LENGTH_SHORT;
    public static final int LENGTH_LONG = Snackbar.LENGTH_LONG;

    @IntDef({LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    private static final int SUCCESS = 0xFF2BB600;
    private static final int WARNING = 0xFFFFC100;
    private static final int ERROR = 0xFFFF0000;
    private static final int MESSAGE = 0xFFFFFFFF;

    private static WeakReference<Snackbar> mWeakReference;

    private WeakReference<View> mParent;

    private CharSequence mMessage;
    private int mMessageColor;
    private int mBgColor;
    private int mBgResource;
    private int mDuration;
    private CharSequence mActionText;
    private int mActionTextColor;
    private View.OnClickListener mActionListener;
    private int mBottomMargin;

    private SnackBarUtil(final View parent) {
        setDefault();
        this.mParent = new WeakReference<>(parent);
    }

    private void setDefault() {
        mMessage = "";
        mMessageColor = DEFAULT_COLOR;
        mBgColor = DEFAULT_COLOR;
        mBgResource = -1;
        mDuration = LENGTH_SHORT;
        mActionText = "";
        mActionTextColor = DEFAULT_COLOR;
        mBottomMargin = 0;
    }

    /**
     * 设置SnackBar依赖view
     *
     * @param mParent 依赖view
     * @return {@link SnackBarUtil}
     */
    public static SnackBarUtil with(@NonNull final View mParent) {
        return new SnackBarUtil(mParent);
    }

    /**
     * 设置消息
     *
     * @param msg 消息
     * @return {@link SnackBarUtil}
     */
    public SnackBarUtil setMessage(@NonNull final CharSequence msg) {
        this.mMessage = msg;
        return this;
    }

    public SnackBarUtil setMessage(@NonNull final int res) {
        View view = mParent.get();
        if (view != null) {
            setMessage(view.getContext().getText(res));
        }
        return this;
    }

    /**
     * 设置消息颜色
     *
     * @param color 颜色
     * @return {@link SnackBarUtil}
     */
    public SnackBarUtil setMessageColor(@ColorInt final int color) {
        this.mMessageColor = color;
        return this;
    }

    /**
     * 设置背景色
     *
     * @param color 背景色
     * @return {@link SnackBarUtil}
     */
    public SnackBarUtil setBgColor(@ColorInt final int color) {
        this.mBgColor = color;
        return this;
    }

    /**
     * 设置背景资源
     *
     * @param bgResource 背景资源
     * @return {@link SnackBarUtil}
     */
    public SnackBarUtil setBgResource(@DrawableRes final int bgResource) {
        this.mBgResource = bgResource;
        return this;
    }

    /**
     * 设置显示时长
     *
     * @param duration 时长
     *                 <ul>
     *                 <li>{@link Duration#LENGTH_INDEFINITE}永久</li>
     *                 <li>{@link Duration#LENGTH_SHORT}短时</li>
     *                 <li>{@link Duration#LENGTH_LONG}长时</li>
     *                 </ul>
     * @return {@link SnackBarUtil}
     */
    public SnackBarUtil setDuration(@Duration final int duration) {
        this.mDuration = duration;
        return this;
    }

    /**
     * 设置行为
     *
     * @param text     文本
     * @param listener 事件
     * @return {@link SnackBarUtil}
     */
    public SnackBarUtil setAction(@NonNull final CharSequence text, @NonNull final View.OnClickListener listener) {
        return setAction(text, DEFAULT_COLOR, listener);
    }

    /**
     * 设置行为
     *
     * @param text     文本
     * @param color    文本颜色
     * @param listener 事件
     * @return {@link SnackBarUtil}
     */

    public SnackBarUtil setAction(@NonNull final CharSequence text, @ColorInt final int color, @NonNull final View.OnClickListener listener) {
        this.mActionText = text;
        this.mActionTextColor = color;
        this.mActionListener = listener;
        return this;
    }

    /**
     * 设置底边距
     *
     * @param bottomMargin 底边距
     */
    public SnackBarUtil setBottomMargin(@IntRange(from = 1) final int bottomMargin) {
        this.mBottomMargin = bottomMargin;
        return this;
    }

    /**
     * 显示SnackBar
     */
    public void show() {
        final View view = mParent.get();
        if (view == null) {
            return;
        }
        if (mMessageColor != DEFAULT_COLOR) {
            SpannableString spannableString = new SpannableString(mMessage);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(mMessageColor);
            spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mWeakReference = new WeakReference<>(Snackbar.make(view, spannableString, mDuration));
        } else {
            mWeakReference = new WeakReference<>(Snackbar.make(view, mMessage, mDuration));
        }
        final Snackbar snackbar = mWeakReference.get();
        final View snackView = snackbar.getView();
        if (mBgResource != -1) {
            snackView.setBackgroundResource(mBgResource);
        } else if (mBgColor != DEFAULT_COLOR) {
            snackView.setBackgroundColor(mBgColor);
        }
        if (mBottomMargin != 0) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackView.getLayoutParams();
            params.bottomMargin = mBottomMargin;
        }
        if (mActionText.length() > 0 && mActionListener != null) {
            if (mActionTextColor != DEFAULT_COLOR) {
                snackbar.setActionTextColor(mActionTextColor);
            }
            snackbar.setAction(mActionText, mActionListener);
        }
        snackbar.show();
    }

    /**
     * 显示预设成功的SnackBar
     */
    public void showSuccess() {
        mBgColor = SUCCESS;
        mMessageColor = MESSAGE;
        mActionTextColor = MESSAGE;
        show();
    }

    /**
     * 显示预设警告的SnackBar
     */
    public void showWarning() {
        mBgColor = WARNING;
        mMessageColor = MESSAGE;
        mActionTextColor = MESSAGE;
        show();
    }

    /**
     * 显示预设错误的SnackBar
     */
    public void showError() {
        mBgColor = ERROR;
        mMessageColor = MESSAGE;
        mActionTextColor = MESSAGE;
        show();
    }

    /**
     * 消失SnackBar
     */
    public static void dismiss() {
        if (mWeakReference != null && mWeakReference.get() != null) {
            mWeakReference.get().dismiss();
            mWeakReference = null;
        }
    }

    /**
     * 获取SnackBar视图
     *
     * @return SnackBar视图
     */
    public static View getView() {
        Snackbar snackbar = mWeakReference.get();
        if (snackbar == null) {
            return null;
        }
        return snackbar.getView();
    }

    /**
     * 添加SnackBar视图
     * <p>在{@link #show()}之后调用</p>
     *
     * @param layoutId 布局文件
     * @param params   布局参数
     */
    public static void addView(@LayoutRes final int layoutId, @NonNull final ViewGroup.LayoutParams params) {
        final View view = getView();
        if (view != null) {
            view.setPadding(0, 0, 0, 0);
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) view;
            View child = LayoutInflater.from(view.getContext()).inflate(layoutId, null);
            layout.addView(child, -1, params);
        }
    }

    /**
     * 添加SnackBar视图
     * <p>在{@link #show()}之后调用</p>
     *
     * @param child  要添加的view
     * @param params 布局参数
     */
    public static void addView(@NonNull final View child, @NonNull final ViewGroup.LayoutParams params) {
        final View view = getView();
        if (view != null) {
            view.setPadding(0, 0, 0, 0);
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) view;
            layout.addView(child, params);
        }
    }
}
