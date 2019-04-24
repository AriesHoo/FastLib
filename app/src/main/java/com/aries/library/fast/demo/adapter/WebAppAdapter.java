package com.aries.library.fast.demo.adapter;

import android.view.ViewGroup;

import com.aries.library.fast.demo.App;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.entity.WebAppEntity;
import com.aries.ui.view.radius.RadiusTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @Author: AriesHoo on 2019/4/24 14:43
 * @E-Mail: AriesHoo@126.com
 * @Function: WebApp适配器
 * @Description:
 */
public class WebAppAdapter extends BaseQuickAdapter<WebAppEntity, BaseViewHolder> {
    private int mPadding;

    public WebAppAdapter() {
        super(R.layout.item_web_app);
        mPadding = App.getContext().getResources().getDimensionPixelSize(R.dimen.dp_margin_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, WebAppEntity item) {
        boolean isLeft = helper.getAdapterPosition() % 2 == 0;
        RadiusTextView text = (RadiusTextView) helper.itemView;
        text.setText(item.title);
        text.getDelegate()
                .setTopDrawable(item.icon)
                .init();

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
        if (params != null) {
            params.topMargin = mPadding;
            params.leftMargin = isLeft ? mPadding : mPadding / 2;
            params.rightMargin = !isLeft ? mPadding : mPadding / 2;
            params.bottomMargin = helper.getAdapterPosition() == getData().size() - 1 ||
                    (getData().size() % 2 == 0 && helper.getAdapterPosition() == getData().size() - 2) ? mPadding : 0;
        }
    }
}
