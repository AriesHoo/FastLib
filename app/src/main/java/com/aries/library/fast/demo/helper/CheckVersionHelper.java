package com.aries.library.fast.demo.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.aries.library.fast.basis.BasisActivity;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.entity.UpdateEntity;
import com.aries.library.fast.demo.retrofit.repository.ApiRepository;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.retrofit.FastDownloadObserver;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.util.FastFileUtil;
import com.aries.library.fast.util.FastFormatUtil;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.annotations.NonNull;

/**
 * @Author: AriesHoo on 2018/7/23 16:06
 * @E-Mail: AriesHoo@126.com
 * Function: 检查版本升级的工具类--该处只做下载演示开发者可根据自己项目情况进行调整
 * Description:
 */
public class CheckVersionHelper {

    private FastDownloadObserver mDownloadObserver;
    private SoftReference<BasisActivity> mActivity;
    private boolean mIsLoading = false;

    private CheckVersionHelper(BasisActivity activity) {
        this.mActivity = new SoftReference<>(activity);
    }

    public static CheckVersionHelper with(BasisActivity activity) {
        return new CheckVersionHelper(activity);
    }


    /**
     * Function:开放平台监测版本升级
     *
     * @param
     * @return
     */
    public void checkVersion(boolean loading) {
        BasisActivity activity = mActivity.get();
        mIsLoading = loading;
        if (activity == null) {
            return;
        }
        ApiRepository.getInstance().updateApp()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(loading ?
                        new FastLoadingObserver<UpdateEntity>(R.string.checking) {
                            @Override
                            public void _onNext(@NonNull UpdateEntity entity) {
                                if (entity == null) {
                                    ToastUtil.show("当前已是最新版本");
                                    return;
                                }
                                checkVersion(entity);
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mIsLoading) {
                                    super.onError(e);
                                }
                            }
                        } :
                        new FastObserver<UpdateEntity>() {
                            @Override
                            public void _onNext(@NonNull UpdateEntity entity) {
                                if (entity == null) {
                                    ToastUtil.show("当前已是最新版本");
                                    return;
                                }
                                checkVersion(entity);
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mIsLoading) {
                                    super.onError(e);
                                }
                            }
                        });
    }

    private void checkVersion(UpdateEntity entity) {
        if (entity == null) {
            if (mIsLoading) {
                ToastUtil.show("版本信息有误");
            }
            return;
        }
        if (!entity.isSuccess()) {
            if (mIsLoading) {
                ToastUtil.show(entity.getMessage());
            }
            return;
        }
        if (TextUtils.isEmpty(entity.url) || !entity.url.contains("apk")) {
            if (mIsLoading) {
                ToastUtil.show("不是有效的下载链接:" + entity.url);
            }
            LoggerManager.e("检测新版本:不是有效的apk下载链接");
            return;
        }
        showAlert(entity);
    }

    /**
     * 提示用户
     *
     * @param entity
     */
    private void showAlert(UpdateEntity entity) {
        Activity activity = mActivity.get();
        if (activity == null || activity.isFinishing()) {
            activity = FastStackUtil.getInstance().getCurrent();
        }
        if (activity == null || activity.isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("发现新版本:V" + entity.versionName)
                .setMessage(entity.getMessage())
                .setPositiveButton(R.string.update_now, (dialog, which) ->
                        downloadApk(entity, "FastLib_" + entity.versionName + ".apk", true));
        if (!entity.force) {
            builder.setNegativeButton("暂不更新", null);
        }
        builder.create()
                .show();
    }

    /**
     * 下载apk--实际情况需自己调整逻辑避免因range不准造成下载解析不了问题--建议普通应用包下载(20M以内的不使用断点续传)
     *
     * @param entity
     * @param fileName      文件名
     * @param isRangeEnable 是否断点续传
     */
    public void downloadApk(UpdateEntity entity, String fileName, boolean isRangeEnable) {
        Activity activity = mActivity.get();
        if (activity == null || activity.isFinishing()) {
            activity = FastStackUtil.getInstance().getCurrent();
        }
        if (activity == null || activity.isFinishing()) {
            return;
        }

        ProgressDialog mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle(entity.getTitle());
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage(entity.getMessage());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(!entity.force);
        mProgressDialog.setProgressNumberFormat("0.00MB/未知");
        mProgressDialog.setCanceledOnTouchOutside(!entity.force);

        //暂停下载-慎用;建议使用 Disposable.dispose();
//        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "暂停", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (mDownloadObserver != null) {
//                    mDownloadObserver.pause();
//                }
//            }
//        });

        File mLocal = new File(FastFileUtil.getCacheDir(), fileName);
        Map<String, Object> header = null;
        long length = mLocal.length();
        if (isRangeEnable) {
            header = new HashMap<>(1);
            header.put("range", "bytes=" + length + "-");
            LoggerManager.i("downloadApk", "length:" + length);
        }
        //不同url不能使用相同的本地绝对路径不然很可能将B的后半部分下载追加到A的后面--最终也是错误的
        ProgressDialog finalMProgressDialog = mProgressDialog;
        mDownloadObserver = new FastDownloadObserver(fileName, finalMProgressDialog, isRangeEnable) {
            @Override
            public void onSuccess(File file) {
                FastFileUtil.installApk(file);
            }

            @Override
            public void onFail(Throwable e) {
                LoggerManager.e("downloadApk", e.getMessage());
                //HTTP 416 Range Not Satisfiable 出现该错误--很大可能性是文件已下载完成传递的
                boolean satisfiable = e != null && e.getMessage().contains("416") && e.getMessage().toLowerCase().contains("range");
                if (satisfiable) {
                    onSuccess(mLocal);
                    return;
                }
                boolean isPause = e != null && e.getMessage().equals(FastDownloadObserver.DOWNLOAD_PAUSE);
                if (isPause) {
                    ToastUtil.show("暂停下载");
                    return;
                }
                ToastUtil.show("下载失败:" + e.getMessage());
            }

            @Override
            public void onProgress(float progress, long current, long total) {
                LoggerManager.i("downloadApk", "current:" + current + ";total:" + total);
                if (!finalMProgressDialog.isShowing()) {
                    return;
                }
                finalMProgressDialog.setProgressNumberFormat(FastFormatUtil.formatDataSize(current) + "/" + FastFormatUtil.formatDataSize(total));
                finalMProgressDialog.setMax((int) total);
                finalMProgressDialog.setProgress((int) current);
            }
        };
        FastRetrofit.getInstance().downloadFile(entity.url, header)
                .compose(((RxAppCompatActivity) activity).bindUntilEvent(ActivityEvent.DESTROY))
                //可自定义保存路径默认//storage/emulated/0/Android/data/<package-name>/cache/xxx/
                .subscribe(mDownloadObserver);
    }

}
