package com.aries.library.fast.demo.module.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.aries.library.fast.basis.BasisActivity;
import com.aries.library.fast.demo.App;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.helper.CheckVersionHelper;
import com.aries.library.fast.demo.helper.ImagePickerHelper;
import com.aries.library.fast.demo.helper.TitleBarViewHelper;
import com.aries.library.fast.demo.module.WebViewActivity;
import com.aries.library.fast.demo.util.SpanTool;
import com.aries.library.fast.demo.widget.OverScrollView;
import com.aries.library.fast.demo.widget.ProgressDialog;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.module.fragment.FastTitleFragment;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.retrofit.FastTransformer;
import com.aries.library.fast.retrofit.FastUploadRequestBody;
import com.aries.library.fast.retrofit.FastUploadRequestListener;
import com.aries.library.fast.util.FastFormatUtil;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.widget.FastLoadDialog;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 * @Description:
 */
public class MineFragment extends FastTitleFragment {

    @BindView(R.id.sv_containerMine) OverScrollView mSvContainer;
    @BindView(R.id.tv_coverMine) TextView mTvCover;
    @BindView(R.id.stv_infoMine) SuperTextView mStvInfo;
    @BindView(R.id.stv_updateMine) SuperTextView mStvUpdate;
    private ImageView mIvHead;
    private boolean mIsLight;

    private ImagePickerHelper mImagePickerHelper;
    private TitleBarViewHelper mTitleBarViewHelper;

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBgColor(Color.WHITE)
                .setTitleMainTextColor(Color.WHITE)
                .setTitleMainText(R.string.mine);
        titleBar.getBackground().setAlpha(0);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mImagePickerHelper = new ImagePickerHelper(mContext);
        mIvHead = mStvInfo.getLeftIconIV();
        GlideManager.loadCircleImg("https://avatars0.githubusercontent.com/u/19605922?s=460&v=4", mIvHead);
        LoggerManager.d("imageHeight:" + mIvHead.getLayoutParams().height + ";screenWidth:" + SizeUtil.getScreenWidth());
        SpanTool.getBuilder(mStvInfo.getLeftString())
                .append("https://github.com/AriesHoo")
                .setUnderline()
                .setForegroundColor(Color.BLUE)
                .setBoldItalic()
                .into(mStvInfo.getLeftTextView());
        SpanTool.getBuilder(mStvInfo.getLeftBottomString())
                .append("http://www.jianshu.com/u/a229eee96115")
                .setUnderline()
                .setForegroundColor(Color.BLUE)
                .setBoldItalic()
                .into(mStvInfo.getLeftBottomTextView());

