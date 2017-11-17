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
    private static volatile RefreshHeaderHelper sInstance;

    private RefreshHeaderHelper() {
    }

    public static RefreshHeaderHelper getInstance() {
        if (sInstance == null) {
            synchronized (RefreshHeaderHelper.class) {
                if (sInstance == null) {
                    sInstance = new RefreshHeaderHelper();
                }
            }
        }
        return sInstance;
    }

    public RefreshHeader getRefreshHeader(Context mContext) {
        MaterialHeader materialHeader = new MaterialHeader(mContext);
        materialHeader.setColorSchemeColors(mContext.getResources().getColor(R.color.colorTextBlack),
                mContext.getResources().getColor(R.color.colorTextBlackLight));
        return materialHeader;
    }
}
