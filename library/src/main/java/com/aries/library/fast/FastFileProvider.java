package com.aries.library.fast;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.aries.library.fast.util.FastFileUtil;
import com.aries.library.fast.util.FastUtil;

/**
 * @Author: AriesHoo on 2018/7/23 14:39
 * @E-Mail: AriesHoo@126.com
 * Function: FileProvider 配合{@link FastFileUtil}
 * Description:
 * 1、2019-9-16 14:34:51 增加FastManager初始化
 */
public class FastFileProvider extends FileProvider {
//     @Override
//     public boolean onCreate() {
//         Context context = getContext().getApplicationContext();
//         if(context==null){
//             context = FastUtil.getApplication();
//         }
//         Log.d("FastFileProvider", "context:" + context);
//         FastManager.init((Application) context);
//         return super.onCreate();
//     }
}
