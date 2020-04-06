package com.aries.library.fast.demo.helper;

import android.app.Activity;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import com.aries.library.fast.BasisHelper;
import com.aries.library.fast.basis.BasisActivity;
import com.aries.library.fast.demo.App;
import com.aries.library.fast.demo.AppData;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.entity.UpdateEntity;
import com.aries.library.fast.demo.retrofit.repository.ApiRepository;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.retrofit.FastDownloadObserver;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.ToastUtil;
import com.download.library.DownloadImpl;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.lang.ref.SoftReference;
import java.util.Stack;

import io.reactivex.annotations.NonNull;

/**
 * @Author: AriesHoo on 2018/7/23 16:06
 * @E-Mail: AriesHoo@126.com
 * Function: 检查版本升级的工具类--该处只做下载演示开发者可根据自己项目情况进行调整
 * Description:
 */
public class CheckVersionHelper extends BasisHelper {

    private FastDownloadObserver mDownloadObserver;
    private SoftReference<Activity> mActivity;
    private boolean mIsLoading = false;

    public CheckVersionHelper(Activity activity) {
        super(activity);
        this.mActivity = new SoftReference<>(activity);
    }

    /**
     * Function:开放平台监测版本升级
     *
     * @param
     * @return
     */
    public void checkVersion(boolean loading) {
        BasisActivity activity = (BasisActivity) mActivity.get();
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
        LoggerManager.i("check_saturation:"+entity.saturation+";sp:"+AppData.getSaturation());
        if (entity.saturation != AppData.getSaturation()) {
            AppData.setSaturation(entity.saturation);
            Stack<Activity> activities = FastStackUtil.getInstance().getStack();
            for (Activity activity : activities) {
                if (activity != null && !activity.isFinishing()) {
                    App.setSaturation(activity);
                }
            }
        }
        if (!entity.isSuccess()) {
            if (mIsLoading) {
                ToastUtil.show(entity.getMessage());
            }
            return;
        }
        if (TextUtils.isEmpty(entity.url)) {
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
        DownloadImpl.getInstance()
                .with(mContext)
                .url(entity.url)
                .quickProgress()
                .setEnableIndicator(true)
                .autoOpenIgnoreMD5()
                .setRetry(5)
                .setBlockMaxTime(100000L)
                .enqueue();
    }

}
