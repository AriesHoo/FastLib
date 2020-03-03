package com.aries.template;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.module.activity.FastTitleActivity;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.FastUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.ActivityEvent;

import butterknife.BindView;

/**
 * @Author: AriesHoo on 2018/11/19 14:25
 * @E-Mail: AriesHoo@126.com
 * @Function: 欢迎页
 * @Description:
 */
public class SplashActivity extends FastTitleActivity {

    @BindView(R.id.tv_appSplash) TextView tvApp;
    @BindView(R.id.tv_versionSplash) TextView tvVersion;
    @BindView(R.id.tv_copyRightSplash) TextView tvCopyRight;
    /**
     * 闪屏
     */
    private long mDelayTime = 5000;

    @Override
    public void beforeSetContentView() {
        LoggerManager.i(TAG, "isTaskRoot:" + isTaskRoot() + ";getCurrent:" + FastStackUtil.getInstance().getCurrent());
        //防止应用后台后点击桌面图标造成重启的假象---MIUI及Flyme上发现过(原生未发现)
        if (!isTaskRoot()) {
            finish();
            return;
        }
        super.beforeSetContentView();
    }


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setStatusBarLightMode(true)
                .setVisibility(View.GONE);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!isTaskRoot()) {
            return;
        }
        if (!StatusBarUtil.isSupportStatusBarFontChange()) {
            //隐藏状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        startGo();
    }

    private void startGo() {
        RxJavaManager.getInstance().setTimer(mDelayTime)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<Long>() {
                    @Override
                    public void _onNext(Long entity) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        FastUtil.startActivity(mContext, MainActivity.class);
                        finish();
                    }
                });
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        StatusBarUtil.hideStatusBar(this, true);
//        NavigationBarUtil.hideNavigationBar(this, true);
//    }
}
