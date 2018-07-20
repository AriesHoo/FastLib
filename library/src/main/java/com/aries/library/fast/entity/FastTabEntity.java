package com.aries.library.fast.entity;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * @Author: AriesHoo on 2018/7/16 9:16
 * @E-Mail: AriesHoo@126.com
 * Function: 主页Tab实体类
 * Description:
 */
public class FastTabEntity implements CustomTabEntity {
    public String mTitle;
    public int mSelectedIcon;
    public int mUnSelectedIcon;
    public Fragment mFragment;

    public FastTabEntity(String title, int unSelectedIcon, int selectedIcon, Fragment fragment) {
        this.mTitle = title;
        this.mSelectedIcon = selectedIcon;
        this.mUnSelectedIcon = unSelectedIcon;
        this.mFragment = fragment;
    }

    public FastTabEntity(int unSelectedIcon, int selectedIcon, Fragment fragment) {
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
