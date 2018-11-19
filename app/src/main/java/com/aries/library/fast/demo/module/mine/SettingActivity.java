package com.aries.library.fast.demo.module.mine;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.allen.library.SuperTextView;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.constant.EventConstant;
import com.aries.library.fast.demo.constant.GlobalConstant;
import com.aries.library.fast.demo.constant.SPConstant;
import com.aries.library.fast.module.activity.FastTitleActivity;
import com.aries.library.fast.util.SPUtil;
import com.aries.ui.view.title.TitleBarView;

import org.simple.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: AriesHoo on 2018/11/19 14:24
 * @E-Mail: AriesHoo@126.com
 * @Function: 设置页
 * @Description:
 */
public class SettingActivity extends FastTitleActivity {

    @BindView(R.id.stv_activityAnimationSetting) SuperTextView stvActivityAnimation;
    @BindView(R.id.switch_activityTabSetting) SwitchCompat switchActivityTab;
    @BindView(R.id.switch_activityAnimationSetting) SwitchCompat switchActivityAnimation;
    private boolean isActivityTabSliding = true;
    private boolean isActivityAnimationAlways = true;
    private List<String> listAnimation;
    private int animationIndex = GlobalConstant.GLOBAL_ADAPTER_ANIMATION_VALUE - 1;
    private int chooseIndex = animationIndex;
    private AlertDialog alertDialog;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        listAnimation = Arrays.asList(getResources().getStringArray(R.array.arrays_animation));
        isActivityTabSliding = (boolean) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_TAB_SLIDING, isActivityTabSliding);
        animationIndex = (int) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_INDEX, animationIndex);
        chooseIndex = animationIndex;
        switchActivityTab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchActivityTab.setTextColor(b ? Color.BLACK : Color.GRAY);
                switchActivityTab.setText(getString(b ? R.string.activity_tab_sliding : R.string.activity_tab_segment));
            }
        });
        switchActivityTab.setChecked(isActivityTabSliding);

        isActivityAnimationAlways = (boolean) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_ALWAYS, true);
        switchActivityAnimation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchActivityAnimation.setTextColor(b ? Color.BLACK : Color.GRAY);
                switchActivityAnimation.setText(b ? "一直有效" : "第一次有效");
                SPUtil.put(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_ALWAYS, b);
                EventBus.getDefault().post(b, EventConstant.EVENT_KEY_CHANGE_ADAPTER_ANIMATION_ALWAYS);
            }
        });
        switchActivityAnimation.setChecked(isActivityAnimationAlways);
        stvActivityAnimation.setLeftBottomString(listAnimation.get(animationIndex));
    }

    @OnClick({R.id.stv_activityAnimationSetting})
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
        if (isActivityTabSliding != switchActivityTab.isChecked()) {
            SPUtil.put(mContext, SPConstant.SP_KEY_ACTIVITY_TAB_SLIDING, switchActivityTab.isChecked());
            EventBus.getDefault().post(switchActivityTab.isChecked(), EventConstant.EVENT_KEY_REFRESH_ACTIVITY_TAB);
        }
        super.onDestroy();
    }
}
