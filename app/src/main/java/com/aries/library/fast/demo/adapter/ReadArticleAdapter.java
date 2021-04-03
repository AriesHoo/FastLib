package com.aries.library.fast.demo.adapter;

import android.view.View;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.base.BaseItemTouchQuickAdapter;
import com.aries.library.fast.demo.base.BaseItemTouchViewHolder;
import com.aries.library.fast.demo.entity.ReadArticleItemEntity;
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
public class ReadArticleAdapter extends BaseItemTouchQuickAdapter<ReadArticleItemEntity, BaseItemTouchViewHolder> implements LoadMoreModule {

    boolean isShowTop;

    public ReadArticleAdapter(boolean isShowTop) {
        super(R.layout.item_read_acticle);
        this.isShowTop = isShowTop;
    }

    @Override
    protected void convert(BaseItemTouchViewHolder helper, ReadArticleItemEntity item) {
        LoggerManager.i("isShowTop", "isShowTop:" + isShowTop);
        helper.setText(R.id.tv_titleArticle, item.title)
                .setText(R.id.tv_summaryArticle, item.summary)
                .setText(R.id.tv_timeArticle, item.getTime());
        RadiusViewHelper.getInstance().setRadiusViewAdapter(((RadiusRelativeLayout) helper.itemView).getDelegate());
    }
}
