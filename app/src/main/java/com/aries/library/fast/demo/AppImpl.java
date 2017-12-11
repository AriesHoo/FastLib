package com.aries.library.fast.demo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aries.library.fast.demo.helper.RefreshHeaderHelper;
import com.aries.library.fast.i.HttpErrorControl;
import com.aries.library.fast.i.IMultiStatusView;
import com.aries.library.fast.i.LoadMoreFoot;
import com.aries.library.fast.i.LoadingDialog;
import com.aries.library.fast.i.MultiStatusView;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.retrofit.FastError;
import com.aries.library.fast.util.ToastUtil;
import com.aries.library.fast.widget.FastLoadDialog;
import com.aries.library.fast.widget.FastLoadMoreView;
import com.aries.library.fast.widget.FastMultiStatusView;
import com.aries.ui.widget.progress.UIProgressView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.marno.easystatelibrary.EasyStatusView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

/**
 * Created: AriesHoo on 2017/11/30 11:43
 * E-Mail: AriesHoo@126.com
 * Function: 应用全局配置管理实现
 * Description:
 */
public class AppImpl implements DefaultRefreshHeaderCreater
        , LoadMoreFoot, MultiStatusView, LoadingDialog, HttpErrorControl {

    private Context mContext;
    private String TAG = this.getClass().getSimpleName();

    public AppImpl(@Nullable Context context) {
        this.mContext = context;
    }

    /**
     * 下拉刷新头配置
     *
     * @param context
     * @param layout
     * @return
     */
    @NonNull
    @Override
    public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
        layout.setEnableHeaderTranslationContent(false);
        return RefreshHeaderHelper.getInstance().getRefreshHeader(mContext);
    }

    /**
     * Adapter加载更多配置
     *
     * @param adapter
     * @return
     */
    @Nullable
    @Override
    public LoadMoreView createDefaultLoadMoreView(BaseQuickAdapter adapter) {
        if (adapter != null) {
            //设置动画是否一直开启
            adapter.isFirstOnly(false);
            //设置动画
            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        }
        //方式一:设置FastLoadMoreView--可参考FastLoadMoreView.Builder相应set方法
        //默认配置请参考FastLoadMoreView.Builder(mContext)里初始化
        return new FastLoadMoreView.Builder(mContext)
                .setLoadingTextFakeBold(true)
//                                .setLoadTextColor(Color.MAGENTA)
//                                //设置Loading 颜色-5.0以上有效
//                                .setLoadingProgressColor(Color.MAGENTA)
//                                //设置Loading drawable--会使Loading颜色失效
//                                .setLoadingProgressDrawable(R.drawable.dialog_loading_wei_bo)
//                                //设置全局TextView颜色
//                                .setLoadTextColor(Color.MAGENTA)
//                                //设置全局TextView文字字号
//                                .setLoadTextSize(SizeUtil.dp2px(14))
//                                .setLoadingText("努力加载中...")
//                                .setLoadingTextColor(Color.GREEN)
//                                .setLoadingTextSize(SizeUtil.dp2px(14))
//                                .setLoadEndText("我是有底线的")
//                                .setLoadEndTextColor(Color.GREEN)
//                                .setLoadEndTextSize(SizeUtil.dp2px(14))
//                                .setLoadFailText("哇哦!出错了")
//                                .setLoadFailTextColor(Color.RED)
//                                .setLoadFailTextSize(SizeUtil.dp2px(14))
                .build();
        //方式二:使用adapter自带--其实我默认设置的和这个基本一致只是提供了相应设置方法
//                        return new SimpleLoadMoreView();
        //方式三:参考SimpleLoadMoreView或FastLoadMoreView完全自定义自己的LoadMoreView
//                        return MyLoadMoreView();
    }

    /**
     * 多状态布局配置
     *
     * @return
     */
    @NonNull
    @Override
    public IMultiStatusView createMultiStatusView() {
        //根据具体情况可设置更多属性具体请参考FastMultiStatusView.Builder里set方法
        //默认设置请参考Builder(Context context)里初始化
        return new FastMultiStatusView.Builder(mContext)
                .setLoadingTextFakeBold(true)
//                                .setTextColor(Color.MAGENTA)
//                                .setTextColorResource(R.color.colorMultiText)
//                                .setTextSizeResource(R.dimen.dp_multi_text_size)
//                                .setTextSize(getResources().getDimensionPixelSize(R.dimen.dp_multi_text_size))
//                                .setLoadingProgressColorResource(R.color.colorMultiProgress)
//                                .setLoadingProgressColor(getResources().getColor(R.color.colorMultiProgress))
//                                .setLoadingTextColor(getResources().getColor(R.color.colorMultiProgress))
//                                .setLoadingText(R.string.fast_multi_loading)
//                                .setEmptyText(R.string.fast_multi_empty)
//                                .setErrorText(R.string.fast_multi_error)
//                                .setNoNetText(R.string.fast_multi_network)
//                                .setTextMarginResource(R.dimen.dp_multi_margin)
//                                .setImageWidthHeightResource(R.dimen.dp_multi_image_size)
//                                .setEmptyImageColorResource(R.color.colorTitleText)
//                                .setEmptyImageDrawable(R.drawable.fast_img_multi_empty)
//                                .setErrorImageColorResource(R.color.colorTitleText)
//                                .setErrorImageDrawable(R.drawable.fast_img_multi_error)
//                                .setNoNetImageColorResource(R.color.colorTitleText)
//                                .setNoNetImageDrawable(R.drawable.fast_img_multi_network)
                .build();
    }

    @Nullable
    @Override
    public FastLoadDialog createLoadingDialog(@Nullable Activity activity) {
        //第一种
//                        return new FastLoadDialog(activity);
        //第二种 使用UIProgressView里的四种模式Loading效果
        return new FastLoadDialog(activity, UIProgressView.STYLE_WEI_BO)
                .setCanceledOnTouchOutside(false)
                .setMessage("请求数据中,请稍候...");
//                        ProgressDialog progressDialog = new ProgressDialog(activity);
//                        progressDialog.setMessage("加载中...");
//                        //第三种--系统ProgressDialog不过系统已标记为过时类不建议使用
//                        return new FastLoadDialog(activity, progressDialog);
//                        第四种--完全自定义Dialog形式
//                        return new FastLoadDialog(activity, MyDialog);
    }

    @Override
    public boolean createHttpErrorControl(int errorRes, int errorCode, @io.reactivex.annotations.NonNull Throwable e, Context context, Object... args) {
        LoggerManager.e(TAG, "args:" + args + ";context:" + context.getClass().getSimpleName());
        if (args != null) {//可以将具体调用界面部分视图传递到全局控制
            if (args.length >= 5) {
                if (args[1] instanceof SmartRefreshLayout) {
                    LoggerManager.e(TAG, "args:" + args[1]);
                    ((SmartRefreshLayout) args[1]).finishRefresh();
                }
                if (args[2] instanceof BaseQuickAdapter) {
                    LoggerManager.e(TAG, "args:" + args[2]);
                    ((BaseQuickAdapter) args[2]).loadMoreComplete();
                }
                if (args[3] instanceof EasyStatusView) {
                    LoggerManager.e(TAG, "args:" + args[3]);
                    ((EasyStatusView) args[3]).error();
                    if ((Integer) args[4] == 0) {
                        if (errorCode == FastError.EXCEPTION_ACCOUNTS) {

                        } else if (errorCode == FastError.EXCEPTION_JSON_SYNTAX) {

                        } else if (errorCode == FastError.EXCEPTION_OTHER_ERROR) {

                        } else if (errorCode == FastError.EXCEPTION_TIME_OUT) {

                        } else {
                            ((EasyStatusView) args[3]).noNet();
                        }
                    } else {
                        ToastUtil.show(errorRes);
                    }
                }
            }
        }
        //返回值true则FastObserver不会回调_onError所有逻辑处理都在全局位置处理
        return false;
    }
}
