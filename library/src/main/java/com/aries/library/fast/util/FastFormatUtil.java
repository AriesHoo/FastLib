package com.aries.library.fast.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created: AriesHoo on 2018/7/11 15:17
 * E-Mail: AriesHoo@126.com
 * Function: 快速格式化工具
 * Description:
 * 1、2018-7-11 15:23:40 将日期格式化迁移至此,新增格式化文件大小
 */
public class FastFormatUtil {

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
        return formatTime(new Date(time), format);
    }

    public static String formatTime(Date time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    /**
     * 格式化数据
     *
     * @param dataSize
     * @return
     */
    public static String formatDataSize(long dataSize) {
        return formatDataSize(dataSize, "###.00");
    }

    /**
     * 格式化文件大小格式
     *
     * @param dataSize 字节数
     * @param pattern  格式
     * @return
     */
    public static String formatDataSize(long dataSize, String pattern) {
        DecimalFormat var2 = new DecimalFormat(pattern);
        return dataSize < 1024L ? dataSize + "Bytes" : (dataSize < 1048576L ? var2.format((double) ((float) dataSize / 1024.0F))
                + "KB" : (dataSize < 1073741824L ? var2.format((double) ((float) dataSize / 1024.0F / 1024.0F))
                + "MB" : (dataSize < 0L ? var2.format((double) ((float) dataSize / 1024.0F / 1024.0F / 1024.0F))
                + "GB" : "Error")));
    }
}
