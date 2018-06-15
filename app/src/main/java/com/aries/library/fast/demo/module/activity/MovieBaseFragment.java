package com.aries.library.fast.demo.module.activity;

import android.os.Bundle;
import android.view.View;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.adapter.SubjectMovieAdapter;
import com.aries.library.fast.demo.base.BaseMovieEntity;
import com.aries.library.fast.demo.constant.ApiConstant;
import com.aries.library.fast.demo.constant.EventConstant;
import com.aries.library.fast.demo.constant.GlobalConstant;
import com.aries.library.fast.demo.constant.SPConstant;
import com.aries.library.fast.demo.entity.SubjectsEntity;
import com.aries.library.fast.demo.helper.BackToTopHelper;
import com.aries.library.fast.demo.module.WebViewActivity;
import com.aries.library.fast.demo.retrofit.repository.ApiRepository;
import com.aries.library.fast.module.fragment.FastRefreshLoadFragment;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.SPUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created: AriesHoo on 2017/8/25 17:03
 * Function: 电影列表
 * Desc:
 */
public class MovieBaseFragment extends FastRefreshLoadFragment<SubjectsEntity> {

    private BaseQuickAdapter mAdapter;
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
        mAdapter = new SubjectMovieAdapter(mUrl == ApiConstant.API_MOVIE_TOP);
        changeAdapterAnimation(0);
        changeAdapterAnimationAlways(true);
        return mAdapter;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fast_layout_multi_status_refresh_recycler;
    }

    @Override
    public int getContentBackground() {
        return -1;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        new BackToTopHelper().init(mRecyclerView, mEasyStatusView);
    }

    @Override
    public void loadData(int page) {
        DEFAULT_PAGE_SIZE = 15;//接口最大支持单页100
        ApiRepository.getInstance().getMovie(mUrl, page * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE)
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseMovieEntity>(mContext,
                        this, mRefreshLayout, mAdapter, mEasyStatusView, page) {
                    @Override
                    public void _onNext(BaseMovieEntity entity) {
//                        mRefreshLayout.finishRefresh();
//                        mAdapter.loadMoreComplete();
//                        if (entity == null || entity.subjects == null || entity.subjects.size() == 0) {
//                            if (page == 0) {
//                                mEasyStatusView.empty();
//                            } else {
//                                mAdapter.loadMoreEnd();
//                            }
//                            return;
//                        }
                        FastManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), entity == null || entity.subjects == null ? new ArrayList<>() : entity.subjects, null);
//                        mEasyStatusView.content();
//                        if (mRefreshLayout.isRefreshing())
//                            mAdapter.setNewData(null);
//                        mAdapter.openLoadAnimation();
//                        mAdapter.addData(entity.subjects);
//                        if (entity.count < DEFAULT_PAGE_SIZE) {
//                            mAdapter.loadMoreEnd();
//                        }
//                        LoggerManager.d("ApiRepository", "title:" + entity.title + ";start:" + entity.start + ";count:" + entity.count + ";total:" + entity.total);
//                        if (!entity.hasMore()) {
//                            mAdapter.loadMoreEnd(page == 0);
//                        }
                    }

//                    @Override
//                    public void _onError(int errorRes, int errorCode, Throwable e) {
//                        super._onError(errorRes, errorCode, e);
//                        mRefreshLayout.finishRefresh();
//                        mAdapter.loadMoreComplete();
//                        LoggerManager.e("error:" + getString(errorRes) + ";errorCode:" + errorCode + ";Throwable:" + e.getMessage());
//                        if (page == 0) {
//                            mEasyStatusView.error();
//                            if (errorCode == FastError.EXCEPTION_ACCOUNTS) {
//
//                            } else if (errorCode == FastError.EXCEPTION_JSON_SYNTAX) {
//
//                            } else if (errorCode == FastError.EXCEPTION_OTHER_ERROR) {
//
//                            } else if (errorCode == FastError.EXCEPTION_TIME_OUT) {
//
//                            } else {
//                                mEasyStatusView.noNet();
//                            }
//                        } else {
//                            ToastUtil.show(errorRes);
//                        }
//                    }
                });
    }

    @Override
    public void onItemClicked(BaseQuickAdapter<SubjectsEntity, BaseViewHolder> adapter, View view, int position) {
        super.onItemClicked(adapter, view, position);
        WebViewActivity.start(mContext, adapter.getItem(position).alt);
    }

    @Subscriber(mode = ThreadMode.MAIN, tag = EventConstant.EVENT_KEY_CHANGE_ADAPTER_ANIMATION)
    public void changeAdapterAnimation(int index) {
        if (mAdapter != null) {
            animationIndex = (int) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_INDEX, animationIndex - 1) + 1;
            mAdapter.openLoadAnimation(animationIndex);
        }
    }

    @Subscriber(mode = ThreadMode.MAIN, tag = EventConstant.EVENT_KEY_CHANGE_ADAPTER_ANIMATION_ALWAYS)
    public void changeAdapterAnimationAlways(boolean always) {
        if (mAdapter != null) {
            animationAlways = (Boolean) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_ALWAYS, true);
            mAdapter.isFirstOnly(!animationAlways);
        }
    }
}
