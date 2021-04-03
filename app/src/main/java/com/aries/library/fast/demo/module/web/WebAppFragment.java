package com.aries.library.fast.demo.module.web;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.adapter.WebAppAdapter;
import com.aries.library.fast.demo.entity.WebAppEntity;
import com.aries.library.fast.demo.module.WebAppActivity;
import com.aries.library.fast.module.fragment.FastTitleRefreshLoadFragment;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: AriesHoo on 2019/4/24 14:36
 * @E-Mail: AriesHoo@126.com
 * @Function: 展示WebApp
 * @Description:
 */
public class WebAppFragment extends FastTitleRefreshLoadFragment<WebAppEntity> {
    private BaseQuickAdapter mAdapter;

    public static WebAppFragment newInstance() {
        Bundle args = new Bundle();
        WebAppFragment fragment = new WebAppFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean isRefreshEnable() {
        return false;
    }

    @Override
    public boolean isLoadMoreEnable() {
        return false;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(mContext, 3, RecyclerView.VERTICAL, false);
    }

    @Override
    public BaseQuickAdapter<WebAppEntity, BaseViewHolder> getAdapter() {
        mAdapter = new WebAppAdapter();
        return mAdapter;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fast_layout_title_refresh_recycler;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText(R.string.web_app);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }


    @Override
    public void loadData(int page) {
        List<WebAppEntity> list = new ArrayList<>();
        list.add(new WebAppEntity(R.drawable.ic_zhi_hu, "知乎", "www.zhihu.com"));
        list.add(new WebAppEntity(R.drawable.ic_jian_shu, "简书", "www.jianshu.com"));
        list.add(new WebAppEntity(R.drawable.ic_little_red_book, "小红书", "www.xiaohongshu.com"));

        list.add(new WebAppEntity(R.drawable.ic_bai_du, "百度", "m.baidu.com"));
        list.add(new WebAppEntity(R.drawable.ic_tie, "贴吧", "c.tieba.baidu.com/index/tbwise/feed?shownew=1"));
        list.add(new WebAppEntity(R.drawable.ic_we_bo, "微博", "m.weibo.cn"));

        list.add(new WebAppEntity(R.drawable.ic_dou_yu, "斗鱼", "m.douyu.com"));
        list.add(new WebAppEntity(R.drawable.ic_hu_ya, "虎牙", "m.huya.com"));
        list.add(new WebAppEntity(R.drawable.ic_listen, "喜马拉雅", "m.ximalaya.com"));

        list.add(new WebAppEntity(R.drawable.ic_bilibili, "Bilibili", "m.bilibili.com/index.html"));
        list.add(new WebAppEntity(R.drawable.ic_you_ku, "优酷", "www.youku.com"));
        list.add(new WebAppEntity(R.drawable.ic_ai_qi_yi, "爱奇艺", "m.iqiyi.com", Color.argb(255, 25, 25, 25)));

        list.add(new WebAppEntity(R.drawable.ic_dou_ban, "豆瓣", "m.douban.com"));
        list.add(new WebAppEntity(R.drawable.ic_wang_yi, "网易", "3g.163.com/touch/#/", Color.parseColor("#F01B1B")));
        list.add(new WebAppEntity(R.drawable.ic_tao_piao_piao, "淘票票", "h5.m.taopiaopiao.com/app/moviemain/pages/index/index.html"));

        list.add(new WebAppEntity(R.drawable.ic_xia_chu_fang, "下厨房", "m.xiachufang.com"));
        list.add(new WebAppEntity(R.drawable.ic_buy, "什么值得买", "m.smzdm.com"));
        list.add(new WebAppEntity(R.drawable.ic_search, "查快递", "m.kuaidi100.com"));
        FastManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), list, null);
    }

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        super.onVisibleChanged(isVisibleToUser);
        if (isVisibleToUser) {
            StatusBarUtil.setStatusBarLightMode(mContext);
        }
    }

    @Override
    public void onItemClicked(BaseQuickAdapter<WebAppEntity, BaseViewHolder> adapter, View view, int position) {
        WebAppEntity item = adapter.getItem(position);
        WebAppActivity.start(mContext, item.url, item.color);
    }
}
