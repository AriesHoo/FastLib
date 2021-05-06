package com.aries.library.fast.demo.module.main.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.helper.ImagePickerHelper;
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
 * @Author: AriesHoo on 2021/5/3 16:53
 * @E-Mail: AriesHoo@126.com
 * @Function: 系统分享功能-分享图片演示
 * @Description:
 */
public class ShareImageActivity extends FastTitleActivity {
    private ImagePickerHelper mImagePickerHelper;

    @Override
    public int getContentLayout() {
        return R.layout.activity_share_image;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleSubText("Image分享单图,Images分享多图");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mImagePickerHelper = new ImagePickerHelper(mContext);
    }

    @OnClick({R.id.btn_shareImageToQQ, R.id.btn_shareImageToQQFriend,
            R.id.btn_shareImagesToQQFriend, R.id.btn_shareImageToQQComputer,
            R.id.btn_shareImageToQQFavorites, R.id.btn_shareImageToQQFaceToFace,
            R.id.btn_shareImageToWeChat, R.id.btn_shareImageToWeChatFriend,
            R.id.btn_shareImageToWeChatFavorites, R.id.btn_shareImageToWeChatTimeLine,
            R.id.btn_shareImageToWeiBo, R.id.btn_shareImagesToWeiBo,
            R.id.btn_shareImageToWeiBoFriend, R.id.btn_shareImagesToWeiBoFriend,
            R.id.btn_shareImageToWeiBoTimeLine, R.id.btn_shareImagesToWeiBoTimeLine,
            R.id.btn_shareImageToWeiBoStory, R.id.btn_shareImageToDingTalk,
            R.id.btn_shareImagesToDingTalk, R.id.btn_shareImageToWeWork,
            R.id.btn_shareImagesToWeWork, R.id.btn_shareImageToAllApps,
            R.id.btn_shareImagesToAllApps, R.id.btn_shareImageToAppActivity,
            R.id.btn_shareImagesToAppActivity})
    public void onViewClicked(View view) {
        mImagePickerHelper.selectPicture(1000, 5, (requestCode, list) -> {
            if (list == null || list.size() == 0 || requestCode != 1000) {
                ToastUtil.show("请选择图片");
                return;
            }
            List<Uri> uriList = new ArrayList<>(list.size());
            for (String str : list) {
                uriList.add(Uri.parse(str));
            }
            switch (view.getId()) {
                case R.id.btn_shareImageToQQ:
                    FastShareUtil.shareImageToQQ(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareImageToQQFriend:
                    FastShareUtil.shareImageToQQFriend(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareImagesToQQFriend:
                    FastShareUtil.shareImagesToQQFriend(mContext, uriList);
                    break;
                case R.id.btn_shareImageToQQComputer:
                    FastShareUtil.shareImageToQQComputer(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareImageToQQFavorites:
                    FastShareUtil.shareImageToQQFavorites(mContext, uriList.get(0), new File(list.get(0)).getName());
                    break;
                case R.id.btn_shareImageToQQFaceToFace:
                    FastShareUtil.shareImageToQQFaceToFace(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareImageToWeChat:
                    FastShareUtil.shareImageToWeChat(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareImageToWeChatFriend:
                    FastShareUtil.shareImageToWeChatFriend(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareImageToWeChatTimeLine:
                    FastShareUtil.shareImageToWeChatTimeLine(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareImageToWeChatFavorites:
                    FastShareUtil.shareImageToWeChatFavorites(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareImageToWeiBo:
                    FastShareUtil.shareImageToWeiBo(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareImagesToWeiBo:
                    FastShareUtil.shareImagesToWeiBo(mContext, uriList);
                    break;
                case R.id.btn_shareImageToWeiBoFriend:
                    FastShareUtil.shareImageToWeiBoFriend(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareImagesToWeiBoFriend:
                    FastShareUtil.shareImagesToWeiBoFriend(mContext, uriList);
                    break;
                case R.id.btn_shareImageToWeiBoTimeLine:
                    FastShareUtil.shareImageToWeiBoTimeLine(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareImagesToWeiBoTimeLine:
                    FastShareUtil.shareImagesToWeiBoTimeLine(mContext, uriList);
                    break;
                case R.id.btn_shareImageToWeiBoStory:
                    FastShareUtil.shareImageToWeiBoStory(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareImageToDingTalk:
                    FastShareUtil.shareImageToDingTalk(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareImagesToDingTalk:
                    FastShareUtil.shareImagesToDingTalk(mContext, uriList);
                    break;
                case R.id.btn_shareImageToWeWork:
                    FastShareUtil.shareImageToWeWork(mContext, uriList.get(0));
                    break;
                case R.id.btn_shareImagesToWeWork:
                    FastShareUtil.shareImagesToWeWork(mContext, uriList);
                    break;
                case R.id.btn_shareImageToAllApps:
                    FastShareUtil.shareImageToAllApps(mContext, uriList.get(0), "Subject", "Title");
                    break;
                case R.id.btn_shareImagesToAllApps:
                    FastShareUtil.shareImagesToAllApps(mContext, uriList, "Subject", "Title");
                    break;
                case R.id.btn_shareImageToAppActivity:
                    FastShareUtil.shareImageToAppActivity(mContext, uriList.get(0), "com.android.mms", "com.android.mms.ui.ComposeMessageActivity", null, null);
                    break;
                case R.id.btn_shareImagesToAppActivity:
                    FastShareUtil.shareImagesToAppActivity(mContext, uriList, "com.android.mms", "com.android.mms.ui.ComposeMessageActivity", null, null);
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
