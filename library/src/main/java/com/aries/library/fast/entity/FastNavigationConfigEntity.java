package com.aries.library.fast.entity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

import com.aries.library.fast.basis.BasisActivity;

/**
 * Created: AriesHoo on 2018/1/5 0005 下午 2:38
 * E-Mail: AriesHoo@126.com
 * Function: 快速设置导航栏
 * Description:
 */
public class FastNavigationConfigEntity {
    private boolean controlEnable;
    private boolean transEnable;


    private boolean addNavigationViewEnable;
    @ColorInt
    private int color = Color.TRANSPARENT;

    @ColorInt
    private int backgroundColor = Color.parseColor("#f8f8f8");
    private Drawable drawable;
    private Drawable backgroundDrawable = new ColorDrawable(backgroundColor);

    public boolean isControlEnable() {
        return controlEnable;
    }

    public boolean isTransEnable() {
        return transEnable;
    }

    public boolean isAddNavigationViewEnable() {
        return addNavigationViewEnable;
    }

    public int getColor() {
        return color;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public Drawable getBackgroundDrawable() {
        return backgroundDrawable;
    }

    /**
     * 是否控制导航栏 设置为true其它属性才有效
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
     * 控制是否设置一个NavigationView用于占据虚拟导航栏位置
     *
     * @param addNavigationViewEnable {@link BasisActivity#addNavigationBar()}
     *                                设置true 肯定全透明其它颜色控制由自定义NavigationView设置
     * @return
     */
    public FastNavigationConfigEntity setAddNavigationViewEnable(boolean addNavigationViewEnable) {
        this.addNavigationViewEnable = addNavigationViewEnable;
        return this;
    }

    /**
     * 设置虚拟导航栏颜色
     *
     * @param color
     * @return
     */
    public FastNavigationConfigEntity setColor(int color) {
        this.color = color;
        return setDrawable(new ColorDrawable(color));
    }

    public FastNavigationConfigEntity setBackgroundColor(int backgroundColor) {
        this.backgroundColor = color;
        return setBackgroundDrawable(new ColorDrawable(backgroundColor));
    }

    /**
     * 设置虚拟导航栏背景资源
     *
     * @param drawable
     * @return
     */
    public FastNavigationConfigEntity setDrawable(Drawable drawable) {
        this.drawable = drawable;
        return this;
    }

    /**
     * 设置假NavigationView父Layout的背景色
     * addNavigationViewEnable 设置为true生效
     *
     * @param backgroundDrawable
     * @return
     */
    public FastNavigationConfigEntity setBackgroundDrawable(Drawable backgroundDrawable) {
        this.backgroundDrawable = backgroundDrawable;
        return this;
    }
}
