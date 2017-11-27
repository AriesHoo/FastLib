package com.aries.library.fast.demo.helper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.SizeUtil;
import com.marno.easystatelibrary.EasyStatusView;

/**
 * Created: AriesHoo on 2017/11/20 14:37
 * E-Mail: AriesHoo@126.com
 * Function: 回到顶部帮助类
 * Description:
 */
public class BackToTopHelper {
    private FloatingActionButton mFloatingActionButton;
    private RecyclerView mRecyclerView;
    private EasyStatusView mEasyStatusView;
    private Context mContext;

    public void init(RecyclerView recyclerView, EasyStatusView easyStatusView) {
        this.mRecyclerView = recyclerView;
        this.mEasyStatusView = easyStatusView;
        this.mContext = recyclerView.getContext();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    int firstVisibleItemPosition = linearManager.findFirstVisibleItemPosition();
                    if (firstVisibleItemPosition > 10) {
                        setBackToTop(true);
                    } else {
                        setBackToTop(false);
                    }
                }
            }
        });
    }

    /**
     * 控制回到顶部
     *
     * @param enable
     */
    private void setBackToTop(boolean enable) {
        if (mFloatingActionButton == null) {
            mFloatingActionButton = new FloatingActionButton(mContext);
            mFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            mFloatingActionButton.setCompatElevation(10);
            mFloatingActionButton.setUseCompatPadding(true);
            mFloatingActionButton.setImageDrawable(
                    FastUtil.getTintDrawable(mContext.getResources().getDrawable(R.drawable.ic_top),
                            mContext.getResources().getColor(R.color.colorTitleText)));
            mFloatingActionButton.setRippleColor(mContext.getResources().getColor(R.color.colorWhitePressed));
            mEasyStatusView.addView(mFloatingActionButton);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mFloatingActionButton.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);//与父容器的左侧对齐
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//与父容器的上侧对齐
            lp.rightMargin = SizeUtil.dp2px(6);
            lp.bottomMargin = SizeUtil.dp2px(6);
            mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecyclerView.smoothScrollToPosition(0);
                }
            });
        }
        mFloatingActionButton.setVisibility(enable ? View.VISIBLE : View.GONE);
    }
}
