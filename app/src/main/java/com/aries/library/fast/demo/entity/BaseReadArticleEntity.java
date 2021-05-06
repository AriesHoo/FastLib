package com.aries.library.fast.demo.entity;

import java.util.List;

public class BaseReadArticleEntity {


    /**
     * data : [{"id":"857hMt47bOY","newsArray":[{"id":3318215,"url":"https://www.jiemian.com/article/5902821.html","title":"OPPO关联公司公开\u201c睡眠训练方法\u201d相关专利，可帮助用户进入睡眠状态","siteName":"界面","mobileUrl":"https://www.jiemian.com/article/5902821.html","autherName":null,"duplicateId":1,"publishDate":"2021-04-02T08:33:00.000Z","language":"zh-cn","hasInstantView":true,"statementType":1},{"id":3318214,"url":"https://www.tmtpost.com/nictation/5153241.html","title":"OPPO关联公司公开\u201c睡眠训练方法\u201d相关专利","siteName":"钛媒体","mobileUrl":"https://www.tmtpost.com/nictation/5153241.html","autherName":"","duplicateId":1,"publishDate":"2021-04-02T00:48:14.981Z","language":"zh-cn","hasInstantView":true,"statementType":1}],"createdAt":"2021-04-02T11:05:49.938Z","eventData":[],"publishDate":"2021-04-02T11:09:04.218Z","summary":"据天眼查App显示，4月2日，OPPO广东移动通信有限公司公开一项名称为\u201c睡眠训练方法及相关设备\u201d的专利，公开号为CN108404275B，申请时间为2018年2月 ... 该专利摘要显示，本申请应用于电子装置，包括脑电波传感器、控制器和发射器，其中：脑电波传感器用于当确定用户准备入睡时，采集用户的脑电波 ... 发射器用于当用户的脑部处于活跃状态时，发出睡眠训练信号，睡眠训练信号用于帮助用户进入睡眠状态。","title":"OPPO关联公司公开\u201c睡眠训练方法\u201d相关专利，可帮助用户进入睡眠状态","updatedAt":"2021-04-02T11:09:04.235Z","timeline":"85809EFb2G4","order":341053,"hasInstantView":true,"extra":{"instantView":true}}]
     * pageSize : 1
     * totalItems : 69566
     * totalPages : 69566
     */

    public int pageSize;
    public int totalItems;
    public int totalPages;
    /**
     * id : 857hMt47bOY
     * newsArray : [{"id":3318215,"url":"https://www.jiemian.com/article/5902821.html","title":"OPPO关联公司公开\u201c睡眠训练方法\u201d相关专利，可帮助用户进入睡眠状态","siteName":"界面","mobileUrl":"https://www.jiemian.com/article/5902821.html","autherName":null,"duplicateId":1,"publishDate":"2021-04-02T08:33:00.000Z","language":"zh-cn","hasInstantView":true,"statementType":1},{"id":3318214,"url":"https://www.tmtpost.com/nictation/5153241.html","title":"OPPO关联公司公开\u201c睡眠训练方法\u201d相关专利","siteName":"钛媒体","mobileUrl":"https://www.tmtpost.com/nictation/5153241.html","autherName":"","duplicateId":1,"publishDate":"2021-04-02T00:48:14.981Z","language":"zh-cn","hasInstantView":true,"statementType":1}]
     * createdAt : 2021-04-02T11:05:49.938Z
     * eventData : []
     * publishDate : 2021-04-02T11:09:04.218Z
     * summary : 据天眼查App显示，4月2日，OPPO广东移动通信有限公司公开一项名称为“睡眠训练方法及相关设备”的专利，公开号为CN108404275B，申请时间为2018年2月 ... 该专利摘要显示，本申请应用于电子装置，包括脑电波传感器、控制器和发射器，其中：脑电波传感器用于当确定用户准备入睡时，采集用户的脑电波 ... 发射器用于当用户的脑部处于活跃状态时，发出睡眠训练信号，睡眠训练信号用于帮助用户进入睡眠状态。
     * title : OPPO关联公司公开“睡眠训练方法”相关专利，可帮助用户进入睡眠状态
     * updatedAt : 2021-04-02T11:09:04.235Z
     * timeline : 85809EFb2G4
     * order : 341053
     * hasInstantView : true
     * extra : {"instantView":true}
     */

    public List<ReadArticleItemEntity> data;


    public String getLastCursor() {
        return data == null || data.size() == 0 ? "" : data.get(data.size() - 1).getLastCursor();
    }
}
