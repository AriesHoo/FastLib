package com.aries.library.fast.demo.module.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.module.WebViewActivity;
import com.aries.library.fast.demo.util.SpanTool;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.module.fragment.FastTitleFragment;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.ui.view.title.TitleBarView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created: AriesHoo on 2017/7/20 11:45
 * Function:
 * Desc:
 */
public class MineFragment extends FastTitleFragment {

    @BindView(R.id.stv_infoMine) SuperTextView stvInfo;
    private ImageView ivHead;

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
        ivHead = stvInfo.getLeftIconIV();
        GlideManager.loadCircleImg("https://avatars3.githubusercontent.com/u/19605922?v=4&s=460", ivHead);
        ivHead.getLayoutParams().height = (int) (SizeUtil.getScreenWidth() * 0.2);
        ivHead.getLayoutParams().width = (int) (SizeUtil.getScreenWidth() * 0.2);
        LoggerManager.d("imageHeight:" + ivHead.getLayoutParams().height + ";screenWidth:" + SizeUtil.getScreenWidth());
        SpanTool.getBuilder(stvInfo.getLeftString())
                .append("https://github.com/AriesHoo")
                .setUnderline()
                .setForegroundColor(Color.BLUE)
                .setBoldItalic()
                .into(stvInfo.getLeftTextView());
        SpanTool.getBuilder(stvInfo.getLeftBottomString())
                .append("http://www.jianshu.com/u/a229eee96115")
                .setUnderline()
                .setForegroundColor(Color.BLUE)
                .setBoldItalic()
                .into(stvInfo.getLeftBottomTextView());

        stvInfo.setLeftTvClickListener(new SuperTextView.OnLeftTvClickListener() {
            @Override
            public void onClickListener() {
                WebViewActivity.start(mContext, "https://github.com/AriesHoo");
            }
        });
        stvInfo.setLeftBottomTvClickListener(new SuperTextView.OnLeftBottomTvClickListener() {
            @Override
            public void onClickListener() {
                WebViewActivity.start(mContext, "http://www.jianshu.com/u/a229eee96115");
            }
        });

        ViewCompat.setElevation(stvInfo, getResources().
                getDimension(R.dimen.dp_elevation));
    }

    @OnClick({R.id.stv_setting, R.id.stv_libraryMine, R.id.stv_thirdLibMine
            , R.id.stv_shareMine})
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
        }
    }

}
