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
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;

import io.reactivex.annotations.NonNull;

/**
 * Created: AriesHoo on 2018/5/28 11:05
 * E-Mail: AriesHoo@126.com
 * Function:检查版本升级的工具类
 * Description:
 */
public class CheckVersionHelper {

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
                                if (mIsLoading)
                                    super.onError(e);
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
                                if (mIsLoading)
                                    super.onError(e);
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
                .setPositiveButton(R.string.update_now, (dialog, which) -> {
                    downloadApk(entity);
                });
        if (!entity.force) {
            builder.setNegativeButton("暂不更新", null);
        }
        builder.create()
                .show();
    }

    /**
     * 下载apk
     *
     * @param entity
     */
    private void downloadApk(UpdateEntity entity) {
        Activity activity = mActivity.get();
        if (activity == null || activity.isFinishing()) {
            activity = FastStackUtil.getInstance().getCurrent();
        }
        if (activity == null || activity.isFinishing()) {
            return;
        }
        final ProgressDialog mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle(entity.getSize());
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(!entity.force);
        mProgressDialog.setCanceledOnTouchOutside(!entity.force);
        FastRetrofit.getInstance().downloadFile(entity.url)
                .subscribe(new FastDownloadObserver("FastLib.apk",128, mProgressDialog) {
                    @Override
                    public void onSuccess(File file) {
//                        ToastUtil.show("下载成功:" + file.getAbsolutePath());
                        FastFileUtil.installApk(file);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        ToastUtil.show("下载失败:" + e.getMessage());
                    }

                    @Override
                    public void onProgress(float progress, long current, long total) {
//                        mProgressDialog.setMax((int) total);
//                        mProgressDialog.setProgress((int) current);
                        mProgressDialog.setProgress((int) (100 * progress));
                    }
                });
    }

    private String getFormatSize(double size) {
        double kilobyte = size / 1024;
        if (kilobyte < 1) {
            return "0M";
        }

        double megabyte = kilobyte / 1024;
        if (megabyte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(megabyte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double gigabyte = megabyte / 1024;
        if (gigabyte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megabyte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigabyte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigabyte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "T";
    }
}
