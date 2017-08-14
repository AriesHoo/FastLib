package com.aries.library.fast.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created: AriesHoo on 2017/8/1 15:01
 * Function:时间转换工具
 * Desc:
 */
public class TimeFormatUtil {

    /**
     * 格式化星期
     *
     * @param millis
     * @return 1-星期日...7-星期六
     */
    public static int formatWeek(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int index = calendar.get(Calendar.DAY_OF_WEEK) + 1;
        return index;
    }

    /**
     * 格式化时间
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatTime(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }
}
