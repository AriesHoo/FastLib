package com.aries.library.fast.demo.entity;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: AriesHoo on 2018/11/19 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function: 电影条目实体
 * @Description:
 */
public class SubjectsEntity implements Serializable {
    /**
     * rating : {"max":10,"average":9.6,"stars":"50","min":0}
     * genres : ["犯罪","剧情"]
     * title : 肖申克的救赎
     * casts : [{"alt":"https://movie.douban.com/celebrity/1054521/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/17525.jpg","large":"https://img3.doubanio.com/img/celebrity/large/17525.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/17525.jpg"},"name":"蒂姆·罗宾斯","id":"1054521"},{"alt":"https://movie.douban.com/celebrity/1054534/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/34642.jpg","large":"https://img3.doubanio.com/img/celebrity/large/34642.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/34642.jpg"},"name":"摩根·弗里曼","id":"1054534"},{"alt":"https://movie.douban.com/celebrity/1041179/","avatars":{"small":"https://img1.doubanio.com/img/celebrity/small/5837.jpg","large":"https://img1.doubanio.com/img/celebrity/large/5837.jpg","medium":"https://img1.doubanio.com/img/celebrity/medium/5837.jpg"},"name":"鲍勃·冈顿","id":"1041179"}]
     * collect_count : 1078121
     * original_title : The Shawshank Redemption
     * subtype : movie
     * directors : [{"alt":"https://movie.douban.com/celebrity/1047973/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/230.jpg","large":"https://img3.doubanio.com/img/celebrity/large/230.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/230.jpg"},"name":"弗兰克·德拉邦特","id":"1047973"}]
     * year : 1994
     * images : {"small":"https://img3.doubanio.com/view/movie_poster_cover/ipst/public/p480747492.webp","large":"https://img3.doubanio.com/view/movie_poster_cover/lpst/public/p480747492.webp","medium":"https://img3.doubanio.com/view/movie_poster_cover/spst/public/p480747492.webp"}
     * alt : https://movie.douban.com/subject/1292052/
     * id : 1292052
     */

    public RatingEntity rating;
    public String title;
    public int collect_count;
    public String original_title;
    public String subtype;
    public String year;
    public ImagesEntity images;
    public String alt;
    public String id;
    public List<String> genres;
    public List<CastsEntity> casts;
    public List<DirectorsEntity> directors;

    public String getGenres() {
        String genre = "";
        if (genres != null && genres.size() > 0) {
            for (String item : genres) {
                if (TextUtils.isEmpty(genre)) {
                    genre = item;
                } else {
                    genre += "&" + item;
                }
            }
        }
        return genre;
    }

    public String getDirectors() {
        String director = "";
        if (directors != null && directors.size() > 0) {
            for (DirectorsEntity item : directors) {
                if (TextUtils.isEmpty(director)) {
                    director = item.name;
                } else {
                    director += "&" + item.name;
                }
            }
        }
        return director;
    }

    public String getCasts() {
        String cast = "";
        if (casts != null && casts.size() > 0) {
            for (CastsEntity item : casts) {
                if (TextUtils.isEmpty(cast)) {
                    cast = item.name;
                } else {
                    cast += "&" + item.name;
                }
            }
        }
        return cast;
    }
}
