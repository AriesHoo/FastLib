package com.aries.library.fast.demo.entity;

import java.io.Serializable;

/**
 * Created: AriesHoo on 2017/8/25 16:37
 * Function: 电影封面Image
 * Desc:
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
