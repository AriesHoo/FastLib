package com.aries.library.fast.basis;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.R;
import com.aries.library.fast.i.IBasisView;
import com.aries.library.fast.i.IFastRefreshLoadView;
import com.aries.library.fast.i.QuitAppControl;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created: AriesHoo on 2017/7/19 15:37
 * E-Mail: AriesHoo@126.com
 * Function: 所有Activity的基类
 * Description:
 * 1、2018-6-15 09:31:42 调整滑动返回类控制
 * 2、2018-6-20 17:15:12 调整主页back键操作逻辑
 * 3、2018-6-21 14:05:57 删除滑动返回控制改由全局控制
 * 4、2018-6-22 13:38:32 删除NavigationViewHelper控制方法改由全局控制
 * 5、2018-6-25 13:25:30 增加解决StatusLayoutManager与SmartRefreshLayout冲突解决方案
 */
public abstract class BasisActivity extends RxAppCompatActivity implements IBasisView {

    protected Activity mContext;
    protected View mContentView;
    protected Unbinder mUnBinder;

    protected boolean mIsViewLoaded = false;
    protected boolean mIsFirstShow = true;
    protected boolean mIsFirstBack = true;
    protected long mDelayBack = 2000;
    protected final String TAG = getClass().getSimpleName();
    private QuitAppControl mQuitAppControl;

    @Nullable
    @Override
    public <T extends View> T findView(int viewId) {
        return (T) findViewById(viewId);
    }

    @Override
    public boolean isEventBusEnable() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isEventBusEnable())
            EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        mContext = this;
        beforeSetContentView();
        mContentView = View.inflate(mContext, getContentLayout(), null);
        //解决StatusLayoutManager与SmartRefreshLayout冲突
        if (this instanceof IFastRefreshLoadView && mContentView.getClass() == SmartRefreshLayout.class) {
            FrameLayout frameLayout = new FrameLayout(mContext);
            if (mContentView.getLayoutParams() != null) {
                frameLayout.setLayoutParams(mContentView.getLayoutParams());
            }
            frameLayout.addView(mContentView);
            mContentView = frameLayout;
        }
        setContentView(mContentView);
        mUnBinder = ButterKnife.bind(this);
        mIsViewLoaded = true;
        beforeInitView();
        initView(savedInstanceState);
    }

    @Override
    protected void onResume() {
        beforeLazyLoad();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (isEventBusEnable())
            EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    @Override
    public void beforeSetContentView() {
    }

    @Override
    public void beforeInitView() {
        if (FastManager.getInstance().getActivityFragmentControl() != null)
            FastManager.getInstance().getActivityFragmentControl().setContentViewBackground(mContentView, this.getClass());
    }

    @Override
    public void loadData() {

    }

    private void beforeLazyLoad() {
        if (!mIsViewLoaded) {//确保视图加载及视图绑定完成避免刷新UI抛出异常
            RxJavaManager.getInstance().setTimer(10, new RxJavaManager.TimerListener() {
                @Override
                public void timeEnd() {
                    beforeLazyLoad();
                }
            });
        } else {
            lazyLoad();
        }
    }

    private void lazyLoad() {
        if (mIsFirstShow) {
            mIsFirstShow = false;
            loadData();
        }
    }


    /**
     * 退出程序
     */
    protected void quitApp() {
        mQuitAppControl = FastManager.getInstance().getQuitAppControl();
        mDelayBack = mQuitAppControl != null ? mQuitAppControl.quipApp(mIsFirstBack, this) : mDelayBack;
        //时延太小/已是第二次提示直接通知执行最终操作
        if (mDelayBack <= 0 || !mIsFirstBack) {
            if (mQuitAppControl != null) {
                mQuitAppControl.quipApp(false, this);
            } else {
                FastStackUtil.getInstance().exit();
            }
            return;
        }
        ToastUtil.show(R.string.fast_quit_app);
        //编写逻辑
        if (mIsFirstBack) {
            mIsFirstBack = false;
            RxJavaManager.getInstance().setTimer(mDelayBack, new RxJavaManager.TimerListener() {
                @Override
                public void timeEnd() {
                    mIsFirstBack = true;
                }
            });
        }
    }
}
