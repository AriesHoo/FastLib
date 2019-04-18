package com.aries.library.fast.demo.impl;

import android.accounts.AccountsException;
import android.accounts.NetworkErrorException;

import com.aries.library.fast.demo.App;
import com.aries.library.fast.demo.R;
import com.aries.library.fast.i.HttpRequestControl;
import com.aries.library.fast.i.IHttpRequestControl;
import com.aries.library.fast.i.OnHttpRequestListener;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.util.NetworkUtil;
import com.aries.library.fast.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;
import retrofit2.HttpException;

/**
 * @Author: AriesHoo on 2018/12/4 18:08
 * @E-Mail: AriesHoo@126.com
 * @Function: 网络请求成功/失败全局处理
 * @Description:
 */
public class HttpRequestControlImpl implements HttpRequestControl {

    private static String TAG = "HttpRequestControlImpl";

    @Override
    public void httpRequestSuccess(IHttpRequestControl httpRequestControl, List<?> list, OnHttpRequestListener listener) {
        if (httpRequestControl == null) {
            return;
        }
        SmartRefreshLayout smartRefreshLayout = httpRequestControl.getRefreshLayout();
        BaseQuickAdapter adapter = httpRequestControl.getRecyclerAdapter();
        StatusLayoutManager statusLayoutManager = httpRequestControl.getStatusLayoutManager();
        int page = httpRequestControl.getCurrentPage();
        int size = httpRequestControl.getPageSize();

        LoggerManager.i(TAG, "smartRefreshLayout:" + smartRefreshLayout + ";adapter:" + adapter + ";status:" + ";page:" + page + ";class:" + httpRequestControl.getRequestClass());
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }
        if (adapter == null) {
            return;
        }
        adapter.loadMoreComplete();
        if (list == null || list.size() == 0) {
            //第一页没有
            if (page == 0) {
                adapter.setNewData(new ArrayList());
                statusLayoutManager.showEmptyLayout();
                if (listener != null) {
                    listener.onEmpty();
                }
            } else {
                adapter.loadMoreEnd();
                if (listener != null) {
                    listener.onNoMore();
                }
            }
            return;
        }
        statusLayoutManager.showSuccessLayout();
        if (smartRefreshLayout.getState()== RefreshState.Refreshing || page == 0) {
            adapter.setNewData(new ArrayList());
        }
        adapter.addData(list);
        if (listener != null) {
            listener.onNext();
        }
        if (list.size() < size) {
            adapter.loadMoreEnd();
            if (listener != null) {
                listener.onNoMore();
            }
        }
    }

    @Override
    public void httpRequestError(IHttpRequestControl httpRequestControl, Throwable e) {
        int reason = R.string.fast_exception_other_error;
//        int code = FastError.EXCEPTION_OTHER_ERROR;
        if (!NetworkUtil.isConnected(App.getContext())) {
            reason = R.string.fast_exception_network_not_connected;
        } else {
            //网络异常--继承于AccountsException
            if (e instanceof NetworkErrorException) {
                reason = R.string.fast_exception_network_error;
                //账户异常
            } else if (e instanceof AccountsException) {
                reason = R.string.fast_exception_accounts;
                //连接异常--继承于SocketException
            } else if (e instanceof ConnectException) {
                reason = R.string.fast_exception_connect;
                //socket异常
            } else if (e instanceof SocketException) {
                reason = R.string.fast_exception_socket;
                // http异常
            } else if (e instanceof HttpException) {
                reason = R.string.fast_exception_http;
                //DNS错误
            } else if (e instanceof UnknownHostException) {
                reason = R.string.fast_exception_unknown_host;
            } else if (e instanceof JsonSyntaxException
                    || e instanceof JsonIOException
                    || e instanceof JsonParseException) {
                //数据格式化错误
                reason = R.string.fast_exception_json_syntax;
            } else if (e instanceof SocketTimeoutException || e instanceof TimeoutException) {
                reason = R.string.fast_exception_time_out;
            } else if (e instanceof ClassCastException) {
                reason = R.string.fast_exception_class_cast;
            }
        }
        if (httpRequestControl == null || httpRequestControl.getStatusLayoutManager() == null) {
            ToastUtil.show(reason);
            return;
        }
        SmartRefreshLayout smartRefreshLayout = httpRequestControl.getRefreshLayout();
        BaseQuickAdapter adapter = httpRequestControl.getRecyclerAdapter();
        StatusLayoutManager statusLayoutManager = httpRequestControl.getStatusLayoutManager();
        int page = httpRequestControl.getCurrentPage();
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh(false);
        }
        if (adapter != null) {
            adapter.loadMoreComplete();
            if (statusLayoutManager == null) {
                return;
            }
            //初始页
            if (page == 0) {
                if (!NetworkUtil.isConnected(App.getContext())) {
                    //可自定义网络错误页面展示
                    statusLayoutManager.showCustomLayout(R.layout.layout_status_layout_manager_error);
                } else {
                    statusLayoutManager.showErrorLayout();
                }
                return;
            }
            //可根据不同错误展示不同错误布局  showCustomLayout(R.layout.xxx);
            statusLayoutManager.showErrorLayout();
        }
    }
}