        mStvInfo.setLeftTvClickListener(() -> WebViewActivity.start(mContext, "https://github.com/AriesHoo"));
        mStvInfo.setLeftBottomTvClickListener(() -> WebViewActivity.start(mContext, "http://www.jianshu.com/u/a229eee96115"));
        mStvInfo.getLeftBottomTextView().setGravity(Gravity.LEFT);
        ViewCompat.setElevation(mStvInfo, getResources().
                getDimension(R.dimen.dp_elevation));
        mStvInfo.setTranslationZ(3f);
        if (!App.isSupportElevation()) {
            mStvInfo.setShapeStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dp_line_size))
                    .setShapeStrokeColor(ContextCompat.getColor(mContext, R.color.colorLineGray))
                    .useShape();
        }

        mIvHead.setOnClickListener(v -> mImagePickerHelper.selectPicture(1000, (requestCode, list) -> {
            if (list == null || list.size() == 0 || requestCode != 1000) {
                return;
            }
            GlideManager.loadCircleImg(list.get(0), mIvHead);
        }));

        mStvUpdate.setRightString("当前版本:V" + FastUtil.getVersionName(mContext));
        //根据屏幕宽度重新调整背景图
        int heightCover = SizeUtil.getScreenWidth() * 1 / 2;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mTvCover.getLayoutParams();
        if (params != null) {
            params.height = heightCover;
        }
        ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) mStvInfo.getLayoutParams();
        if (margin != null) {
            margin.topMargin = heightCover - SizeUtil.dp2px(20);
        }
        mTitleBarViewHelper = new TitleBarViewHelper(mContext)
                .setOverScrollView(mSvContainer)
                .setShowTextEnable(true)
                .setMaxHeight(heightCover)
                .setOnScrollListener(new TitleBarViewHelper.OnScrollListener() {
                    @Override
                    public void onScrollChange(int alpha, boolean isLightMode) {
                        mIsLight = isLightMode;
                    }
                });
    }

    /**
     * 演示文件上传--需设置自己的上传路径
     *
     * @param listFile
     */
    private void uploadFile(List<String> listFile) {
        if (listFile == null) {
            return;
        }
        final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setTitle("上传文件");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage("上传中...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setProgressNumberFormat("");
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                //额外参数
                .addFormDataPart("basicId", "basicId");
        //添加上传实体
        for (int i = 0; i < listFile.size(); i++) {
            File file = new File(listFile.get(i));
            int finalI = i;
            builder.addFormDataPart("uploadfiles", file.getName(), getUploadRequestBody(file, new FastUploadRequestListener() {
                @Override
                public void onProgress(float progress, long current, long total) {
                    if (!mProgressDialog.isShowing()) {
                        return;
                    }
                    mProgressDialog.setMessage("上传中(" + (finalI + 1) + "/" + listFile.size() + ")");
                    mProgressDialog.setProgressNumberFormat(FastFormatUtil.formatDataSize(current) + "/" + FastFormatUtil.formatDataSize(total));
                    mProgressDialog.setMax((int) total);
                    mProgressDialog.setProgress((int) current);
                    LoggerManager.i("uploadFile", ":i=" + finalI + ";progress:" + progress + ";current:" + current + ";total:" + total);
                }

                @Override
                public void onFail(Throwable e) {
                    LoggerManager.i("uploadFile", "error:" + e.getMessage());
                }
            }));
        }
        RequestBody requestBody = builder.build();
        //上传地址需自行设置
        FastRetrofit.getInstance().uploadFile("http://XXXX/v1/ftp/upload-files", requestBody)
                .compose(FastTransformer.switchSchedulers())
                .subscribe(new FastLoadingObserver<ResponseBody>(new FastLoadDialog(mContext, mProgressDialog)) {
                    @Override
                    public void _onNext(ResponseBody entity) {
                        String message = "上传返回:";
                        try {
                            message += entity.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        new AlertDialog.Builder(mContext)
                                .setTitle("上传文件返回结果")
                                .setMessage(message)
                                .setPositiveButton(R.string.ensure, null)
                                .create()
                                .show();
                    }
                });
    }


    private RequestBody getUploadRequestBody(File file, FastUploadRequestListener listener) {
        if (listener == null) {
            return RequestBody.create(MultipartBody.FORM, file);
        }
        return new FastUploadRequestBody(RequestBody.create(MultipartBody.FORM, file), listener);
    }

    @OnClick({R.id.stv_setting, R.id.stv_libraryMine, R.id.stv_thirdLibMine
            , R.id.stv_shareMine, R.id.stv_updateMine, R.id.stv_uploadMine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.stv_setting:
                FastUtil.startActivity(mContext, SettingActivity.class);
                break;
            case R.id.stv_libraryMine:
                WebViewActivity.start(mContext, "https://github.com/AriesHoo/FastLib/blob/master/README.md");
                break;
            case R.id.stv_thirdLibMine:
                FastUtil.startActivity(mContext, ThirdLibraryActivity.class);
                break;
            case R.id.stv_shareMine:
                FastUtil.startShareText(mContext, getString(R.string.share_content));
                break;
            case R.id.stv_updateMine:
                //演示大文件下载--王者荣耀
//                UpdateEntity updateEntity = new UpdateEntity();
//                updateEntity.url = "http://gdown.baidu.com/data/wisegame/008c0de8d4355b41/wangzherongyao_35011414.apk";
//                CheckVersionHelper.with((BasisActivity) mContext)
//                        .downloadApk(updateEntity, "king_glory.apk", true);
                CheckVersionHelper.with((BasisActivity) mContext)
                        .checkVersion(true);
                break;
            case R.id.stv_uploadMine:
                mImagePickerHelper.selectFile(1001, 5, (requestCode, list) -> {
                    if (list == null || list.size() == 0 || requestCode != 1001) {
                        return;
                    }
                    uploadFile(list);
                });
                break;
        }
    }

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        super.onVisibleChanged(isVisibleToUser);
        if (isVisibleToUser) {
            if (mIsLight) {
                StatusBarUtil.setStatusBarLightMode(mContext);
            } else {
                StatusBarUtil.setStatusBarDarkMode(mContext);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mImagePickerHelper != null) {
            mImagePickerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTitleBarViewHelper != null) {
            mTitleBarViewHelper.onDestroy();
        }
    }
}
