package com.aries.library.fast.demo.module.main.sample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.module.activity.FastTitleActivity;
import com.aries.library.fast.util.FastShareUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.view.title.TitleBarView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: AriesHoo on 2021/4/30 13:43
 * @E-Mail: AriesHoo@126.com
 * @Function: 系统分享功能封装演示
 * @Description:
 */
public class ShareTextActivity extends FastTitleActivity {
    @BindView(R.id.ret_textShareUtil)
    EditText mRetTextShareUtil;

    @Override
    public int getContentLayout() {
        return R.layout.activity_share_text;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @OnClick({R.id.btn_shareTextToQQ, R.id.btn_shareTextToQQFriend, R.id.btn_shareTextToQQComputer,
            R.id.btn_shareTextToQQFavorites, R.id.btn_shareTextToWeChat, R.id.btn_shareTextToWeChatFriend,
            R.id.btn_shareTextToWeChatFavorites, R.id.btn_shareTextToWeiBo, R.id.btn_shareTextToWeiBoFriend,
            R.id.btn_shareTextToWeiBoTimeLine, R.id.btn_shareTextToDingTalk, R.id.btn_shareTextToWeWork,
            R.id.btn_shareTextToApps, R.id.btn_shareTextToAllApps})
    public void onViewClicked(View view) {
        if (TextUtils.isEmpty(mRetTextShareUtil.getText().toString())) {
            ToastUtil.show("请输入分享文本信息");
            return;
        }
        switch (view.getId()) {
            case R.id.btn_shareTextToQQ:
                FastShareUtil.shareTextToQQ(mContext, mRetTextShareUtil.getText().toString());
                break;
            case R.id.btn_shareTextToQQFriend:
                FastShareUtil.shareTextToQQFriend(mContext, mRetTextShareUtil.getText().toString());
                break;
            case R.id.btn_shareTextToQQComputer:
                FastShareUtil.shareTextToQQComputer(mContext, mRetTextShareUtil.getText().toString());
                break;
            case R.id.btn_shareTextToQQFavorites:
                FastShareUtil.shareTextToQQFavorites(mContext, mRetTextShareUtil.getText().toString());
                break;
            case R.id.btn_shareTextToWeChat:
                FastShareUtil.shareTextToWeChat(mContext, mRetTextShareUtil.getText().toString());
                break;
            case R.id.btn_shareTextToWeChatFriend:
                FastShareUtil.shareTextToWeChatFriend(mContext, mRetTextShareUtil.getText().toString());
                break;
            case R.id.btn_shareTextToWeChatFavorites:
                FastShareUtil.shareTextToWeChatFavorites(mContext, mRetTextShareUtil.getText().toString());
                break;
            case R.id.btn_shareTextToWeiBo:
                FastShareUtil.shareTextToWeiBo(mContext, mRetTextShareUtil.getText().toString());
                break;
            case R.id.btn_shareTextToWeiBoFriend:
                FastShareUtil.shareTextToWeiBoFriend(mContext, mRetTextShareUtil.getText().toString());
                break;
            case R.id.btn_shareTextToWeiBoTimeLine:
                FastShareUtil.shareTextToWeiBoTimeLine(mContext, mRetTextShareUtil.getText().toString());
                break;
            case R.id.btn_shareTextToDingTalk:
                FastShareUtil.shareTextToDingTalk(mContext, mRetTextShareUtil.getText().toString());
                break;
            case R.id.btn_shareTextToWeWork:
                FastShareUtil.shareTextToWeWork(mContext, mRetTextShareUtil.getText().toString());
                break;
            case R.id.btn_shareTextToApps:
                List<String> listApp = new ArrayList<>();
                listApp.add(FastShareUtil.QQ_PACKAGE_NAME);
                listApp.add(FastShareUtil.WE_CHAT_PACKAGE_NAME);
                listApp.add(FastShareUtil.DING_TALK_PACKAGE_NAME);
                listApp.add(FastShareUtil.WEI_BO_PACKAGE_NAME);
                FastShareUtil.shareTextToApps(mContext, mRetTextShareUtil.getText().toString(), listApp, "Subject", "Title");
                break;
            case R.id.btn_shareTextToAllApps:
                FastShareUtil.shareTextToAllApps(mContext, mRetTextShareUtil.getText().toString(), "Subject", "Title");
                break;
        }
    }
}
