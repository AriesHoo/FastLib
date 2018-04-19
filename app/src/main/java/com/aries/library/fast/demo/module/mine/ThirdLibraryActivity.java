package com.aries.library.fast.demo.module.mine;

import android.os.Bundle;
import android.view.View;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.adapter.WidgetAdapter;
import com.aries.library.fast.demo.constant.GlobalConstant;
import com.aries.library.fast.demo.constant.SPConstant;
import com.aries.library.fast.demo.entity.WidgetEntity;
import com.aries.library.fast.demo.module.main.sample.SwipeBackActivity;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.module.activity.FastRefreshLoadActivity;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SPUtil;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: AriesHoo on 2017/8/7 16:45
 * Function: 项目使用其他第三方库列表
 * Desc:
 */
public class ThirdLibraryActivity extends FastRefreshLoadActivity<WidgetEntity> {

    private BaseQuickAdapter mAdapter;
    private int animationIndex = GlobalConstant.GLOBAL_ADAPTER_ANIMATION_VALUE;

    @Override
    public int getContentBackground() {
        return R.color.colorBackground;
    }

    @Override
    public boolean isLoadMoreEnable() {
        return false;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText(R.string.third_part);
    }

    @Override
    public int getContentLayout() {
        return R.layout.fast_layout_title_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public BaseQuickAdapter<WidgetEntity, BaseViewHolder> getAdapter() {
        mAdapter = new WidgetAdapter();
        animationIndex = (int) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_INDEX, animationIndex - 1) + 1;
        mAdapter.openLoadAnimation(animationIndex);
        return mAdapter;
    }

    @Override
    public void loadData(int page) {
        mAdapter.openLoadAnimation(animationIndex);
        List<WidgetEntity> list = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.arrays_library_list_title);
        String[] contents = getResources().getStringArray(R.array.arrays_library_list_content);
        String[] urls = getResources().getStringArray(R.array.arrays_library_list_url);
        for (int i = 0; i < titles.length; i++) {
            list.add(new WidgetEntity(titles[i], contents[i], urls[i]));
        }
        //此处只是为演示两种快速观察者使用、实际情况一般只会使用一种一般列表展示性使用FastObserver、
        // 类登录等待校验的使用FastLoadingObserver
        //当然可以自定义
        RxJavaManager.getInstance().getDelayObservable(list, 1)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(mRefreshLayout.isRefreshing() ? new FastObserver<List<WidgetEntity>>() {
                    @Override
                    public void _onNext(List<WidgetEntity> entity) {
                        mAdapter.openLoadAnimation(animationIndex);
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadmore();
                        mAdapter.loadMoreComplete();
                        if (mRefreshLayout.isRefreshing()) {
                            mAdapter.setNewData(entity);
                        } else {
                            mAdapter.addData(entity);
                        }
                    }

                    @Override
                    public void _onError(int errorRes, int errorCode, Throwable e) {

                    }
                } : new FastLoadingObserver<List<WidgetEntity>>(mContext) {
                    @Override
                    public void _onNext(List<WidgetEntity> entity) {
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadmore();
                        mAdapter.loadMoreComplete();
                        if (mRefreshLayout.isRefreshing()) {
                            mAdapter.setNewData(entity);
                        } else {
                            mAdapter.addData(entity);
                        }
                    }

                    @Override
                    public void _onError(int errorRes, int errorCode, Throwable e) {
                    }
                });
    }

    @Override
    public void onItemClicked(BaseQuickAdapter<WidgetEntity, BaseViewHolder> adapter, View view, int position) {
        super.onItemClicked(adapter, view, position);
        WidgetEntity entity = adapter.getItem(position);
//        WebViewActivity.start(mContext, entity.url);
        FastUtil.startActivity(mContext, SwipeBackActivity.class);
    }
}
