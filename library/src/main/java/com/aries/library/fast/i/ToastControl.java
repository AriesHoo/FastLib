package com.aries.library.fast.i;

import android.widget.Toast;

import com.aries.ui.view.radius.RadiusTextView;

/**
 * @Author: AriesHoo on 2019/1/18 17:49
 * @E-Mail: AriesHoo@126.com
 * @Function: {@link com.aries.library.fast.util.ToastUtil} 控制
 * @Description:
 */
public interface ToastControl {

    /**
     * 处理其它异常情况
     *
     * @return
     */
    Toast getToast();

    /**
     * 设置Toast
     *
     * @param toast    ToastUtil 中的Toast
     * @param textView ToastUtil 中的Toast设置的View
     */
    void setToast(Toast toast, RadiusTextView textView);
}
