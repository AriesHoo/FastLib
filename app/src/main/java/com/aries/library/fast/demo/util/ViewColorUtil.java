package com.aries.library.fast.demo.util;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.library.fast.util.FastUtil;

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

    public void changeColor(View rootView, int alpha, boolean mIsLight, boolean showText) {
        if (rootView != null) {
            //滚动视图
            if (rootView instanceof TextView) {
                TextView textView = (TextView) rootView;
                Drawable[] drawables = textView.getCompoundDrawables();
                for (Drawable item : drawables) {
                    if (item != null) {
                        FastUtil.getTintDrawable(item, Color.argb(mIsLight ? alpha : 255 - alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255));
                    }
                }
                if (!showText) {
                    textView.setTextColor(Color.argb(alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255));
                }else {
                    textView.setTextColor(Color.argb(mIsLight ? alpha : 255 - alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255));
                }
            } else if (rootView instanceof ImageView) {
                FastUtil.getTintDrawable(((ImageView) rootView).getDrawable(), Color.argb(mIsLight ? alpha : 255 - alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255));
            } else if (rootView instanceof ViewGroup) {
                ViewGroup contentView = (ViewGroup) rootView;
                int size = contentView.getChildCount();
                //循环遍历所有子View
                for (int i = 0; i < size; i++) {
                    View childView = contentView.getChildAt(i);
                    //递归查找
                    changeColor(childView, alpha, mIsLight, showText);
                }
            }
        }
    }
}
