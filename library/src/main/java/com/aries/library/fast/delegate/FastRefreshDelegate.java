package com.aries.library.fast.delegate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.R;
import com.aries.library.fast.i.IFastRefreshView;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.ui.util.FindViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * @Author: AriesHoo on 2019/3/22 14:18
 * @E-Mail: AriesHoo@126.com
 * @Function: 快速实现下拉刷新布局代理
 * @Description:
 */
public class FastRefreshDelegate {

    public SmartRefreshLayout mRefreshLayout;
    public View mRootView;
    private FastManager mManager;
    private IFastRefreshView mIFastRefreshView;
    private Context mContext;

    public FastRefreshDelegate(View rootView, IFastRefreshView iFastRefreshView) {
        this.mRootView = rootView;
        this.mIFastRefreshView = iFastRefreshView;
        this.mContext = rootView.getContext().getApplicationContext();
        this.mManager = FastManager.getInstance();
        if (mIFastRefreshView == null) {
            return;
        }
        if (mRootView == null) {
            mRootView = mIFastRefreshView.getContentView();
        }
        if (mRootView == null) {
            return;
        }
        getRefreshLayout(rootView);
        initRefreshHeader();
        if (mRefreshLayout != null) {
            mIFastRefreshView.setRefreshLayout(mRefreshLayout);
        }
    }

    /**
     * 初始化刷新头配置
     */
    protected void initRefreshHeader() {
        if (mRefreshLayout == null) {
            return;
        }
        if (mRefreshLayout.getRefreshHeader() != null) {
            return;
        }
        mRefreshLayout.setRefreshHeader(mIFastRefreshView.getRefreshHeader() != null
                ? mIFastRefreshView.getRefreshHeader() :
                mManager.getDefaultRefreshHeader() != null ?
                        mManager.getDefaultRefreshHeader().createRefreshHeader(mContext, mRefreshLayout) :
                        new ClassicsHeader(mContext).setSpinnerStyle(SpinnerStyle.Translate));
        mRefreshLayout.setOnRefreshListener(mIFastRefreshView);
        mRefreshLayout.setEnableRefresh(mIFastRefreshView.isRefreshEnable());
    }

    /**
     * 获取布局里的刷新Layout
     *
     * @param rootView
     * @return
     */
    private void getRefreshLayout(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.smartLayout_rootFastLib);
        if (mRefreshLayout == null) {
            mRefreshLayout = FindViewUtil.getTargetView(rootView, SmartRefreshLayout.class);
        }
        //原布局无SmartRefreshLayout 将rootView 从父布局移除并添加进SmartRefreshLayout 将SmartRefreshLayout作为新的
        if (mRefreshLayout == null && mIFastRefreshView.isRefreshEnable()) {
            ViewGroup parentLayout;
            ViewGroup.LayoutParams params = mRootView.getLayoutParams();

            if (mRootView.getParent() != null) {
                parentLayout = (ViewGroup) mRootView.getParent();
            } else {
                parentLayout = mRootView.getRootView().findViewById(android.R.id.content);
            }
            //如果此时parentLayout为null 可能mRootView为Fragment 根布局
            if (parentLayout == null) {
                return;
            }
            int index = parentLayout.indexOfChild(mRootView);
            //先移除rootView
            parentLayout.removeView(mRootView);
            //新建SmartRefreshLayout
            mRefreshLayout = new SmartRefreshLayout(mRootView.getContext());
            //将rootView添加进SmartRefreshLayout
            mRefreshLayout.addView(mRootView);
            //将SmartRefreshLayout添加进parentLayout
            parentLayout.addView(mRefreshLayout, index, params);
        }
    }


    /**
     * 与Activity 及Fragment onDestroy 及时解绑释放避免内存泄露
     */
    public void onDestroy() {
        mRefreshLayout = null;
        mContext = null;
        mManager = null;
        mRootView = null;
        LoggerManager.i("FastRefreshDelegate", "onDestroy");
    }
}
