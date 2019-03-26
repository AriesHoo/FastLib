package com.aries.library.fast.demo.module;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.module.main.MainActivity;
import com.aries.library.fast.demo.util.NotificationUtil;
import com.aries.library.fast.i.IFastRefreshView;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.module.activity.FastWebActivity;
import com.aries.library.fast.retrofit.FastDownloadObserver;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.util.FastFileUtil;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.action.sheet.UIActionSheetDialog;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.LogUtils;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.download.AgentWebDownloader;
import com.just.agentweb.download.DefaultDownloadImpl;
import com.just.agentweb.download.DownloadListenerAdapter;
import com.just.agentweb.download.DownloadingService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.io.File;

import androidx.appcompat.app.AlertDialog;

/**
 * @Author: AriesHoo on 2018/7/30 11:04
 * @E-Mail: AriesHoo@126.com
 * Function: 应用内浏览器
 * Description:
 * 1、2018-7-30 11:04:22 新增图片下载功能
 */
public class WebViewActivity extends FastWebActivity implements IFastRefreshView {

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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String url = intent.getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            start(mContext, url);
            finish();
        }
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
        //设置 IAgentWebSettings
        mAgentBuilder.setAgentWebWebSettings(getSettings());
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
        UIActionSheetDialog actionSheetDialog = new UIActionSheetDialog.ListSheetBuilder(mContext)
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
        actionSheetDialog.show();
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
                        new AlertDialog.Builder(mContext)
                                .setTitle("保存成功")
                                .setMessage("图片已保存至" + mFilePath + "文件夹")
                                .setPositiveButton(R.string.ensure, null)
                                .create()
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //横竖屏切换过后不能设置滑动返回--可以切换试一试效果
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public void onBackPressed() {
        Activity activity = FastStackUtil.getInstance().getPrevious();
        if (activity == null) {
            FastUtil.startActivity(mContext, MainActivity.class);
        }
        super.onBackPressed();

    }

    /**
     * 更新于 AgentWeb  4.0.0
     */
    protected DownloadListenerAdapter mDownloadListenerAdapter = new DownloadListenerAdapter() {

        private DownloadingService mDownloadingService;

        /**
         *
         * @param url                下载链接
         * @param userAgent          UserAgent
         * @param contentDisposition ContentDisposition
         * @param mimetype           资源的媒体类型
         * @param contentLength      文件长度
         * @param extra              下载配置 ， 用户可以通过 Extra 修改下载icon ， 关闭进度条 ， 是否强制下载。
         * @return true 表示用户处理了该下载事件 ， false 交给 AgentWeb 下载
         */
        @Override
        public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
            LogUtils.i(TAG, "onStart:" + url);
            // 是否开启断点续传
            extra.setOpenBreakPointDownload(true)
                    //下载通知的icon
                    .setIcon(R.drawable.ic_file_download_black_24dp)
                    // 连接最大时长
                    .setConnectTimeOut(6000)
                    // 以8KB位单位，默认60s ，如果60s内无法从网络流中读满8KB数据，则抛出异常
                    .setBlockMaxTime(10 * 60 * 1000)
                    // 下载最大时长
                    .setDownloadTimeOut(Long.MAX_VALUE)
                    // 串行下载更节省资源哦
                    .setParallelDownload(false)
                    // false 关闭进度通知
                    .setEnableIndicator(true)
                    // 自定义请求头
                    .addHeader("Cookie", "xx")
                    // 下载完成自动打开
                    .setAutoOpen(true)
                    // 强制下载，不管网络网络类型
                    .setForceDownload(true);
            return false;
        }

        /**
         *
         * 不需要暂停或者停止下载该方法可以不必实现
         * @param url
         * @param downloadingService  用户可以通过 DownloadingService#shutdownNow 终止下载
         */
        @Override
        public void onBindService(String url, DownloadingService downloadingService) {
            super.onBindService(url, downloadingService);
            mDownloadingService = downloadingService;
            LogUtils.i(TAG, "onBindService:" + url + "  DownloadingService:" + downloadingService);
        }

        /**
         * 回调onUnbindService方法，让用户释放掉 DownloadingService。
         * @param url
         * @param downloadingService
         */
        @Override
        public void onUnbindService(String url, DownloadingService downloadingService) {
            super.onUnbindService(url, downloadingService);
            mDownloadingService = null;
            LogUtils.i(TAG, "onUnbindService:" + url);
        }

        /**
         *
         * @param url  下载链接
         * @param loaded  已经下载的长度
         * @param length    文件的总大小
         * @param usedTime   耗时 ，单位ms
         * 注意该方法回调在子线程 ，线程名 AsyncTask #XX 或者 AgentWeb # XX
         */
        @Override
        public void onProgress(String url, long loaded, long length, long usedTime) {
            int mProgress = (int) ((loaded) / Float.valueOf(length) * 100);
            LogUtils.i(TAG, "onProgress:" + mProgress);
            //进度到100--主动调用关闭下载并重启下载因断点下载直接回调成功
            if (mProgress == 100) {
                mDownloadingService.shutdownNow().performReDownload();
            }
            super.onProgress(url, loaded, length, usedTime);
        }

        /**
         *
         * @param path 文件的绝对路径
         * @param url  下载地址
         * @param throwable    如果异常，返回给用户异常
         * @return true 表示用户处理了下载完成后续的事件 ，false 默认交给AgentWeb 处理
         */
        @Override
        public boolean onResult(String path, String url, Throwable throwable) {
            NotificationUtil.getInstance().cancelAll();
            //下载成功
            if (null == throwable) {
                if (path.endsWith("apk")) {
                    FastFileUtil.installApk(new File(path), getPackageName() + ".AgentWebFileProvider");
                }
            } else {//下载失败

            }
            // true  不会发出下载完成的通知 , 或者打开文件
            return path.endsWith("apk");
        }
    };

    /**
     * @return IAgentWebSettings
     */
    public IAgentWebSettings getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.just.agentweb:download:4.0.0 ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            @Override
            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
                return super.setDownloader(webView,
                        DefaultDownloadImpl
                                .create((Activity) webView.getContext(),
                                        webView,
                                        mDownloadListenerAdapter,
                                        mDownloadListenerAdapter,
                                        this.mAgentWeb.getPermissionInterceptor()));
            }
        };
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        mAgentWeb.getUrlLoader().reload();
        refreshLayout.finishRefresh();
    }

    @Override
    public void setRefreshLayout(SmartRefreshLayout refreshLayout) {
        int statusHeight = StatusBarUtil.getStatusBarHeight() + getResources().getDimensionPixelSize(R.dimen.dp_title_height);
        refreshLayout.setHeaderInsetStartPx(statusHeight);
    }
}
