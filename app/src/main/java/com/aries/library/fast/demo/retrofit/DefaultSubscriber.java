package com.aries.library.fast.demo.retrofit;

import android.accounts.NetworkErrorException;

import com.aries.library.fast.demo.retrofit.exception.AccountsException;
import com.aries.library.fast.util.ToastUtil;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created: AriesHoo on 2017/6/23 17:23
 * Function:
 * Desc:
 */
public abstract class DefaultSubscriber<T> extends rx.Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        String reason = "";
        if (e instanceof AccountsException) {//账户异常
            reason = "账户异常";
            ToastUtil.show("登录失效，请重新登录");
        } else if (e instanceof JsonSyntaxException) {//数据格式化错误
            reason = "数据格式化错误";
        } else if (e instanceof HttpException) {// http异常
            reason = "http异常";
        } else if (e instanceof UnknownHostException || e instanceof ConnectException) {//未连接网络或DNS错误
            reason = "未连接网络或DNS错误";
        } else if (e instanceof NetworkErrorException) {
            reason = "网络错误";
            ToastUtil.show("网络错误");
        } else if (e instanceof SocketException) {
            reason = "连接超时";
        } else {
            reason = "其他错误";
        }
    }

    @Override
    public void onNext(T entity) {
        _onNext(entity);
    }

    public abstract void _onNext(T entity);
}
