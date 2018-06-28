package com.aries.library.fast.demo.module.mine;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.adapter.WidgetAdapter;
import com.aries.library.fast.demo.constant.GlobalConstant;
import com.aries.library.fast.demo.constant.SPConstant;
import com.aries.library.fast.demo.entity.WidgetEntity;
import com.aries.library.fast.demo.module.WebViewActivity;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.module.activity.FastRefreshLoadActivity;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created: AriesHoo on 2017/8/7 16:45
 * Function: 项目使用其他第三方库列表
 * Desc:
 */
public class ThirdLibraryActivity extends FastRefreshLoadActivity<WidgetEntity> {

    @BindView(R.id.rv_contentFastLib) RecyclerView mRvContent;
    @BindView(R.id.smartLayout_rootFastLib) SmartRefreshLayout mSmartLayout;
    private BaseQuickAdapter mAdapter;
    private int animationIndex = GlobalConstant.GLOBAL_ADAPTER_ANIMATION_VALUE;

    @Override
    public boolean isLoadMoreEnable() {
        return false;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
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
                .subscribe(new FastObserver<List<WidgetEntity>>() {
                    @Override
                    public void _onNext(List<WidgetEntity> entity) {
                        mAdapter.openLoadAnimation(animationIndex);
                        mStatusManager.showSuccessLayout();
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadMore();
                        mAdapter.loadMoreComplete();
                        if (mRefreshLayout.isRefreshing()) {
                            mAdapter.setNewData(entity);
                        } else {
                            mAdapter.addData(entity);
                        }
                    }
                });
    }

    @Override
    public void onItemClicked(BaseQuickAdapter<WidgetEntity, BaseViewHolder> adapter, View view, int position) {
        super.onItemClicked(adapter, view, position);
        WidgetEntity entity = adapter.getItem(position);
        WebViewActivity.start(mContext, entity.url);
    }
}
