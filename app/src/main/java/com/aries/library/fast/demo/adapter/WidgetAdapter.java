package com.aries.library.fast.demo.adapter;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.entity.WidgetEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created: AriesHoo on 2017/7/14 9:55
 * Function:
 * Desc:
 */
public class WidgetAdapter extends BaseQuickAdapter<WidgetEntity, BaseViewHolder> {

    public WidgetAdapter() {
        super(R.layout.item_widget, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, WidgetEntity item) {
        helper.setText(R.id.tv_titleWidget, item.title);
        helper.setText(R.id.tv_contentWidget, item.content);
    }
}
