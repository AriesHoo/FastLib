package com.aries.library.fast.demo.util;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.ui.util.DrawableUtil;
import com.aries.ui.view.title.TitleBarView;

/**
 * @Author: AriesHoo on 2018/11/2 13:27
 * @E-Mail: AriesHoo@126.com
 * @Function: 标题栏背景颜色变更帮助类
 * @Description:
 */
public class ViewColorUtil {

    private static volatile ViewColorUtil sInstance;

    private ViewColorUtil() {
    }

    public static ViewColorUtil getInstance() {
        if (sInstance == null) {
            synchronized (ViewColorUtil.class) {
                if (sInstance == null) {
                    sInstance = new ViewColorUtil();
                }
            }
        }
        return sInstance;
    }

    public void changeColor(View rootView, int alpha, boolean mIsLight, boolean showText, boolean mutate) {
        if (rootView != null) {
            int color = Color.argb(mIsLight ? alpha : 255 - alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255);
            int colorText = !showText ? Color.argb(alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255) : color;
            if (rootView instanceof TitleBarView) {
                ((TitleBarView) rootView).setLeftTextDrawableTint(color)
                        .setTextColor(colorText)
                        .setRightTextDrawableTint(color)
                        .setActionTint(color);
            }
            if (rootView instanceof TextView) {
                //滚动视图
                TextView textView = (TextView) rootView;
                Drawable[] drawables = textView.getCompoundDrawables();
                for (Drawable item : drawables) {
                    if (item != null) {
                        if (mutate) {
                            item = item.mutate();
                        }
                        if (!showText) {
                            DrawableUtil.setTintDrawable(item, Color.argb(alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255));
                        } else {
                            DrawableUtil.setTintDrawable(item, color);
                        }
                    }
                }
                if (!showText) {
                    textView.setTextColor(Color.argb(alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255));
                } else {
                    textView.setTextColor(color);
                }
            } else if (rootView instanceof ImageView) {
                Drawable drawable = ((ImageView) rootView).getDrawable();
                drawable = drawable.mutate();
                DrawableUtil.setTintDrawable(drawable, color);
            } else if (rootView instanceof ViewGroup) {
                ViewGroup contentView = (ViewGroup) rootView;
                int size = contentView.getChildCount();
                //循环遍历所有子View
                for (int i = 0; i < size; i++) {
                    View childView = contentView.getChildAt(i);
                    //递归查找
                    changeColor(childView, alpha, mIsLight, showText, mutate);
                }
            }
        }
    }
}
