package com.aries.library.fast.entity;

import java.io.Serializable;

import butterknife.Unbinder;

/**
 * @Author: AriesHoo on 2019/3/4 11:42
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public class ActivityEntity implements Serializable {
    private Unbinder unBinder;

    public Unbinder getUnBinder() {
        return unBinder;
    }

    public void setUnBinder(Unbinder unBinder) {
        this.unBinder = unBinder;
    }

}
