package com.aries.library.fast.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
 * 3、2019-3-8 15:37:23 修改{@link #formatWeek(long)}错误
 */
public class FastFormatUtil {
    /**
     * 格式化数据
     *
     * @param dataSize 数据字节数
     * @return
     */
    public static String formatDataSize(long dataSize) {
        return formatDataSize(dataSize, "###.##");
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
            int index = result.indexOf(".");
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 格式化金钱
     *
     * @param money  金钱数
     * @param format 格式
     * @return 格式化后字符串
     */
    public static String formatMoney(double money, String format) {
        NumberFormat nf = new DecimalFormat(format);
        return nf.format(money);
    }

    public static String formatMoney(double money) {
        return formatMoney(money, "#,###.##");
    }

    public static String formatMoney(String money, String format) {
        try {
            return formatMoney(Double.parseDouble(money), format);
        } catch (Exception e) {
            return money;
        }
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
     * 格式化星期
     *
     * @param millis
     * @return 1-星期日...7-星期六
     */
    public static int formatWeek(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int index = calendar.get(Calendar.DAY_OF_WEEK);
        return index;
    }

}
