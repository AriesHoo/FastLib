package com.aries.library.fast.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Toast;

import com.aries.library.fast.FastConstant;
import com.aries.library.fast.FastManager;
import com.aries.library.fast.R;
import com.aries.library.fast.i.ToastControl;
import com.aries.ui.view.radius.RadiusTextView;

import androidx.annotation.ColorInt;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: AriesHoo on 2018/7/23 14:37
 * @E-Mail: AriesHoo@126.com
 * Function: Toast 工具
 * Description:
 * 1、2018-7-11 15:40:26 去掉Toast返回值并新增子线程弹出Toast功能
 * 2、2019-1-18 18:09:07 新增{@link ToastControl} 全局
 */
public class ToastUtil {

    public static Context sContext;
    private static Toast sSystemToast;
    private static RadiusTextView sTextView;
    /**
     * 是否前台运行才显示toast
     */
    private static boolean sIsShowRunningForeground;
    private static Builder sBuilder;
    private static Builder sBuilderSuccess;
    private static Builder sBuilderFailed;
    private static Builder sBuilderWarning;

    public static void init(Context context) {
        init(context, false);
    }

    public static void init(Context context, boolean isShowRunningForeground) {
        init(context, isShowRunningForeground, getBuilder());
    }

    /**
     * @param context
     * @param isShowRunningForeground 是否前台运行才显示toast
     * @param builder                 Toast配置
     */
    public static void init(Context context, boolean isShowRunningForeground, Builder builder) {
        if (context != null) {
            sContext = context.getApplicationContext();
        }
        sIsShowRunningForeground = isShowRunningForeground;
        sBuilder = builder;
    }

    public static void show(int content) {
        show(content, sIsShowRunningForeground);
    }

    public static void show(int content, boolean isShowRunningForeground) {
        show(content, isShowRunningForeground, getBuilder());
    }

    public static void show(int content, Builder builder) {
        show(content, sIsShowRunningForeground, builder);
    }

    public static void show(int content, boolean isShowRunningForeground, Builder builder) {
        if (null == sContext) {
            throw new NullPointerException(FastConstant.EXCEPTION_NOT_INIT);
        }
        show(sContext.getText(content), isShowRunningForeground, builder);
    }

    public static void show(CharSequence content) {
        show(content, sIsShowRunningForeground);
    }

    public static void show(CharSequence content, boolean isShowRunningForeground) {
        show(content, isShowRunningForeground, getBuilder());
    }

    public static void show(CharSequence content, Builder builder) {
        show(content, sIsShowRunningForeground, builder);
    }

