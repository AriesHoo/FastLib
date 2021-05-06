package com.aries.library.fast.demo.module.main.sample;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

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

import butterknife.OnClick;

/**
 * @Author: AriesHoo on 2021/5/3 22:57
 * @E-Mail: AriesHoo@126.com
 * @Function: 系统分享功能-分享音频演示
 * @Description:
 */
public class ShareVideoActivity extends FastTitleActivity {
    private ImagePickerHelper mImagePickerHelper;

    @Override
    public int getContentLayout() {
        return R.layout.activity_share_video;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleSubText("Video分享单视频,Videos分享多视频");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mImagePickerHelper = new ImagePickerHelper(mContext);
    }

    @OnClick({R.id.btn_shareVideoToQQ, R.id.btn_shareVideoToQQFriend,
            R.id.btn_shareVideosToQQFriend, R.id.btn_shareVideoToQQComputer,
            R.id.btn_shareVideoToQQFavorites, R.id.btn_shareVideoToQQFaceToFace,
            R.id.btn_shareVideoToWeChat, R.id.btn_shareVideoToWeChatFriend,
            R.id.btn_shareVideoToWeChatFavorites, R.id.btn_shareVideoToWeiBo,
            R.id.btn_shareVideoToWeiBoFriend, R.id.btn_shareVideoToWeiBoTimeLine,
            R.id.btn_shareVideoToWeiBoStory, R.id.btn_shareVideoToDingTalk,
            R.id.btn_shareVideosToDingTalk, R.id.btn_shareVideoToWeWork,
            R.id.btn_shareVideosToWeWork, R.id.btn_shareVideoToAllApps,
            R.id.btn_shareVideosToAllApps, R.id.btn_shareVideoToAppActivity,
            R.id.btn_shareVideosToAppActivity})
    public void onViewClicked(View view) {
        mImagePickerHelper.selectVideo(2000, 5, (requestCode, list) -> {
            if (list == null || list.size() == 0 || requestCode != 2000) {
                ToastUtil.show("请选择视频文件");
                return;
            }
            List<Uri> uriList = new ArrayList<>(list.size());
            for (String str : list) {
                uriList.add(Uri.parse(str));
            }
            if (uriList.isEmpty()) {
                ToastUtil.show("视频文件为空");
                return;
            }
            switch (view.getId()) {
                case R.id.btn_shareVideoToQQ:
                    FastShareUtil.shareVideoToQQ(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareVideoToQQFriend:
                    FastShareUtil.shareVideoToQQFriend(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareVideosToQQFriend:
                    FastShareUtil.shareVideosToQQFriend(mContext, uriList);
                    break;
                case R.id.btn_shareVideoToQQComputer:
                    FastShareUtil.shareVideoToQQComputer(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareVideoToQQFavorites:
                    FastShareUtil.shareVideoToQQFavorites(mContext, uriList.get(0), new File(list.get(0)).getName());
                    break;
                case R.id.btn_shareVideoToQQFaceToFace:
                    FastShareUtil.shareVideoToQQFaceToFace(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareVideoToWeChat:
                    FastShareUtil.shareVideoToWeChat(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareVideoToWeChatFriend:
                    FastShareUtil.shareVideoToWeChatFriend(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareVideoToWeChatFavorites:
                    FastShareUtil.shareVideoToWeChatFavorites(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareVideoToWeiBo:
                    FastShareUtil.shareVideoToWeiBo(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareVideoToWeiBoFriend:
                    FastShareUtil.shareVideoToWeiBoFriend(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareVideoToWeiBoTimeLine:
                    FastShareUtil.shareVideoToWeiBoTimeLine(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareVideoToWeiBoStory:
                    FastShareUtil.shareVideoToWeiBoStory(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareVideoToDingTalk:
                    FastShareUtil.shareVideoToDingTalk(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareVideosToDingTalk:
                    FastShareUtil.shareVideosToDingTalk(mContext, uriList);
                    break;
                case R.id.btn_shareVideoToWeWork:
                    FastShareUtil.shareVideoToWeWork(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareVideosToWeWork:
                    FastShareUtil.shareVideosToWeWork(mContext, uriList);
                    break;
                case R.id.btn_shareVideoToAllApps:
                    FastShareUtil.shareVideoToAllApps(mContext, uriList.get(0), "Subject", "Title");
                    break;
                case R.id.btn_shareVideosToAllApps:
                    FastShareUtil.shareVideosToAllApps(mContext, uriList, "Subject", "Title");
                    break;
                case R.id.btn_shareVideoToAppActivity:
                    FastShareUtil.shareVideoToAppActivity(mContext, uriList.get(0), "com.android.mms", "com.android.mms.ui.ComposeMessageActivity", null, null);
                    break;
                case R.id.btn_shareVideosToAppActivity:
                    FastShareUtil.shareVideosToAppActivity(mContext, uriList, "com.android.mms", "com.android.mms.ui.ComposeMessageActivity", null, null);
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
