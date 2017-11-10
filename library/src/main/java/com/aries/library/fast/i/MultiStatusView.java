package com.aries.library.fast.i;

import android.support.annotation.NonNull;

import com.marno.easystatelibrary.EasyStatusView;

/**
 * Created: AriesHoo on 2017/11/10 13:58
 * E-Mail: AriesHoo@126.com
 * Function:
 * Description:
 */
public interface MultiStatusView {
    @NonNull
    IMultiStatusView createMultiStatusView(EasyStatusView easyStatusView);
}
