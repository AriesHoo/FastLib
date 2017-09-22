package com.aries.library.fast.demo.module.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.aries.library.fast.demo.App;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.base.BaseTitleFragment;
import com.aries.library.fast.demo.helper.RefreshHeaderHelper;
import com.aries.library.fast.demo.module.WebViewActivity;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.ui.view.title.TitleBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created: AriesHoo on 2017/7/20 11:45
 * Function:
 * Desc:
 */
public class MineFragment extends BaseTitleFragment {
    @BindView(R.id.iv_headMine) ImageView ivHead;
    @BindView(R.id.iv_bgMine) ImageView ivBg;
    @BindView(R.id.fLayout_mine) FrameLayout fLayoutMine;
    @BindView(R.id.stv_libraryMine) SuperTextView stvLibrary;
    @BindView(R.id.stv_gitMine) SuperTextView stvGit;
    @BindView(R.id.stv_thirdLib) SuperTextView stvThird;
    @BindView(R.id.smartLayout_mine) SmartRefreshLayout smartLayout;
    private String[] imgBacks;

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setVisibility(View.GONE);
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void beforeInitView() {
        initRefresh();
        super.beforeInitView();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        GlideManager.loadCircleImg("https://avatars3.githubusercontent.com/u/19605922?v=4&s=460", ivHead);
        setImageBack();
        fLayoutMine.getLayoutParams().height = App.getImageHeight();
        ivHead.getLayoutParams().height = (int) (SizeUtil.getScreenWidth() * 0.167);
        ivHead.getLayoutParams().width = (int) (SizeUtil.getScreenWidth() * 0.167);
        LoggerManager.d("imageHeight:" + ivHead.getLayoutParams().height + ";screenWidth:" + SizeUtil.getScreenWidth());
    }

    private void initRefresh() {
        smartLayout.setRefreshHeader(RefreshHeaderHelper.getInstance().getRefreshHeader(mContext));
        smartLayout.setEnableHeaderTranslationContent(false);
        smartLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                RxJavaManager.getInstance().setTimer(1000, new RxJavaManager.TimerListener() {
                    @Override
                    public void timeEnd() {
                        setImageBack();
                    }
                });
            }
        });
    }

    private void setImageBack() {
        if (imgBacks == null) {
            imgBacks = getResources().getStringArray(R.array.arrays_banner_all);
        }
        int position = FastUtil.getRandom(imgBacks.length) - 1;
        GlideManager.loadImg(imgBacks[position], ivBg);
        smartLayout.finishRefresh();
    }

    @OnClick({R.id.iv_headMine, R.id.stv_setting, R.id.stv_libraryMine,
            R.id.stv_gitMine, R.id.stv_thirdLib})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_headMine:
                WebViewActivity.start(mContext, "https://github.com/AriesHoo");
                break;
            case R.id.stv_setting:
                FastUtil.startActivity(mContext, SettingActivity.class);
                break;
            case R.id.stv_libraryMine:
                WebViewActivity.start(mContext, "https://github.com/AriesHoo/FastLib/blob/master/README.md");
                break;
            case R.id.stv_gitMine:
                WebViewActivity.start(mContext, "https://github.com/AriesHoo");
                break;
            case R.id.stv_thirdLib:
                FastUtil.startActivity(mContext, ThirdLibraryActivity.class);
                break;
        }
    }

    @Override
    public void loadData() {
        smartLayout.autoRefresh();
        mIsFirstShow = true;
    }
}
