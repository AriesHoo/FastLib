package com.aries.library.fast.demo.module.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.adapter.WidgetAdapter;
import com.aries.library.fast.demo.entity.WidgetEntity;
import com.aries.library.fast.demo.module.sample.SwipeBackActivity;
import com.aries.library.fast.demo.module.sample.ali.ALiPayMainActivity;
import com.aries.library.fast.demo.module.sample.news.NewsMainActivity;
import com.aries.library.fast.demo.retrofit.DefaultSubscriber;
import com.aries.library.fast.enums.RxLifeCycle;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.module.fragment.FastTitleRefreshLoadFragment;
import com.aries.library.fast.util.AppUtil;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created: AriesHoo on 2017/7/20 11:45
 * Function:
 * Desc:
 */
public class HomeFragment extends FastTitleRefreshLoadFragment<WidgetEntity> {

    protected BGABanner banner;
    private BaseQuickAdapter mAdapter;
    private List<Class> listActivity = new ArrayList<>();
    private List<Integer> listArraysBanner = Arrays.asList(R.array.arrays_banner_all
            , R.array.arrays_banner_an, R.array.arrays_banner_si
            , R.array.arrays_banner_liu, R.array.arrays_banner_di, R.array.arrays_banner_jun);

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean isLoadMoreEnable() {
        return false;
    }

    @Override
    public BaseQuickAdapter<WidgetEntity, BaseViewHolder> getAdapter() {
        mAdapter = new WidgetAdapter();
        return mAdapter;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setVisibility(View.GONE);
        titleBar.setTitleMainText(R.string.home);
    }

    @Override
    public int getContentLayout() {
        return R.layout.fast_layout_title_multi_status_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setBanner(0);
    }

    @Override
    public void loadData(int page) {
        if (listActivity.size() > 0) {
            int random = AppUtil.getRandom(1000);
            int position = (random % (listArraysBanner.size() - 1)) + 1;
            LoggerManager.d("position:" + position + ";random:" + random);
            setBanner(position);
            return;
        }
        listActivity.clear();
        listActivity.add(SwipeBackActivity.class);
        listActivity.add(ALiPayMainActivity.class);
        listActivity.add(NewsMainActivity.class);
        List<WidgetEntity> list = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.arrays_home_list_title);
        for (int i = 0; i < titles.length; i++) {
            WidgetEntity entity = new WidgetEntity();
            entity.title = titles[i];
            entity.content = getString(R.string.home_list_content);
            entity.activity = listActivity.get(i);
            list.add(entity);
        }
        RxJavaManager.getInstance().getDelayObservable(list, 200, TimeUnit.MILLISECONDS)
                .compose(bindLifeCycle(RxLifeCycle.DESTROY))
                .subscribe(new DefaultSubscriber<List<WidgetEntity>>() {
                    @Override
                    public void _onNext(List<WidgetEntity> entity) {
                        mEasyStatusView.content();
                        mAdapter.setNewData(entity);
                        mRefreshLayout.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mEasyStatusView.error();
                        mRefreshLayout.finishRefresh(false);
                        mAdapter.loadMoreComplete();
                    }
                });
    }

    private void setBanner(int position) {
        List<String> images = Arrays.asList(getResources().getStringArray(listArraysBanner.get(position)));
        if (banner == null) {
            View v = View.inflate(mContext, R.layout.layout_banner, null);
            banner = (BGABanner) v.findViewById(R.id.banner);
            banner.setAdapter(new BGABanner.Adapter() {
                @Override
                public void fillBannerItem(BGABanner banner, View itemView, Object model, int position) {
                    GlideManager.loadImg(model, (ImageView) itemView);
                }
            });
            banner.setDelegate(new BGABanner.Delegate() {
                @Override
                public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                    WebViewActivity.start(mContext, model.toString(), false);
                }
            });
            mAdapter.addHeaderView(v);
        }
        banner.setData(images, null);
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void onItemClicked(BaseQuickAdapter<WidgetEntity, BaseViewHolder> adapter, View
            view, int position) {
        super.onItemClicked(adapter, view, position);
        WidgetEntity entity = adapter.getItem(position);
        if (position == 0) {
            SwipeBackActivity.start(mContext, entity.title);
        } else {
            AppUtil.startActivity(mContext, entity.activity, null, true);
        }
    }
}
