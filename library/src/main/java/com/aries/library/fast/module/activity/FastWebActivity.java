package com.aries.library.fast.module.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.R;
import com.aries.library.fast.i.TitleBarViewControl;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.util.DrawableUtil;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.BasisDialog;
import com.aries.ui.widget.action.sheet.UIActionSheetDialog;
import com.aries.ui.widget.i.NavigationBarControl;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebCreator;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

/**
 * @Author: AriesHoo on 2018/6/28 14:59
 * @E-Mail: AriesHoo@126.com
 * Function: App内快速实现WebView功能
 * Description:
 * 1、调整WebView自适应屏幕代码属性{@link #initAgentWeb()}
 * 2、2019-3-20 11:45:07 增加url自动添加http://功能及规范url
 */
public abstract class FastWebActivity extends FastTitleActivity implements NavigationBarControl {

    protected ViewGroup mContainer;
    /**
     * {@use mUrl}
     */
    @Deprecated
    protected String url = "";
    protected String mUrl = "";
    protected String mCurrentUrl;
    protected AlertDialog mAlertDialog;
    protected AgentWeb mAgentWeb;
    protected AgentWeb.CommonBuilder mAgentBuilder;
    protected UIActionSheetDialog mActionSheetView;
    private TitleBarViewControl mTitleBarViewControl;
    /**
     * WebView是否处于暂停状态
     */
    private boolean mIsPause;

    public void onWebViewPause() {
        onPause();
    }


    public void onWebViewResume() {
        onResume();
    }


    protected static void start(Context mActivity, Class<? extends FastWebActivity> activity, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        FastUtil.startActivity(mActivity, activity, bundle);
    }


    protected void setAgentWeb(AgentWeb mAgentWeb) {

    }

    protected void setAgentWeb(AgentWeb.CommonBuilder mAgentBuilder) {

    }

    /**
     * 设置进度条颜色
     *
     * @return
     */
    @ColorInt
    protected int getProgressColor() {
        return -1;
    }

    /**
     * 设置进度条高度 注意此处最终AgentWeb会将其作为float 转dp2px
     * {@link DefaultWebCreator#createWebView()}   height_dp}
     *
     * @return
     */
    protected int getProgressHeight() {
        return 2;
    }

    @Override
    public void beforeSetContentView() {
        super.beforeSetContentView();
        mTitleBarViewControl = FastManager.getInstance().getTitleBarViewControl();
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        mContainer = findViewById(R.id.lLayout_containerFastWeb);
        mUrl = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(mUrl)) {
            mUrl = mUrl.startsWith("http") ? mUrl : "http://" + mUrl;
            getIntent().putExtra("url", mUrl);
        }
        url = mUrl;
        mCurrentUrl = mUrl;
        initAgentWeb();
        super.beforeInitView(savedInstanceState);

    }

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
        super.beforeSetTitleBar(titleBar);
        if (mTitleBarViewControl != null) {
            mTitleBarViewControl.createTitleBarViewControl(titleBar, this.getClass());
        }
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        })
                .setRightTextDrawable(DrawableUtil.setTintDrawable(
                        ContextCompat.getDrawable(mContext, R.drawable.fast_ic_more),
                        ContextCompat.getColor(mContext, R.color.colorTitleText)))
                .setOnRightTextClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showActionSheet();
                    }
                })
                .addLeftAction(titleBar.new ImageAction(
                        DrawableUtil.setTintDrawable(ContextCompat.getDrawable(mContext, R.drawable.fast_ic_close),
                                ContextCompat.getColor(mContext, R.color.colorTitleText)), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog();
                    }
                }));
    }

    @Override
    public int getContentLayout() {
        return R.layout.fast_activity_fast_web;
    }

    protected void initAgentWeb() {
        mAgentBuilder = AgentWeb.with(this)
                .setAgentWebParent(mContainer, new ViewGroup.LayoutParams(-1, -1))
                .useDefaultIndicator(getProgressColor() != -1 ? getProgressColor() : ContextCompat.getColor(mContext, R.color.colorTitleText),
                        getProgressHeight())
                .setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        mCurrentUrl = view.getUrl();
                        mTitleBar.setTitleMainText(title);
                    }
                })
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK);
        setAgentWeb(mAgentBuilder);
        mAgentWeb = mAgentBuilder
                .createAgentWeb()//
                .ready()
                .go(mUrl);
        WebSettings webSettings = mAgentWeb.getAgentWebSettings().getWebSettings();
        //设置webView自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        setAgentWeb(mAgentWeb);
    }

    protected void showDialog() {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.fast_web_alert_title)
                    .setMessage(R.string.fast_web_alert_msg)
                    .setNegativeButton(R.string.fast_web_alert_left, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                        }
                    })
                    .setPositiveButton(R.string.fast_web_alert_right, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                            mContext.finish();
                        }
                    }).create();
        }
        mAlertDialog.show();
        //show之后可获取对应Button对象设置文本颜色--show之前获取对象为null
        mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    protected void showActionSheet() {
        if (mActionSheetView == null) {
            mActionSheetView = new UIActionSheetDialog.ListSheetBuilder(mContext)
                    .addItems(R.array.fast_arrays_web_more)
                    .setOnItemClickListener(new UIActionSheetDialog.OnItemClickListener() {
                        @Override
                        public void onClick(BasisDialog dialog, View itemView, int i) {
                            switch (i) {
                                case 0:
                                    mAgentWeb.getUrlLoader().reload();
                                    break;
                                case 1:
                                    FastUtil.copyToClipboard(mContext, mCurrentUrl);
                                    ToastUtil.show(R.string.fast_copy_success);
                                    break;
                                case 2:
                                    FastUtil.startShareText(mContext, mCurrentUrl);
                                    break;
                            }
                        }
                    })
                    .setCancel(R.string.fast_cancel)
                    .setTextSizeUnit(TypedValue.COMPLEX_UNIT_DIP)
                    .create();
        }
        mActionSheetView.setNavigationBarControl(this);
        mActionSheetView.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mAgentWeb != null && mAgentWeb.back()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        if (mAgentWeb != null && mAgentWeb.getWebLifeCycle() != null && !mIsPause && !isFinishing()) {
            mAgentWeb.getWebLifeCycle().onPause();
            mIsPause = true;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mAgentWeb != null && mAgentWeb.getWebLifeCycle() != null && mIsPause) {
            mAgentWeb.getWebLifeCycle().onResume();
            mIsPause = false;
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAgentWeb != null && mAgentWeb.getWebLifeCycle() != null) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean setNavigationBar(Dialog dialog, NavigationViewHelper helper, View bottomView) {
        return false;
    }

}
