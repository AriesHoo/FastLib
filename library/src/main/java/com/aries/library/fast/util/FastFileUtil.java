package com.aries.library.fast.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.R;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

/**
 * @Author: AriesHoo on 2018/7/20 15:41
 * @E-Mail: AriesHoo@126.com
 * Function: FileProvider文件路径处理帮助类
 * Description:
 * 1、2018-7-23 14:28:33 新增安装apk兼容7.0以下版本
 * 2、2018-7-24 10:13:01 新增{@link #installApk(File, String)} 以传入开发者自定义authority--FileProvider
 */
public class FastFileUtil {

    /**
     * 获取系统缓存路径
     *
     * @return
     */
    public static String getCacheDir() {
        Context context = FastManager.getInstance().getApplication().getApplicationContext();
        //storage/emulated/0/Android/data/<package-name>/cache/xxx/
        File cacheDir = context.getExternalCacheDir();
        String fileDir = context.getString(R.string.fast_external_cache_dir);
        if (cacheDir == null) {
            //data/data/<package-name>/cache/xxx/
            cacheDir = context.getCacheDir();
            fileDir = context.getString(R.string.fast_cache_dir);
        }
        //该路径需和fast_file_path配置一致
        cacheDir = new File(cacheDir, fileDir);
        return cacheDir.getAbsolutePath();
    }

    /**
     * 获取Environment.getExternalStorageDirectory()目录--注意读写SD卡权限{android.permission.WRITE_EXTERNAL_STORAGE}
     * 即:/storage/emulated/0/xxx/
     *
     * @return
     */
    public static String getExternalStorageDirectory() {
        Context context = FastManager.getInstance().getApplication().getApplicationContext();
        File file = new File(Environment.getExternalStorageDirectory().toString());
        String fileDir = context.getString(R.string.fast_external_storage_directory);
        //该路径需和fast_file_path配置一致
        file = new File(file, fileDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }


    /**
     * 获取context.getFilesDir()
     * /data/data/<package-name>/files/xxx/
     *
     * @return
     */
    public static String getFilesDir() {
        Context context = FastManager.getInstance().getApplication().getApplicationContext();
        File file = context.getFilesDir();
        String fileDir = context.getString(R.string.fast_files_dir);
        //该路径需和fast_file_path配置一致
        file = new File(file, fileDir);
        return file.getAbsolutePath();
    }

    /**
     * 获取 context.getExternalFilesDir
     * /storage/emulated/0/Android/data/<package_name>/files/xxx/
     *
     * @return
     */
    public static String getExternalFilesDir() {
        Context context = FastManager.getInstance().getApplication().getApplicationContext();
        File file = context.getExternalFilesDir(null);
        String fileDir = context.getString(R.string.fast_external_files_dir);
        //该路径需和fast_file_path配置一致
        file = new File(file, fileDir);
        return file.getAbsolutePath();
    }

    /**
     * 安装apk 包路径在FastFileProvider 配置路径下
     *
     * @param apkPath apk 文件对象
     */
    public static void installApk(File apkPath) {
        Context context = FastManager.getInstance().getApplication().getApplicationContext();
        if (context == null || apkPath == null) {
            return;
        }
        installApk(apkPath, context.getPackageName() + ".FastFileProvider");
    }

    /**
     * 安装App 使用lib FileProvider
     * 使用{@link #getCacheDir()} ()}创建文件包
     *
     * @param apkPath apk 文件对象
     */
    public static void installApk(File apkPath, @NonNull String authority) {
        Context context = FastManager.getInstance().getApplication().getApplicationContext();
        if (context == null || apkPath == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // context 使用startActivity需增加 FLAG_ACTIVITY_NEW_TASK TAG 否则低版本上(目前发现在7.0以下版本)会提示以下错误
        //android.util.AndroidRuntimeException: Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri apkUri;
        //判断版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //加入provider
            apkUri = FileProvider.getUriForFile(context, authority, apkPath);
            //授予一个URI的临时权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            apkUri = Uri.fromFile(apkPath);
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
