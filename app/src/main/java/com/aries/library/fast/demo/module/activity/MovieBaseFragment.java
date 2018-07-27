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

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * Created: AriesHoo on 2018/7/6 10:07
 * E-Mail: AriesHoo@126.com
 * Function:电影列表示例
 * Description:
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
        return R.layout.fast_layout_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
//        new BackToTopHelper().init(mRecyclerView);

    }

    @Override
    public void loadData(int page) {
//        if (ApiConstant.API_MOVIE_TOP.equals(mUrl)) {
//            FastRetrofit.getInstance().setBaseUrl("http://www.baidu.com/");
//            Map<String, Object> map = new HashMap<>();
//            map.put("test", "test");
//            FastRetrofit.getInstance()
//                    .addHeader(map)
//                    .addHeader("ht", "ht");
//        }
        mDefaultPageSize = 15;//接口最大支持单页100
        ApiRepository.getInstance().getMovie(mUrl, page * mDefaultPage, mDefaultPageSize)
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseMovieEntity>(getIHttpRequestControl()) {
                    @Override
                    public void _onNext(BaseMovieEntity entity) {
                        mStatusManager.showSuccessLayout();
                        FastManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), entity == null || entity.subjects == null ? new ArrayList<>() : entity.subjects, null);
                    }
                });
    }

    @Override
    public void onItemClicked(BaseQuickAdapter<SubjectsEntity, BaseViewHolder> adapter, View view, int position) {
        super.onItemClicked(adapter, view, position);
        WebViewActivity.start(mContext, adapter.getItem(position).alt);
    }

    //单独设置状态
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
