package com.aries.library.fast.demo.module.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.aries.library.fast.basis.BasisActivity;
import com.aries.library.fast.demo.App;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.helper.CheckVersionHelper;
import com.aries.library.fast.demo.helper.ImagePickerHelper;
import com.aries.library.fast.demo.module.WebViewActivity;
import com.aries.library.fast.demo.util.SpanTool;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.module.fragment.FastTitleFragment;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.retrofit.FastTransformer;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.view.title.TitleBarView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created: AriesHoo on 2018/7/3 13:42
 * E-Mail: AriesHoo@126.com
 * Function:我的
 * Description:
 */
public class MineFragment extends FastTitleFragment {

    @BindView(R.id.stv_infoMine) SuperTextView mStvInfo;
    @BindView(R.id.stv_updateMine) SuperTextView mStvUpdate;
    private ImageView mIvHead;

    private ImagePickerHelper mImagePickerHelper;

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
        titleBar.setTitleMainText(R.string.mine);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mImagePickerHelper = new ImagePickerHelper(mContext);
        mIvHead = mStvInfo.getLeftIconIV();
        GlideManager.loadCircleImg("https://avatars3.githubusercontent.com/u/19605922?v=4&s=460", mIvHead);
//        mIvHead.getLayoutParams().height = (int) (SizeUtil.getScreenWidth() * 0.2);
//        mIvHead.getLayoutParams().width = (int) (SizeUtil.getScreenWidth() * 0.2);
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
        if (!App.isSupportElevation()) {
            mStvInfo.setShapeStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dp_line_size))
                    .setShapeStrokeColor(getResources().getColor(R.color.colorLineGray))
                    .useShape();
        }

        mIvHead.setOnClickListener(v -> mImagePickerHelper.selectPicture(1000, (requestCode, list) -> {
            if (list == null || list.size() == 0) {
                return;
            }
            GlideManager.loadCircleImg(list.get(0), mIvHead);
//            uploadFile(new File(list.get(0)));
        }));

        mStvUpdate.setRightString("当前版本:V" + FastUtil.getVersionName(mContext));
    }

    /**
     * 演示文件上传
     *
     * @param file
     */
    private void uploadFile(File file) {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                //额外参数
                .addFormDataPart("basicId", "basicId")
                //key为与后台协商制定
                .addFormDataPart("uploadfiles", file.getName(), RequestBody.create(MultipartBody.FORM, file))
                .addFormDataPart("uploadfiles", file.getName(), RequestBody.create(MultipartBody.FORM, file))
                .build();
        FastRetrofit.getInstance().uploadFile("http://XXXX:8088/v1/ftp/upload-files", requestBody)
                .compose(FastTransformer.switchSchedulers())
                .subscribe(new FastLoadingObserver<ResponseBody>(mContext, R.string.uploading) {
                    @Override
                    public void _onNext(ResponseBody entity) {
                        ToastUtil.show("entity:" + entity.contentLength());
                    }
                });
    }

    @OnClick({R.id.stv_setting, R.id.stv_libraryMine, R.id.stv_thirdLibMine
            , R.id.stv_shareMine, R.id.stv_updateMine})
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
                CheckVersionHelper.with((BasisActivity) mContext)
                        .checkVersion(true);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mImagePickerHelper != null && requestCode == 1000) {
            mImagePickerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
}
