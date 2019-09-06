package com.aries.library.fast.basis;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;

import com.aries.library.fast.BasisHelper;

import butterknife.ButterKnife;

/**
 * @Author: AriesHoo on 2019/6/27 15:29
 * @E-Mail: AriesHoo@126.com
 * @Function: 包含View的BasisHelper
 * @Description:
 */
public abstract class BasisViewHelper<T extends BasisViewHelper> extends BasisHelper {

    protected View mContentView;

    public BasisViewHelper(Activity context) {
        super(context);
        getContentView();
    }

    /**
     * xml布局
     *
     * @return
     */
    public abstract @LayoutRes
    int getContentLayout();

    public View getContentView() {
        if (mContentView == null) {
            mContentView = View.inflate(mContext, getContentLayout(), null);
            mUnBinder = ButterKnife.bind(this, mContentView);
        }
        return mContentView;
    }

    protected T back() {
        return (T) this;
    }

    /**
     * 获取View
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int id) {
        return getContentView().findViewById(id);
    }

    public T setBackgroundColor(int id, int color) {
        View textView = getContentView().findViewById(id);
        textView.setBackgroundColor(color);
        return back();
    }

    public T setBackgroundResource(int id, int resId) {
        View textView = getContentView().findViewById(id);
        textView.setBackgroundResource(resId);
        return back();
    }

    public T setChecked(int viewId, boolean checked) {
        View view = getView(viewId);
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(checked);
        }
        return back();
    }

    public T setEnabled(int id, boolean enabled) {
        View view = getContentView().findViewById(id);
        view.setEnabled(enabled);
        return back();
    }

    public T setFakeBoldText(int id, boolean fakeBoldText) {
        TextView textView = getContentView().findViewById(id);
        textView.getPaint().setFakeBoldText(fakeBoldText);
        return back();
    }

    public T setTextSize(int id, float dp) {
        return setTextSize(id, TypedValue.COMPLEX_UNIT_DIP, dp);
    }

    public T setTextSize(int id, int unit, float dp) {
        TextView textView = getContentView().findViewById(id);
        textView.setTextSize(unit, dp);
        return back();
    }

    public T setText(int id, int resId) {
        return setText(id, mContext.getText(resId));
    }

    /**
     * 设置TextView文本
     *
     * @param id
     * @param text
     * @return
     */
    public T setText(int id, CharSequence text) {
        TextView textView = getContentView().findViewById(id);
        textView.setText(text);
        return back();
    }

    public T setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        return setTextColor(viewId, ColorStateList.valueOf(textColor));
    }

    public T setTextColor(@IdRes int viewId, ColorStateList colorStateList) {
        TextView view = getView(viewId);
        view.setTextColor(colorStateList);
        return back();
    }

    public T setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        return setImageDrawable(viewId, ContextCompat.getDrawable(mContext, imageResId));
    }

    public T setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        return setImageDrawable(viewId, new BitmapDrawable(bitmap));
    }

    public T setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return back();
    }

    public T setSelected(int id, boolean selected) {
        View view = getContentView().findViewById(id);
        view.setSelected(selected);
        return back();
    }

    public T setOnClickListener(int id, View.OnClickListener listener) {
        View view = getContentView().findViewById(id);
        view.setOnClickListener(listener);
        return back();
    }

    public T setVisibility(int id, int visibility) {
        View view = getContentView().findViewById(id);
        view.setVisibility(visibility);
        return back();
    }

    public T setContentViewGone(boolean visible) {
        getContentView().setVisibility(visible ? View.VISIBLE : View.GONE);
        return back();
    }

    public T setOnCheckedChangeListener(int id, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton button = getContentView().findViewById(id);
        button.setOnCheckedChangeListener(listener);
        return back();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContentView = null;
    }
}
