package com.aries.library.fast.demo.module.main.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.aries.library.fast.demo.R;
import com.aries.library.fast.module.activity.FastTitleActivity;
import com.aries.ui.view.title.TitleBarView;

import butterknife.BindView;

/**
 * @Author: AriesHoo on 2018/11/19 14:23
 * @E-Mail: AriesHoo@126.com
 * @Function: activity+顶部输入框模式
 * @Description:
 */
public class TitleWithEditTextActivity extends FastTitleActivity {

    @BindView(R.id.et_bottomTitle) EditText mEtBottom;
    @BindView(R.id.v_bottom) View mVBottom;

    @Override
    public int getContentLayout() {
        return R.layout.activity_title_with_edit_text;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
    }
}
