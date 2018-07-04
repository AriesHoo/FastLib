package com.aries.library.fast.retrofit;

import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.util.FastFileUtil;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.widget.FastLoadDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * Created: AriesHoo on 2018/7/3 16:32
 * E-Mail: AriesHoo@126.com
 * Function:快速下载观察者
 * Description:
 */
public abstract class FastDownloadObserver extends FastLoadingObserver<ResponseBody> {

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
        this(FastFileUtil.createCacheFile(), destFileName, null);
    }

    public FastDownloadObserver(String destFileDir, String destFileName) {
        this(destFileDir, destFileName, null);
    }

    public FastDownloadObserver(String destFileName, Dialog dialog) {
        this(FastFileUtil.createCacheFile(), destFileName, dialog);
    }

    public FastDownloadObserver(String destFileDir, String destFileName, Dialog dialog) {
        super(new FastLoadDialog(FastStackUtil.getInstance().getCurrent(), dialog));
        this.mDestFileDir = TextUtils.isEmpty(destFileDir) ? FastFileUtil.createCacheFile() : destFileDir;
        this.mDestFileName = destFileName;
        this.mDialog = dialog;
        LoggerManager.i("FastDownloadObserver", "destFileDir:" + destFileDir);
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
                LoggerManager.i("FastDownloadObserver", "finalSum:" + finalSum + ";total:" + total);
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
