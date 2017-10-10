package com.aries.library.fast.demo.helper;

import android.content.Context;

import com.aries.library.fast.demo.R;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshHeader;

/**
 * Created: AriesHoo on 2017/9/11 9:04
 * Function: 刷新头帮助类用于全局设置
 * Desc:
 */
public class RefreshHeaderHelper {
    private static volatile RefreshHeaderHelper instance;

    private RefreshHeaderHelper() {
    }

    public static RefreshHeaderHelper getInstance() {
        if (instance == null) {
            synchronized (RefreshHeaderHelper.class) {
                if (instance == null) {
                    instance = new RefreshHeaderHelper();
                }
            }
        }
        return instance;
    }

    public RefreshHeader getRefreshHeader(Context mContext) {
        MaterialHeader materialHeader = new MaterialHeader(mContext);
        materialHeader.setColorSchemeColors(R.color.colorTextBlack, R.color.colorTextBlack);
        return materialHeader;
    }
}
