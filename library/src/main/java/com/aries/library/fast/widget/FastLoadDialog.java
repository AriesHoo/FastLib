package com.aries.library.fast.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.WindowManager;

import com.aries.library.fast.R;
import com.aries.ui.widget.progress.UIProgressView;

import java.lang.ref.WeakReference;

/**
 * Created: AriesHoo on 2017/8/24 16:03
 * Function:快速创建网络请求loading
 * Desc:
 */
public class FastLoadDialog {

    private Dialog mDialog = null;

    private Activity mActivity;
    private final WeakReference<Activity> mReference;

    public FastLoadDialog(Activity activity) {
        this(activity, UIProgressView.STYLE_NORMAL);
    }

    /**
     * @param activity
     * @param style
     */
    public FastLoadDialog(Activity activity, int style) {
        this(activity, new UIProgressView(activity, style).setMessage(R.string.fast_loading));
    }

    public FastLoadDialog(Activity activity, Dialog dialog) {
        this.mReference = new WeakReference<>(activity);
        this.mDialog = dialog;
    }

    /**
     * 设置是否可点击返回键关闭dialog
     *
     * @param enable
     * @return
     */
    public FastLoadDialog setCancelable(boolean enable) {
        if (mDialog != null) {
            mDialog.setCancelable(enable);
        }
        return this;
    }

    /**
     * 设置是否可点击dialog以外关闭
     *
     * @param enable
     * @return
     */
    public FastLoadDialog setCanceledOnTouchOutside(boolean enable) {
        if (mDialog != null) {
            mDialog.setCanceledOnTouchOutside(enable);
        }
        return this;
    }

    /**
     * @param msg
     * @return
     */
    public FastLoadDialog setMessage(CharSequence msg) {
        if (mDialog instanceof UIProgressView) {
            ((UIProgressView) mDialog).setMessage(msg);
        } else if (mDialog instanceof ProgressDialog) {
            ((ProgressDialog) mDialog).setMessage(msg);
        }
        return this;
    }

    /**
     * @param msg
     * @return
     */
    public FastLoadDialog setMessage(int msg) {
        mActivity = mReference.get();
        if (mActivity != null) {
            return setMessage(mActivity.getString(msg));
        }
        return this;
    }

    /**
     * @param enable 设置全透明
     * @return
     */
    public FastLoadDialog setFullTrans(boolean enable) {
        if (mDialog != null) {
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.dimAmount = enable ? 0f : 0.5f;// 黑暗度
            mDialog.getWindow().setAttributes(lp);
        }
        return this;
    }

    public void show() {
        mActivity = mReference.get();
        if (mActivity != null && mDialog != null && !mActivity.isFinishing()) {
            mDialog.show();
        }
    }

    public void dismiss() {
        mActivity = mReference.get();
        if (mDialog != null && !mActivity.isFinishing()) {
            mDialog.dismiss();
        }
    }
}
