package com.aries.library.fast.entity;

import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * Created: AriesHoo on 2018/1/5 0005 下午 2:38
 * E-Mail: AriesHoo@126.com
 * Function: 快速设置导航栏
 * Description:
 */
public class FastNavigationConfigEntity {
    private boolean controlEnable;
    private boolean transEnable;
    @ColorInt
    private int color = Color.TRANSPARENT;

    public boolean isControlEnable() {
        return controlEnable;
    }

    public boolean isTransEnable() {
        return transEnable;
    }

    public int getColor() {
        return color;
    }

    /**
     * 是否控制导航栏 设置为true其它两个属性才有效
     *
     * @param controlEnable
     * @return
     */
    public FastNavigationConfigEntity setControlEnable(boolean controlEnable) {
        this.controlEnable = controlEnable;
        return this;
    }

    /**
     * 是否全透明 设置全透明true color才有效
     *
     * @param transEnable
     * @return
     */
    public FastNavigationConfigEntity setTransEnable(boolean transEnable) {
        this.transEnable = transEnable;
        return this;
    }

    /**
     * @param color
     * @return
     */
    public FastNavigationConfigEntity setColor(int color) {
        this.color = color;
        return this;
    }
}
