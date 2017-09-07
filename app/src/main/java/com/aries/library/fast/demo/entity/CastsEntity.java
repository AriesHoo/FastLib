package com.aries.library.fast.demo.entity;

import java.io.Serializable;

/**
 * Created: AriesHoo on 2017/8/25 16:43
 * Function: 电影演员实体
 * Desc:
 */

public class CastsEntity implements Serializable {
    /**
     * alt : https://movie.douban.com/celebrity/1317646/
     * avatars : {"small":"https://img3.doubanio.com/img/celebrity/small/40783.jpg","large":"https://img3.doubanio.com/img/celebrity/large/40783.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/40783.jpg"}
     * name : 戴恩·德哈恩
     * id : 1317646
     */

    public String alt;
    public AvatarsEntity avatars;
    public String name;
    public String id;
}
