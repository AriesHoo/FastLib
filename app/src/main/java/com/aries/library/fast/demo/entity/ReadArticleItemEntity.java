package com.aries.library.fast.demo.entity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;


public class ReadArticleItemEntity {
    public String id;
    public String createdAt;
    public String publishDate;
    public String summary;
    public String title;
    public String updatedAt;
    public String timeline;
    public int order;
    public boolean hasInstantView;
    public String url;
    public String mobileUrl;
    /**
     * instantView : true
     */

    public ExtraEntity extra;
    /**
     * id : 3318215
     * url : https://www.jiemian.com/article/5902821.html
     * title : OPPO关联公司公开“睡眠训练方法”相关专利，可帮助用户进入睡眠状态
     * siteName : 界面
     * mobileUrl : https://www.jiemian.com/article/5902821.html
     * autherName : null
     * duplicateId : 1
     * publishDate : 2021-04-02T08:33:00.000Z
     * language : zh-cn
     * hasInstantView : true
     * statementType : 1
     */

    public List<NewsArrayEntity> newsArray;
    public List<?> eventData;

    public static class ExtraEntity {
        public boolean instantView;
    }

    public static class NewsArrayEntity {
        public int id;
        public String url;
        public String title;
        public String siteName;
        public String mobileUrl;
        public Object autherName;
        public int duplicateId;
        public String publishDate;
        public String language;
        public boolean hasInstantView;
        public int statementType;

        public String getUrl() {
            if (mobileUrl != null) {
                return mobileUrl;
            }
            if (url != null) {
                return url;
            }
            return "";
        }
    }


    public String getUrl() {
        if (mobileUrl != null) {
            return mobileUrl;
        }
        if (url != null) {
            return url;
        }
        return newsArray != null && newsArray.size() > 0
                ? newsArray.get(0).getUrl()
                : "";
    }

    public String getLastCursor() {
        if (0 != order) {
            return order + "";
        }
        return getUtcTime(publishDate, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    public String getTime() {
        return publishDate.replaceAll("Z", "").replaceAll("T", " ").substring(0, 19);
    }

    /**
     * 转UTC时间获取时间戳
     *
     * @param dateStr
     * @param format
     * @return
     */
    private String getUtcTime(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);

            ///设置UTC
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return String.valueOf(sdf.parse(dateStr).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
