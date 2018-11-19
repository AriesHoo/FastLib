package com.aries.library.fast.demo.entity;

import java.io.Serializable;

/**
 * @Author: AriesHoo on 2018/11/19 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function: 电影封面Image
 * @Description:
 */
public class ImagesEntity implements Serializable{
    /**
     * small : https://img3.doubanio.com/view/movie_poster_cover/ipst/public/p480747492.webp
     * large : https://img3.doubanio.com/view/movie_poster_cover/lpst/public/p480747492.webp
     * medium : https://img3.doubanio.com/view/movie_poster_cover/spst/public/p480747492.webp
     */

    public String small;
    public String large;
    public String medium;
}
