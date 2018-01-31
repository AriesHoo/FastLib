package com.aries.library.fast.demo.constant;

import com.aries.ui.util.RomUtil;

/**
 * Created: AriesHoo on 2017/9/29 11:30
 * Function: 应用全局配置-默认
 * Desc:
 */
public class GlobalConstant {

    /**
     * 全局banner动画position
     */
    public static final int GLOBAL_BANNER_TRANSITION_POSITION = 3;

    /**
     * 全局adapter动画值 1-5
     */
    public static final int GLOBAL_ADAPTER_ANIMATION_VALUE = 2;

    public static final boolean mControlEnable = true;
    public static final boolean mTransEnable = false;
    public static final boolean mPlusNavigationViewEnable = RomUtil.isEMUI();
}
