package com.aries.library.fast.demo.base;

import android.os.Build;
import android.view.View;

import com.aries.library.fast.demo.touch.ItemTouchHelperViewHolder;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * @Author: AriesHoo on 2018/8/9 17:29
 * @E-Mail: AriesHoo@126.com
 * Function: 实现拖拽ViewHolder
 * Description:
 */
public class BaseItemTouchViewHolder extends BaseViewHolder implements ItemTouchHelperViewHolder {

    public BaseItemTouchViewHolder(View view) {
        super(view);
    }

    @Override
    public void onItemSelectedChanged() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemView.setTranslationZ(30);
        }
    }

    @Override
    public void onItemClear() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemView.setTranslationZ(0);
        }
    }

}
