package com.aries.library.fast.demo.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.demo.module.WebAppActivity;
import com.aries.library.fast.demo.retrofit.repository.BaseRepository;
import com.aries.library.fast.i.FastObserverControl;
import com.aries.library.fast.i.FastRecyclerViewControl;
import com.aries.library.fast.i.IFastRefreshLoadView;
import com.aries.library.fast.i.LoadMoreFoot;
import com.aries.library.fast.i.LoadingDialog;
import com.aries.library.fast.i.MultiStatusView;
import com.aries.library.fast.i.QuitAppControl;
import com.aries.library.fast.i.TitleBarViewControl;
import com.aries.library.fast.i.ToastControl;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.retrofit.FastNullException;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.SizeUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.library.fast.widget.FastLoadDialog;
import com.aries.library.fast.widget.FastLoadMoreView;
import com.aries.ui.util.DrawableUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.radius.RadiusTextView;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.progress.UIProgressDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import io.reactivex.Observable;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @Author: AriesHoo on 2018/7/30 11:34
 * @E-Mail: AriesHoo@126.com
 * Function: 应用全局配置管理实现
 * Description:
 * 1、新增友盟统计功能对接
 */
public class AppImpl implements DefaultRefreshHeaderCreator, LoadMoreFoot,
        FastRecyclerViewControl, MultiStatusView, LoadingDialog,
        TitleBarViewControl, QuitAppControl, ToastControl, FastObserverControl {

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
        layout.setEnableHeaderTranslationContent(false)
                .setPrimaryColorsId(R.color.colorAccent)
                .setEnableOverScrollDrag(false);
        MaterialHeader materialHeader = new MaterialHeader(mContext);
        materialHeader.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.colorTextBlack),
                ContextCompat.getColor(mContext, R.color.colorTextBlackLight));
        return materialHeader;
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
            adapter.openLoadAnimation();
        }
        //方式一:设置FastLoadMoreView--可参考FastLoadMoreView.Builder相应set方法
        //默认配置请参考FastLoadMoreView.Builder(mContext)里初始化
        return new FastLoadMoreView.Builder(mContext)
                .setLoadingTextFakeBold(true)
                .setLoadingSize(SizeUtil.dp2px(20))
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
     * 全局设置
     *
     * @param recyclerView
     * @param cls
     */
    @Override
    public void setRecyclerView(RecyclerView recyclerView, Class<?> cls) {
        LoggerManager.i(TAG, "setRecyclerView-" + cls.getSimpleName() + "context:" + recyclerView.getContext() + ";:" + (Activity.class.isAssignableFrom(recyclerView.getContext().getClass())) + ";:" + (recyclerView.getContext() instanceof Activity));
    }

    @Override
    public void setMultiStatusView(StatusLayoutManager.Builder statusView, IFastRefreshLoadView iFastRefreshLoadView) {
    }

    /**
     * 这里将局部设置的FastLoadDialog 抛至该处用于全局设置，在局部使用{@link com.aries.library.fast.retrofit.FastLoadingObserver}
     *
     * @param activity
     * @return
     */
    @Nullable
    @Override
    public FastLoadDialog createLoadingDialog(@Nullable Activity activity) {
        return new FastLoadDialog(activity,
                new UIProgressDialog.WeBoBuilder(activity)
                        .setMessage("加载中")
                        .create())
                .setCanceledOnTouchOutside(false)
                .setMessage("请求数据中,请稍候...");
        //注意使用UIProgressDialog时最好在Builder里设置提示文字setMessage不然后续再设置文字信息也不会显示
//        return new FastLoadDialog(activity, new UIProgressDialog.WeChatBuilder(activity)
//                .setBackgroundColor(Color.parseColor("#FCFCFC"))
////                .setMinHeight(SizeUtil.dp2px(140))
////                .setMinWidth(SizeUtil.dp2px(270))
//                .setTextSizeUnit(TypedValue.COMPLEX_UNIT_PX)
//                .setMessage(R.string.fast_loading)
//                .setLoadingSize(SizeUtil.dp2px(30))
//                .setTextSize(SizeUtil.dp2px(16f))
//                .setTextPadding(SizeUtil.dp2px(10))
//                .setTextColorResource(R.color.colorTextGray)
//                .setIndeterminateDrawable(FastUtil.getTintDrawable(ContextCompat.getDrawable(mContext, R.drawable.dialog_loading), ContextCompat.getColor(mContext, R.color.colorTitleText)))
//                .setBackgroundRadius(SizeUtil.dp2px(6f))
//                .create());
//        Dialog dialog = new PictureDialog(activity);
//        return new FastLoadDialog(activity, dialog)
//                .setCancelable(true)
//                .setCanceledOnTouchOutside(true);
    }

    /**
     * 控制全局TitleBarView
     *
     * @param titleBar
     * @return
     */
    @Override
    public boolean createTitleBarViewControl(TitleBarView titleBar, Class<?> cls) {
        //默认的MD风格返回箭头icon如使用该风格可以不用设置
        Drawable mDrawable = DrawableUtil.setTintDrawable(ContextCompat.getDrawable(mContext, R.drawable.fast_ic_back),
                ContextCompat.getColor(mContext, R.color.colorTitleText));
        //是否支持状态栏白色
        boolean isSupport = StatusBarUtil.isSupportStatusBarFontChange();
        boolean isActivity = Activity.class.isAssignableFrom(cls);
        Activity activity = FastStackUtil.getInstance().getActivity(cls);
        //设置TitleBarView 所有TextView颜色
        titleBar.setStatusBarLightMode(isSupport)
                //不支持黑字的设置白透明
                .setStatusAlpha(isSupport ? 0 : 102)
                .setLeftTextDrawable(isActivity ? mDrawable : null)
                .setDividerHeight(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? SizeUtil.dp2px(0.5f) : 0);
        if (activity != null) {
            titleBar.setTitleMainText(activity.getTitle())
                    .setOnLeftTextClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.finish();
                        }
                    });
        }
        if (activity instanceof WebAppActivity) {
            return false;
        }
        ViewCompat.setElevation(titleBar, mContext.getResources().getDimension(R.dimen.dp_elevation));
        return false;
    }

    /**
     * @param isFirst  是否首次提示
     * @param activity 操作的Activity
     * @return 延迟间隔--如不需要设置两次提示可设置0--最佳方式是直接在回调中执行你想要的操作
     */
    @Override
    public long quipApp(boolean isFirst, Activity activity) {
        //默认配置
        if (isFirst) {
            ToastUtil.show(R.string.fast_quit_app);
        } else {
            FastStackUtil.getInstance().exit(false);
        }
        return 2000;
    }


    @Override
    public Toast getToast() {
        return null;
    }

    @Override
    public void setToast(Toast toast, RadiusTextView textView) {

    }

    /**
     * @param o {@link FastObserver} 对象用于后续事件逻辑
     * @param e 原始错误
     * @return true 拦截操作不进行原始{@link FastObserver#onError(Throwable)}后续逻辑
     * false 不拦截继续后续逻辑
     * {@link FastNullException} 已在{@link FastObserver#onError} ｝处理如果为该类型Exception可不用管,参考
     * {@link BaseRepository#transform(Observable)} 处理逻辑
     */
    @Override
    public boolean onError(FastObserver o, Throwable e) {
        return false;
    }
}
