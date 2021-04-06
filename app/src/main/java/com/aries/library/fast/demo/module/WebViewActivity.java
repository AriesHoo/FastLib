package com.aries.library.fast.demo.module;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.module.main.MainActivity;
import com.aries.library.fast.i.IFastRefreshView;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.module.activity.FastWebActivity;
import com.aries.library.fast.retrofit.FastDownloadObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.util.FastFileUtil;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.helper.navigation.NavigationBarUtil;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.util.DrawableUtil;
import com.aries.ui.util.RomUtil;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.action.sheet.UIActionSheetDialog;
import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.download.library.ResourceRequest;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebSettingsImpl;
import com.just.agentweb.DefaultDownloadImpl;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.IVideo;
import com.just.agentweb.MiddlewareWebChromeBase;
import com.just.agentweb.VideoImpl;
import com.just.agentweb.WebListenerManager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.io.File;

/**
 * @Author: AriesHoo on 2018/7/30 11:04
 * @E-Mail: AriesHoo@126.com
 * Function: 应用内浏览器
 * Description:
 * 1、2018-7-30 11:04:22 新增图片下载功能
 * 2、2019-4-18 09:34:51 增加BasisDialog 虚拟导航栏沉浸控制
 */
public class WebViewActivity extends FastWebActivity implements IFastRefreshView {

    private String mFilePath = FastFileUtil.getCacheDir();
    private String mFormat = "保存图片<br><small><font color='#2394FE'>图片文件夹路径:%1s</font></small>";
    private static boolean mIsShowTitle = true;
    private RefreshLayout mRefreshLayout;

    public static void start(Context mActivity, String url) {
        start(mActivity, url, true);
    }

    public static void start(Context mActivity, String url, boolean isShowTitle) {
        mIsShowTitle = isShowTitle;
        start(mActivity, WebViewActivity.class, url,true);
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
        mAgentBuilder.setAgentWebWebSettings(AgentWebSettingsImpl.getInstance())
                .setAgentWebWebSettings(getSettings())
                .useMiddlewareWebChrome(new MiddlewareWebChromeBase() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        if (newProgress == 100 && mRefreshLayout != null) {
                            mCurrentUrl = view.getUrl();
                            mRefreshLayout.finishRefresh();
                            int position = (int) SPUtil.get(mContext, mCurrentUrl, 0);
                            view.scrollTo(0, position);
                        }
                    }

                    @Override
                    public void onHideCustomView() {
                        super.onHideCustomView();
                        getIVideo().onHideCustomView();
                        //显示状态栏
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        NavigationBarUtil.setNavigationBarLightMode(mContext);
                    }

                    @Override
                    public void onShowCustomView(View view, CustomViewCallback callback) {
                        super.onShowCustomView(view, callback);
                        getIVideo().onShowCustomView(view, callback);
                        //隐藏状态栏
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        RxJavaManager.getInstance().setTimer(100)
                                .subscribe(new FastObserver<Long>() {
                                    @Override
                                    public void _onNext(Long entity) {
                                        NavigationBarUtil.setNavigationBarDarkMode(mContext);
                                    }
                                });
                    }
                });
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
                .setNavigationBarControl(this)
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
    public void onRefresh(RefreshLayout refreshLayout) {
        mAgentWeb.getUrlLoader().reload();
    }

    @Override
    public View getContentView() {
        LoggerManager.i("getContentView");
        if (mAgentWeb != null) {
            LoggerManager.i("getContentView", "webView:" + mAgentWeb.getWebCreator().getWebView());
            return mAgentWeb.getWebCreator().getWebView();
        }
        return null;
    }

//    @Override
//    public void setRefreshLayout(SmartRefreshLayout refreshLayout) {
//        this.mRefreshLayout = refreshLayout;
//        refreshLayout.setRefreshHeader(new StoreHouseHeader(mContext)
//                .initWithString("FastLib Refresh")
//                .setTextColor(ContextCompat.getColor(mContext, R.color.colorTextBlack)))
//                .setPrimaryColorsId(R.color.transparent)
//                .setEnableHeaderTranslationContent(true);
//    }

    @Override
    public boolean setNavigationBar(Dialog dialog, NavigationViewHelper helper, View bottomView) {
        Drawable drawableTop = ContextCompat.getDrawable(mContext, R.color.colorLineGray);
        DrawableUtil.setDrawableWidthHeight(drawableTop, SizeUtil.getScreenWidth(), SizeUtil.dp2px(0.5f));
        helper.setPlusNavigationViewEnable(true)
                .setNavigationViewColor(Color.argb(isTrans() ? 0 : 60, 0, 0, 0))
                .setNavigationViewDrawableTop(drawableTop)
                .setNavigationLayoutColor(Color.WHITE);
        //导航栏在底部控制才有意义,不然会很丑;开发者自己决定;这里仅供参考
        return NavigationBarUtil.isNavigationAtBottom(dialog.getWindow());
    }

    protected boolean isTrans() {
        return (RomUtil.isEMUI() && (RomUtil.getEMUIVersion().compareTo("EmotionUI_4.1") > 0));
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

    @Override
    protected void onPause() {
        super.onPause();
        WebView webView = mAgentWeb.getWebCreator().getWebView();
        SPUtil.put(mContext, webView.getUrl(), webView.getScrollY());
    }

    private IVideo mIVideo = null;

    private IVideo getIVideo() {
        if (mIVideo == null) {
            mIVideo = new VideoImpl(mContext, mAgentWeb.getWebCreator().getWebView());
        }
        return mIVideo;
    }


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
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.download.library:Downloader:4.1.1' ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            @Override
            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
                return super.setDownloader(webView,
                        new DefaultDownloadImpl((Activity) webView.getContext(),
                                webView,
                                this.mAgentWeb.getPermissionInterceptor()) {

                            @Override
                            protected ResourceRequest createResourceRequest(String url) {
                                return DownloadImpl.getInstance()
                                        .with(getApplicationContext())
                                        .url(url)
                                        .quickProgress()
                                        .addHeader("", "")
                                        .setEnableIndicator(true)
                                        .autoOpenIgnoreMD5()
                                        .setRetry(5)
                                        .setBlockMaxTime(100000L);
                            }

                            @Override
                            protected void taskEnqueue(ResourceRequest resourceRequest) {
                                resourceRequest.enqueue(new DownloadListenerAdapter() {
                                    @Override
                                    public void onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, Extra extra) {
                                        super.onStart(url, userAgent, contentDisposition, mimetype, contentLength, extra);
                                    }

                                    @MainThread
                                    @Override
                                    public void onProgress(String url, long downloaded, long length, long usedTime) {
                                        super.onProgress(url, downloaded, length, usedTime);
                                    }

                                    @Override
                                    public boolean onResult(Throwable throwable, Uri path, String url, Extra extra) {
                                        return super.onResult(throwable, path, url, extra);
                                    }
                                });
                            }
                        });
            }
        };
    }
}
