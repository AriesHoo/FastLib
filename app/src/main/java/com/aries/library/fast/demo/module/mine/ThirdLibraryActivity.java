package com.aries.library.fast.demo.module.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.adapter.WidgetAdapter;
import com.aries.library.fast.demo.constant.GlobalConstant;
import com.aries.library.fast.demo.constant.SPConstant;
import com.aries.library.fast.demo.entity.WidgetEntity;
import com.aries.library.fast.demo.module.WebViewActivity;
import com.aries.library.fast.demo.touch.ItemTouchHelperCallback;
import com.aries.library.fast.demo.touch.OnItemTouchHelperListener;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.module.activity.FastRefreshLoadActivity;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * @Author: AriesHoo on 2018/8/10 10:17
 * @E-Mail: AriesHoo@126.com
 * Function: 项目使用其他第三方库列表
 * Description:
 */
public class ThirdLibraryActivity extends FastRefreshLoadActivity<WidgetEntity> {

    @BindView(R.id.rv_contentFastLib) RecyclerView mRvContent;
    @BindView(R.id.smartLayout_rootFastLib) SmartRefreshLayout mSmartLayout;
    private WidgetAdapter mAdapter;
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelperCallback()
                        .setAdapter(mAdapter)
                        .setOnItemTouchHelperListener(new OnItemTouchHelperListener() {
                            @Override
                            public void onStart(int start) {
                                mRefreshLayout.setEnableRefresh(false);
                                LoggerManager.i(TAG, "onStart-start:" + start);
                            }

                            @Override
                            public void onMove(int from, int to) {
                                LoggerManager.i(TAG, "onMove-from:" + from + ";to:" + to);
                            }

                            @Override
                            public void onMoved(int from, int to) {
                                LoggerManager.i(TAG, "onMoved-from:" + from + ";to:" + to);
                            }

                            @Override
                            public void onEnd(int star, int end) {
                                mRefreshLayout.setEnableRefresh(true);
                                LoggerManager.i(TAG, "onEnd-star:" + star + ";end:" + end);
                                if (star != end) {
                                    ToastUtil.show("从---" + star + "---拖拽至---" + end + "---");
                                }
                            }
                        }));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public BaseQuickAdapter<WidgetEntity, BaseViewHolder> getAdapter() {
        mAdapter = new WidgetAdapter();
        animationIndex = (int) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_INDEX, animationIndex - 1) + 1;
        mAdapter.openLoadAnimation(animationIndex);
        return (BaseQuickAdapter) mAdapter;
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
                .subscribe(new FastLoadingObserver<List<WidgetEntity>>("我就试一试不好用") {
                    @Override
                    public void _onNext(List<WidgetEntity> entity) {
                        mAdapter.openLoadAnimation(animationIndex);
                        mStatusManager.showSuccessLayout();
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadMore();
                        mAdapter.loadMoreComplete();
                        if (mRefreshLayout.getState()== RefreshState.Refreshing) {
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
        WidgetEntity item = adapter.getItem(position);
        if (item == null || TextUtils.isEmpty(item.url)) {
            return;
        }
        WebViewActivity.start(mContext, item.url, true);
    }
}
