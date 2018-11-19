package com.aries.library.fast.demo.entity;

import java.io.Serializable;

/**
 * @Author: AriesHoo on 2018/11/19 14:15
 * @E-Mail: AriesHoo@126.com
 * @Function: 演员头像实体
 * @Description:
 */
public class AvatarsEntity implements Serializable {
    /**
     * small : https://img3.doubanio.com/img/celebrity/small/17525.jpg
     * large : https://img3.doubanio.com/img/celebrity/large/17525.jpg
     * medium : https://img3.doubanio.com/img/celebrity/medium/17525.jpg
     */
    public String small;
    public String large;
    public String medium;
}
