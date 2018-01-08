package com.aries.library.fast.i;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.aries.library.fast.entity.FastNavigationConfigEntity;

/**
 * Created: AriesHoo on 2018/1/5 0005 下午 2:41
 * E-Mail: AriesHoo@126.com
 * Function:
 * Description:
 */
public interface NavigationBarControl {


    /**
     * @param activity
     * @return
     */
    @NonNull
    FastNavigationConfigEntity createNavigationBarControl(Activity activity);
}
