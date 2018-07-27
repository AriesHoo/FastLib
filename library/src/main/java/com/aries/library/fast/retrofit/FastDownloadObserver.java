package com.aries.library.fast.retrofit;

import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.util.FastFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * @Author: AriesHoo on 2018/7/23 13:41
 * @E-Mail: AriesHoo@126.com
 * Function:快速下载观察者
 * Description:
 * 1、2018-7-11 16:38:18 去掉部分参数
 * 2、2018-7-12 11:28:04 修改继承关系方便全局错误控制
 */
public abstract class FastDownloadObserver extends FastObserver<ResponseBody> {

    /**
     * 提示Dialog
     */
    private Dialog mDialog;
    /**
     * 主线程Handler用于通知进度更新
     */
    private Handler mHandler;
    /**
     * 目标文件存储的文件夹路径
     */
    private String mDestFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String mDestFileName;

    public FastDownloadObserver(String destFileName) {
        this(FastFileUtil.getCacheDir(), destFileName);
    }

    public FastDownloadObserver(String destFileDir, String destFileName) {
        this(destFileDir, destFileName, null);
    }

    public FastDownloadObserver(String destFileName, Dialog dialog) {
        this(FastFileUtil.getCacheDir(), destFileName, dialog);
    }

    public FastDownloadObserver(String destFileDir, String destFileName, Dialog dialog) {
        super();
        this.mDestFileDir = TextUtils.isEmpty(destFileDir) ? FastFileUtil.getCacheDir() : destFileDir;
        this.mDestFileName = destFileName;
        this.mDialog = dialog;
        LoggerManager.i("FastDownloadObserver", "mDestFileDir:" + mDestFileDir);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(final Throwable e) {
        getMainLooperHandler().post(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                onFail(e);
            }
        });
    }

    @Override
    public void _onNext(ResponseBody entity) {

    }

    @Override
    public void onNext(ResponseBody entity) {
        final File file;
        try {
            file = saveFile(entity);
            getMainLooperHandler().post(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                    onSuccess(file);
                }
            });
        } catch (IOException e) {
            onError(e);
        }
    }

    protected void showProgressDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgressDialog();
    }

    /**
     * 保存文件
     *
     * @param response
     * @return 返回保存文件
     * @throws IOException 写入文件IO异常
     */
    public File saveFile(ResponseBody response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.byteStream();
            final long total = response.contentLength();
            long sum = 0;
            File dir = new File(mDestFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final long finalSum1 = sum;
            getMainLooperHandler().post(new Runnable() {
                @Override
                public void run() {
                    onProgress(finalSum1 * 1.0f / total, finalSum1, total);
                }
            });
            File file = new File(dir, mDestFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                getMainLooperHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        onProgress(finalSum * 1.0f / total, finalSum, total);
                    }
                });
            }
            fos.flush();
            return file;
        } finally {
            try {
                response.close();
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                onError(e);
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                onError(e);
            }
        }
    }

    private Handler getMainLooperHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }


    /**
     * 下载完成-返回文件
     *
     * @param file 文件对象
     */
    public abstract void onSuccess(File file);

    /**
     * 下载失败返回错误对象
     *
     * @param e 错误对象
     */
    public abstract void onFail(Throwable e);

    /**
     * 下载进度
     *
     * @param progress 进度如 0.01
     * @param current  当前已下载字节数
     * @param total    总文件字节数
     */
    public abstract void onProgress(float progress, long current, long total);
}
