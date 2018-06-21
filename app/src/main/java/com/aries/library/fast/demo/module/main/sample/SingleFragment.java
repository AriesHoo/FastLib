package com.aries.library.fast.demo.module.main.sample;

import android.os.Bundle;

import com.aries.library.fast.demo.module.activity.MovieBaseFragment;

/**
 * Created: AriesHoo on 2018/5/28/028 22:04
 * E-Mail: AriesHoo@126.com
 * Function:
 * Description:
 */
public class SingleFragment extends MovieBaseFragment {

    @Override
    public boolean isSingle() {
        return true;
    }

    public static SingleFragment newInstance(String url) {
        Bundle args = new Bundle();
        SingleFragment fragment = new SingleFragment();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }
}
