package com.aries.library.fast.demo.base;

import com.aries.library.fast.demo.helper.TitleBarHelper;
import com.aries.library.fast.module.fragment.FastTitleFragment;
import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2017/7/26 22:50
 * Function: 带TitleBarView的Fragment
 * Desc:
 */
public abstract class BaseTitleFragment extends FastTitleFragment {

    @Override
    public boolean isLightStatusBarEnable() {
        return true;
    }

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
        super.beforeSetTitleBar(titleBar);
        TitleBarHelper.getInstance().setTitleBarView(titleBar, mContext,false);
    }
}
