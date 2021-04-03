package com.aries.library.fast.demo.module.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.adapter.SubjectMovieAdapter;
import com.aries.library.fast.demo.base.BaseItemTouchQuickAdapter;
import com.aries.library.fast.demo.base.BaseMovieEntity;
import com.aries.library.fast.demo.constant.ApiConstant;
import com.aries.library.fast.demo.constant.GlobalConstant;
import com.aries.library.fast.demo.entity.SubjectsEntity;
import com.aries.library.fast.demo.module.WebViewActivity;
import com.aries.library.fast.demo.retrofit.repository.ApiRepository;
import com.aries.library.fast.demo.touch.ItemTouchHelperCallback;
import com.aries.library.fast.demo.touch.OnItemTouchHelperListener;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.module.fragment.FastRefreshLoadFragment;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.util.ArrayList;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @Author: AriesHoo on 2018/8/10 10:14
 * @E-Mail: AriesHoo@126.com
 * Function: 电影列表示例
 * Description:
 */
public class MovieBaseFragment extends FastRefreshLoadFragment<SubjectsEntity> {

    private BaseItemTouchQuickAdapter mAdapter;
    private String mUrl;
    private int animationIndex = GlobalConstant.GLOBAL_ADAPTER_ANIMATION_VALUE;
    private boolean animationAlways = true;

    public static MovieBaseFragment newInstance(String url) {
        Bundle args = new Bundle();
        MovieBaseFragment fragment = new MovieBaseFragment();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void beforeSetContentView() {
        super.beforeSetContentView();
        mUrl = getArguments().getString("url");
    }

    @Override
    public BaseQuickAdapter<SubjectsEntity, BaseViewHolder> getAdapter() {
        mAdapter = new SubjectMovieAdapter(ApiConstant.API_MOVIE_TOP.equals(mUrl));
//        changeAdapterAnimation(0);
//        changeAdapterAnimationAlways(true);
        return mAdapter;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fast_layout_refresh_recycler;
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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //获取最后一个可见view的位置
                LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                int lastPosition = linearManager.findLastVisibleItemPosition();
                // 如果滑动到倒数第三条数据，就自动加载下一页数据
                if (lastPosition >= layoutManager.getItemCount() - 5) {
                    onLoadMore();
                }

            }
        });
    }

    @Override
    public void loadData(int page) {
        //接口最大支持单页100
        mDefaultPageSize = 15;
        ApiRepository.getInstance().getMovie(mUrl, page * mDefaultPage, mDefaultPageSize)
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseMovieEntity>(getIHttpRequestControl()) {
                    @Override
                    public void _onNext(BaseMovieEntity entity) {
                        LoggerManager.i("url:" + mUrl);
                        mStatusManager.showSuccessLayout();
                        FastManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), entity == null || entity.subjects == null ? new ArrayList<>() : entity.subjects, null);
                    }
                });
    }

    @Override
    public void onItemClicked(BaseQuickAdapter<SubjectsEntity, BaseViewHolder> adapter, View view, int position) {
        super.onItemClicked(adapter, view, position);
        WebViewActivity.start(mContext, adapter.getItem(position).alt+"?apikey=0b2bdeda43b5688921839c8ecb20399b");
    }

    /**
     * 单独设置状态
     *
     * @param statusView
     */
    @Override
    public void setMultiStatusView(StatusLayoutManager.Builder statusView) {
        super.setMultiStatusView(statusView);
    }

    //演示单独控制多状态布局点击事件
//    @Override
//    public OnStatusChildClickListener getMultiStatusViewChildClickListener() {
//        return new OnStatusChildClickListener() {
//            @Override
//            public void onEmptyChildClick(View view) {
//                ToastUtil.show("空啦");
//            }
//
//            @Override
//            public void onErrorChildClick(View view) {
//                ToastUtil.show("错啦");
//            }
//
//            @Override
//            public void onCustomerChildClick(View view) {
//            }
//        };
//    }

//    @SuppressLint("WrongConstant")
//    @Subscriber(mode = ThreadMode.MAIN, tag = EventConstant.EVENT_KEY_CHANGE_ADAPTER_ANIMATION)
//    public void changeAdapterAnimation(int index) {
//        if (mAdapter != null) {
//            animationIndex = (int) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_INDEX, animationIndex - 1) + 1;
//            mAdapter.openLoadAnimation(animationIndex);
//        }
//    }
//
//    @Subscriber(mode = ThreadMode.MAIN, tag = EventConstant.EVENT_KEY_CHANGE_ADAPTER_ANIMATION_ALWAYS)
//    public void changeAdapterAnimationAlways(boolean always) {
//        if (mAdapter != null) {
//            animationAlways = (Boolean) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_ALWAYS, true);
//            mAdapter.isFirstOnly(!animationAlways);
//        }
//    }
}
