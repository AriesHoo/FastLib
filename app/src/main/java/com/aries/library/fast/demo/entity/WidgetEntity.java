package com.aries.library.fast.demo.entity;

import android.app.Activity;

/**
 * @Author: AriesHoo on 2018/11/19 14:18
 * @E-Mail: AriesHoo@126.com
 * @Function: 描述性条目实体
 * @Description:
 */
public class WidgetEntity {

    public String title;
    public String content;
    public String url;
    public Class<? extends Activity> activity;

    public WidgetEntity() {
    }

    public WidgetEntity(String title, String content, Class<? extends Activity> activity) {
        this.title = title;
        this.content = content;
        this.activity = activity;
    }

    public WidgetEntity(String title, String content, String url) {
        this.title = title;
        this.content = content;
        this.url = url;
    }
}
