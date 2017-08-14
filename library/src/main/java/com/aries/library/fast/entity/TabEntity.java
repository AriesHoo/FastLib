package com.aries.library.fast.entity;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Created: AriesHoo on 2017/7/28 14:23
 * Function: 主页Tab实体类
 * Desc:
 */
public class TabEntity implements CustomTabEntity {
    public String mTitle;
    public int mSelectedIcon;
    public int mUnSelectedIcon;
    public Fragment mFragment;

    public TabEntity(String title, int unSelectedIcon, int selectedIcon, Fragment fragment) {
        this.mTitle = title;
        this.mSelectedIcon = selectedIcon;
        this.mUnSelectedIcon = unSelectedIcon;
        this.mFragment = fragment;
    }

    public TabEntity(int unSelectedIcon, int selectedIcon, Fragment fragment) {
        mSelectedIcon = selectedIcon;
        mUnSelectedIcon = unSelectedIcon;
        mFragment = fragment;
    }

    @Override
    public String getTabTitle() {
        if (TextUtils.isEmpty(mTitle)) {
            return "";
        }
        return mTitle;
    }

    @Override
    public int getTabSelectedIcon() {
        return mSelectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return mUnSelectedIcon;
    }
}
