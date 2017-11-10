package com.aries.library.fast.demo.adapter;

import android.view.ViewGroup;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.entity.WidgetEntity;
import com.aries.library.fast.demo.helper.RadiusViewHelper;
import com.aries.ui.view.radius.RadiusRelativeLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created: AriesHoo on 2017/7/14 9:55
 * Function: 描述性条目适配器
 * Desc:
 */
public class WidgetAdapter extends BaseQuickAdapter<WidgetEntity, BaseViewHolder> {

    public WidgetAdapter() {
        super(R.layout.item_widget, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, WidgetEntity item) {
        helper.setText(R.id.tv_titleWidget, item.title)
                .setText(R.id.tv_contentWidget, item.content);
        int marginSize = helper.itemView.getResources().getDimensionPixelSize(R.dimen.dp_margin_item);
        ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
        margin.setMargins(marginSize, marginSize, marginSize,
                helper.getLayoutPosition() == getItemCount() - 1 ? marginSize : 0);
        RadiusViewHelper.getInstance().setRadiusViewAdapter(((RadiusRelativeLayout) helper.itemView).getDelegate());
    }
}
