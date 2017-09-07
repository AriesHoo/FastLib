package com.aries.library.fast.demo.module.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.allen.library.SuperTextView;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.base.BaseTitleActivity;
import com.aries.library.fast.demo.constant.EventConstant;
import com.aries.library.fast.demo.constant.SPConstant;
import com.aries.library.fast.util.SPUtil;
import com.aries.ui.view.title.TitleBarView;

import org.simple.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created: AriesHoo on 2017/8/26 15:31
 * Function: 设置页
 * Desc:
 */

public class SettingActivity extends BaseTitleActivity {

    @BindView(R.id.stv_activityTabSetting) SuperTextView stvActivityTab;

    private boolean isActivityTabSliding = false;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText(R.string.setting);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        isActivityTabSliding = (boolean) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_TAB_SLIDING, true);
        stvActivityTab.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                stvActivityTab.setLeftTextColor(b ? Color.BLACK : Color.GRAY);
                stvActivityTab.setLeftString(getString(b ? R.string.activity_tab_sliding : R.string.activity_tab_segment));
            }
        });
        stvActivityTab.setSwitchIsChecked(!isActivityTabSliding);
        stvActivityTab.setSwitchIsChecked(isActivityTabSliding);
    }

    @Override
    protected void onDestroy() {
        if (isActivityTabSliding != stvActivityTab.getSwitchIsChecked()) {
            SPUtil.put(mContext, SPConstant.SP_KEY_ACTIVITY_TAB_SLIDING, stvActivityTab.getSwitchIsChecked());
            isActivityTabSliding = (boolean) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_TAB_SLIDING, true);
            EventBus.getDefault().post(isActivityTabSliding, EventConstant.EVENT_KEY_REFRESH_ACTIVITY_TAB);
        }
        super.onDestroy();
    }
}
