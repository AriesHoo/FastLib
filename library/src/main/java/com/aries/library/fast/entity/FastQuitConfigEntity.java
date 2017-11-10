package com.aries.library.fast.entity;

import android.support.annotation.ColorInt;

/**
 * Created: AriesHoo on 2017/11/6 13:06
 * E-Mail: AriesHoo@126.com
 * Function: 主页返回键功能参数
 * Description:
 */
public class FastQuitConfigEntity {

    private boolean isSnackBarEnable;
    private boolean isBackToTaskEnable;
    private boolean isBackToTaskDelayEnable;
    @ColorInt
    private int snackBarBackgroundColor;
    @ColorInt
    private int snackBarMessageColor;
    private CharSequence quitMessage;
    private long quitDelay;

    public boolean isSnackBarEnable() {
        return isSnackBarEnable;
    }

    /**
     * 设置退出提示框是一SnackBar还是Toast
     *
     * @param snackBarEnable
     * @return
     */
    public FastQuitConfigEntity setSnackBarEnable(boolean snackBarEnable) {
        isSnackBarEnable = snackBarEnable;
        return this;
    }

    public boolean isBackToTaskEnable() {
        return isBackToTaskEnable;
    }

    /**
     * 设置点击返回键是否返回桌面
     *
     * @param backToTaskEnable
     * @return
     */
    public FastQuitConfigEntity setBackToTaskEnable(boolean backToTaskEnable) {
        isBackToTaskEnable = backToTaskEnable;
        return this;
    }

    public boolean isBackToTaskDelayEnable() {
        return isBackToTaskDelayEnable;
    }

    /**
     * 设置点击返回键返回桌面是否延迟--即是否先提示一次再退回主页
     *
     * @param backToTaskDelayEnable
     * @return
     */
    public FastQuitConfigEntity setBackToTaskDelayEnable(boolean backToTaskDelayEnable) {
        isBackToTaskDelayEnable = backToTaskDelayEnable;
        return this;
    }

    public int getSnackBarBackgroundColor() {
        return snackBarBackgroundColor;
    }

    /**
     * 设置SnackBar背景颜色值
     *
     * @param snackBarBackgroundColor
     */
    public FastQuitConfigEntity setSnackBarBackgroundColor(@ColorInt int snackBarBackgroundColor) {
        this.snackBarBackgroundColor = snackBarBackgroundColor;
        return this;
    }

    public int getSnackBarMessageColor() {
        return snackBarMessageColor;
    }

    /**
     * 设置SnackBar提示文字颜色
     *
     * @param snackBarMessageColor
     * @return
     */
    public FastQuitConfigEntity setSnackBarMessageColor(@ColorInt int snackBarMessageColor) {
        this.snackBarMessageColor = snackBarMessageColor;
        return this;
    }

    public CharSequence getQuitMessage() {
        return quitMessage;
    }

    /**
     * 设置退出提示文字
     *
     * @param quitMessage
     * @return
     */
    public FastQuitConfigEntity setQuitMessage(CharSequence quitMessage) {
        this.quitMessage = quitMessage;
        return this;
    }

    public long getQuitDelay() {
        return quitDelay;
    }

    /**
     * 设置提示等待时间
     *
     * @param quitDelay
     * @return
     */
    public FastQuitConfigEntity setQuitDelay(long quitDelay) {
        this.quitDelay = quitDelay;
        return this;
    }

}
