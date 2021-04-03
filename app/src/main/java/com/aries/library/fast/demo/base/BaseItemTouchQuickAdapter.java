package com.aries.library.fast.demo.base;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.aries.library.fast.demo.touch.ItemTouchHelperAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: AriesHoo on 2018/8/10 10:04
 * @E-Mail: AriesHoo@126.com
 * Function: 实现拖拽排序功能
 * Description:
 */
public abstract class BaseItemTouchQuickAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> implements ItemTouchHelperAdapter {

    public BaseItemTouchQuickAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
//        this.mData = data == null ? new ArrayList<>() : data;
//        if (layoutResId != 0) {
//            this.mLayoutResId = layoutResId;
//        }
    }

    public BaseItemTouchQuickAdapter(@Nullable List<T> data) {
        this(0, data);
    }

    public BaseItemTouchQuickAdapter(@LayoutRes int layoutResId) {
        this(layoutResId, null);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < 0 || toPosition < 0 || toPosition >= getData().size()) {
            return;
        }
        Collections.swap(getData(), fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {

    }
}
