package com.aries.library.fast.module.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.aries.library.fast.FastConfig;
import com.aries.library.fast.R;
import com.aries.library.fast.entity.FastTitleConfigEntity;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.action.sheet.UIActionSheetView;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.ChromeClientCallbackManager;
import com.just.agentweb.DefaultWebCreator;

/**
 * Created: AriesHoo on 2017/8/5 19:42
 * Function: App内快速实现WebView功能
 * Desc:
 */
public abstract class FastWebActivity extends FastTitleActivity {

    protected ViewGroup mContainer;
    protected String url = "";
    protected String mCurrentUrl;
    protected AlertDialog mAlertDialog;
    protected AgentWeb mAgentWeb;
    protected AgentWeb.CommonAgentBuilder mAgentBuilder;
    protected UIActionSheetView mActionSheetView;
    protected FastTitleConfigEntity mTitleConfig;

    protected static void start(Activity mActivity, Class<? extends FastWebActivity> activity, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        FastUtil.startActivity(mActivity, activity, bundle);
    }

    @Deprecated
    protected void setAgentWeb(AgentWeb mAgentWeb, AgentWeb.CommonAgentBuilder mAgentBuilder) {

    }

    protected void setAgentWeb(AgentWeb mAgentWeb) {

    }

    protected void setAgentWeb(AgentWeb.CommonAgentBuilder mAgentBuilder) {

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
     * {@link DefaultWebCreator#createGroupWithWeb()  height_dp}
     *
     * @return
     */
    protected int getProgressHeight() {
        return 2;
    }

    @Override
    public void beforeSetContentView() {
        mTitleConfig = FastConfig.getInstance(this).getTitleConfig();
        super.beforeSetContentView();
    }

    @Override
    public void beforeInitView() {
        mContainer = findViewByViewId(R.id.lLayout_containerFastWeb);
        url = getIntent().getStringExtra("url");
        mCurrentUrl = url;
        initAgentWeb();
        super.beforeInitView();

    }

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
        super.beforeSetTitleBar(titleBar);

        titleBar.setTitleMainTextMarquee(mTitleConfig.isTitleMainTextMarquee())
                .setOnLeftTextClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .setRightTextDrawable(FastUtil.getTintDrawable(
                        getResources().getDrawable(R.drawable.fast_ic_more),
                        mTitleConfig.getTitleTextColor()))
                .setOnRightTextClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showActionSheet();
                    }
                })
                .addLeftAction(titleBar.new ImageAction(
                        FastUtil.getTintDrawable(getResources().getDrawable(R.drawable.fast_ic_close),
                                mTitleConfig.getTitleTextColor()), new View.OnClickListener() {
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
        mAgentBuilder = AgentWeb.with(this)//
                .setAgentWebParent(mContainer, new ViewGroup.LayoutParams(-1, -1))//
                .useDefaultIndicator()//
                .setIndicatorColorWithHeight(getProgressColor() != -1 ? getProgressColor() : mTitleConfig.getTitleTextColor(),
                        getProgressHeight())
                .setReceivedTitleCallback(new ChromeClientCallbackManager.ReceivedTitleCallback() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        mCurrentUrl = view.getUrl();
                        mTitleBar.setTitleMainText(title);
                    }
                })
                .setSecurityType(AgentWeb.SecurityType.strict);
        setAgentWeb(mAgentBuilder);
        mAgentWeb = mAgentBuilder
                .createAgentWeb()//
                .ready()
                .go(url);
        setAgentWeb(mAgentWeb);
        setAgentWeb(mAgentWeb, mAgentBuilder);
    }

    protected void showDialog() {
        if (mAlertDialog == null)
            mAlertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.fast_web_alert_title)
                    .setMessage(R.string.fast_web_alert_msg)
                    .setNegativeButton(R.string.fast_web_alert_left, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null)
                                mAlertDialog.dismiss();
                        }
                    })
                    .setPositiveButton(R.string.fast_web_alert_right, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (mAlertDialog != null)
                                mAlertDialog.dismiss();
                            mContext.finish();
                        }
                    }).create();
        mAlertDialog.show();
        //show之后可获取对应Button对象设置文本颜色--show之前获取对象为null
        mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    protected void showActionSheet() {
        if (mActionSheetView == null) {
            mActionSheetView = new UIActionSheetView(mContext, UIActionSheetView.STYLE_NORMAL)
                    .setItems(R.array.fast_arrays_web_more, new UIActionSheetView.OnSheetItemListener() {
                        @Override
                        public void onClick(int i) {
                            switch (i) {
                                case 0:
                                    mAgentWeb.getLoader().reload();
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
                    .setItemsTextColorResource(R.color.colorTabTextSelect)
                    .setCancelColorResource(R.color.colorTabTextSelect)
                    .setTitleColorResource(R.color.colorTabTextSelect)
                    .setCancelMessage(R.string.fast_cancel)
                    .setCancelMessageTextSize(TypedValue.COMPLEX_UNIT_DIP, 16)
                    .setItemsTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        }
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
        if (mAgentWeb.back()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
}
