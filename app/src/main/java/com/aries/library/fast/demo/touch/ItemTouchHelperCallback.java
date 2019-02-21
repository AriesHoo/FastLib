package com.aries.library.fast.demo.touch;


import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

/**
 * @Author: AriesHoo on 2018/8/9 17:25
 * @E-Mail: AriesHoo@126.com
 * Function: ItemTouchHelper CallBack
 * Description:
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter mAdapter;
    private OnItemTouchHelperListener mOnItemTouchHelperListener;
    private int mStarPosition;

    public ItemTouchHelperCallback() {
        this(null);
    }

    public ItemTouchHelperCallback(ItemTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //上下拖拽
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //向右侧滑
        int swipeFlags = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (mAdapter != null) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        if (mOnItemTouchHelperListener != null) {
            mOnItemTouchHelperListener.onMove(target.getAdapterPosition(), viewHolder.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        if (mOnItemTouchHelperListener != null) {
            mOnItemTouchHelperListener.onMoved(fromPos, toPos);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (mAdapter == null) {
            return;
        }
        mAdapter.onItemSwiped(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            mStarPosition = viewHolder.getAdapterPosition();
            if (mOnItemTouchHelperListener != null) {
                mOnItemTouchHelperListener.onStart(mStarPosition);
            }
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                ItemTouchHelperViewHolder itemTouchHelperViewHolder =
                        (ItemTouchHelperViewHolder) viewHolder;
                itemTouchHelperViewHolder.onItemSelectedChanged();

            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            ItemTouchHelperViewHolder itemTouchHelperViewHolder =
                    (ItemTouchHelperViewHolder) viewHolder;
            itemTouchHelperViewHolder.onItemClear();
        }
        if (mOnItemTouchHelperListener != null) {
            mOnItemTouchHelperListener.onEnd(mStarPosition, viewHolder.getAdapterPosition());
        }
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /**
     * 设置监听
     *
     * @param onItemTouchListener
     * @return
     */
    public ItemTouchHelperCallback setOnItemTouchHelperListener(OnItemTouchHelperListener onItemTouchListener) {
        mOnItemTouchHelperListener = onItemTouchListener;
        return this;
    }

    /**
     * @param adapter
     * @return
     */
    public ItemTouchHelperCallback setAdapter(ItemTouchHelperAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }
}
