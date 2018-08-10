package com.aries.library.fast.demo.touch;

/**
 * @Author: AriesHoo on 2018/8/9 17:25
 * @E-Mail: AriesHoo@126.com
 * Function: 用于实现RecyclerView侧滑和移动item的帮助接口
 * Description:
 */
public interface ItemTouchHelperAdapter {
    /**
     * 当item被移动时调用
     *
     * @param fromPosition item的起点
     * @param toPosition   item的终点
     */
    void onItemMove(int fromPosition, int toPosition);

    /**
     * 当item被侧滑时调用
     *
     * @param position 被侧滑的item的position
     */
    void onItemSwiped(int position);
}
