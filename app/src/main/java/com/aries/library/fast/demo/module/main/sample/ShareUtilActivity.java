package com.aries.library.fast.demo.module.main.sample;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.helper.CheckVersionHelper;
import com.aries.library.fast.demo.helper.ImagePickerHelper;
import com.aries.library.fast.demo.module.WebViewActivity;
import com.aries.library.fast.demo.module.mine.SettingActivity;
import com.aries.library.fast.demo.module.mine.ThirdLibraryActivity;
import com.aries.library.fast.demo.widget.OverScrollView;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.module.activity.FastTitleActivity;
import com.aries.library.fast.util.FastShareUtil;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.view.title.TitleBarView;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: AriesHoo on 2021/4/30 13:43
 * @E-Mail: AriesHoo@126.com
 * @Function: 系统分享功能封装演示
 * @Description:
 */
public class ShareUtilActivity extends FastTitleActivity {

    private ImagePickerHelper mImagePickerHelper;
    private static final int FILE_SELECT_CODE = 10000;
    private String mType = "*/*";


    @Override
    public int getContentLayout() {
        return R.layout.activity_share_util;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        mImagePickerHelper = new ImagePickerHelper(mContext);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @OnClick({R.id.btn_shareText, R.id.btn_shareImage,
            R.id.btn_shareVideo, R.id.btn_shareAudio,
            R.id.btn_shareOtherFile,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_shareText:
                FastUtil.startActivity(mContext, ShareTextActivity.class);
                break;
            case R.id.btn_shareImage:
                FastUtil.startActivity(mContext, ShareImageActivity.class);
                break;
            case R.id.btn_shareVideo:
                FastUtil.startActivity(mContext, ShareVideoActivity.class);
                break;
            case R.id.btn_shareAudio:
                FastUtil.startActivity(mContext, ShareAudioActivity.class);
                break;
            case R.id.btn_shareOtherFile:
                openFileChooser(mType);
                break;
        }
    }

    private void openFileChooser(String type) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "chooseFile"), FILE_SELECT_CODE);
            overridePendingTransition(0, 0);
        } catch (Exception ex) {
            ToastUtil.show("error:" + ex.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mImagePickerHelper != null) {
            mImagePickerHelper.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            Uri shareFileUrl = data.getData();

            FastShareUtil.shareFile(mContext, shareFileUrl, mType, "Subject", "Title", null, null);
            LoggerManager.i("shareFileUrl:" + shareFileUrl + ";getScheme:" + shareFileUrl.getScheme());
        }
    }


}