    /**
     * @param content
     * @param isShowRunningForeground
     * @param builder
     * @return
     */
    public static void show(final CharSequence content, final boolean isShowRunningForeground, final Builder builder) {
        if (null == sContext) {
            throw new NullPointerException(FastConstant.EXCEPTION_NOT_INIT);
        } else {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                showToast(content, isShowRunningForeground, builder);
                return;
            }
            Observable.just("")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>() {
                        @Override
                        public void onNext(String s) {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            showToast(content, isShowRunningForeground, builder);
                        }
                    });
        }
    }

    private static void showToast(CharSequence content, boolean isShowRunningForeground, Builder builder) {
        //过滤空字符情况
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(content.toString().trim())) {
            return;
        }
        //修复快速点击无法显示的问题,修复超过50之后无法显示的问题
        sSystemToast = SingleToast.getInstance();
        sTextView = new RadiusTextView(sContext);
        if (builder == null) {
            builder = getBuilder();
        }
        int duration = builder.duration == Toast.LENGTH_LONG || builder.duration == Toast.LENGTH_SHORT ? builder.duration :
                content.length() > 10 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        sTextView.getDelegate()
                .setTextColor(builder.textColor)
                .setRadius(builder.radius)
                .setStrokeColor(builder.strokeColor)
                .setBackgroundColor(builder.backgroundColor)
                .setLeftDrawableHeight(builder.textDrawableGravity == Gravity.LEFT ? builder.textDrawableHeight : 0)
                .setLeftDrawableWidth(builder.textDrawableGravity == Gravity.LEFT ? builder.textDrawableWidth : 0)
                .setLeftDrawable(builder.textDrawableGravity == Gravity.LEFT ? builder.textDrawable : null)
                .setTopDrawableHeight(builder.textDrawableGravity == Gravity.TOP ? builder.textDrawableHeight : 0)
                .setTopDrawableWidth(builder.textDrawableGravity == Gravity.TOP ? builder.textDrawableWidth : 0)
                .setTopDrawable(builder.textDrawableGravity == Gravity.TOP ? builder.textDrawable : null)
                .setRightDrawableHeight(builder.textDrawableGravity == Gravity.RIGHT ? builder.textDrawableHeight : 0)
                .setRightDrawableWidth(builder.textDrawableGravity == Gravity.RIGHT ? builder.textDrawableWidth : 0)
                .setRightDrawable(builder.textDrawableGravity == Gravity.RIGHT ? builder.textDrawable : null)
                .setBottomDrawableHeight(builder.textDrawableGravity == Gravity.BOTTOM ? builder.textDrawableHeight : 0)
                .setBottomDrawableWidth(builder.textDrawableGravity == Gravity.BOTTOM ? builder.textDrawableWidth : 0)
                .setBottomDrawable(builder.textDrawableGravity == Gravity.BOTTOM ? builder.textDrawable : null)
                .setRippleEnable(false)
                .init();
        sTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.textSize);
        sTextView.setPadding(builder.paddingLeft, builder.paddingTop, builder.paddingRight, builder.paddingBottom);
        sTextView.setCompoundDrawablePadding(builder.textDrawablePadding);
        sTextView.setGravity(builder.textGravity);
        if (builder.background != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sTextView.setBackground(builder.background);
            } else {
                sTextView.setBackgroundDrawable(builder.background);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sTextView.setElevation(builder.elevation);
        }
        sTextView.setMinimumHeight(builder.minHeight);
        sTextView.setMinimumWidth(builder.minWidth);
        sTextView.setText(content);
        sSystemToast.setView(sTextView);
        sSystemToast.setDuration(duration);
        sSystemToast.setGravity(builder.gravity,
                builder.gravityXOffset > -1 ? builder.gravityXOffset : 0,
                builder.gravityYOffset > -1 ? builder.gravityYOffset :
                        builder.gravity == Gravity.BOTTOM ? SizeUtil.dp2px(64) : 0);
        ToastControl control = FastManager.getInstance().getToastControl();
        if (control != null) {
            control.setToast(sSystemToast, sTextView);
        }
        if (!isShowRunningForeground || (isShowRunningForeground && FastUtil.isRunningForeground(sContext))) {
            sSystemToast.show();
        }
    }

    public static void showSuccess(CharSequence msg) {
        show(msg, sIsShowRunningForeground, getSuccessBuilder());
    }

    public static void showSuccess(int msg) {
        show(msg, sIsShowRunningForeground, getSuccessBuilder());
    }

    public static void showFailed(CharSequence msg) {
        show(msg, sIsShowRunningForeground, getFailedBuilder());
    }

    public static void showFailed(int msg) {
        show(msg, sIsShowRunningForeground, getFailedBuilder());
    }

    public static void showWarning(CharSequence msg) {
        show(msg, sIsShowRunningForeground, getWarningBuilder());
    }

    public static void showWarning(int msg) {
        show(msg, sIsShowRunningForeground, getWarningBuilder());
    }

    /**
     * 获取当前全局设置属性Builder
     *
     * @return
     */
    public static Builder getBuilder() {
        if (sBuilder == null) {
            sBuilder = new Builder();
        }
        return sBuilder;
    }

    public static Builder getSuccessBuilder() {
        if (sBuilderSuccess == null) {
            sBuilderSuccess = getDrawableBuilder(R.drawable.fast_ic_success);
        }
        return sBuilderSuccess;
    }

    public static Builder getFailedBuilder() {
        if (sBuilderFailed == null) {
            sBuilderFailed = getDrawableBuilder(R.drawable.fast_ic_failed);
        }
        return sBuilderFailed;
    }

    public static Builder getWarningBuilder() {
        if (sBuilderWarning == null) {
            sBuilderWarning = getDrawableBuilder(R.drawable.fast_ic_warning);
        }
        return sBuilderWarning;
    }

    private static Builder getDrawableBuilder(int res) {
        if (null == sContext) {
            throw new NullPointerException(FastConstant.EXCEPTION_NOT_INIT);
        }
        return new Builder()
                .setElevation(8)
                .setTextDrawable(sContext.getResources().getDrawable(res))
                .setTextDrawableGravity(Gravity.TOP)
                .setTextDrawablePadding(SizeUtil.dp2px(10))
                .setTextDrawableWidth(SizeUtil.dp2px(36))
                .setTextDrawableHeight(SizeUtil.dp2px(36))
                .setTextGravity(Gravity.CENTER)
                .setPaddingLeft(SizeUtil.dp2px(24))
                .setPaddingTop(SizeUtil.dp2px(20))
                .setPaddingRight(SizeUtil.dp2px(24))
                .setPaddingBottom(SizeUtil.dp2px(20))
                .setRadius(SizeUtil.dp2px(8))
                .setTextSize(SizeUtil.dp2px(16))
                .setGravityYOffset(0)
                .setGravity(Gravity.CENTER)
                .setMinWidth(SizeUtil.dp2px(140));

    }

    /**
     * 新建属性Builder
     *
     * @return
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        int gravity;
        int duration;
        float elevation;
        int gravityXOffset;
        int gravityYOffset;
        int textGravity;
        @ColorInt
        int textColor;
        int textSize;
        Drawable textDrawable;
        int textDrawableWidth;
        int textDrawableHeight;
        int textDrawablePadding;
        int textDrawableGravity;
        int paddingLeft;
        int paddingTop;
        int paddingRight;
        int paddingBottom;
        Drawable background;
        @ColorInt
        int backgroundColor;
        @ColorInt
        int strokeColor;
        int strokeWidth;
        int radius;
        int minWidth;
        int minHeight;

        public Builder() {
            setGravity(Gravity.BOTTOM)
                    .setGravityXOffset(-1)
                    .setGravityYOffset(-1)
                    .setDuration(-1)
                    .setElevation(0)
                    .setTextGravity(Gravity.LEFT)
                    .setTextColor(Color.WHITE)
                    .setTextSize(SizeUtil.dp2px(14))
                    .setTextDrawable(null)
                    .setTextDrawableWidth(-1)
                    .setTextDrawableHeight(-1)
                    .setTextDrawablePadding(SizeUtil.dp2px(2))
                    .setTextDrawableGravity(Gravity.LEFT)
                    .setPaddingLeft(SizeUtil.dp2px(16))
                    .setPaddingTop(SizeUtil.dp2px(10))
                    .setPaddingRight(SizeUtil.dp2px(16))
                    .setPaddingBottom(SizeUtil.dp2px(10))
                    .setBackground(null)
                    .setBackgroundColor(Color.argb(187, 0, 0, 0))
                    .setStrokeColor(Color.TRANSPARENT)
                    .setStrokeWidth(0)
                    .setRadius(SizeUtil.dp2px(6f))
                    .setMinWidth(0)
                    .setMinHeight(0);
        }

        /**
         * 设置Toast位置 {@link Gravity }{@link Toast#setGravity(int, int, int)}
         *
         * @param gravity
         * @return
         */
        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * 设置Toast位置x轴偏移量 {@link Toast#setGravity(int, int, int)}
         *
         * @param gravityXOffset
         * @return
         */
        public Builder setGravityXOffset(int gravityXOffset) {
            this.gravityXOffset = gravityXOffset;
            return this;
        }

        /**
         * 设置Toast位置y轴偏移量{@link Toast#setGravity(int, int, int)}
         *
         * @param gravityYOffset
         * @return
         */
        public Builder setGravityYOffset(int gravityYOffset) {
            this.gravityYOffset = gravityYOffset;
            return this;
        }

        /**
         * 显示时长
         * {@link Toast#LENGTH_LONG} 3500ms
         * {@link Toast#LENGTH_SHORT} 2000ms
         * 其它参数则根据长度 >10 长时长 其它短时长
         *
         * @param duration
         * @return
         */
        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        /**
         * 设置海拔效果 5.0以上{@link android.view.View#setElevation(float)}
         *
         * @param elevation
         * @return
         */
        public Builder setElevation(float elevation) {
            this.elevation = elevation;
            return this;
        }

        /**
         * 设置文本位置{@link Gravity} {@link android.widget.TextView#setGravity(int)}
         *
         * @param textGravity
         * @return
         */
        public Builder setTextGravity(int textGravity) {
            this.textGravity = textGravity;
            return this;
        }

        /**
         * 设置文本颜色{@link com.aries.ui.view.radius.delegate.RadiusTextViewDelegate#setTextColor(int)}
         *
         * @param textColor
         * @return
         */
        public Builder setTextColor(@ColorInt int textColor) {
            this.textColor = textColor;
            return this;
        }

        /**
         * 设置文本尺寸 px 可通过
         * {@link SizeUtil}的各种方法进行设置
         * {@link SizeUtil#dp2px(float)}
         * {@link SizeUtil#sp2px(float)}
         * {@link android.widget.TextView#setTextSize(int, float)}
         *
         * @param textSize
         * @return
         */
        public Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        /**
         * 设置文本Drawable 图标资源
         * {@link android.widget.TextView#setCompoundDrawables(Drawable, Drawable, Drawable, Drawable)}
         *
         * @param textDrawable
         * @return
         */
        public Builder setTextDrawable(Drawable textDrawable) {
            this.textDrawable = textDrawable;
            return this;
        }

        /**
         * 设置文本Drawable图标资源宽度 默认为原始宽度
         *
         * @param textDrawableWidth
         * @return
         */
        public Builder setTextDrawableWidth(int textDrawableWidth) {
            this.textDrawableWidth = textDrawableWidth;
            return this;
        }

        /**
         * 设置文本Drawable图标资源高度 默认为原始高度
         *
         * @param textDrawableHeight
         * @return
         */
        public Builder setTextDrawableHeight(int textDrawableHeight) {
            this.textDrawableHeight = textDrawableHeight;
            return this;
        }

        /**
         * 设置文本与drawable资源间距
         *
         * @param textDrawablePadding
         * @return
         */
        public Builder setTextDrawablePadding(int textDrawablePadding) {
            this.textDrawablePadding = textDrawablePadding;
            return this;
        }

        /**
         * 设置文本资源位置
         * {@link Gravity#LEFT}
         * {@link Gravity#TOP}
         * {@link Gravity#RIGHT}
         * {@link Gravity#BOTTOM}
         *
         * @param textDrawableGravity
         * @return
         */
        public Builder setTextDrawableGravity(int textDrawableGravity) {
            this.textDrawableGravity = textDrawableGravity;
            return this;
        }

        /**
         * 设置文本左边距
         *
         * @param paddingLeft
         * @return
         */
        public Builder setPaddingLeft(int paddingLeft) {
            this.paddingLeft = paddingLeft;
            return this;
        }

        /**
         * 设置文本上边距
         *
         * @param paddingTop
         * @return
         */
        public Builder setPaddingTop(int paddingTop) {
            this.paddingTop = paddingTop;
            return this;
        }

        /**
         * 设置文本右边距
         *
         * @param paddingRight
         * @return
         */
        public Builder setPaddingRight(int paddingRight) {
            this.paddingRight = paddingRight;
            return this;
        }

        /**
         * 设置文本下边距
         *
         * @param paddingBottom
         * @return
         */
        public Builder setPaddingBottom(int paddingBottom) {
            this.paddingBottom = paddingBottom;
            return this;
        }

        /**
         * 设置背景样式可以设置定义GradientDrawable样式达到设置线框及圆角 xml及java代码均可
         *
         * @param background
         * @return
         */
        public Builder setBackground(Drawable background) {
            this.background = background;
            return this;
        }

        /**
         * 设置背景色 通过设置 GradientDrawable 类型的背景同时可以控制 线框颜色、线框宽度、圆角弧度
         * 设置这4种样式时确保setBackground 为null
         *
         * @param backgroundColor
         * @return
         */
        public Builder setBackgroundColor(@ColorInt int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        /**
         * 设置背景边框颜色--需同步设置setBackgroundColor及setStrokeWidth
         *
         * @param strokeColor
         * @return
         */
        public Builder setStrokeColor(int strokeColor) {
            this.strokeColor = strokeColor;
            return this;
        }

        /**
         * 设置背景边框宽度
         *
         * @param strokeWidth
         * @return
         */
        public Builder setStrokeWidth(int strokeWidth) {
            this.strokeWidth = strokeWidth;
            return this;
        }

        /**
         * 设置背景圆角弧度
         *
         * @param radius
         * @return
         */
        public Builder setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder setMinWidth(int minWidth) {
            this.minWidth = minWidth;
            return this;
        }

        public Builder setMinHeight(int minHeight) {
            this.minHeight = minHeight;
            return this;
        }
    }

    public static class SingleToast {
        private static Toast mToast;

        private static class SingleToastHolder {
            private static final SingleToast INSTANCE = new SingleToast();
        }

        private SingleToast() {
            mToast = new Toast(sContext);
        }

        private Toast getToast() {
            return mToast;
        }

        public static final Toast getInstance() {
            ToastControl control = FastManager.getInstance().getToastControl();
            if (control != null && control.getToast() != null) {
                return control.getToast();
            }
            //目前发现Android 9.0版本系统Toast做了单例操作造成短时间快速Toast 后面无法弹出问题
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return new Toast(sContext);
            }
            return SingleToastHolder.INSTANCE.getToast();
        }
    }
}
