package com.aries.library.fast.demo.module.mine;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;

import com.allen.library.SuperTextView;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.base.BaseTitleActivity;
import com.aries.library.fast.demo.constant.EventConstant;
import com.aries.library.fast.demo.constant.SPConstant;
import com.aries.library.fast.util.SPUtil;
import com.aries.ui.view.title.TitleBarView;

import org.simple.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created: AriesHoo on 2017/8/26 15:31
 * Function: 设置页
 * Desc:
 */

public class SettingActivity extends BaseTitleActivity {

    @BindView(R.id.stv_activityTabSetting) SuperTextView stvActivityTab;
    @BindView(R.id.stv_activityAnimationSetting) SuperTextView stvActivityAnimation;
    private boolean isActivityTabSliding = false;
    private boolean isActivityAnimationAlways = true;
    private List<String> listAnimation;
    private int animationIndex = 4;
    private int chooseIndex = 4;
    private AlertDialog alertDialog;

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
        listAnimation = Arrays.asList(getResources().getStringArray(R.array.arrays_animation));
        isActivityTabSliding = (boolean) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_TAB_SLIDING, true);
        animationIndex = (int) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_INDEX, animationIndex);
        chooseIndex = animationIndex;
        stvActivityTab.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                stvActivityTab.setLeftTextColor(b ? Color.BLACK : Color.GRAY);
                stvActivityTab.setLeftString(getString(b ? R.string.activity_tab_sliding : R.string.activity_tab_segment));
            }
        });
        stvActivityTab.setSwitchIsChecked(!isActivityTabSliding);
        stvActivityTab.setSwitchIsChecked(isActivityTabSliding);

        isActivityAnimationAlways = (boolean) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_ALWAYS, true);
        stvActivityAnimation.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                stvActivityAnimation.setRightTextColor(b ? Color.BLACK : Color.GRAY);
                stvActivityAnimation.setRightString(b ? "一直有效" : "第一次有效");
                SPUtil.put(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_ALWAYS, b);
                EventBus.getDefault().post(b, EventConstant.EVENT_KEY_CHANGE_ADAPTER_ANIMATION_ALWAYS);
            }
        });
        stvActivityAnimation.setSwitchIsChecked(!isActivityAnimationAlways);
        stvActivityAnimation.setSwitchIsChecked(isActivityAnimationAlways);
        stvActivityAnimation.setLeftBottomString(listAnimation.get(animationIndex));
    }

    @OnClick(R.id.stv_activityAnimationSetting)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.stv_activityAnimationSetting:
                if (alertDialog == null) {
                    alertDialog = new AlertDialog.Builder(mContext)
                            .setSingleChoiceItems(R.array.arrays_animation, animationIndex, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    chooseIndex = which;
                                }
                            })
                            .setTitle("选择Adapter条目加载动画")
                            .setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SPUtil.put(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_INDEX, chooseIndex);
                                    animationIndex = (int) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_INDEX, animationIndex);
                                    stvActivityAnimation.setLeftBottomString(listAnimation.get(animationIndex));
                                    EventBus.getDefault().post(animationIndex, EventConstant.EVENT_KEY_CHANGE_ADAPTER_ANIMATION);
                                }
                            })
                            .create();
                }
                alertDialog.getListView().setVerticalScrollBarEnabled(false);
                alertDialog.getListView().setHorizontalScrollBarEnabled(false);
                alertDialog.show();
                break;
        }
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
