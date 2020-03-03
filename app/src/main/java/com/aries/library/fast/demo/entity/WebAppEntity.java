package com.aries.library.fast.demo.entity;

import android.graphics.Color;

/**
 * @Author: AriesHoo on 2019/4/24 14:28
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public class WebAppEntity {


    public int icon;
    public CharSequence title;
    public String url;
    public int color = Color.WHITE;

    public WebAppEntity() {
    }

    public WebAppEntity(int icon, CharSequence title, String url, int color) {
        this.icon = icon;
        this.title = title;
        this.url = url;
        this.color = color;
    }

    public WebAppEntity(int icon, CharSequence title, String url) {
        this(icon, title, url, Color.WHITE);
    }
}
