package com.aries.library.fast.demo.helper;

import com.aries.library.fast.demo.App;
import com.aries.library.fast.demo.R;
import com.aries.ui.view.radius.delegate.RadiusViewDelegate;

/**
 * Created: AriesHoo on 2017/11/1 10:00
 * E-Mail: AriesHoo@126.com
 * Function:
 * Description:
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
            delegate.setStrokeWidth(App.getContext().getResources().getDimensionPixelSize(R.dimen.dp_line_size));
            delegate.setStrokeColor(App.getContext().getResources().getColor(R.color.colorLineGray));
        }
    }
}
