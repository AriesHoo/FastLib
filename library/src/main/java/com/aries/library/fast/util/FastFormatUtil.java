package com.aries.library.fast.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: AriesHoo on 2018/7/23 14:30
 * @E-Mail: AriesHoo@126.com
 * Function: 快速格式化工具
 * Description:
 * 1、2018-7-11 15:23:40 将日期格式化TimeFormatUtil迁移至此,新增格式化文件大小
 * 2、2018-8-30 09:20:01 新增格式化保留小数点方法
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
     * 格式化时间(类似yyyy-MM-dd HH:mm:ss)
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatTime(long time, String format) {
        return formatTime(new Date(time), format);
    }

    /**
     * 格式化时间(类似yyyy-MM-dd HH:mm:ss)
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatTime(Date time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    /**
     * 格式化数据
     *
     * @param dataSize 数据字节数
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

    /**
     * 格式化数据保留maxLength位小数--四舍五入
     *
     * @param value
     * @param maxLength
     * @return
     */
    public static String formatDoubleSize(double value, int maxLength) {
        return formatDoubleSize(value, maxLength, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 格式化数据保留maxLength位小数
     *
     * @param value
     * @param maxLength
     * @param roundingMode 保留小数规则-如四舍五入 BigDecimal.ROUND_HALF_UP {@link BigDecimal#ROUND_DOWN}
     * @return
     */
    public static String formatDoubleSize(double value, int maxLength, int roundingMode) {
        String result = value + "";
        BigDecimal bg = new BigDecimal(value);
        try {
            result = bg.setScale(maxLength, roundingMode).doubleValue() + "";
        } catch (Exception e) {
        }
        return result;
    }
}
