package com.aries.library.fast.demo.helper;

import androidx.core.content.ContextCompat;

import com.aries.library.fast.demo.App;
import com.aries.library.fast.demo.R;
import com.aries.ui.view.radius.delegate.RadiusViewDelegate;

/**
 * @Author: AriesHoo on 2018/9/30 9:16
 * @E-Mail: AriesHoo@126.com
 * @Function: 圆角View帮助类
 * @Description:
 */
public class RadiusViewHelper {

    private static volatile RadiusViewHelper sInstance;

    private RadiusViewHelper() {
    }

    public static RadiusViewHelper getInstance() {
        if (sInstance == null) {
            synchronized (RadiusViewHelper.class) {
                if (sInstance == null) {
                    sInstance = new RadiusViewHelper();
                }
            }
        }
        return sInstance;
    }

    public void setRadiusViewAdapter(RadiusViewDelegate delegate) {
        if (!App.isSupportElevation()) {
            delegate.setStrokeWidth(App.getContext().getResources().getDimensionPixelSize(R.dimen.dp_line_size))
                    .setStrokeColor(ContextCompat.getColor(App.getContext(), R.color.colorLineGray))
                    .init();
        }
    }
}
