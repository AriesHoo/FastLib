package com.aries.library.fast.demo.adapter;

import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.entity.WidgetEntity;
import com.aries.library.fast.util.SizeUtil;
import com.aries.ui.view.radius.RadiusRelativeLayout;
import com.aries.ui.view.radius.RadiusViewDelegate;
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
        ViewCompat.setElevation(helper.itemView, helper.itemView.getResources().
                getDimensionPixelSize(R.dimen.dp_elevation));
        int marginSize = helper.itemView.getResources().getDimensionPixelSize(R.dimen.dp_margin_item);
        ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
        margin.setMargins(marginSize, marginSize, marginSize,
                helper.getLayoutPosition() == getItemCount() - 1 ? marginSize : 0);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            RadiusViewDelegate delegate = ((RadiusRelativeLayout) helper.itemView).getDelegate();
            delegate.setStrokeWidth(SizeUtil.dp2px(1));
            delegate.setStrokeColor(mContext.getResources().getColor(R.color.colorLineGray));
        }
    }
}
