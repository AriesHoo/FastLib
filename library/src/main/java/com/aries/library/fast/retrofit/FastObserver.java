package com.aries.library.fast.retrofit;

import android.accounts.AccountsException;
import android.accounts.NetworkErrorException;
import android.app.Activity;

import com.aries.library.fast.R;
import com.aries.library.fast.util.FastStackUtil;
import com.aries.library.fast.util.NetworkUtil;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.observers.DefaultObserver;
import retrofit2.HttpException;

/**
 * Created: AriesHoo on 2017/8/24 9:56
 * Function:Retrofit快速观察者
 * Desc:
 */
public abstract class FastObserver<T> extends DefaultObserver<T> {

    @Override
    public void onComplete() {
        
    }

    @Override
    public void onError(Throwable e) {
        int reason = R.string.fast_exception_other_error;
        int code = FastError.EXCEPTION_OTHER_ERROR;
        Activity current = FastStackUtil.getInstance().getCurrent();
        if (!NetworkUtil.isConnected(current)) {
            reason = R.string.fast_exception_network_not_connected;
            code = FastError.EXCEPTION_NETWORK_NOT_CONNECTED;
        } else {
            if (e instanceof NetworkErrorException) {//网络异常--继承于AccountsException
                reason = R.string.fast_exception_network_error;
                code = FastError.EXCEPTION_NETWORK_ERROR;
            } else if (e instanceof AccountsException) {//账户异常
                reason = R.string.fast_exception_accounts;
                code = FastError.EXCEPTION_ACCOUNTS;
            } else if (e instanceof ConnectException) {//连接异常--继承于SocketException
                reason = R.string.fast_exception_connect;
                code = FastError.EXCEPTION_CONNECT;
            } else if (e instanceof SocketException) {//socket异常
                reason = R.string.fast_exception_socket;
                code = FastError.EXCEPTION_SOCKET;
            } else if (e instanceof HttpException) {// http异常
                reason = R.string.fast_exception_http;
                code = FastError.EXCEPTION_HTTP;
            } else if (e instanceof UnknownHostException) {//DNS错误
                reason = R.string.fast_exception_unknown_host;
                code = FastError.EXCEPTION_UNKNOWN_HOST;
            } else if (e instanceof JsonSyntaxException
                    || e instanceof JsonIOException
                    || e instanceof JsonParseException) {//数据格式化错误
                reason = R.string.fast_exception_json_syntax;
                code = FastError.EXCEPTION_JSON_SYNTAX;
            } else if (e instanceof SocketTimeoutException || e instanceof TimeoutException) {
                reason = R.string.fast_exception_time_out;
                code = FastError.EXCEPTION_TIME_OUT;
            } else if (e instanceof ClassCastException) {
                reason = R.string.fast_exception_class_cast;
                code = FastError.EXCEPTION_CLASS_CAST;
            }
        }
        _onError(reason, code, e);
    }

    @Override
    public void onNext(T entity) {
        _onNext(entity);
    }

    public abstract void _onNext(T entity);

    public abstract void _onError(int errorRes, int errorCode, Throwable e);
}
