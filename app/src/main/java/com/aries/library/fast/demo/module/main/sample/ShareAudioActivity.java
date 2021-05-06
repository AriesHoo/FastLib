package com.aries.library.fast.demo.module.main.sample;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.helper.ImagePickerHelper;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.module.activity.FastTitleActivity;
import com.aries.library.fast.util.FastShareUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.view.title.TitleBarView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: AriesHoo on 2021/5/3 22:57
 * @E-Mail: AriesHoo@126.com
 * @Function: 系统分享功能-分享音频演示
 * @Description:
 */
public class ShareAudioActivity extends FastTitleActivity {
    private ImagePickerHelper mImagePickerHelper;

    @Override
    public int getContentLayout() {
        return R.layout.activity_share_audio;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleSubText("Audio分享单音频,Audios分享多音频");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mImagePickerHelper = new ImagePickerHelper(mContext);
    }

    @OnClick({R.id.btn_shareAudioToQQ, R.id.btn_shareAudioToQQFriend,
            R.id.btn_shareAudiosToQQFriend, R.id.btn_shareAudioToQQComputer,
            R.id.btn_shareAudioToQQFavorites, R.id.btn_shareAudioToQQFaceToFace,
            R.id.btn_shareAudioToWeChatFriend, R.id.btn_shareAudioToWeChatFavorites,
            R.id.btn_shareAudioToWeiBoFriend, R.id.btn_shareAudioToDingTalk,
            R.id.btn_shareAudiosToDingTalk, R.id.btn_shareAudioToWeWork,
            R.id.btn_shareAudiosToWeWork, R.id.btn_shareAudioToAllApps,
            R.id.btn_shareAudiosToAllApps, R.id.btn_shareAudioToAppActivity,
            R.id.btn_shareAudiosToAppActivity})
    public void onViewClicked(View view) {
        mImagePickerHelper.selectAudio(3000, 5, (requestCode, list) -> {
            if (list == null || list.size() == 0 || requestCode != 3000) {
                ToastUtil.show("请选择音频文件");
                return;
            }
            List<Uri> uriList = new ArrayList<>(list.size());
            for (String str : list) {
                uriList.add(Uri.parse(str));
            }
            if (uriList.isEmpty()) {
                ToastUtil.show("音频文件为空");
                return;
            }
            switch (view.getId()) {
                case R.id.btn_shareAudioToQQ:
                    FastShareUtil.shareAudioToQQ(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareAudioToQQFriend:
                    FastShareUtil.shareAudioToQQFriend(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareAudiosToQQFriend:
                    FastShareUtil.shareAudiosToQQFriend(mContext, uriList);
                    break;
                case R.id.btn_shareAudioToQQComputer:
                    FastShareUtil.shareAudioToQQComputer(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareAudioToQQFavorites:
                    FastShareUtil.shareAudioToQQFavorites(mContext, uriList.get(0), new File(list.get(0)).getName());
                    break;
                case R.id.btn_shareAudioToQQFaceToFace:
                    FastShareUtil.shareAudioToQQFaceToFace(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareAudioToWeChatFriend:
                    FastShareUtil.shareAudioToWeChatFriend(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareAudioToWeChatFavorites:
                    FastShareUtil.shareAudioToWeChatFavorites(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareAudioToWeiBoFriend:
                    FastShareUtil.shareAudioToWeiBoFriend(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareAudioToDingTalk:
                    FastShareUtil.shareAudioToDingTalk(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareAudiosToDingTalk:
                    FastShareUtil.shareAudiosToDingTalk(mContext, uriList);
                    break;
                case R.id.btn_shareAudioToWeWork:
                    FastShareUtil.shareAudioToWeWork(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareAudiosToWeWork:
                    FastShareUtil.shareAudiosToWeWork(mContext, uriList);
                    break;
                case R.id.btn_shareAudioToAllApps:
                    FastShareUtil.shareAudioToAllApps(mContext, uriList.get(0), "Subject", "Title");
                    break;
                case R.id.btn_shareAudiosToAllApps:
                    FastShareUtil.shareAudiosToAllApps(mContext, uriList, "Subject", "Title");
                    break;
                case R.id.btn_shareAudioToAppActivity:
                    FastShareUtil.shareAudioToAppActivity(mContext, uriList.get(0), "com.android.mms", "com.android.mms.ui.ComposeMessageActivity", null, null);
                    break;
                case R.id.btn_shareAudiosToAppActivity:
                    FastShareUtil.shareAudiosToAppActivity(mContext, uriList, "com.android.mms", "com.android.mms.ui.ComposeMessageActivity", null, null);
                    break;
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mImagePickerHelper != null) {
            mImagePickerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
}
