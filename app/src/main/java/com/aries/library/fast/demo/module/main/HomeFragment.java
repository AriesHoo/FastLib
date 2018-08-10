package com.aries.library.fast.demo.module.main;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.demo.App;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.adapter.WidgetAdapter;
import com.aries.library.fast.demo.base.BaseItemTouchQuickAdapter;
import com.aries.library.fast.demo.constant.GlobalConstant;
import com.aries.library.fast.demo.constant.SPConstant;
import com.aries.library.fast.demo.entity.WidgetEntity;
import com.aries.library.fast.demo.module.WebViewActivity;
import com.aries.library.fast.demo.module.main.sample.QQTitleActivity;
import com.aries.library.fast.demo.module.main.sample.SwipeBackActivity;
import com.aries.library.fast.demo.module.main.sample.TestFragmentActivity;
import com.aries.library.fast.demo.module.main.sample.TestStatusActivity;
import com.aries.library.fast.demo.module.main.sample.TitleWithEditTextActivity;
import com.aries.library.fast.demo.module.main.sample.ToastActivity;
import com.aries.library.fast.demo.module.main.sample.ali.ALiPayMainActivity;
import com.aries.library.fast.demo.module.main.sample.news.NewsMainActivity;
import com.aries.library.fast.demo.touch.ItemTouchHelperCallback;
import com.aries.library.fast.demo.touch.OnItemTouchHelperListener;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.manager.RxJavaManager;
import com.aries.library.fast.module.fragment.FastTitleRefreshLoadFragment;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.TransitionEffect;

/**
 * Created: AriesHoo on 2017/7/20 11:45
 * Function:
 * Desc:
 */
public class HomeFragment extends FastTitleRefreshLoadFragment<WidgetEntity> {

    protected BGABanner banner;
    private BaseItemTouchQuickAdapter mAdapter;
    private List<Class> listActivity = new ArrayList<>();
    private List<Integer> listArraysBanner = Arrays.asList(R.array.arrays_banner_all
            , R.array.arrays_banner_an, R.array.arrays_banner_si
            , R.array.arrays_banner_liu, R.array.arrays_banner_di, R.array.arrays_banner_jun);

