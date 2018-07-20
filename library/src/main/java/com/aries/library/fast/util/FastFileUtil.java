package com.aries.library.fast.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.aries.library.fast.FastManager;

import java.io.File;

/**
 * @Author: AriesHoo on 2018/7/20 15:41
 * @E-Mail: AriesHoo@126.com
 * Function: FileProvider文件路径处理帮助类
 * Description:
 */
public class FastFileUtil {

    /**
     * 获取系统缓存路径
     *
     * @return
     */
    public static String createCacheFile() {
        Context context = FastManager.getInstance().getApplication().getApplicationContext();
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        //该路径需和fast_file_path配置一致
        cacheDir = new File(cacheDir, "fast_file_cache");
        return cacheDir.getAbsolutePath();
    }

    /**
     * 安装App 使用lib FileProvider
     * 使用{@link #createCacheFile()}创建文件包
     *
     * @param apkPath
     */
    public static void installApk(File apkPath) {
        Context context = FastManager.getInstance().getApplication().getApplicationContext();
        if (context == null || apkPath == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //加入provider
            Uri apkUri = null;
            try {
                apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".FastFileProvider", apkPath);
            } catch (Exception e) {

            }
            if (apkUri == null) {
                try {
                    apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".FileProvider", apkPath);
                } catch (Exception e) {

                }
            }
            //授予一个URI的临时权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkPath), "application/vnd.android.package-archive");
        }
        // context 使用startActivity需增加 FLAG_ACTIVITY_NEW_TASK TAG 否则低版本上(目前发现在7.0以下版本)会提示以下错误
        //android.util.AndroidRuntimeException: Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
