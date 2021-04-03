package com.aries.library.fast.demo.adapter;

import android.view.View;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.base.BaseItemTouchQuickAdapter;
import com.aries.library.fast.demo.base.BaseItemTouchViewHolder;
import com.aries.library.fast.demo.entity.SubjectsEntity;
import com.aries.library.fast.demo.helper.RadiusViewHelper;
import com.aries.library.fast.manager.GlideManager;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.ui.view.radius.RadiusRelativeLayout;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.flyco.labelview.LabelView;

/**
 * @Author: AriesHoo on 2018/8/10 9:53
 * @E-Mail: AriesHoo@126.com
 * Function:
 * Description:
 */
public class SubjectMovieAdapter extends BaseItemTouchQuickAdapter<SubjectsEntity, BaseItemTouchViewHolder> implements LoadMoreModule {

    boolean isShowTop;

    public SubjectMovieAdapter(boolean isShowTop) {
        super(R.layout.item_subject_movie);
        this.isShowTop = isShowTop;
    }

    @Override
    protected void convert(BaseItemTouchViewHolder helper, SubjectsEntity item) {
        LoggerManager.i("isShowTop", "isShowTop:" + isShowTop);
        helper.setText(R.id.tv_titleMovie, item.title)
                .setText(R.id.tv_typeMovie, "题材:" + item.getGenres())
                .setText(R.id.tv_yearMovie, "年份:" + item.year)
                .setText(R.id.tv_directorMovie, "导演:" + item.getDirectors())
                .setText(R.id.tv_castMovie, "主演:" + item.getCasts());
        GlideManager.loadImg(item.images.large, helper.getView(R.id.iv_coverMovie));
        LabelView labelView = helper.getView(R.id.lv_topMovie);
        labelView.setText("Top" + (helper.getLayoutPosition() + 1));
        labelView.setVisibility(isShowTop ? View.VISIBLE : View.GONE);
        RadiusViewHelper.getInstance().setRadiusViewAdapter(((RadiusRelativeLayout) helper.itemView).getDelegate());
    }
}
