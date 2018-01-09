package com.aries.library.fast.demo.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.module.activity.FastWebActivity;
import com.aries.library.fast.util.NavigationBarUtil;
import com.aries.ui.util.RomUtil;
import com.aries.ui.view.title.TitleBarView;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DownLoadResultListener;

import java.io.File;

/**
 * Created: AriesHoo on 2017/10/13 8:47
 * E-Mail: AriesHoo@126.com
 * Function: 应用内浏览器
 * Desc:
 */
public class WebViewActivity extends FastWebActivity {

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
    public boolean isLightStatusBarEnable() {
        return mIsShowTitle;
    }

    @Override
    public int getContentBackground() {
        return -1;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        if (!mIsShowTitle) {
            titleBar.setVisibility(View.GONE);
        }
        titleBar.setTitleMainTextMarquee(true)
                .setDividerVisible(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP);
    }

    @Override
    protected void setAgentWeb(AgentWeb.CommonAgentBuilder mAgentBuilder) {
        super.setAgentWeb(mAgentBuilder);
        mAgentBuilder.addDownLoadResultListener(new DownLoadResultListener() {
            @Override
            public void success(String path) {
                if (path.endsWith(".apk")) {
                    installApk(getApplicationContext(), path);
                }
            }

            @Override
            public void error(String path, String resUrl, String cause, Throwable e) {

            }
        });
    }

    @Override
    protected void setAgentWeb(AgentWeb mAgentWeb) {
        super.setAgentWeb(mAgentWeb);
        WebView mWebView = mAgentWeb.getWebCreator().get();
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult hitTestResult = mWebView.getHitTestResult();
                if (hitTestResult == null) {
                    return false;
                }
                if (!mIsShowTitle) {
                    showActionSheet();
                }
                LoggerManager.d("onLongClick:hitTestResult-Type:" + hitTestResult.getType() + ";Extra:" + hitTestResult.getExtra());
                return true;
            }
        });
    }

    @Override
    protected boolean isSwipeBackEnable() {
        return super.isSwipeBackEnable() && !(RomUtil.isEMUI() && NavigationBarUtil.hasSoftKeys(getWindowManager()));
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if(!isSwipeBackEnable()){
            getWindow().getDecorView().setBackgroundResource(R.color.colorBackground);
        }
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
        if (mSwipeBackHelper != null) {
            mSwipeBackHelper.setSwipeBackEnable(false);
        }
    }

    @Override
    protected View getNavigationBarControlView() {
        return isSwipeBackEnable()?super.getNavigationBarControlView():null;
    }
}
