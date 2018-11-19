package com.aries.library.fast.demo.module.main.sample;

import android.os.Bundle;

import com.aries.library.fast.demo.module.activity.MovieBaseFragment;

/**
 * @Author: AriesHoo on 2018/11/19 14:20
 * @E-Mail: AriesHoo@126.com
 * @Function: 单Fragment Activity懒加载示例
 * @Description:
 */
public class SingleFragment extends MovieBaseFragment {

    public static SingleFragment newInstance(String url) {
        Bundle args = new Bundle();
        SingleFragment fragment = new SingleFragment();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }
}
