package com.aries.smart.module.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.aries.library.fast.module.fragment.FastTitleFragment;
import com.aries.library.fast.util.FastUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.smart.R;
import com.aries.ui.view.radius.RadiusTextView;
import com.aries.ui.view.title.TitleBarView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: AriesHoo on 2018/8/10 12:22
 * @E-Mail: AriesHoo@126.com
 * Function: 主页演示
 * Description:
 */
public class HomeFragment extends FastTitleFragment {

    protected final static String LENOVO_PACKAGE_NAME = "com.lenovo.browser.hd";
    protected final static String GOOGLE_PACKAGE_NAME = "com.android.chrome";

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText(R.string.home);
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        super.onVisibleChanged(isVisibleToUser);
        //Fragment 可见性变化回调

        RadiusTextView radiusTextView = mContentView.findViewById(R.id.rtv_webMainFragment);
        radiusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://47.108.167.249:9796/"));
                if (isAppInstall(LENOVO_PACKAGE_NAME)) {
                    intent.setPackage(LENOVO_PACKAGE_NAME);
                    startActivity(intent);
                } else if (isAppInstall(GOOGLE_PACKAGE_NAME)) {
                    intent.setPackage(GOOGLE_PACKAGE_NAME);
                    startActivity(intent);
                }
                Intent chooserIntent = Intent.createChooser(intent, "iiii");
                ///筛选只分享应用
                List<ResolveInfo> resInfo = mContext.getPackageManager().queryIntentActivities(intent, 0);
                if (!resInfo.isEmpty()) {
                    List<String> list = new ArrayList<>();
                    for (ResolveInfo info : resInfo) {
                        ActivityInfo activityInfo = info.activityInfo;
                        list.add(activityInfo.packageName + ';' + activityInfo.targetActivity);
                    }
                    ToastUtil.show("string" + new Gson().toJson(list));
                    FastUtil.copyToClipboard(mContext, new Gson().toJson(list));
                }
                //mContext.startActivity(chooserIntent);
            }

        });
    }

    public boolean isAppInstall(String packageName) {
        Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
        return launchIntent != null;
    }


}
