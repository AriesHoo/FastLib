package com.aries.library.fast.demo.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;

import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.module.activity.FastWebActivity;
import com.aries.library.fast.retrofit.FastDownloadObserver;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.util.FastFileUtil;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SnackBarUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.action.sheet.UIActionSheetDialog;
import com.just.agentweb.AgentWeb;

import java.io.File;

/**
 * @Author: AriesHoo on 2018/7/30 11:04
 * @E-Mail: AriesHoo@126.com
 * Function: 应用内浏览器
 * Description:
 * 1、2018-7-30 11:04:22 新增图片下载功能
 */
public class WebViewActivity extends FastWebActivity {

    private String mFilePath = FastFileUtil.getCacheDir();
    private String mFormat = "保存图片<br><small><font color='#2394FE'>图片文件夹路径:%1s</font></small>";
    private static boolean mIsShowTitle = true;

    public static void start(Activity mActivity, String url) {
        start(mActivity, url, true);
    }

    public static void start(Activity mActivity, String url, boolean isShowTitle) {
        mIsShowTitle = isShowTitle;
        start(mActivity, WebViewActivity.class, url);
    }

    @Override
    protected int getProgressColor() {
        return super.getProgressColor();
    }

    @Override
    protected int getProgressHeight() {
        return super.getProgressHeight();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        if (!mIsShowTitle) {
            titleBar.setStatusBarLightMode(false)
                    .setVisibility(View.GONE);
        }
        titleBar.setTitleMainTextMarquee(true)
                .setDividerVisible(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP);
    }

    @Override
    protected void setAgentWeb(AgentWeb.CommonBuilder mAgentBuilder) {
        super.setAgentWeb(mAgentBuilder);
    }

    @Override
    protected void setAgentWeb(AgentWeb mAgentWeb) {
        super.setAgentWeb(mAgentWeb);
        WebView mWebView = mAgentWeb.getWebCreator().getWebView();
        mWebView.setOnLongClickListener(v -> {
            WebView.HitTestResult hitTestResult = mWebView.getHitTestResult();
            if (hitTestResult == null) {
                return false;
            }
            if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE
                    || hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                showDownDialog(hitTestResult.getExtra());
            } else if (!mIsShowTitle) {
                showActionSheet();
            }
            LoggerManager.d("onLongClick:hitTestResult-Type:" + hitTestResult.getType() + ";Extra:" + hitTestResult.getExtra());
            return true;
        });
    }

    private void showDownDialog(String url) {
        mActionSheetView = new UIActionSheetDialog.ListSheetBuilder(mContext)
                .addItem(Html.fromHtml(String.format(mFormat, mFilePath)))
                .setOnItemClickListener((dialog, itemView, i) -> {
                    switch (i) {
                        case 0:
                            downImg(url);
                            break;
                    }
                })
                .setCancel(com.aries.library.fast.R.string.fast_cancel)
                .setTextSizeUnit(TypedValue.COMPLEX_UNIT_DIP)
                .create();
        mActionSheetView.show();
    }

    /**
     * 下载图片
     *
     * @param url
     */
    private void downImg(String url) {
        String fileName = "/" + System.currentTimeMillis() + "_" + FastUtil.getRandom(100000) + ".jpg";
        FastRetrofit.getInstance().downloadFile(url)
                .subscribe(new FastDownloadObserver(mFilePath, fileName) {
                    @Override
                    public void onSuccess(File file) {
                        SnackBarUtil.with(mContainer)
                                .setMessage("图片已保存至" + mFilePath + "文件夹")
                                .setMessageColor(Color.parseColor("#2394FE"))
                                .setBgColor(Color.WHITE)
                                .show();
                    }

                    @Override
                    public void onFail(Throwable e) {
                        ToastUtil.show("图片保存失败" + e.getMessage());
                    }

                    @Override
                    public void onProgress(float progress, long current, long total) {
                        LoggerManager.i(TAG, "progress:" + progress);
                    }
                });
    }


    private void installApk(Context context, String apkPath) {
        if (context == null || TextUtils.isEmpty(apkPath)) {
            return;
        }
        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".AgentWebFileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //横竖屏切换过后不能设置滑动返回--可以切换试一试效果
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }
}