    private List<TransitionEffect> listTransitionEffect = new ArrayList<>();
    private int transitionIndex = GlobalConstant.GLOBAL_BANNER_TRANSITION_POSITION;
    private int chooseIndex = 0;
    private AlertDialog alertDialog;

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
        titleBar.setRightTextDrawable(R.drawable.ic_transition)
                .setDividerVisible(false)
//                .setTitleMainText(getClass().getSimpleName())
//                .setTitleMainTextColor(Color.MAGENTA)
                .setStatusAlpha(StatusBarUtil.isSupportStatusBarFontChange() ? 0 : 102)
                .setOnRightTextClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (alertDialog == null) {
                            alertDialog = new AlertDialog.Builder(mContext)
                                    .setTitle("选择banner切换动画")
                                    .setSingleChoiceItems(R.array.arrays_transition, transitionIndex, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            chooseIndex = which;
                                        }
                                    })
                                    .setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SPUtil.put(mContext, SPConstant.SP_KEY_HOME_TRANSITION_INDEX, chooseIndex);
                                            transitionIndex = (int) SPUtil.get(mContext, SPConstant.SP_KEY_HOME_TRANSITION_INDEX, transitionIndex);
                                            if (banner != null) {
                                                banner.setTransitionEffect(listTransitionEffect.get(transitionIndex));
                                            }
                                        }
                                    })
                                    .create();
                            alertDialog.getListView().setVerticalScrollBarEnabled(false);
                            alertDialog.getListView().setHorizontalScrollBarEnabled(false);
                        }
                        alertDialog.show();
                    }
                })
                .setStatusAlpha(0)
                .setBgColor(Color.TRANSPARENT);
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        LoggerManager.d(TAG, "initView");
        listTransitionEffect.add(TransitionEffect.Default);
        listTransitionEffect.add(TransitionEffect.Alpha);
        listTransitionEffect.add(TransitionEffect.Rotate);
        listTransitionEffect.add(TransitionEffect.Cube);
        listTransitionEffect.add(TransitionEffect.Flip);
        listTransitionEffect.add(TransitionEffect.Accordion);
        listTransitionEffect.add(TransitionEffect.ZoomFade);
        listTransitionEffect.add(TransitionEffect.Fade);
        listTransitionEffect.add(TransitionEffect.ZoomCenter);
        listTransitionEffect.add(TransitionEffect.ZoomStack);
        listTransitionEffect.add(TransitionEffect.Stack);
        listTransitionEffect.add(TransitionEffect.Depth);
        listTransitionEffect.add(TransitionEffect.Zoom);
        transitionIndex = (int) SPUtil.get(mContext, SPConstant.SP_KEY_HOME_TRANSITION_INDEX, transitionIndex);
        chooseIndex = transitionIndex;
        setBanner(0);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelperCallback(mAdapter)
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
                                ToastUtil.show("从---" + star + "---拖拽至---" + end + "---");
                            }
                        }));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void loadData(int page) {
        if (listActivity.size() > 0) {
            int random = FastUtil.getRandom(100);
            int position = (random % (listArraysBanner.size() - 1)) + 1;
            LoggerManager.d("position:" + position + ";random:" + random);
            setBanner(position);
            return;
        }
        listActivity.clear();
        listActivity.add(SwipeBackActivity.class);
        listActivity.add(QQTitleActivity.class);
        listActivity.add(ALiPayMainActivity.class);
        listActivity.add(NewsMainActivity.class);
        listActivity.add(TestStatusActivity.class);
        listActivity.add(TestFragmentActivity.class);
        listActivity.add(ToastActivity.class);
        listActivity.add(TitleWithEditTextActivity.class);
        List<WidgetEntity> list = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.arrays_home_list_title);
        for (int i = 0; i < titles.length; i++) {
            WidgetEntity entity = new WidgetEntity();
            entity.title = titles[i];
            entity.content = getString(R.string.home_list_content);
            entity.activity = listActivity.get(i);
            list.add(entity);
        }
        RxJavaManager.getInstance().getDelayObservable(list, 2, TimeUnit.MILLISECONDS)
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<List<WidgetEntity>>() {
                    @Override
                    public void _onNext(List<WidgetEntity> entity) {
                        FastManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), entity, null);
                    }
                });
    }

    private void setBanner(int position) {
        List<String> images = Arrays.asList(getResources().getStringArray(listArraysBanner.get(position)));
        if (banner == null) {
            View v = View.inflate(mContext, R.layout.layout_banner, null);
            banner = v.findViewById(R.id.banner);
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
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) banner.getLayoutParams();
            params.height = App.getImageHeight();
            LoggerManager.d("banner:" + params.height + ";width:" + SizeUtil.getScreenWidth());
        }
        banner.setData(images, getTips(images));
        banner.setTransitionEffect(listTransitionEffect.get(transitionIndex));
        mRefreshLayout.finishRefresh();
    }

    private List<String> getTips(List<String> images) {
        List<String> listTips = new ArrayList<>();
        int size = images == null ? 0 : images.size();
        for (int i = 0; i < size; i++) {
            listTips.add("点击查看原图");
        }
        return listTips;
    }

    @Override
    public void onItemClicked(BaseQuickAdapter<WidgetEntity, ? extends BaseViewHolder> adapter, View view, int position) {
        super.onItemClicked(adapter, view, position);
        WidgetEntity entity = adapter.getItem(position);
        if (position == 0) {
            SwipeBackActivity.start(mContext, entity.title);
        } else {
            FastUtil.startActivity(mContext, entity.activity);
        }
    }
}
