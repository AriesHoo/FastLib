package com.aries.library.fast.retrofit;

import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.aries.library.fast.util.FastFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.observers.DefaultObserver;
import okhttp3.ResponseBody;

/**
 * Created: AriesHoo on 2018/7/3 16:32
 * E-Mail: AriesHoo@126.com
 * Function:快速下载观察者
 * Description:
 * 1、2018-7-11 16:38:18 去掉部分参数
 */
public abstract class FastDownloadObserver extends DefaultObserver<ResponseBody> {

    private Dialog mDialog;
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
        this(FastFileUtil.createCacheFile(), destFileName);
    }

    public FastDownloadObserver(String destFileDir, String destFileName) {
        this(destFileDir, destFileName, null);
    }

    public FastDownloadObserver(String destFileName, Dialog dialog) {
        this(FastFileUtil.createCacheFile(), destFileName, dialog);
    }

    public FastDownloadObserver(String destFileDir, String destFileName, Dialog dialog) {
        super();
        this.mDestFileDir = TextUtils.isEmpty(destFileDir) ? FastFileUtil.createCacheFile() : destFileDir;
        this.mDestFileName = destFileName;
        this.mDialog = dialog;
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
     * @return
     * @throws IOException
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
                if (is != null) is.close();
            } catch (IOException e) {
                onError(e);
            }
            try {
                if (fos != null) fos.close();
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


    public abstract void onSuccess(File file);

    public abstract void onFail(Throwable e);

    public abstract void onProgress(float progress, long current, long total);
}
