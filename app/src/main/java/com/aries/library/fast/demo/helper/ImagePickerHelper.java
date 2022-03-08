package com.aries.library.fast.demo.helper;

import android.app.Activity;
import android.content.Intent;

import com.aries.library.fast.demo.GlideEngine;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.base.BaseHelper;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.ui.util.StatusBarUtil;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureSelectorUIStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: AriesHoo on 2018/11/19 14:18
 * @E-Mail: AriesHoo@126.com
 * @Function: 图片选择帮助类-演示控制三方库状态栏及导航栏效果
 * @Description:
 */
public class ImagePickerHelper extends BaseHelper {

    public final static int IMG = 10000;
    private int mRequestCode;

    private OnImageSelect mOnImageSelect;

    public interface OnImageSelect {
        /**
         * 回调选择结果
         *
         * @param requestCode code
         * @param list        文件列表
         */
        void onImageSelect(int requestCode, List<String> list);
    }

    public ImagePickerHelper(Activity activity) {
        super(activity);
    }

    public void selectPicture(int requestCode, OnImageSelect onImageSelect) {
        this.mOnImageSelect = onImageSelect;
        this.mRequestCode = requestCode;
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .isCompress(true)
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)
                .selectionMode(PictureConfig.SINGLE)
                // 外部传入图片加载引擎，必传项
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(mRequestCode);
    }

    public void selectPicture(int requestCode, int count, OnImageSelect onImageSelect) {
        this.mOnImageSelect = onImageSelect;
        this.mRequestCode = requestCode;
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .minSelectNum(1)
                .isCompress(true)
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)
                .maxSelectNum(count)
                .selectionMode(PictureConfig.TYPE_PICTURE)
                // 外部传入图片加载引擎，必传项
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(mRequestCode);
    }

    public void selectVideo(int requestCode, int count, OnImageSelect onImageSelect) {
        this.mOnImageSelect = onImageSelect;
        this.mRequestCode = requestCode;
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofVideo())
                .isCompress(true)
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)
                .minVideoSelectNum(1)
                .maxVideoSelectNum(count)
                .selectionMode(PictureConfig.TYPE_VIDEO)
                // 外部传入图片加载引擎，必传项
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(mRequestCode);
    }

    public void selectAudio(int requestCode, int count, OnImageSelect onImageSelect) {
        this.mOnImageSelect = onImageSelect;
        this.mRequestCode = requestCode;
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofAudio())
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)
                .minSelectNum(1)
                .maxSelectNum(count)
                .selectionMode(PictureConfig.TYPE_ALL)
                // 外部传入图片加载引擎，必传项
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(mRequestCode);
    }

    public void selectFile(int requestCode, int count, OnImageSelect onImageSelect) {
        this.mOnImageSelect = onImageSelect;
        this.mRequestCode = requestCode;
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)
                .maxSelectNum(count)
                .selectionMode(PictureConfig.TYPE_ALL)
                // 外部传入图片加载引擎，必传项
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(mRequestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LoggerManager.i("onActivityResult", "path:");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode != mRequestCode) {
                return;
            }
            // 图片、视频、音频选择结果回调
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            LoggerManager.i("onActivityResult", "selectList:" + new Gson().toJson(selectList));
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
            // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
            List<String> list = new ArrayList<>();
            for (LocalMedia item : selectList) {
                if (item.isCompressed()) {
                    list.add(item.getCompressPath());
                } else {
                    list.add(item.getRealPath());
                }
                LoggerManager.i("onActivityResult", "path:" + item.getPath());
            }
            if (mOnImageSelect != null) {
                mOnImageSelect.onImageSelect(mRequestCode, list);
            }
        }
    }

}
