package com.aries.library.fast.util;

import java.util.Date;

/**
 * Created: AriesHoo on 2018/6/30/030 18:21
 * E-Mail: AriesHoo@126.com
 * Function:时间转换工具
 * Description:
 * 1、2018-7-11 15:18:08 标记废弃使用{@link FastFormatUtil}代替
 */
@Deprecated
public class TimeFormatUtil {

    /**
     * 格式化星期
     *
     * @param millis
     * @return 1-星期日...7-星期六
     */
    public static int formatWeek(long millis) {
        return FastFormatUtil.formatWeek(millis);
    }

    /**
     * 格式化时间
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatTime(long time, String format) {
        return formatTime(new Date(time), format);
    }

    public static String formatTime(Date time, String format) {
        return FastFormatUtil.formatTime(time, format);
    }
}
