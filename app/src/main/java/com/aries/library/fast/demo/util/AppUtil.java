package com.aries.library.fast.demo.util;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created: AriesHoo on 2017/9/21 18:19
 * Function:
 * Desc:
 */

public class AppUtil {

    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
