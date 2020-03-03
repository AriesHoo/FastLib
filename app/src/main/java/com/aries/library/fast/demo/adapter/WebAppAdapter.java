package com.aries.library.fast.demo.adapter;

import android.view.ViewGroup;

import com.aries.library.fast.demo.App;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.entity.WebAppEntity;
import com.aries.library.fast.util.SizeUtil;
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
    private int mPaddingTop;


    public WebAppAdapter() {
        super(R.layout.item_web_app);
        mPadding = App.getContext().getResources().getDimensionPixelSize(R.dimen.dp_margin_item);
        mPaddingTop = (((SizeUtil.getScreenWidth() - 4 * mPadding) / 3) - SizeUtil.dp2px(36 + 18) - mPadding) / 2;
    }

    @Override
    protected void convert(BaseViewHolder helper, WebAppEntity item) {
        int index = helper.getAdapterPosition() % 3;
        int size = getData().size();
        int percent = size % 3;

        RadiusTextView text = (RadiusTextView) helper.itemView;
        text.setText(item.title);
        text.getDelegate()
                .setTopDrawable(item.icon)
                .init();

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
        if (params != null) {
            params.topMargin = mPadding;
            params.leftMargin = index == 0 ? mPadding : mPadding / 2;
            params.rightMargin = index == 2 ? mPadding : mPadding / 2;
            params.bottomMargin =
                    helper.getAdapterPosition() == size - 1
                            || percent != 1 && helper.getAdapterPosition() == getData().size() - 2
                            || percent == 0 && helper.getAdapterPosition() == getData().size() - 3 ? mPadding : 0;
        }
        helper.itemView.setPadding(mPadding, mPaddingTop, mPadding, mPaddingTop);
    }
}
