package com.aries.library.fast.demo.module.main.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.module.activity.FastTitleActivity;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.view.title.TitleBarView;

import butterknife.OnClick;

/**
 * Created: AriesHoo on 2017/12/9 17:05
 * E-Mail: AriesHoo@126.com
 * Function:
 * Description:
 */
public class ToastActivity extends FastTitleActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_toast;
    }

    @Override
    protected void beforeControlNavigation(NavigationViewHelper navigationHelper) {
        super.beforeControlNavigation(navigationHelper);
        navigationHelper.setNavigationLayoutColor(Color.BLUE);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("ToastUtil工具类示例");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mContentView.setBackgroundColor(Color.BLUE);
    }

    @OnClick({R.id.rtv_system, R.id.rtv_normal, R.id.rtv_success, R.id.rtv_failed, R.id.rtv_warning})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rtv_system:
                Toast.makeText(mContext, R.string.toast_system, Toast.LENGTH_SHORT).show();
                break;
            case R.id.rtv_normal:
                ToastUtil.show(R.string.toast_normal, ToastUtil.newBuilder().setGravity(Gravity.CENTER));
                break;
            case R.id.rtv_success:
                ToastUtil.showSuccess(R.string.toast_success);
                break;
            case R.id.rtv_failed:
                ToastUtil.showFailed(R.string.toast_failed);
                break;
            case R.id.rtv_warning:
                ToastUtil.showWarning(R.string.toast_warning);
                break;
        }
    }
}
