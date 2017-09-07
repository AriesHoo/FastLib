package com.aries.library.fast.demo.entity;

import java.io.Serializable;

/**
 * Created: AriesHoo on 2017/8/25 16:47
 * Function: 电影导演实体
 * Desc:
 */
public class DirectorsEntity implements Serializable {
    /**
     * alt : https://movie.douban.com/celebrity/1047973/
     * avatars : {"small":"https://img3.doubanio.com/img/celebrity/small/230.jpg","large":"https://img3.doubanio.com/img/celebrity/large/230.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/230.jpg"}
     * name : 弗兰克·德拉邦特
     * id : 1047973
     */

    public String alt;
    public AvatarsEntityX avatars;
    public String name;
    public String id;
}
