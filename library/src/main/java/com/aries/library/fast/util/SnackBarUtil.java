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
 * Created: AriesHoo on 2018/6/30/030 18:21
 * E-Mail: AriesHoo@126.com
 * Function:SnackBar工具类
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

    private WeakReference<View> parent;

    private CharSequence message;
    private int messageColor;
    private int bgColor;
    private int bgResource;
    private int duration;
    private CharSequence actionText;
    private int actionTextColor;
    private View.OnClickListener actionListener;
    private int bottomMargin;

    private SnackBarUtil(final View parent) {
        setDefault();
        this.parent = new WeakReference<>(parent);
    }

    private void setDefault() {
        message = "";
        messageColor = DEFAULT_COLOR;
        bgColor = DEFAULT_COLOR;
        bgResource = -1;
        duration = LENGTH_SHORT;
        actionText = "";
        actionTextColor = DEFAULT_COLOR;
        bottomMargin = 0;
    }

    /**
     * 设置SnackBar依赖view
     *
     * @param parent 依赖view
     * @return {@link SnackBarUtil}
     */
    public static SnackBarUtil with(@NonNull final View parent) {
        return new SnackBarUtil(parent);
    }

    /**
     * 设置消息
     *
     * @param msg 消息
     * @return {@link SnackBarUtil}
     */
    public SnackBarUtil setMessage(@NonNull final CharSequence msg) {
        this.message = msg;
        return this;
    }

    /**
     * 设置消息颜色
     *
     * @param color 颜色
     * @return {@link SnackBarUtil}
     */
    public SnackBarUtil setMessageColor(@ColorInt final int color) {
        this.messageColor = color;
        return this;
    }

    /**
     * 设置背景色
     *
     * @param color 背景色
     * @return {@link SnackBarUtil}
     */
    public SnackBarUtil setBgColor(@ColorInt final int color) {
        this.bgColor = color;
        return this;
    }

    /**
     * 设置背景资源
     *
     * @param bgResource 背景资源
     * @return {@link SnackBarUtil}
     */
    public SnackBarUtil setBgResource(@DrawableRes final int bgResource) {
        this.bgResource = bgResource;
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
        this.duration = duration;
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
        this.actionText = text;
        this.actionTextColor = color;
        this.actionListener = listener;
        return this;
    }

    /**
     * 设置底边距
     *
     * @param bottomMargin 底边距
     */
    public SnackBarUtil setBottomMargin(@IntRange(from = 1) final int bottomMargin) {
        this.bottomMargin = bottomMargin;
        return this;
    }

    /**
     * 显示SnackBar
     */
    public void show() {
        final View view = parent.get();
        if (view == null) return;
        if (messageColor != DEFAULT_COLOR) {
            SpannableString spannableString = new SpannableString(message);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(messageColor);
            spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mWeakReference = new WeakReference<>(Snackbar.make(view, spannableString, duration));
        } else {
            mWeakReference = new WeakReference<>(Snackbar.make(view, message, duration));
        }
        final Snackbar snackbar = mWeakReference.get();
        final View snackView = snackbar.getView();
        if (bgResource != -1) {
            snackView.setBackgroundResource(bgResource);
        } else if (bgColor != DEFAULT_COLOR) {
            snackView.setBackgroundColor(bgColor);
        }
        if (bottomMargin != 0) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackView.getLayoutParams();
            params.bottomMargin = bottomMargin;
        }
        if (actionText.length() > 0 && actionListener != null) {
            if (actionTextColor != DEFAULT_COLOR) {
                snackbar.setActionTextColor(actionTextColor);
            }
            snackbar.setAction(actionText, actionListener);
        }
        snackbar.show();
    }

    /**
     * 显示预设成功的SnackBar
     */
    public void showSuccess() {
        bgColor = SUCCESS;
        messageColor = MESSAGE;
        actionTextColor = MESSAGE;
        show();
    }

    /**
     * 显示预设警告的SnackBar
     */
    public void showWarning() {
        bgColor = WARNING;
        messageColor = MESSAGE;
        actionTextColor = MESSAGE;
        show();
    }

    /**
     * 显示预设错误的SnackBar
     */
    public void showError() {
        bgColor = ERROR;
        messageColor = MESSAGE;
        actionTextColor = MESSAGE;
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
        if (snackbar == null) return null;
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
