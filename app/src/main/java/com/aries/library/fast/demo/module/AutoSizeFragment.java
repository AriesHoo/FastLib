package com.aries.library.fast.demo.module;

import android.os.Bundle;

import com.aries.library.fast.basis.BasisFragment;
import com.aries.library.fast.demo.R;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * @Author: AriesHoo on 2019/4/28 9:20
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public class AutoSizeFragment extends BasisFragment implements CustomAdapt {

    public static AutoSizeFragment newInstance() {
        Bundle args = new Bundle();
        AutoSizeFragment fragment = new AutoSizeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_auto_size;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 414;
    }
}
