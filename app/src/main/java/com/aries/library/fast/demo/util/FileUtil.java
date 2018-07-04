package com.aries.library.fast.demo.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created: AriesHoo on 2018/5/28 11:03
 * E-Mail: AriesHoo@126.com
 * Function:FileProvider文件路径处理帮助类
 * Description:
 */
public class FileUtil {
    /**
     * 创建apk缓存地址
     *
     * @param context
     * @param name
     * @return
     */
    public static File create(Context context, String name) {
        File cacheDir = getCacheDir(context);
        cacheDir.mkdirs();
        if (TextUtils.isEmpty(name)) {
            name = "t_" + System.currentTimeMillis();
        }
        File file = new File(cacheDir, "update_" + name);
        if (file.exists()) {
            file.delete();
            file = new File(cacheDir, "update_" + name);
        }
        return file;
    }

    /**
     * 获取系统缓存路径
     *
     * @param context
     * @return
     */
    private static File getCacheDir(Context context) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        cacheDir = new File(cacheDir, "FM");
        return cacheDir;
    }

    /**
     * 获取系统缓存路径
     *
     * @param context
     * @return
     */
    public static String createCacheDir(Context context) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        cacheDir = new File(cacheDir, "FM");
        return cacheDir.getAbsolutePath();
    }


    //获取文件存放根路径
    public static File getAppDir(Context context) {
        String dirPath = "";
        //SD卡是否存在
        boolean isSdCardExists = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        boolean isRootDirExists = Environment.getExternalStorageDirectory().exists();
        if (isSdCardExists && isRootDirExists) {
            dirPath = String.format("%s/%s/", Environment.getExternalStorageDirectory().getAbsolutePath(), "FM");
        } else {
            dirPath = String.format("%s/%s/", context.getApplicationContext().getFilesDir().getAbsolutePath(), "FM");
        }

        File appDir = new File(dirPath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return appDir;
    }

    //获取录音存放路径
    public static File getAppRecordDir(Context context) {
        File appDir = getAppDir(context);
        File recordDir = new File(appDir.getAbsolutePath(), "record/");
        if (!recordDir.exists()) {
            recordDir.mkdir();
        }
        return recordDir;
    }

}
