package com.aries.library.fast.i;

import com.aries.ui.helper.navigation.NavigationViewHelper;

/**
 * Created: AriesHoo on 2018/6/21 13:33
 * E-Mail: AriesHoo@126.com
 * Function:扩展BasisActivity独有属性
 * Description:
 */
public interface IBasisActivity extends IBasisView {

    /**
     * 设置init之前用于调整属性
     *
     * @param navigationHelper
     */
    void beforeControlNavigation(NavigationViewHelper navigationHelper);
}
